package me.mdjnewman.vetted.web.config

import me.mdjnewman.vetted.core.VettedCoreMarker
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = arrayOf(VettedCoreMarker::class))
class WebAxonConfig