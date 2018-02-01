package me.mdjnewman.vetted.importer

import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.PhoneNumber
import me.mdjnewman.vetted.command.AddNoteToClientCommand
import me.mdjnewman.vetted.command.AddPhoneNumberToClientCommand
import me.mdjnewman.vetted.command.MigrateClientCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.allOf

@Component
class Runner(
    private val commandGateway: CommandGateway,
    private val accessDb: AccessDatabaseLoader
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val newClientIds: Map<String, UUID> = createClients(
            accessDb.clientTableRows,
            accessDb::postCodeFor,
            accessDb::stateFor,
            commandGateway
        )

        allOf(
            addClientNotes(accessDb.clientTableRows, newClientIds, commandGateway),
            addPhoneNumbers(accessDb.clientTableRows, newClientIds, commandGateway),
            addMostCommonDistance(accessDb.clientTableRows, newClientIds, commandGateway)
        ).get()
    }
}

private fun createClients(
    clientTableRows: List<ClientTableRow>,
    postcodeLookup: (String) -> String,
    stateLookup: (String) -> String,
    commandGateway: CommandGateway
): Map<String, UUID> {
    val createClientCommands = clientTableRows.map { migrateClientCommand(it, postcodeLookup, stateLookup) }

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
    allOf(
        *clientTableRows
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
    )

private fun addMostCommonDistance(
    clientTableRows: List<ClientTableRow>,
    newClientIds: Map<String, UUID>,
    commandGateway: CommandGateway
) =
    allOf(
        *clientTableRows
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
    )

private fun addPhoneNumbers(
    clientTableRows: List<ClientTableRow>,
    newClientIds: Map<String, UUID>,
    commandGateway: CommandGateway
) =
    allOf(
        *clientTableRows
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
    )

private fun migrateClientCommand(
    clientRow: ClientTableRow,
    postcodeLookup: (String) -> String,
    stateLookup: (String) -> String
) =
    MigrateClientCommand(
        clientId = UUID.randomUUID(),
        name = clientRow.name,
        address = Address(
            addressLineOne = clientRow.street,
            addressLineTwo = null,
            postcode = postcodeLookup(clientRow.town),
            town = clientRow.town,
            state = stateLookup(clientRow.town)
        ),
        priorId = clientRow.clientId
    )
