package org.starbunk.bunkbot

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.Event
import discord4j.core.`object`.entity.Guild
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.starbunk.bunkbot.listeners.BunkEventListener

@Configuration
open class BunkConfig {
    @Value("\${token}")
    private var token: String = ""

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
    open fun starBunkGuild(client: GatewayDiscordClient, @Value("\${starbunk.guild.id}") guildId: Long): Guild =
        client.getGuildById(Snowflake.of(guildId)).block() ?: throw NullPointerException("Could not find Guild with id $guildId")


    @Bean
    open fun discordRestClient(): RestClient {
        return RestClient.create(token)
    }
}