package me.mdjnewman.vetted.client

import me.mdjnewman.vetted.model.AddClientNoteCommandDTO
import me.mdjnewman.vetted.model.ClientResource
import me.mdjnewman.vetted.model.CreateClientCommandDTO
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.CompletableFuture

interface ClientClient : ClientResource {

    @POST("/api/v1/clients/_create")
    override fun create(@Body dto: CreateClientCommandDTO): CompletableFuture<Void>

    @POST("/api/v1/clients/_add-note")
    override fun addNote(@Body dto: AddClientNoteCommandDTO): CompletableFuture<Void>
}