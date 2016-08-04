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
import java.io._

object Flows
  extends WithStream {

  init(2000) {
    val doubled = Flow[Int].map(_*2)
    
    val evens = Flow[Int].filter(_%2==0)
    
    def grouped[A] = Flow[A].grouped(8)
    
    def toList[A] = Flow[A].fold(List[A]()) { (acc,elem) => elem::acc }
  }
}