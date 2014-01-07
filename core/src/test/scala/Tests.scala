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

  test("tableAnnotation") {
    @tableAnnotation(Col[String]("name"), Col[Int]("age")) object Users {}
    assert(Users.isInstanceOf[Table[(String, Int)]])
    assert(Users.name.isInstanceOf[ColImpl[String]])
    assert(Users.age.isInstanceOf[ColImpl[Int]])
    // Check that the columns reference their table:
    assert(Users.name.table === Users)
    assert(Users.age.table === Users)
  }
}
