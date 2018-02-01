package me.mdjnewman.vetted

import me.mdjnewman.krafty.utils.validate
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class PhoneNumber(
    @Size(max = 50)
    val type: String,

    @Pattern(regexp = "[+0-9]{5,20}")
    val number: String
) {
    init {
        validate()
    }
}
