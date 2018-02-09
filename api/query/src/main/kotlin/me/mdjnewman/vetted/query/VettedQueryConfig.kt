package me.mdjnewman.vetted.query

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan(
    basePackages = [
        "org.axonframework.eventsourcing.eventstore.jpa",
        "org.axonframework.eventhandling.tokenstore",
        "org.axonframework.eventhandling.saga.repository.jpa",
        "me.mdjnewman.vetted.query"
    ]
)
class VettedQueryConfig