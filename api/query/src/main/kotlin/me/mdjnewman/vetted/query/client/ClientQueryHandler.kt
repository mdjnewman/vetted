package me.mdjnewman.vetted.query.client

import com.fasterxml.jackson.databind.ObjectMapper
import me.mdjnewman.vetted.query.bindCompletableFuture
import org.axonframework.queryhandling.QueryHandler
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.Requests
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ClientQueryHandler(
    private val esClient: RestHighLevelClient,
    private val objectMapper: ObjectMapper
) {

    @QueryHandler
    fun handleSearchQuery(searchTerm: String?): CompletableFuture<List<ClientDocument>> {

        val queryBuilder = if (searchTerm != null && searchTerm.isNotEmpty())
            QueryBuilders.queryStringQuery(searchTerm)
        else QueryBuilders.matchAllQuery()

        val cf = CompletableFuture<SearchResponse>()

        esClient
            .searchAsync(
                Requests
                    .searchRequest("clients")
                    .types("doc")
                    .source(
                        SearchSourceBuilder.searchSource()
                            .query(queryBuilder)
                            .size(10000)
                    ),
                bindCompletableFuture(cf)
            )

        return cf
            .thenApply { it.hits.map { objectMapper.convertValue(it.source, ClientDocument::class.java)!! } }
    }
}