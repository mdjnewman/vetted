package me.mdjnewman.vetted.importer

import me.mdjnewman.vetted.core.VettedCoreMarker
import me.mdjnewman.vetted.coreinfrastructure.VettedCoreInfrastructureMarker
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackageClasses = [
    Application::class,
    VettedCoreMarker::class,
    VettedCoreInfrastructureMarker::class]
)
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
