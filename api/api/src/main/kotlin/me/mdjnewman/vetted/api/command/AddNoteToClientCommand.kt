package me.mdjnewman.vetted.api.command

import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.UUID
import javax.validation.constraints.NotEmpty

data class AddNoteToClientCommand(
    @TargetAggregateIdentifier
    val clientId: UUID,

    @get: NotEmpty
    val noteText: String
)
