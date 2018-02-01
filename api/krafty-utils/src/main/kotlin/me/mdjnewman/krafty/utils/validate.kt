package me.mdjnewman.krafty.utils

import javax.validation.ConstraintViolationException
import javax.validation.Validation

private val validator = Validation.buildDefaultValidatorFactory().validator

fun Any.validate() {
    val set = validator.validate(this)
    if (!set.isEmpty()) {
        throw ConstraintViolationException("Error creating ${this::class.simpleName}", set)
    }
}