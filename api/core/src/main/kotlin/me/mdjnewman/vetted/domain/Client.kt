package me.mdjnewman.vetted.domain

import me.mdjnewman.vetted.event.ClientCreatedEvent
import me.mdjnewman.vetted.event.ClientNoteAddedEvent
import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.ClientNote
import me.mdjnewman.vetted.command.AddClientNoteCommand
import me.mdjnewman.vetted.command.CreateClientCommand
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

    private var contactName: String? = null

    private var regularTravelDistance: Int? = null

    private var isBadDebtor: Boolean = false

    private val contactNumbers: MutableSet<PhoneNumber> = mutableSetOf()

    private val notes: MutableSet<ClientNote> = mutableSetOf()

    @CommandHandler
    constructor(command: CreateClientCommand) {
        apply(ClientCreatedEvent(
            clientId = command.clientId,
            name = command.name,
            address = command.address
        ))
    }

    @Suppress("unused")
    @Deprecated(message = "Framework use only")
    constructor()

    @CommandHandler
    fun addNote(command: AddClientNoteCommand) {
        val clientNote = ClientNote(command.noteText)
        if (!notes.contains(clientNote)) {
            apply(ClientNoteAddedEvent(id, clientNote))
        }
    }

    @EventSourcingHandler
    fun on(event: ClientCreatedEvent) {
        this.id = event.clientId
        this.name = event.name
        this.address = event.address
    }

    @EventSourcingHandler
    fun on(event: ClientNoteAddedEvent) {
        notes.add(event.clientNote)
    }
}