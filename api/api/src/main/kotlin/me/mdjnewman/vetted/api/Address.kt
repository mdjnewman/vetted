package me.mdjnewman.vetted.api

import javax.validation.constraints.NotEmpty

data class Address(

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