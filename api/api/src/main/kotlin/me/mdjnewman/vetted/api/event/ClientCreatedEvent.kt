package me.mdjnewman.vetted.api.event

import me.mdjnewman.vetted.api.Address
import java.time.ZonedDateTime
import java.util.UUID

sealed class ClientCreatedEvent {
    abstract val clientId: UUID
    abstract val name: String
    abstract val address: Address
    abstract val dateCreated: ZonedDateTime
}

data class NewClientCreatedEvent(
    override val clientId: UUID,
    override val name: String,
    override val address: Address,
    override val dateCreated: ZonedDateTime = ZonedDateTime.now()
) : ClientCreatedEvent()

data class ClientMigratedEvent(
    override val clientId: UUID,
    override val name: String,
    override val address: Address,
    override val dateCreated: ZonedDateTime = ZonedDateTime.now(),
    val priorId: String
) : ClientCreatedEvent()