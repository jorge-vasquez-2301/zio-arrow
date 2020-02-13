package zio.arrow
package laws

// Generic Arrow Laws
trait Arr[->[_, _]] {

  def id[A]: A -> A

  def compose[A, B, C](f: B -> C, g: A -> B): A -> C
  def <<<[A, B, C](f: B -> C, g: A -> B): A -> C = compose(f, g)

  def andThen[A, B, C](f: ->[A, B], g: ->[B, C]): ->[A, C] =
    compose(g, f)

  def >>>[A, B, C](f: ->[A, B], g: ->[B, C]): ->[A, C] = andThen(f, g)

  class Laws {
    def lunit[A, B](f: A -> B) =
      (id[A] >>> f) === f
    def runit[A, B](f: A -> B) =
      (f >>> id[B]) === f
    def assoc[A, B, C, D](f: A -> B, g: B -> C, h: C -> D) =
      (f >>> (g >>> h)) === ((f >>> g) >>> h)
  }

}
