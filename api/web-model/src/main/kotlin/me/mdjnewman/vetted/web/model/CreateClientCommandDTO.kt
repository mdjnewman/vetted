package me.mdjnewman.vetted.web.model

import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

data class CreateClientCommandDTO(

    val clientId: UUID,

    @get: NotEmpty
    val name: String,

    @get: Valid
    val address: AddressDTO

)
