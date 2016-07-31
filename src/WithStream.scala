import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._

trait WithStream {
  implicit val system = ActorSystem("my-system")
  implicit val mat = ActorMaterializer()
  implicit val exec = scala.concurrent.ExecutionContext.global

  def init[A](timeout:Int)(f: => A): Unit = {
    f
    Thread.sleep(timeout)
    system.terminate()
  }
}
