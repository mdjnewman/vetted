package me.mdjnewman.vetted.core.domain

import me.mdjnewman.krafty.utils.validate
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
