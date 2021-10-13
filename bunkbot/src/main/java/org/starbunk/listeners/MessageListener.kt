package org.starbunk.listeners

import discord4j.core.`object`.entity.Message
import reactor.core.publisher.Mono

abstract class MessageListener {
    open fun processCommand(eventMessage: Message): Mono<Void> {
        return Mono.just(eventMessage)
            .filter { message ->
                message.author.map { user -> !user.isBot }.orElse(false)
            }
            .then()
    }
}