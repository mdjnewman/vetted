package me.mdjnewman.vetted.web.config

import me.mdjnewman.vetted.core.VettedCoreMarker
import me.mdjnewman.vetted.coreinfrastructure.VettedCoreInfrastructureMarker
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [
    VettedCoreMarker::class,
    VettedCoreInfrastructureMarker::class]
)
class WebAxonConfig