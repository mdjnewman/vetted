package me.mdjnewman.vetted.handler

import me.mdjnewman.vetted.domain.Client
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.axonframework.commandhandling.model.Repository
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.util.UUID

@Lazy
@Component
class ClientCommandHandler(
        private val repository: Repository<Client>
) {
    @CommandHandler
    fun handle(command: DoTheThingCommand) {
        try {
            val clientAggregate = repository.load(command.clientId.toString())
            clientAggregate.execute { client -> client.doTheThing() }
        } catch (ex: AggregateNotFoundException) {
//            eventBus.publish(asEventMessage(ClientNotFoundEvent(command.getBankTransferId())));
            throw ex
        }
    }

//
//    @CommandHandler
//    public void handle(CreditDestinationBankAccountCommand command) {
//        try {
//            Aggregate<BankAccount> bankAccountAggregate = repository.load(command.getBankAccountId());
//            bankAccountAggregate.execute(bankAccount -> bankAccount
//                    .credit(command.getAmount(), command.getBankTransferId()));
//
//        }
//        catch (AggregateNotFoundException exception) {
//            eventBus.publish(asEventMessage(new DestinationBankAccountNotFoundEvent(command.getBankTransferId())));
//        }
//    }
}

data class DoTheThingCommand(
        val clientId: UUID
)