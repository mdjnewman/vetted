package me.mdjnewman.vetted.core.domain

import me.mdjnewman.vetted.Address
import me.mdjnewman.vetted.command.CreateClientCommand
import me.mdjnewman.vetted.event.ClientCreatedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ClientTests {

    private lateinit var fixture: FixtureConfiguration<Client>

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(Client::class.java)
    }

    @Test
    fun shouldInstantiateFromCreateClientCommand() {
        val clientId = UUID.randomUUID()

        val command = CreateClientCommand(clientId, "asd", Address(
            addressLineOne = "asdas",
            postcode = "4006",
            state = "QLD",
            town = "Warwick"
        ))

        fixture
            .givenNoPriorActivity()
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(ClientCreatedEvent(clientId, command.name, command.address))
    }
}
