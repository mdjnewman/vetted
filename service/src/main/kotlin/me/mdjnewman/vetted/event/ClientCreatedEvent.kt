package me.mdjnewman.vetted.event

import me.mdjnewman.vetted.domain.Address
import java.util.UUID

data class ClientCreatedEvent(
    val clientId: UUID,
    val name: String,
    val address: Address
)
