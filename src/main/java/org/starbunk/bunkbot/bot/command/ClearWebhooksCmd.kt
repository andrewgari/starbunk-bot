package org.starbunk.bunkbot.bot.command

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Message
import discord4j.discordjson.Id
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.MessageCreateListener
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class ClearWebhooksCmd: CommandBot() {

    override val command = "clearWebhooks"

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

    override fun processMessage(eventMessage: Message): Mono<Void> =
        commandPipeline(eventMessage)
            .flatMap(Message::getGuild)
            .map(Guild::getId)
            .cast(Long::class.java)
            .map {
                restClient.webhookService.getGuildWebhooks(it)
                    .filter { data ->
                        data.user().get().id() == selfId(restClient)
                    }
                    .publishOn(Schedulers.boundedElastic())
                    .mapNotNull { data ->
                        log.warn("Deleting Webhook: ${data.name().get()}")
                        restClient.webhookService
                            .deleteWebhook(data.id().asLong(), "Cova Told Me to.")
                            .block()
                    }
                    .blockLast()
            }
            .then()
}