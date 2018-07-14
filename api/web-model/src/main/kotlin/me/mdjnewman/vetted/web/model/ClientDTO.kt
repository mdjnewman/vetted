package me.mdjnewman.vetted.web.model

import java.time.ZonedDateTime
import java.util.UUID

data class ClientDTO(
    val id: UUID,
    val name: String,
    val address: AddressDTO,
    val dateCreated: ZonedDateTime,
    val notes: Set<ClientNoteDTO>,
    val priorId: String?,
    val contactNumbers: Set<PhoneNumberDTO>
)