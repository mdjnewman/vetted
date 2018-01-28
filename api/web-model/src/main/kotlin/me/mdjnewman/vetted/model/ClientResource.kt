package me.mdjnewman.vetted.model

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.concurrent.CompletableFuture
import javax.validation.Valid

interface ClientResource {

    companion object {
        const val PATH = "/api/v1/clients"
        const val NAME = "vetted-web"
    }

    @RequestMapping(
        path = arrayOf("/_create"),
        method = arrayOf(RequestMethod.POST)
    )
    fun create(@Valid @RequestBody dto: CreateClientCommandDTO): CompletableFuture<Void>

    @RequestMapping(
        path = arrayOf("/_add-note"),
        method = arrayOf(RequestMethod.POST)
    )
    fun addNote(@Valid @RequestBody dto: AddClientNoteCommandDTO): CompletableFuture<Void>
}