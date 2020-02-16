package bench
import BenchUtils._

object Perf extends App {
  setup()

  val workers = files.map(f => worker(f._1))
  workers.foreach(println)

  // clean()

}
