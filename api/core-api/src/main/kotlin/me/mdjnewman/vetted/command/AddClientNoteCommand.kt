package me.mdjnewman.vetted.command

import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.UUID
import javax.validation.constraints.NotEmpty

data class AddClientNoteCommand(
    @TargetAggregateIdentifier
    val clientId: UUID,

    @get: NotEmpty
    val noteText: String
)
