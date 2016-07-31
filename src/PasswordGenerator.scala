import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

object PasswordGenerator
    extends App
    with WithStream {
  init(1000) {
    def randomCharSrc(rnd:Random) =
      Source(rnd.alphanumeric)
    
    def toString(length:Int) =
      Flow[Char].grouped(length).map(_.mkString(""))
    
    val filterSecure =
      Flow[String].filter { x =>
        x.find(_.isDigit).isDefined &&
        x.find(_.isLetter).isDefined
      }
    
    val filterMemorisable =
      Flow[String].filter { x =>
        x.count(_.isLetter) >= 3
      }

    def passwords(length:Int) =
      randomCharSrc(new Random()).
        via(toString(length)).
        via(filterSecure).
        via(filterMemorisable)

    passwords(12).
      take(20).
      runForeach(println)

  }
}
