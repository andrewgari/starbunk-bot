package org.starbunk.bunkbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Component

@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "starbunk")
class BunkProperties {
}