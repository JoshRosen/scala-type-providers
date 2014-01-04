import scala.language.experimental.macros
import scala.reflect.macros.Context

/**
 * Builds `Table` objects using fake type providers and structural types.
 *
 * See http://meta.plasm.us/posts/2013/06/19/macro-supported-dsls-for-schema-bindings/
 */
object StructuralTypeTableBuilder {
  def apply(columns: Col[_]*) = macro apply_impl

  def apply_impl(c: Context)(columns: c.Expr[Col[_]]*): c.Expr[Any] = {
    import c.universe._

    val className = newTypeName(c.fresh())

    val columnInfo = columns.map { col =>
      val q"Col.apply[$colType]($colName)" = col.tree
      (colName, colType)
    }

    val colTypes = columnInfo.map(_._2)
    val numCols = columnInfo.size
    val productType = newTypeName(s"Product$numCols")
    val tableType = tq"$productType[..$colTypes]"

    val columnVals = columnInfo.map { case (colName, colType) =>
      val fieldName = newTermName(c.eval(c.Expr[String](c.resetAllAttrs(colName.duplicate))).toLowerCase)
      q"val $fieldName = new ColImpl[$colType]($colName, $className.this)"
    }

    c.Expr { q"""
      class $className extends Table[$tableType] { ..$columnVals }
      new $className {}
    """}
  }
}
