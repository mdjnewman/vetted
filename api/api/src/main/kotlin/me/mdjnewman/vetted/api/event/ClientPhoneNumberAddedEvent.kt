package me.mdjnewman.vetted.api.event

import me.mdjnewman.vetted.api.PhoneNumber
import java.util.UUID

data class ClientPhoneNumberAddedEvent(
    val id: UUID,
    val phoneNumber: PhoneNumber
)