package me.mdjnewman.vetted.api.event

import me.mdjnewman.vetted.api.Address
import java.time.ZonedDateTime
import java.util.UUID

data class ClientCreatedEvent(
    val clientId: UUID,
    val name: String,
    val address: Address,
    val dateCreated: ZonedDateTime = ZonedDateTime.now()
)
