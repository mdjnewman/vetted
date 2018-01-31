package me.mdjnewman.vetted.importer

import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.Row
import com.healthmarketscience.jackcess.Table
import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.command.MigrateClientCommand
import me.mdjnewman.vetted.core.VettedCoreMarker
import org.axonframework.commandhandling.gateway.CommandGateway
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture

@SpringBootApplication
@ComponentScan(basePackageClasses = arrayOf(VettedCoreMarker::class))
class Application {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun init(
        commandGateway: CommandGateway
    ) = CommandLineRunner {
        val databaseToImport = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))
        val postcodeTable = databaseToImport.getTable("Postcodes")
        val clientsTable = databaseToImport.getTable("Clients")

        val migrateCommandResults =
            clientsTable
                .map(::buildClientTableRow)
                .map {
                    MigrateClientCommand(
                        clientId = UUID.randomUUID(),
                        name = it.name,
                        address = Address(
                            addressLineOne = it.street,
                            addressLineTwo = null,
                            postcode = getPostcode(it.town, postcodeTable),
                            town = it.town,
                            state = getState(it.town, postcodeTable)
                        ),
                        priorId = it.clientId
                    )
                }
                .map { commandGateway.send<Void>(it) }
                .toTypedArray()

        CompletableFuture.allOf(*migrateCommandResults).get()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

private fun getConcatenatedName(fName: String?, lName: String?): String =
    listOf(fName, lName)
        .filter { it != null && it.trim().isNotEmpty() }
        .joinToString(" ")

private fun getPostcode(town: String, table: Table): String =
    findRowForTown(table, town)["Postcode"] as String

private fun getState(town: String, table: Table): String =
    findRowForTown(table, town)["State"] as String

private fun findRowForTown(table: Table, town: String): Row =
    table.firstOrNull { town.equals(it["Town"] as String, true) }
        ?: throw RuntimeException("No row for town: $town")

private fun buildClientTableRow(row: Row): ClientTableRow {
    return ClientTableRow(
        fName = row["First Name"] as String?,
        lName = row["Last Name/Trading Name"] as String?,
        street = row["Street Address/PO Box"] as String,
        town = row["Town"] as String,
        clientId = row["ClientId"] as String
    )
}

data class ClientTableRow(
    private val fName: String?,
    private val lName: String?,
    val street: String,
    val town: String,
    val clientId: String
) {
    val name = getConcatenatedName(fName, lName)
}