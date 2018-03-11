package me.mdjnewman.vetted.importer

import me.mdjnewman.vetted.core.VettedCoreConfig
import me.mdjnewman.vetted.coreinfrastructure.VettedCoreInfrastructureConfig
import me.mdjnewman.vetted.query.VettedQueryConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [
    VettedCoreConfig::class,
    VettedCoreInfrastructureConfig::class,
    VettedQueryConfig::class
])
class Application

fun main(args: Array<String>) {
    SpringApplication
        .run(Application::class.java, *args)
        .close()
}
