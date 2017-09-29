package common

import javax.validation.ConstraintViolationException
import javax.validation.Validation

fun Any.validate() {
    val factory = Validation.buildDefaultValidatorFactory()
    val validator = factory.validator
    val set = validator.validate(this)
    if (!set.isEmpty()) {
        throw ConstraintViolationException("Error creating ${this::class.simpleName}", set)
    }
}