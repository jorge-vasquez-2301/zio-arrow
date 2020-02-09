package zio.arrow
package laws

trait Category[->[_, _]] {
  def id[A]: A -> A
  def compose[A, B, C](f: B -> C, g: A -> B): A -> C

  implicit def homEq[A, B]: Eq[A -> B] = Eq.fromUniversalEquals

  class CatLaws {
    def lunit[A, B](f: A -> B) =
      (id[A] >> f) === f
    def runit[A, B](f: A -> B) =
      (f >> id[B]) === f
    def assoc[A, B, C, D](f: A -> B, g: B -> C, h: C -> D) =
      (f >> (g >> h)) === ((f >> g) >> h)
  }
}
