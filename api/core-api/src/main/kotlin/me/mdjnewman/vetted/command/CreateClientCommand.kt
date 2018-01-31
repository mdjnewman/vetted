package me.mdjnewman.vetted.command

import me.mdjnewman.vetted.Address
import java.time.ZonedDateTime
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

data class CreateClientCommand(

    val clientId: UUID,

    @get: NotEmpty
    val name: String,

    @get: Valid
    val address: Address,

    val dateCreated: ZonedDateTime = ZonedDateTime.now()

)

data class MigrateClientCommand(

    val clientId: UUID,

    @get: NotEmpty
    val name: String,

    @get: Valid
    val address: Address,

    @get: NotEmpty
    val priorId: String,

    val dateCreated: ZonedDateTime = ZonedDateTime.now()

)
