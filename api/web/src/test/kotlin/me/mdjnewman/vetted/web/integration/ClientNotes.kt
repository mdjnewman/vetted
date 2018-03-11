package me.mdjnewman.vetted.web.integration

import me.mdjnewman.krafty.test.performIgnorant
import me.mdjnewman.krafty.test.withJsonBody
import me.mdjnewman.vetted.api.command.AddNoteToClientCommand
import me.mdjnewman.vetted.web.Application
import me.mdjnewman.vetted.web.model.AddressDTO
import me.mdjnewman.vetted.web.model.CreateClientCommandDTO
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class), webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class ClientNotes {

    @Inject
    private lateinit var mockMvc: MockMvc

    @Test
    fun shouldAddNoteToClient() {
        val createClientCommand = CreateClientCommandDTO(
            name = "Fred Smith",
            clientId = UUID.randomUUID(),
            address = AddressDTO(
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

        val addClientNoteCommand = AddNoteToClientCommand(
            clientId = createClientCommand.clientId,
            noteText = "Lovely note about a client"
        )

        mockMvc
            .performIgnorant(
                post("/api/v1/clients/_add-note").withJsonBody(addClientNoteCommand)
            )
            .andExpect(status().isOk)
    }
}
