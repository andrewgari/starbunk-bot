package org.starbunk.bunkbot.bot.command

import discord4j.core.`object`.entity.Message
import discord4j.discordjson.Id
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.MessageCreateListener
import reactor.core.publisher.Mono

@Service
class ClearWebhooksCmd: MessageCreateListener(), CommandBot {

    @Autowired
    private lateinit var restClient: RestClient

    @Autowired
    private lateinit var selfId: Id

    override val command: String
        get() = "!clearWebhooks"

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { eventMessage.content.equals(command) }
            .flatMap { it.guild }
            .map { it.id.asLong() }
            .map {
                restClient.webhookService.getGuildWebhooks(it)
                    .filter {
                        it.user().get().id() == selfId
                    }
                    .mapNotNull {
                        log.warn("Deleting Webhook: ${it.name().get()}")
                        restClient.webhookService
                            .deleteWebhook(it.id().asLong(), "Cova Told Me to.")
                            .block()
                    }
                    .blockLast()
                false
            }
            .then()
}