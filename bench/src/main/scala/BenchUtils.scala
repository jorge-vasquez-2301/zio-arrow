package bench

object BenchUtils {

  val rand = new scala.util.Random

  /**
   * Generates a random Int from a specific range
   */
  def fromRange(start: Int, end: Int) = rand.nextInt((end - start) + 1)

  /**
   * Simple non-stack safe factorial function
   */
  def fact(n: Int): Int =
    if (n == 0) return 1
    else return n * fact(n - 1)

}
