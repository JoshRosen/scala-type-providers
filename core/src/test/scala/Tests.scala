import org.scalatest.FunSuite

class Tests extends FunSuite {
  test("StructuralTypeTableBuilder") {
    val users = StructuralTypeTableBuilder(Col[String]("name"), Col[Int]("age"))
    assert(users.isInstanceOf[Table[(String, Int)]])
    assert(users.name.isInstanceOf[ColImpl[String]])
    assert(users.age.isInstanceOf[ColImpl[Int]])
    // Check that the columns reference their table:
    assert(users.name.table === users)
    assert(users.age.table === users)
  }
}
