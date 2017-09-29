package me.mdjnewman.vetted.domain

import common.validate
import java.time.ZonedDateTime
import javax.validation.constraints.NotEmpty

data class ClientNote(
        @get: NotEmpty
        val noteText: String
) {
    val dateCreated = ZonedDateTime.now()

    init {
        validate()
    }
}
