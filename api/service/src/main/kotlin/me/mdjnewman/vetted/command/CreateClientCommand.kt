package me.mdjnewman.vetted.command

import me.mdjnewman.vetted.domain.Address
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

data class CreateClientCommand(

    val clientId: UUID,

    @get: NotEmpty
    val name: String,

    @get: Valid
    val address: Address

)
