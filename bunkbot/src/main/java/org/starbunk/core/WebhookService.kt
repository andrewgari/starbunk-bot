package org.starbunk.core

import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.Webhook
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.discordjson.json.WebhookCreateRequest
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.starbunk.listeners.MessageCreateListener
import reactor.core.publisher.Mono
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap

@Service
class WebhookService : MessageCreateListener() {

    @Autowired
    lateinit var gatewayClient: GatewayDiscordClient

    @Autowired
    private lateinit var webClient: RestClient

    private val webhookMap = HashMap<Long, Webhook>()

    @PostConstruct
    fun prepareWebhooks() {
        webClient.webhookService.getGuildWebhooks(856617421427441674)
            .collectList()
            .subscribe { webhookList ->
                webhookList.forEach {
                    val name = it.name().orElse("")

                    if (name.contains("bunkbot", true)) {
                        webhookMap[it.id().asLong()] = Webhook(gatewayClient, it)
                    }
                    webhookMap.forEach {
                        log.info("${it.key}, ${it.value.name}")
                    }
                }
                ""
            }
    }


    override fun getEventType(): Class<MessageCreateEvent> =
        MessageCreateEvent::class.java

    //TODO: Fix issue where multiple webhooks are made
    override fun execute(event: MessageCreateEvent): Mono<Void> {
        log.info("WebhookService Executing")
        return Mono.just(event)
            .map(MessageCreateEvent::getMessage)
            .filter { message -> !webhookMap.containsKey(message.channelId.asLong()) }
            .flatMap(Message::getChannel)
            .cast(TextChannel::class.java)
            .map { textChannel ->
                log.info("Creating Webhook on ${textChannel.name}")
                webClient.webhookService.createWebhook(
                    textChannel.id.asLong(),
                    WebhookCreateRequest.builder().name("BunkBot-${textChannel.name}").build(),
                    "Creating Webhook for New Message")
            }
            .map { data ->
                data.block()?.let { data ->
                    Webhook(gatewayClient, data).let { webhook ->
                        webhookMap[event.message.channelId.asLong()] = webhook
                    }
                }
            }
            .then()
    }
}