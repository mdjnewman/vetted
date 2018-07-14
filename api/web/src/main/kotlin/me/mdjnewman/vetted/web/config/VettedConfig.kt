package me.mdjnewman.vetted.web.config

import me.mdjnewman.vetted.core.VettedCoreConfig
import me.mdjnewman.vetted.coreinfrastructure.VettedCoreInfrastructureConfig
import me.mdjnewman.vetted.query.VettedQueryConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [
    VettedCoreConfig::class,
    VettedCoreInfrastructureConfig::class,
    VettedQueryConfig::class
])
class VettedConfig