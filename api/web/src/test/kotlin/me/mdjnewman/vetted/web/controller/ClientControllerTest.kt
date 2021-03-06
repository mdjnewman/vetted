package me.mdjnewman.vetted.web.controller

import me.mdjnewman.krafty.test.performIgnorant
import me.mdjnewman.krafty.test.withJsonBody
import me.mdjnewman.vetted.api.command.CreateClientCommand
import me.mdjnewman.vetted.web.model.AddressDTO
import me.mdjnewman.vetted.web.model.CreateClientCommandDTO
import me.mdjnewman.vetted.web.model.ErrorCode
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.elasticsearch.client.RestHighLevelClient
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
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

    @MockBean
    private lateinit var queryGateway: QueryGateway

    /**
     * TODO - shouldn't be required
     */
    @MockBean
    private lateinit var esClient: RestHighLevelClient

    @Test
    fun shouldSucceed() {

        val newClientId = UUID.randomUUID()
        val createClientCommand = CreateClientCommandDTO(
            newClientId,
            "asdf",
            AddressDTO("asdf", null, "asdf", "asdf", "adf")
        )

        `when`(commandGateway.send<Void>(any(CreateClientCommand::class.java)))
            .thenReturn(CompletableFuture.completedFuture(null))

        mockMvc
            .performIgnorant(post("/api/v1/clients/_create").withJsonBody(createClientCommand))
            .andExpect(status().isOk)
            .andDo(print())
            .andExpect(content().string(""))
    }

    @Test
    fun shouldFailWithValidationErrors() {

        val createClientCommand = CreateClientCommandDTO(
            UUID.randomUUID(),
            "",
            AddressDTO("", null, "", "", "")
        )

        mockMvc
            .performIgnorant(post("/api/v1/clients/_create").withJsonBody(createClientCommand))
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andExpect(jsonPath("errorCode").value(ErrorCode.VALIDATION_ERROR.toString()))
            .andExpect(jsonPath("details").isArray)
    }
}
