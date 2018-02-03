package me.mdjnewman.vetted.core.domain

import me.mdjnewman.vetted.api.Address
import me.mdjnewman.vetted.api.command.CreateClientCommand
import me.mdjnewman.vetted.api.event.ClientCreatedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime
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

        val dateCreated = ZonedDateTime.now()

        val command = CreateClientCommand(
            clientId = clientId,
            name = "asd",
            address = Address(
                addressLineOne = "asdas",
                postcode = "4006",
                state = "QLD",
                town = "Warwick"
            ),
            dateCreated = dateCreated
        )

        fixture
            .givenNoPriorActivity()
            .`when`(command)
            .expectSuccessfulHandlerExecution()
            .expectEvents(ClientCreatedEvent(clientId, command.name, command.address, dateCreated))
    }
}
