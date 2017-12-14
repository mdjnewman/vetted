package me.mdjnewman.vetted.controller

import me.mdjnewman.vetted.model.command.CreateClientCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/clients")
class ClientController(
    private val commandGateway: CommandGateway
) {

    @RequestMapping(
        path = arrayOf("/_create"),
        method = arrayOf(RequestMethod.POST)
    )
    fun create(@Valid @RequestBody createClientCommand: CreateClientCommand): CompletableFuture<String> {
        return commandGateway.send<String>(createClientCommand)
    }

}
