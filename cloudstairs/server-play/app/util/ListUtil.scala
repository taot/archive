package util

object ListUtil {

  def split[A](list: List[A])(p: (A) => Boolean): List[List[A]] = {
    if (list == null) {
      throw new NullPointerException("the list to split cannot be null")
    }
    val remaining = list.dropWhile(p)
    if (remaining.isEmpty) {
      return Nil
    }
    remaining span (!p(_)) match {
      case (xs, Nil) => xs :: Nil
      case (xs, ys) => xs :: split(ys)(p)
    }
  }
}
