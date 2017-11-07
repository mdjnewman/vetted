package me.mdjnewman.vetted.controller

import me.mdjnewman.krafty.model.ApiValidationResponse
import me.mdjnewman.krafty.model.ValidationError
import org.axonframework.commandhandling.model.ConcurrencyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ErrorHandlingAdvice {

    @ExceptionHandler(ConcurrencyException::class)
    fun handleException(exception: ConcurrencyException): ResponseEntity<*> {
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body("nope")
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(exception: ConstraintViolationException): ResponseEntity<ApiValidationResponse> {
        return status(HttpStatus.BAD_REQUEST).body(
            ApiValidationResponse(
                exception.constraintViolations.map {
                    ValidationError(
                        field = it.propertyPath.toString(),
                        message = it.message
                    )
                }
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<ApiValidationResponse> {
        return status(HttpStatus.BAD_REQUEST).body(
            ApiValidationResponse(
                exception.bindingResult.fieldErrors.map {
                    ValidationError(
                        field = it.field.toString(),
                        message = it.defaultMessage ?: "Invalid value"
                    )
                }
            )
        )
    }
}
