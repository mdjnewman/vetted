package me.mdjnewman.vetted.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.mdjnewman.vetted.api.Address
import me.mdjnewman.vetted.api.command.AddNoteToClientCommand
import me.mdjnewman.vetted.api.command.CreateClientCommand
import me.mdjnewman.vetted.query.client.ClientDocument
import me.mdjnewman.vetted.web.model.AddClientNoteCommandDTO
import me.mdjnewman.vetted.web.model.ClientResource
import me.mdjnewman.vetted.web.model.ClientResource.Companion.PATH
import me.mdjnewman.vetted.web.model.CreateClientCommandDTO
import org.axonframework.commandhandling.gateway.CommandGateway
import org.elasticsearch.client.Requests
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders.matchAllQuery
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.elasticsearch.search.builder.SearchSourceBuilder.searchSource
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
    private val esClient: RestHighLevelClient,
    private val objectMapper: ObjectMapper
) : ClientResource {
    @RequestMapping(
        path = arrayOf("/_create"),
        method = arrayOf(POST)
    )
    override fun create(@Valid @RequestBody dto: CreateClientCommandDTO): CompletableFuture<Void> {
        return commandGateway.send<Void>(
            CreateClientCommand(
                clientId = dto.clientId,
                address = dto.address.let {
                    Address(
                        addressLineOne = it.addressLineOne,
                        addressLineTwo = it.addressLineTwo,
                        town = it.town,
                        state = it.state,
                        postcode = it.postcode
                    )
                },
                name = dto.name
            )
        )
    }

    @RequestMapping(
        path = arrayOf("/_add-note"),
        method = arrayOf(POST)
    )
    override fun addNote(@Valid @RequestBody dto: AddClientNoteCommandDTO): CompletableFuture<Void> {
        return commandGateway.send<Void>(
            AddNoteToClientCommand(
                clientId = dto.clientId,
                noteText = dto.noteText
            )
        )
    }

    /**
     * TODO - refactor the below, async, paging, etc
     */
    @RequestMapping(
        path = arrayOf("/"),
        method = arrayOf(RequestMethod.GET)
    )
    fun clients(
        @RequestParam("searchTerm", required = false) searchTerm: String?
    ): CompletableFuture<List<ClientDocument>> {

        val queryBuilder = if (searchTerm != null && searchTerm.isNotEmpty())
            queryStringQuery(searchTerm)
        else matchAllQuery()

        val values = esClient
            .search(
                Requests
                    .searchRequest("clients")
                    .types("doc")
                    .source(
                        searchSource()
                            .query(queryBuilder)
                            .size(10000)
                    )
            )
            .hits.map { objectMapper.convertValue(it.source, ClientDocument::class.java)!! }

        return CompletableFuture.completedFuture(values)
    }
}
