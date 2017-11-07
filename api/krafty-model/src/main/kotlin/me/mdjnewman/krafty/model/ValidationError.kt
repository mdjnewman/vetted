package me.mdjnewman.krafty.model

data class ValidationError(
    val field: String,
    val message: String
)