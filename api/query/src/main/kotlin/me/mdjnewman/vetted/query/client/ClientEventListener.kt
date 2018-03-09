package me.mdjnewman.vetted.query.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import me.mdjnewman.vetted.api.event.ClientCreatedEvent
import me.mdjnewman.vetted.api.event.ClientMigratedEvent
import me.mdjnewman.vetted.api.event.ClientNoteAddedEvent
import org.apache.http.HttpHost
import org.axonframework.eventhandling.EventHandler
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.script.Script
import org.elasticsearch.script.Script.DEFAULT_SCRIPT_LANG
import org.elasticsearch.script.Script.DEFAULT_SCRIPT_TYPE
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ClientEventListener {
    private final val logger = LoggerFactory.getLogger(this.javaClass)!!
    private final val client: RestHighLevelClient
    private final val objectMapper: ObjectMapper

    init {
        logger.info("${this::class.simpleName} instantiated")

        // TODO
        client = RestHighLevelClient(
            RestClient.builder(
                HttpHost("localhost", 9200, "http")))

        // TODO
        objectMapper = ObjectMapper()
        objectMapper.findAndRegisterModules()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

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