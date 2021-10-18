package org.starbunk.bunkbot

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.Event
import discord4j.discordjson.Id
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.starbunk.bunkbot.listeners.BunkEventListener
import java.lang.NullPointerException

@Configuration
open class BunkConfig {
    @Value("\${token}")
    private val token: String = ""

    @Bean
    @Qualifier (value = "vennId")
    open fun vennId(): Long = 151120340343455744

    @Bean
    @Qualifier (value = "covaId")
    open fun covaId(): Long = 139592376443338752

    @Bean
    @Qualifier (value = "pickleId")
    open fun pickleId(): Long = 486516247576444928

    @Bean
    @Qualifier (value = "guyId")
    open fun guyId(): Long = 113035990725066752

    @Bean
    @Qualifier(value = "selfId")
    open fun selfId(client: RestClient): Id {
        return client.self
            .map { it.id() }
            .blockOptional()
            .orElseThrow()
    }

    @Bean
    open fun <T : Event> gatewayDiscordClient(eventListeners: List<BunkEventListener<T>>): GatewayDiscordClient {
        val client = DiscordClientBuilder.create(token)
            .build()
            .login()
            .block()
        if (client != null) {
            for (listener in eventListeners) {
                client.on(listener.eventType)
                    .flatMap { event: T ->
                        listener.execute(
                            event
                        )
                    }
                    .onErrorResume { error: Throwable? ->
                        listener.handleError(
                            error
                        )
                    }
                    .subscribe()
            }
            return client
        }
        throw NullPointerException("Could not establish Gateway with Client")
    }

    @Bean
    open fun discordRestClient(): RestClient {
        return RestClient.create(token)
    }
}