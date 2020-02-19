package bench

import zio.{ DefaultRuntime }
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._

import BenchUtils._
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

/**
 * Randomizer test App
 */
object randTest extends App {
  val g1 = fromRange(minRange, maxRange)
  println(g1)
}

/**
 * Benchmarks preparation App
 */
object Prepare extends App {
  setup()
}

/**
 * Cleanup App
 */
object Clean extends App {
  clean()
}

/**
 * Result Validation app
 * This validates that all results are consistent
 */
object Validate extends App {
  val test = new SocketBenchmark()

  setup()

  val plainRes = test.plainBench
  val zioRes   = test.zioBench
  val arrRes   = test.arrowBench

  assert(plainRes == zioRes)
  assert(zioRes == arrRes)

  println(s"Plain  compute result \t: ${plainRes}")
  println(s"ZIO    compute result \t: ${zioRes}")
  println(s"ZArrow compute result \t: ${arrRes}")

}
