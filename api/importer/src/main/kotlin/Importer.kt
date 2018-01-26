
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.util.ExportUtil
import java.io.File

fun main(args: Array<String>) {
    val database = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))
    database.tableNames.forEach { println(it) }
    database.queries.forEach { println(it.name) }

    ExportUtil.exportAll(database, File("/Users/mnewman/Desktop/foo"), ".csv", true)

    val table = database.getTable("Transactions")
    table.last().let { println("Column 'TransactionID' has value: " + it["TransactionID"]) }
}
