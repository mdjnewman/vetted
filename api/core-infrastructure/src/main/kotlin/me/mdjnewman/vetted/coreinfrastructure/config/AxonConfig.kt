package me.mdjnewman.vetted.coreinfrastructure.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import org.axonframework.commandhandling.AsynchronousCommandBus
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.concurrent.Executors

@Configuration
class AxonConfig {

    @Bean
    @ConditionalOnProperty("axon.use-async-command-bus", matchIfMissing = true)
    fun bus(
        tm: TransactionManager,
        @Value("\${axon.command-bus.executor.pool-size}") poolSize: Int
    ): CommandBus {
        val bus = AsynchronousCommandBus(
            Executors.newFixedThreadPool(poolSize)
        )
        bus.registerHandlerInterceptor(TransactionManagingInterceptor(tm))
        bus.registerDispatchInterceptor(BeanValidationInterceptor<CommandMessage<*>>())

        return bus
    }

    @Primary
    @Bean
    @ConditionalOnProperty("axon.use-cbor-serializer", matchIfMissing = true)
    fun serializer(): Serializer {
        val objectMapper = ObjectMapper(CBORFactory())
        objectMapper.findAndRegisterModules()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        return JacksonSerializer(objectMapper)
    }
}
