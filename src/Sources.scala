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

object Sources
    extends WithStream {

  init(2000) {
    val singleSrc = Source.single("nico")
    
    val rangeSrc = Source(0 until 100)
    
    val numberSrc = Source.unfold(0) { n => Some(n+1 -> n) }
    
    val fileSrc = FileIO.fromPath(Paths.get("./file.txt"))
    
    val stream: InputStream = ???
    val streamSrc = StreamConverters.fromInputStream(() => stream)
  }
}
