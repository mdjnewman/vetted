package me.mdjnewman.vetted.query.client

import me.mdjnewman.vetted.api.event.ClientCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ClientEventListener(
    val clientEntryRepository: ClientEntryRepository
) {
    private final val logger = LoggerFactory.getLogger(this.javaClass)!!

    init {
        logger.info("${this::class.simpleName} instantiated")
    }

    @EventHandler
    fun on(clientCreatedEvent: ClientCreatedEvent) {
        val clientEntry = ClientEntry(
            id = clientCreatedEvent.clientId
        )
        clientEntryRepository.saveAndFlush(clientEntry)
    }
}