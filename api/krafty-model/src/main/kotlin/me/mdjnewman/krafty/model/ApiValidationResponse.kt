package me.mdjnewman.krafty.model

data class ApiValidationResponse<out E>(
    override val details: List<ApiValidationErrorDetails>,
    override val errorCode: E
) : ApiErrorResponse<ApiValidationErrorDetails, E>
