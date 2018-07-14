package me.mdjnewman.vetted.web.controller

import me.mdjnewman.vetted.query.client.ClientDocument
import me.mdjnewman.vetted.web.model.AddClientNoteCommandDTO
import me.mdjnewman.vetted.web.model.ClientResource
import me.mdjnewman.vetted.web.model.ClientResource.Companion.PATH
import me.mdjnewman.vetted.web.model.CreateClientCommandDTO
import me.mdjnewman.vetted.web.model.GetClientsResult
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import javax.validation.Valid

@RestController
@RequestMapping(PATH)
class ClientController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) : ClientResource {
    @RequestMapping(
        path = ["/_create"],
        method = [POST]
    )
    override fun create(@Valid @RequestBody dto: CreateClientCommandDTO): CompletableFuture<Void> {
        return commandGateway.send<Void>(
            dto.toCreateClientCommand()
        )
    }

    @RequestMapping(
        path = ["/_add-note"],
        method = [POST]
    )
    override fun addNote(@Valid @RequestBody dto: AddClientNoteCommandDTO): CompletableFuture<Void> {
        return commandGateway.send<Void>(
            dto.toAddNoteToClientCommand()
        )
    }

    /**
     * TODO - paging etc
     */
    @RequestMapping(
        path = ["/"],
        method = [(RequestMethod.GET)]
    )
    override fun clients(
        @RequestParam("searchTerm", required = false) searchTerm: String?
    ): CompletableFuture<GetClientsResult> {

        val searchResult = queryGateway.queryMany<String?, ClientDocument>(searchTerm)

        return searchResult
            .thenApply {
                GetClientsResult(
                    data = it.map { it.toClientDTO() }
                )
            }
    }
}