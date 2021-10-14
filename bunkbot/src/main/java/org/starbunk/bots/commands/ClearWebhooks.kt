package org.starbunk.bots.commands

import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.discordjson.Id
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.starbunk.listeners.MessageCreateListener
import reactor.core.publisher.Mono

@Service
class ClearWebhooks: MessageCreateListener(), CommandBot {

    @Autowired
    private lateinit var restClient: RestClient

    @Autowired
    private lateinit var selfId: Id

    override val command: String
        get() = "!clearWebhooks"

    override fun execute(event: MessageCreateEvent?): Mono<Void> {
        if (selfId.asLong()< 0) log.warn("Did not get SelfId")
        return super.execute(event)
    }

    override fun processCommand(eventMessage: Message): Mono<Void> =
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