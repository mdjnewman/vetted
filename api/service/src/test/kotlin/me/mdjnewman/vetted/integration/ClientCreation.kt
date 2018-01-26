package me.mdjnewman.vetted.integration

import me.mdjnewman.krafty.test.performIgnorant
import me.mdjnewman.krafty.test.withJsonBody
import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.VettedApplication
import me.mdjnewman.vetted.command.AddClientNoteCommand
import me.mdjnewman.vetted.command.CreateClientCommand
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(VettedApplication::class), webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class ClientCreation {

    @Inject
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldRejectDuplicateCreation() {
        val createClientCommand = CreateClientCommand(
            name = "Fred Smith",
            clientId = UUID.randomUUID(),
            address = Address(
                addressLineOne = "1 One Lane",
                town = "Townsville",
                state = "Nope",
                postcode = "4006"
            )
        )

        mockMvc
            .performIgnorant(
                post("/api/v1/clients/_create").withJsonBody(createClientCommand)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().string(CoreMatchers.containsString("-")))

        mockMvc
            .performIgnorant(
                post("/api/v1/clients/_create").withJsonBody(createClientCommand)
            )
            .andExpect(status().is5xxServerError)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldBeMovedSomewhereElse() {
        val createClientCommand = CreateClientCommand(
            name = "Fred Smith",
            clientId = UUID.randomUUID(),
            address = Address(
                addressLineOne = "1 One Lane",
                town = "Townsville",
                state = "Nope",
                postcode = "4006"
            )
        )

        mockMvc
            .performIgnorant(
                post("/api/v1/clients/_create").withJsonBody(createClientCommand)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().string(CoreMatchers.containsString("-")))

        val addClientNoteCommand = AddClientNoteCommand(
            clientId = createClientCommand.clientId,
            noteText = "Lovely note about a client"
        )

        mockMvc
            .performIgnorant(
                post("/api/v1/clients/_add-note").withJsonBody(addClientNoteCommand)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }
}
