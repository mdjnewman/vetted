package me.mdjnewman.vetted.handler

import me.mdjnewman.vetted.domain.Client
import org.axonframework.commandhandling.model.Repository
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class ClientCommandHandler(
    private val repository: Repository<Client>
) {
//    @CommandHandler
//    fun handle(command: DoTheThingCommand) {
//        try {
//            val clientAggregate = repository.load(command.clientId.toString())
//            clientAggregate.execute { client -> client.doTheThing() }
//        } catch (ex: AggregateNotFoundException) {
//            eventBus.publish(asEventMessage(ClientNotFoundEvent(command.getBankTransferId())));
//            throw ex
//        }
//    }
}
