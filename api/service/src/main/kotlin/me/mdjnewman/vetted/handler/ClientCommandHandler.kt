package me.mdjnewman.vetted.handler

import me.mdjnewman.vetted.domain.Client
import me.mdjnewman.vetted.event.ClientNotFoundEvent
import me.mdjnewman.vetted.model.command.AddClientNoteCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.axonframework.commandhandling.model.Repository
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage.asEventMessage
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class ClientCommandHandler(
    private val repository: Repository<Client>,
    private val eventBus: EventBus
) {
    @CommandHandler
    fun handle(command: AddClientNoteCommand) {
        try {
            val clientAggregate = repository.load(command.clientId.toString())
            clientAggregate.execute { client -> client.addNote(command.noteText) }
        } catch (ex: AggregateNotFoundException) {
            eventBus.publish(asEventMessage<ClientNotFoundEvent>(ClientNotFoundEvent(command.clientId)))
            throw ex
        }
    }
}
