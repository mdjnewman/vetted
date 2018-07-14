package me.mdjnewman.vetted.web.model

import java.util.concurrent.CompletableFuture

interface ClientResource {

    companion object {
        const val PATH = "/api/v1/clients"
    }

    fun create(dto: CreateClientCommandDTO): CompletableFuture<Void>
    fun addNote(dto: AddClientNoteCommandDTO): CompletableFuture<Void>
    fun clients(searchTerm: String?): CompletableFuture<GetClientsResult>
}
