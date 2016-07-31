import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._
import java.nio.file.Paths
import akka.util.ByteString
import java.nio.file.Path
import java.nio.file.Files
import scala.io.StdIn

object CountWords
    extends App
    with WithStream {

  init(2000) {
    val file1 = Paths.get(getClass.getResource("/06-loremipsum.txt").toURI)
    val words = Framing.delimiter(ByteString(" "), 256, true).map(_.utf8String)
    
    val count = Flow[String].fold(Map.empty[String, Int]) {
      case (map, elem) =>
        val value = map.get(elem) map(_+1) getOrElse(1)
        map + (elem -> value)
    }
    
    def sortByValue(descending:Boolean) = Flow[Map[String, Int]].map { map =>
      if(descending)
        map.toList.sortBy(_._2).reverse
      else
        map.toList.sortBy(_._2)
    }
    val take10 = Flow[Seq[(String, Int)]].map { xs => xs.take(10) }
    
    val blueprint = FileIO.fromPath(file1).
      via(words).
      via(count).
      via(sortByValue(true)).
      via(take10).
      toMat(Sink.head)(Keep.right)
    
    blueprint.run().foreach { list =>
      println(list.zipWithIndex.map {
        case ((word, cnt), idx) =>
          val pos = idx+1
          f"$pos%2d $word%10s $cnt%03d"
      }.mkString("\n"))
    }
  }
}
