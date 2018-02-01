package me.mdjnewman.vetted.importer

import com.healthmarketscience.jackcess.DatabaseBuilder
import com.healthmarketscience.jackcess.Row
import com.healthmarketscience.jackcess.Table
import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.PhoneNumber
import me.mdjnewman.vetted.command.AddNoteToClientCommand
import me.mdjnewman.vetted.command.AddPhoneNumberToClientCommand
import me.mdjnewman.vetted.command.MigrateClientCommand
import me.mdjnewman.vetted.core.VettedCoreMarker
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import java.io.File
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.allOf

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

@SpringBootApplication
@ComponentScan(basePackageClasses = arrayOf(VettedCoreMarker::class))
class Application {

    @Bean
    fun init(
        commandGateway: CommandGateway
    ) = CommandLineRunner { _ ->
        val databaseToImport = DatabaseBuilder.open(File("/Users/mnewman/projects/mdbtools/main.accdb"))

        val clientTableRows = databaseToImport.getTable("Clients").map(::buildClientTableRow)

        val newClientIds: Map<String, UUID> = createClients(
            clientTableRows,
            databaseToImport.getTable("Postcodes"),
            commandGateway
        )

        addClientNotes(clientTableRows, newClientIds, commandGateway)
        addPhoneNumbers(clientTableRows, newClientIds, commandGateway)
        addMostCommonDistance(clientTableRows, newClientIds, commandGateway)
    }
}

private fun createClients(
    clientTableRows: List<ClientTableRow>,
    postcodeTable: Table,
    commandGateway: CommandGateway
): Map<String, UUID> {
    val createClientCommands = clientTableRows.map { migrateClientCommand(it, postcodeTable) }

    val newClientIds: Map<String, UUID> = createClientCommands.associate { it.priorId to it.clientId }

    allOf(
        *createClientCommands
            .map { commandGateway.send<CompletableFuture<Void>>(it) }
            .toTypedArray()
    ).get()

    return newClientIds
}

private fun addClientNotes(
    clientTableRows: List<ClientTableRow>,
    newClientIds: Map<String, UUID>,
    commandGateway: CommandGateway
) =
    allOf(*clientTableRows
        .flatMap { row ->
            listOf(row.clientNotes, row.contact)
                .filter { it != null }
                .map {
                    AddNoteToClientCommand(
                        clientId = newClientIds[row.clientId]!!,
                        noteText = it!!
                    )
                }
        }
        .map { commandGateway.send<CompletableFuture<Void>>(it) }
        .toTypedArray()
    ).get()

private fun addMostCommonDistance(
    clientTableRows: List<ClientTableRow>,
    newClientIds: Map<String, UUID>,
    commandGateway: CommandGateway
) =
    allOf(*clientTableRows
        .map { row ->
            row.mostCommonDistance?.let {
                AddNoteToClientCommand(
                    clientId = newClientIds[row.clientId]!!,
                    noteText = "Most common travel distance is $it km")
            }
        }
        .filter { it != null }
        .map { commandGateway.send<CompletableFuture<Void>>(it) }
        .toTypedArray()
    ).get()

private fun addPhoneNumbers(
    clientTableRows: List<ClientTableRow>,
    newClientIds: Map<String, UUID>,
    commandGateway: CommandGateway
) =
    allOf(*clientTableRows
        .flatMap { row ->
            listOf(Pair("Home", row.homePhone), Pair("Mobile", row.mobilePhone))
                .filter { it.second != null }
                .map {
                    AddPhoneNumberToClientCommand(
                        clientId = newClientIds[row.clientId]!!,
                        phoneNumber = PhoneNumber(it.first, it.second!!)
                    )
                }
        }
        .map { commandGateway.send<CompletableFuture<Void>>(it) }
        .toTypedArray()
    ).get()

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

private fun migrateClientCommand(
    clientRow: ClientTableRow,
    postcodeTable: Table
) =
    MigrateClientCommand(
        clientId = UUID.randomUUID(),
        name = clientRow.name,
        address = Address(
            addressLineOne = clientRow.street,
            addressLineTwo = null,
            postcode = getPostcode(clientRow.town, postcodeTable),
            town = clientRow.town,
            state = getState(clientRow.town, postcodeTable)
        ),
        priorId = clientRow.clientId
    )
