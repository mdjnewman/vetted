package me.mdjnewman.vetted.domain

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test
import javax.validation.ConstraintViolationException

class ClientNoteTest {
    @Test
    fun shouldFailValidationWhenEmpty() {
        try {
            ClientNote(noteText = "")
            fail()
        } catch (cve: ConstraintViolationException) {
            assertThat(cve.constraintViolations, hasSize(1))
            assertThat(cve.constraintViolations.first().propertyPath.toString(), equalTo(ClientNote::noteText.name))
        }
    }
}
