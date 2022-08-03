package org.starbunk.bunkbot.bot.command

import discord4j.core.`object`.entity.Message
import discord4j.discordjson.Id
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.MessageCreateListener
import reactor.core.publisher.Mono


class ClearWebhooksCmd: CommandBot() {

    @Autowired
    @org.springframework.context.annotation.Lazy
    private lateinit var restClient: RestClient

    @Bean
    @Qualifier(value = "selfId")
    private fun selfId(client: RestClient): Id {
        return client.self
            .map { it.id() }
            .blockOptional()
            .orElseThrow {
                Exception("Error getting id")
            }
    }

    override val command: String
        get() = "!clearWebhooks"

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { eventMessage.content.equals(command) }
            .flatMap { it.guild }
            .map { it.id.asLong() }
            .map {
                restClient.webhookService.getGuildWebhooks(it)
                    .filter { data ->
                        data.user().get().id() == selfId(restClient)
                    }
                    .mapNotNull { data ->
                        log.warn("Deleting Webhook: ${data.name().get()}")
                        restClient.webhookService
                            .deleteWebhook(data.id().asLong(), "Cova Told Me to.")
                            .block()
                    }
                    .blockLast()
                false
            }
            .then()
}