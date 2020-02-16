package bench

import zio._
import zio.arrow._

import BenchUtils._

object Perf extends App {
  def run(args: List[String]) =
    app.fold(_ => 1, _ => 0)
  // app.as(0)

  val app = {
    setup()

    val workers = files.map(f => worker(f._1))
    workers.foreach(println)
    println(sum(workers))

    arrWorkers.run(1)

    ZIO.effect(clean())
  }

}
