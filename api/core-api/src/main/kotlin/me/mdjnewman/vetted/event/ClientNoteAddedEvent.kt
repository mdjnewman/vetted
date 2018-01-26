package me.mdjnewman.vetted.event

import me.mdjnewman.vetted.ClientNote
import java.util.UUID

data class ClientNoteAddedEvent(
    val clientId: UUID,
    val clientNote: ClientNote
)