trait Table[T]

class Col[T] private (name: String)

object Col {
  def apply[T](name: String) = new Col[T](name)
}

case class ColImpl[ColType](name: String, table: Table[_])