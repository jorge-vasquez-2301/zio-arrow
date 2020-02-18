package bench

import zio.{ DefaultRuntime }
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

  val rt = new DefaultRuntime {}

  @Benchmark
  def plainBench(): Long = {
    val workers: List[Long] = files.map(f => worker(f._1))
    val sumPlain: Long      = workers.sum
    sumPlain
  }

  @Benchmark
  def zioBench(): Long = rt.unsafeRun(monWorkers)

  @Benchmark
  def arrowBench(): Long = rt.unsafeRun(arrWorkers.run(0L))
}

object Prepare extends App {
  setup()
  // clean()
}
