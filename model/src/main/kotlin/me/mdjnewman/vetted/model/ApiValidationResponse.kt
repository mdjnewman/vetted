package me.mdjnewman.vetted.model

import org.springframework.http.HttpStatus

data class ApiValidationResponse(
    override val details: List<ValidationError>
) : ApiErrorResponse<ValidationError> {
    override val httpStatus: Int = HttpStatus.BAD_REQUEST.value()
}
