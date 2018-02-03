package me.mdjnewman.vetted.api.command

import me.mdjnewman.vetted.api.PhoneNumber
import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.UUID

data class AddPhoneNumberToClientCommand(
    @TargetAggregateIdentifier
    val clientId: UUID,

    val phoneNumber: PhoneNumber
)