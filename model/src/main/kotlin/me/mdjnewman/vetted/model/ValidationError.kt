package me.mdjnewman.vetted.model

data class ValidationError(
    val field: String,
    val message: String
)