package me.mdjnewman.vetted.importer

import com.healthmarketscience.jackcess.Database
import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.Row
import org.springframework.stereotype.Component
import java.io.File

@Component
class AccessDatabaseLoader {

    private final val databaseToImport: Database = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))

    private val postcodes: Map<String, String> = databaseToImport.getTable("Postcodes")
        .associate { Pair((it["Town"] as String).toUpperCase(), (it["Postcode"] as String)) }

    private val states: Map<String, String> = databaseToImport.getTable("Postcodes")
        .associate { Pair((it["Town"] as String).toUpperCase(), (it["State"] as String)) }

    val clientTableRows: List<ClientTableRow> =
        databaseToImport.getTable("Clients").map(::buildClientTableRow)

    fun postCodeFor(town: String): String =
        postcodes[town.toUpperCase()] ?: throw Exception("No postcode found for town $town")

    fun stateFor(town: String): String =
        states[town.toUpperCase()] ?: throw Exception("No state found for town $town")

    private fun buildClientTableRow(row: Row) =
        ClientTableRow(
            fName = (row["First Name"] as String?)?.trim(),
            lName = (row["Last Name/Trading Name"] as String?)?.trim(),
            street = (row["Street Address/PO Box"] as String).trim(),
            town = (row["Town"] as String).trim(),
            clientId = (row["ClientID"] as Int).toString(),
            clientNotes = (row["Client Notes"] as String?)?.trim(),
            contact = (row["Contact"] as String?)?.trim(),
            homePhone = (row["Home Phone"] as String?)?.trim(),
            mobilePhone = (row["Mobile Phone"] as String?)?.trim(),
            mostCommonDistance = (row["Most Common Distance"] as Int?)
        )
}

data class ClientTableRow(
    private val fName: String?,
    private val lName: String?,
    val street: String,
    val town: String,
    val clientId: String,
    val clientNotes: String?,
    val contact: String?,
    val homePhone: String?,
    val mobilePhone: String?,
    val mostCommonDistance: Int?
) {
    val name = getConcatenatedName(fName, lName)
}

private fun getConcatenatedName(fName: String?, lName: String?): String =
    listOf(fName, lName)
        .map { it?.trim() }
        .filter { it != null && it.isNotEmpty() }
        .joinToString(" ")