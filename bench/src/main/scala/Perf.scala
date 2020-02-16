package bench

import zio._
import zio.arrow._
import zio.duration._

import BenchUtils._

object Perf extends App {
  def run(args: List[String]) =
    app.fold(_ => 1, _ => 0)
  // app.as(0)

  val app = {
    setup()

    println("Plain workers run time")

    val t0 = System.nanoTime

    val workers  = files.map(f => worker(f._1))
    val sumPlain = sum(workers)

    val t1 = System.nanoTime
    showTime(t1 - t0)

    println(s">>>> Plain sum = ${sumPlain}")

    println("ZIO Monad workers run time")

    val t2 = System.nanoTime

    val monWorkers = ZIO.traverse(files) { f =>
      ZIO.effect(worker(f._1))
    }
    val sumMon = monWorkers.fold(_ => ZIO.fail("Failed"), data => sum(data))

    val t3 = System.nanoTime
    showTime(t3 - t2)

    println(s">>>> ZIO sum = ${sumMon}")

    println("ZIO Arrow workers run time")

    val t4 = System.nanoTime

    val arrWorkers = files.foldLeft(ZArrow.identity[Int]) {
      case (acc, item) => acc >>> ZArrow.lift(_ => worker(item._1))
    }
    val sumArr = arrWorkers.run(0)

    val t5 = System.nanoTime
    showTime(t5 - t4)

    println(s">>>> Arrow sum = ${sumArr}")

    ZIO.effect(clean())
  }

}
