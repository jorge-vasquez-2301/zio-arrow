package bench

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._

import zio.arrow._
import Helper._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class ApiBenchmark {
  @Benchmark
  def lift() = ZArrow.lift(plusOne)

  @Benchmark
  def compose() = (add1 <<< mul2).run(1)

  @Benchmark
  def endThen() = (add1 >>> mul2).run(1)

  @Benchmark
  def zipWith = (add1 <*> mul2)(_ -> _).run(1)

}

object Helper {
  val plusOne = (_: Int) + 1
  val mulTwo  = (_: Int) * 2

  val add1: ZArrow[Nothing, Int, Int] = ZArrow(plusOne)
  val mul2: ZArrow[Nothing, Int, Int] = ZArrow(mulTwo)
}
