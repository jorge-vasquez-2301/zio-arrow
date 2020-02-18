package bench

import zio.{ IO, ZIO }
import zio.arrow._
import zio.console.putStrLn

import BenchUtils._

object randTest extends App {
  val g1 = fromRange(minRange, maxRange)
  println(g1)
}

object Perf extends zio.App {
  def run(args: List[String]) =
    // app.fold(_ => 1, _ => 0)
    app.as(0)

  val app = {
    setup()

    // files.foreach(println)

    println("Plain workers run time")

    val t0 = System.nanoTime

    val workers: List[Long] = files.map(f => worker(f._1))
    val sumPlain: Long      = workers.sum

    val t1 = System.nanoTime
    showTime(t1 - t0)

    println(s">>>> Plain sum = ${sumPlain}")

    println("ZIO Monad workers run time")

    val t2 = System.nanoTime

    val sumMon: ZIO[Any, Throwable, Long] = for {
      list <- ZIO.traverse(files) { item =>
               ZIO.effect(worker(item._1))
             }
      out = list.sum
    } yield out

    val t3 = System.nanoTime
    showTime(t3 - t2)

    // ZIO.succeed(println("Hiiiiii"))
    sumMon >>= (res => putStrLn(res.toString))
    // sumMon >>= (res => ZIO.succeed(println(res)))

    println("ZIO Arrow workers run time")

    val t4 = System.nanoTime

    val arrWorkers: ZArrow[Nothing, Long, Long] = files.foldLeft(ZArrow.identity[Long]) {
      case (acc, item) => acc >>> ZArrow.lift(_ => worker(item._1))
    }
    val sumArr: IO[Nothing, Long] = arrWorkers.run(0)

    val t5 = System.nanoTime
    showTime(t5 - t4)

    println(s">>>> Arrow sum = ${sumArr}")
    ZIO.succeed(sumArr)

    // ZIO.effect(clean())
  }

}
