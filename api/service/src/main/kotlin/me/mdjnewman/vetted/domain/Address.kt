package me.mdjnewman.vetted.domain

import javax.validation.constraints.NotEmpty

// TODO - move somewhere else
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