import akka.stream._
import akka.stream.scaladsl._
import akka.NotUsed
import scala.concurrent.Future
import akka.actor.ActorSystem
import scala.concurrent.Await
import scala.concurrent.duration._

object Graph
    extends App
    with WithStream {

  init(2000) {
    val producer = Source.unfold(1)( x => Some(x+1 -> x) )
    val sink = Sink.fold[List[Int], Int](List[Int]()) ( (acc,elem) => elem :: acc )

    val graph = Source.fromGraph(GraphDSL.create() {
      implicit builder =>
      import GraphDSL.Implicits._
      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[String](2))
      val prod = builder.add(producer)
      val small = builder.add(Flow[Int].filter(_ < 10).map("small "+_))
      val big = builder.add(Flow[Int].filter(_ > 10).map("big "+_))
      
      prod ~> bcast ~> small ~> merge.in(0)
      bcast.outlet ~> big ~> merge.in(1)
      SourceShape(merge.out)
    })
    
    graph.
      take(30).
      runWith(Sink.seq).
      foreach(println)
  }
}
