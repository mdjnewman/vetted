package me.mdjnewman.vetted.command

import me.mdjnewman.vetted.PhoneNumber
import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.UUID

data class AddPhoneNumberToClientCommand(
    @TargetAggregateIdentifier
    val clientId: UUID,

    val phoneNumber: PhoneNumber
)