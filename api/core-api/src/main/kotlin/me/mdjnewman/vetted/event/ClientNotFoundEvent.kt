package me.mdjnewman.vetted.event

import java.util.UUID

data class ClientNotFoundEvent(
    val clientId: UUID
)