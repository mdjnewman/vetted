package me.mdjnewman.vetted.event

import me.mdjnewman.vetted.Address
import java.time.ZonedDateTime
import java.util.UUID

data class ClientCreatedEvent(
    val clientId: UUID,
    val name: String,
    val address: Address,
    val dateCreated: ZonedDateTime = ZonedDateTime.now()
)

data class ClientMigratedEvent(
    val clientId: UUID,
    val name: String,
    val address: Address,
    val priorId: String,
    val dateCreated: ZonedDateTime = ZonedDateTime.now()
)
