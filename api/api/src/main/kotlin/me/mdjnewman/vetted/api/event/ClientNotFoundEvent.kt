package me.mdjnewman.vetted.api.event

import java.util.UUID

data class ClientNotFoundEvent(
    val clientId: UUID
)