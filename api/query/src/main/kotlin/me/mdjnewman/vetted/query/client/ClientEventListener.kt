package me.mdjnewman.vetted.query.client

import com.fasterxml.jackson.databind.ObjectMapper
import me.mdjnewman.vetted.api.event.ClientCreatedEvent
import me.mdjnewman.vetted.api.event.ClientMigratedEvent
import me.mdjnewman.vetted.api.event.ClientNoteAddedEvent
import org.axonframework.eventhandling.EventHandler
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.script.Script
import org.elasticsearch.script.Script.DEFAULT_SCRIPT_LANG
import org.elasticsearch.script.Script.DEFAULT_SCRIPT_TYPE
import org.springframework.stereotype.Component

@Component
class ClientEventListener(
    private val client: RestHighLevelClient,
    private val objectMapper: ObjectMapper
) {
    @EventHandler
    fun on(clientCreatedEvent: ClientCreatedEvent) {

        val clientEntry = ClientDocument(
            id = clientCreatedEvent.clientId,
            name = clientCreatedEvent.name,
            address = clientCreatedEvent.address,
            dateCreated = clientCreatedEvent.dateCreated,
            notes = mutableSetOf(),
            priorId = (clientCreatedEvent as? ClientMigratedEvent)?.priorId
        )

        val request = IndexRequest(
            "clients",
            "doc",
            clientCreatedEvent.clientId.toString()
        )

        val jsonString = objectMapper.writeValueAsString(clientEntry)

        request.source(jsonString, XContentType.JSON)

        client.index(request)
    }

    @EventHandler
    fun on(event: ClientNoteAddedEvent) {
        val convertValue = objectMapper.convertValue(event.clientNote, Map::class.java)
        val request = UpdateRequest("clients", "doc", event.clientId.toString())
            .script(
                Script(
                    DEFAULT_SCRIPT_TYPE,
                    DEFAULT_SCRIPT_LANG,
                    "ctx._source.notes.add(params.note)",
                    mapOf("note" to convertValue)
                ))
        client.update(request)
    }
}