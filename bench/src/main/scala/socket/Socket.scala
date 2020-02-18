package bench

import zio.{ IO, ZIO }
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._

import BenchUtils._

object randTest extends App {
  val g1 = fromRange(minRange, maxRange)
  println(g1)
}

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class SocketBenchmark {

  @Benchmark
  def plainBench(): Long = {
    val workers: List[Long] = files.map(f => worker(f._1))
    val sumPlain: Long      = workers.sum
    sumPlain
  }

  @Benchmark
  def zioBench(): ZIO[Any, Throwable, Long] =
    for {
      list <- monWorkers
      out  = list.sum
    } yield out

  @Benchmark
  def arrowBench(): IO[Nothing, Long] = arrWorkers.run(0L)
}

object Perf extends zio.App {
  def run(args: List[String]) =
    // app.fold(_ => 1, _ => 0)
    app.as(0)

  val app = {
    ZIO.succeed(setup())
    // ZIO.effect(clean())
  }

}
