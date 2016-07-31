import java.nio.file.Paths
import akka.util.ByteString
import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._

object City
    extends App
    with WithStream {
  init(1000) {
    val srcFile = Paths.get(getClass.getResource("/cities.csv").toURI)
    val targetFile = Paths.get(System.getProperty("user.home")).resolve("Downloads/filteredCities.csv")

    case class City(name:String, country:String, population:Long)

    val cachedCities = List(
      City("Berlin", "Deutschland", 3520031),
      City("München", "Deutschland", 1450381))

    val fileSrc = FileIO.fromPath(srcFile).
      via(Framing.delimiter(ByteString("\n"), 2048, true)).
        map(_.utf8String)

    val memSrc = Source(cachedCities)

    val csvToCity =
      Flow[String].
        map(_.split(",").map(_.trim).toList).
        collect {
          case name::country::pop::Nil =>
            City(name, country, pop.toLong)
        }

    def sort(maxElems:Long) =
      Flow[City].
        take(maxElems).
        fold(List[City]())((lst, elem) => elem::lst).
        mapConcat(lst => lst.sortBy(city => city.population).reverse)

    val toCsvLine =
      Flow[City].map {
        case City(name, country, pop) =>
          s"$name, $country, $pop"
      }

    FileIO.toPath(targetFile)

    val toByteString =
      Flow[String].map(line => ByteString(line+"\n"))

    fileSrc.via(csvToCity).
      merge(memSrc).
      filter(x => x.population > 500000).
      via(sort(50)).
      via(toCsvLine).
      via(toByteString).
      to(FileIO.toPath(targetFile)).
      run().onComplete(_ => println(s"Written to $targetFile"))
  }
}
