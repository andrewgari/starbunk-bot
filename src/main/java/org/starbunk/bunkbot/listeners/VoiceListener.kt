package org.starbunk.bunkbot.listeners

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.starbunk.bunkbot.service.WebhookComponent
import reactor.core.publisher.Mono

abstract class VoiceListener : BunkEventListener<VoiceStateUpdateEvent> {

    protected val log: Logger = LoggerFactory.getLogger(VoiceStateUpdateEvent::class.java.name)

    @Lazy @Autowired
    protected lateinit var webhookComponent: WebhookComponent

    @Lazy @Autowired
    protected lateinit var gateway: GatewayDiscordClient

    override fun getEventType(): Class<VoiceStateUpdateEvent> =
        VoiceStateUpdateEvent::class.java

    override fun execute(event: VoiceStateUpdateEvent): Mono<Void> =
        processEvent(event)


    open fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> =
        Mono.just(event)
            .then()
}