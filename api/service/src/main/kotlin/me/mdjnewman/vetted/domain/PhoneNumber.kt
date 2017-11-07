package me.mdjnewman.vetted.domain

import common.validate
import javax.validation.constraints.Pattern

data class PhoneNumber(
        val type: PhoneNumberType,

        @Pattern(regexp = "[0-9]{5,20}")
        val number: String
) {

    init {
        validate()
    }

}