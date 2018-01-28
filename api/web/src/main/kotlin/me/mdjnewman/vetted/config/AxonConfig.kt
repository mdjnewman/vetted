package me.mdjnewman.vetted.config

import me.mdjnewman.vetted.domain.Client
import me.mdjnewman.vetted.handler.ClientCommandHandler
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.eventhandling.EventBus
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.spring.config.AxonConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {

    @Autowired
    private lateinit var axonConfiguration: AxonConfiguration

    @Autowired
    private lateinit var eventBus: EventBus

    @Bean
    fun clientCommandHandler(): ClientCommandHandler {
        return ClientCommandHandler(axonConfiguration.repository(Client::class.java), eventBus)
    }

    //    @Bean
    //    public SagaConfiguration bankTransferManagementSagaConfiguration() {
    //        return SagaConfiguration.trackingSagaManager(BankTransferManagementSaga.class);
    //    }

    @Autowired
    fun configure(@Qualifier("localSegment") simpleCommandBus: SimpleCommandBus) {
        simpleCommandBus.registerDispatchInterceptor(BeanValidationInterceptor<CommandMessage<*>>())
    }
}
