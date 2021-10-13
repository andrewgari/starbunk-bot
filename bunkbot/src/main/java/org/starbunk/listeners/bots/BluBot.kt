package org.starbunk.listeners.bots

import discord4j.core.event.domain.message.MessageCreateEvent
import org.springframework.stereotype.Service
import org.starbunk.listeners.MessageCreateListener
import reactor.core.publisher.Mono

@Service
class BluBot: MessageCreateListener() {
    override fun getEventType(): Class<MessageCreateEvent> = MessageCreateEvent::class.java

    override fun execute(event: MessageCreateEvent): Mono<Void> {
        log.info("BluBot: ${event.message.content}")
        return processCommand(event.message)
    }
}