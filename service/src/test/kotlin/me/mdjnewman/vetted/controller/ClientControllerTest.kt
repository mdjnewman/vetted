package me.mdjnewman.vetted.controller

import me.mdjnewman.vetted.command.CreateClientCommand
import me.mdjnewman.vetted.domain.Address
import krafty.test.performIgnorant
import krafty.test.withJsonBody
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = arrayOf(ClientController::class))
class ClientControllerTest {

    @Inject
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var commandGateway: CommandGateway

    @Test
    fun shouldSucceed() {

        val newClientId = UUID.randomUUID()
        val createClientCommand = CreateClientCommand(newClientId, "asdf", Address("asdf", null, "asdf", "asdf", "adf"))

        `when`(commandGateway.send<String>(eq(createClientCommand))).thenReturn(CompletableFuture.completedFuture(newClientId.toString()))

        mockMvc
            .performIgnorant(post("/api/v1/clients/_create").withJsonBody(createClientCommand))
            .andExpect(status().isOk)
            .andDo(print())
            .andExpect(content().string(newClientId.toString()))
    }

    @Test
    fun shouldFailWithValidationErrors() {

        val completedFuture = CompletableFuture.completedFuture("asd")

        `when`(commandGateway.send<String>(isA(CreateClientCommand::class.java))).thenReturn(completedFuture)

        val createClientCommand = CreateClientCommand(UUID.randomUUID(), "", Address("", null, "", "", ""))

        mockMvc
            .performIgnorant(post("/api/v1/clients/_create").withJsonBody(createClientCommand))
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andExpect(jsonPath("httpStatus").value(HttpStatus.BAD_REQUEST.value().toString()))
            .andExpect(jsonPath("details").isArray)
    }

}

