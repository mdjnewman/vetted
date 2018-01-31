package me.mdjnewman.vetted.web.controller

import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.command.AddClientNoteCommand
import me.mdjnewman.vetted.command.CreateClientCommand
import me.mdjnewman.vetted.model.AddClientNoteCommandDTO
import me.mdjnewman.vetted.model.ClientResource
import me.mdjnewman.vetted.model.ClientResource.Companion.PATH
import me.mdjnewman.vetted.model.CreateClientCommandDTO
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import javax.validation.Valid

@RestController
@RequestMapping(PATH)
class ClientController(
    private val commandGateway: CommandGateway
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
            AddClientNoteCommand(
                clientId = dto.clientId,
                noteText = dto.noteText
            )
        )
    }
}
