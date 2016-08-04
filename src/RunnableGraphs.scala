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

object RunnableGraphs
  extends WithStream {
  
    init(2000) {
val numberSrc = Source.unfold(0) { n => Some(n+1 -> n) }
val sum:Sink[Int, Future[Int]] = Sink.fold[Int,Int](0) { (acc, elem) => acc + elem }

val evens = Flow[Int].filter(_%2==0)
val limit = Flow[Int].take(20)
      
val graph =
   numberSrc.
   to(sum)
val graph2 =
   numberSrc.
   via(evens).
   via(limit)
   .to(sum)
    }
}