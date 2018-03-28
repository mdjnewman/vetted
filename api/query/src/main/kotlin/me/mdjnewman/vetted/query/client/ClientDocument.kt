package me.mdjnewman.vetted.query.client

import me.mdjnewman.vetted.api.Address
import me.mdjnewman.vetted.api.ClientNote
import me.mdjnewman.vetted.api.PhoneNumber
import java.time.ZonedDateTime
import java.util.UUID

class ClientDocument(
    val id: UUID,
    val name: String,
    val address: Address,
    val dateCreated: ZonedDateTime,
    val notes: Set<ClientNote>,
    val priorId: String?,
    val contactNumbers: Set<PhoneNumber>
)
