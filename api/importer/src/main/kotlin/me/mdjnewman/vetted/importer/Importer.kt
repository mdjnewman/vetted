package me.mdjnewman.vetted.importer

import me.mdjnewman.vetted.core.VettedCoreMarker
import me.mdjnewman.vetted.coreinfrastructure.VettedCoreInfrastructureMarker
import me.mdjnewman.vetted.query.VettedQueryConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@ComponentScan(basePackageClasses = [
    Application::class,
    VettedCoreMarker::class,
    VettedCoreInfrastructureMarker::class]
)
@Import(value = arrayOf(VettedQueryConfig::class))
class Application

fun main(args: Array<String>) {
    val context = SpringApplication.run(Application::class.java, *args)
    context.close()
}
