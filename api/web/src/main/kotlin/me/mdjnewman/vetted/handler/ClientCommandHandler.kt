package me.mdjnewman.vetted.handler

import me.mdjnewman.vetted.domain.Client
import org.axonframework.commandhandling.model.Repository
import org.axonframework.eventhandling.EventBus
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class ClientCommandHandler(
    private val repository: Repository<Client>,
    private val eventBus: EventBus
)