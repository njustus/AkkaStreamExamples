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

object Sinks
    extends WithStream {

  init(2000) {
    val stdio = Sink.foreach(println)
    
    def first[A] = Sink.head[A]
    
    val sum = Sink.fold[Int,Int](0) { (acc, elem) => acc + elem }
    
    def toList[A] = Sink.fold[List[A], A](List[A]()) { (acc,elem) => elem::acc }
  }
}
