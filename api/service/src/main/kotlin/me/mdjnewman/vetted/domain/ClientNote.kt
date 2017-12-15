package me.mdjnewman.vetted.domain

import me.mdjnewman.krafty.utils.validate
import java.time.ZonedDateTime
import javax.validation.constraints.NotEmpty

data class ClientNote(
    @get: NotEmpty
    val noteText: String,
    val dateCreated: ZonedDateTime = ZonedDateTime.now()
) {
    init {
        validate()
    }
}
