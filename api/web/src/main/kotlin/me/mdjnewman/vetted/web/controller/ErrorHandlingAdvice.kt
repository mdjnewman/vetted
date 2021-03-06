package me.mdjnewman.vetted.web.controller

import me.mdjnewman.krafty.model.ApiValidationErrorDetails
import me.mdjnewman.krafty.model.ApiValidationResponse
import me.mdjnewman.krafty.model.ResourceNotFoundDetails
import me.mdjnewman.krafty.model.ResourceNotFoundResponse
import me.mdjnewman.krafty.model.UnknownErrorResponse
import me.mdjnewman.vetted.web.model.ErrorCode
import me.mdjnewman.vetted.web.model.ErrorCode.RESOURCE_NOT_FOUND
import me.mdjnewman.vetted.web.model.ErrorCode.VALIDATION_ERROR
import org.axonframework.commandhandling.model.AggregateNotFoundException
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
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<*> {
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            UnknownErrorResponse(errorCode = ErrorCode.UNKNOWN_ERROR)
        )
    }

    @ExceptionHandler(ConcurrencyException::class)
    fun handleException(exception: ConcurrencyException): ResponseEntity<*> {
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body("nope")
    }

    @ExceptionHandler(AggregateNotFoundException::class)
    fun handleException(exception: AggregateNotFoundException): ResponseEntity<*> {
        return status(HttpStatus.NOT_FOUND).body(
            ResourceNotFoundResponse(
                details = listOf(ResourceNotFoundDetails(exception.aggregateIdentifier)),
                errorCode = RESOURCE_NOT_FOUND
            )
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(exception: ConstraintViolationException): ResponseEntity<ApiValidationResponse<ErrorCode>> {
        return status(HttpStatus.BAD_REQUEST).body(
            ApiValidationResponse(
                exception.constraintViolations.map {
                    ApiValidationErrorDetails(
                        field = it.propertyPath.toString(),
                        message = it.message
                    )
                },
                errorCode = VALIDATION_ERROR
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<ApiValidationResponse<ErrorCode>> {
        return status(HttpStatus.BAD_REQUEST).body(
            ApiValidationResponse(
                details = exception.bindingResult.fieldErrors.map {
                    ApiValidationErrorDetails(
                        field = it.field.toString(),
                        message = it.defaultMessage ?: "Invalid value"
                    )
                },
                errorCode = VALIDATION_ERROR
            )
        )
    }
}
