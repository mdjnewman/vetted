package me.mdjnewman.vetted.api.command

import me.mdjnewman.vetted.api.PhoneNumber
import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.UUID
import javax.validation.Valid

data class AddPhoneNumberToClientCommand(
    @TargetAggregateIdentifier
    val clientId: UUID,

    @get: Valid
    val phoneNumber: PhoneNumber
)