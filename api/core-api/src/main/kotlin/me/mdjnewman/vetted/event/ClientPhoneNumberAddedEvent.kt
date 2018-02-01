package me.mdjnewman.vetted.event

import me.mdjnewman.vetted.PhoneNumber
import java.util.UUID

data class ClientPhoneNumberAddedEvent(
    val id: UUID,
    val phoneNumber: PhoneNumber
)