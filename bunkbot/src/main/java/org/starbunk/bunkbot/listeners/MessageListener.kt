package org.starbunk.bunkbot.listeners

import discord4j.core.`object`.entity.Message
import reactor.core.publisher.Mono

abstract class MessageListener {
    open fun processMessage(eventMessage: Message): Mono<Void> {
        return Mono.just(eventMessage)
            .filter { message ->
                message.author.map { user -> !user.isBot }.orElse(false)
            }
            .then()
    }

    fun Message.isBot(): Boolean = author.map { user -> !user.isBot }.orElse(false)
}