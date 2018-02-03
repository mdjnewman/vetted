package me.mdjnewman.vetted.api.event

import me.mdjnewman.vetted.api.ClientNote
import java.util.UUID

data class ClientNoteAddedEvent(
    val clientId: UUID,
    val clientNote: ClientNote
)