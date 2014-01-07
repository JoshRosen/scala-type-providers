import scala.reflect.macros.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation


object tableAnnotation {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val q"new tableAnnotation( ..$columns)" = c.prefix.tree

    val inputs = annottees.map(_.tree).toList
    assert (inputs.length == 1)
    val q"object $obj { ..$body}" = inputs.head

    val columnInfo = columns.map { col =>
      val q"Col[$colType]($colName)" = col  // For some reason, we don't get passed Col.apply trees.
      (colName, colType)
    }

    val colTypes = columnInfo.map(_._2)
    val numCols = columnInfo.size
    val productType = newTypeName(s"Product$numCols")
    val tableType = tq"$productType[..$colTypes]"

    val columnVals = columnInfo.map { case (colName, colType) =>
      val fieldName = newTermName(c.eval(c.Expr[String](c.resetAllAttrs(colName.duplicate))).toLowerCase)
      q"val $fieldName = new ColImpl[$colType]($colName, this)"
    }

    c.Expr { q"""
      object $obj extends Table[$tableType] {
        ..$body
        ..$columnVals
      }
    """}
  }
}

class tableAnnotation(columns: Col[_]*) extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro tableAnnotation.impl
}
