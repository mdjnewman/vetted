package me.mdjnewman.vetted.web.model

import java.util.UUID
import javax.validation.constraints.NotEmpty

data class AddClientNoteCommandDTO(
    val clientId: UUID,

    @get: NotEmpty
    val noteText: String
)
