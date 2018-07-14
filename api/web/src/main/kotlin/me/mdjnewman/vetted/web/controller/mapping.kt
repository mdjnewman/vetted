package me.mdjnewman.vetted.web.controller

import me.mdjnewman.vetted.api.Address
import me.mdjnewman.vetted.api.command.AddNoteToClientCommand
import me.mdjnewman.vetted.api.command.CreateClientCommand
import me.mdjnewman.vetted.query.client.ClientDocument
import me.mdjnewman.vetted.web.model.AddClientNoteCommandDTO
import me.mdjnewman.vetted.web.model.AddressDTO
import me.mdjnewman.vetted.web.model.ClientDTO
import me.mdjnewman.vetted.web.model.ClientNoteDTO
import me.mdjnewman.vetted.web.model.CreateClientCommandDTO
import me.mdjnewman.vetted.web.model.PhoneNumberDTO

fun CreateClientCommandDTO.toCreateClientCommand(): CreateClientCommand =
    CreateClientCommand(
        clientId = this.clientId,
        address = this.address.let {
            Address(
                addressLineOne = it.addressLineOne,
                addressLineTwo = it.addressLineTwo,
                town = it.town,
                state = it.state,
                postcode = it.postcode
            )
        },
        name = this.name
    )

fun AddClientNoteCommandDTO.toAddNoteToClientCommand() =
    AddNoteToClientCommand(
        clientId = this.clientId,
        noteText = this.noteText
    )

fun ClientDocument.toClientDTO() = ClientDTO(
    id = this.id,
    name = this.name,
    address = this.address.let {
        AddressDTO(
            addressLineOne = it.addressLineOne,
            addressLineTwo = it.addressLineTwo,
            town = it.town,
            state = it.state,
            postcode = it.postcode
        )
    },
    dateCreated = this.dateCreated,
    notes = this.notes.mapTo(mutableSetOf(), {
        ClientNoteDTO(
            noteText = it.noteText
        )
    }),
    priorId = this.priorId,
    contactNumbers = this.contactNumbers.mapTo(mutableSetOf(), {
        PhoneNumberDTO(
            type = it.type,
            number = it.number
        )
    })
)