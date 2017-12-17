package me.mdjnewman.krafty.model

data class ApiValidationErrorDetails(
    val field: String,
    val message: String
)