package me.mdjnewman.vetted.core.domain

import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.ClientNote
import me.mdjnewman.vetted.PhoneNumber
import me.mdjnewman.vetted.command.AddNoteToClientCommand
import me.mdjnewman.vetted.command.AddPhoneNumberToClientCommand
import me.mdjnewman.vetted.command.CreateClientCommand
import me.mdjnewman.vetted.command.MigrateClientCommand
import me.mdjnewman.vetted.event.ClientCreatedEvent
import me.mdjnewman.vetted.event.ClientMigratedEvent
import me.mdjnewman.vetted.event.ClientNoteAddedEvent
import me.mdjnewman.vetted.event.ClientPhoneNumberAddedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Client {

    @AggregateIdentifier
    final lateinit var id: UUID
        private set

    final lateinit var name: String
        private set

    final lateinit var address: Address
        private set

    private val contactNumbers: MutableSet<PhoneNumber> = mutableSetOf()

    private val notes: MutableSet<ClientNote> = mutableSetOf()

    @CommandHandler
    constructor(command: CreateClientCommand) {
        apply(ClientCreatedEvent(
            clientId = command.clientId,
            name = command.name,
            address = command.address,
            dateCreated = command.dateCreated
        ))
    }

    @CommandHandler
    constructor(command: MigrateClientCommand) {
        apply(ClientMigratedEvent(
            clientId = command.clientId,
            name = command.name,
            address = command.address,
            priorId = command.priorId,
            dateCreated = command.dateCreated
        ))
    }

    @Suppress("unused")
    @Deprecated(message = "Framework use only")
    constructor()

    @CommandHandler
    fun addNote(command: AddNoteToClientCommand) {
        val clientNote = ClientNote(command.noteText)
        if (!notes.contains(clientNote)) {
            apply(ClientNoteAddedEvent(id, clientNote))
        }
    }

    @CommandHandler
    fun addPhoneNumber(command: AddPhoneNumberToClientCommand) {
        if (!contactNumbers.contains(command.phoneNumber)) {
            apply(ClientPhoneNumberAddedEvent(id, command.phoneNumber))
        }
    }

    @EventSourcingHandler
    fun on(event: ClientCreatedEvent) {
        this.id = event.clientId
        this.name = event.name
        this.address = event.address
    }

    @EventSourcingHandler
    fun on(event: ClientMigratedEvent) {
        this.id = event.clientId
        this.name = event.name
        this.address = event.address
    }

    @EventSourcingHandler
    fun on(event: ClientNoteAddedEvent) {
        notes.add(event.clientNote)
    }

    @EventSourcingHandler
    fun on(event: ClientPhoneNumberAddedEvent) {
        contactNumbers.add(event.phoneNumber)
    }
}
