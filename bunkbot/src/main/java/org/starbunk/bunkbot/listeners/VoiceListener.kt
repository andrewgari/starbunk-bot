package org.starbunk.bunkbot.listeners

import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.starbunk.bunkbot.service.WebhookComponent
import reactor.core.publisher.Mono

abstract class VoiceListener {

    @Autowired
    protected var webhookComponent: WebhookComponent? = null

    open fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> {
        return Mono.just(event)
            .then()
    }
}