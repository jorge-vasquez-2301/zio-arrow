package zio.arrow

// arr id = id

// arr (f >>> g) = arr f >>> arr g

// first (arr f) = arr (first f)

// first (f >>> g) = first f >>> first g

// first f >>> arr fst = arr fst >>> f

// first f >>> arr (id *** g) = arr (id *** g) >>> first f

// first (first f) >>> arr assoc = arr assoc >>> first f

object Laws {
  // import ZArrow._

  def ident[A](data: A) = ZArrow.identity[A].run(data) == data
}
