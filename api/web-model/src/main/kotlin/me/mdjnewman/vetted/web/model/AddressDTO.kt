package me.mdjnewman.vetted.web.model

import javax.validation.constraints.NotEmpty

data class AddressDTO(

    @get: NotEmpty
    val addressLineOne: String,

    val addressLineTwo: String? = null,

    @get: NotEmpty
    val town: String,

    @get: NotEmpty
    val state: String,

    @get: NotEmpty
    val postcode: String

)