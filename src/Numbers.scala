import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._

object Numbers
    extends App
    with WithStream {

  init(2000) {
    def noStream:Stream[Int] = {
      def stream(n:Int):Stream[Int] = n #:: stream(n+1)
      stream(0)
    }
    
    val numbers = Source.unfold(0) { x =>
      Some(x+1 -> x)
    }
    
    val fibs = Source.unfold((0,1)) { case (fib_2, fib_1) =>
      val fib_n = fib_2+fib_1
      Some( (fib_1, fib_n) -> fib_n )
    }
    
    val facs = Source.unfold((0,1)) { case (n_1, fac_1) =>
      val fac_n = fac_1*(n_1+1)
      Some( (n_1+1,fac_n) -> fac_n )
    }
    
    val src = Source(noStream)
    
    val onlyEvens = Flow[Int].filter(x => x%2 == 0)
    val doubled = Flow[Int].map(x => x*2)
    def dropSmallerThan(y:Int) = Flow[Int].dropWhile { x => x<y }
    
    val sum:Sink[Int,Future[Int]] = Sink.fold(0)(_+_)
    
    val ergFuture = src.
      via(doubled).
      via(onlyEvens).
      via(dropSmallerThan(200)).
      take(20).
      toMat(sum)(Keep.right)
    
    ergFuture.run().foreach(x => println("erg: "+x))
    fibs.take(10).runWith(Sink.seq).foreach(xs => println("fibs: "+xs))
    facs.take(10).runWith(Sink.seq).foreach(xs => println("facs "+xs))
  }
}
