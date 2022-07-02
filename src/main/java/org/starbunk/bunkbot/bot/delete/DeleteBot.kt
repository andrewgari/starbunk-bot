package org.starbunk.bunkbot.bot.delete

import discord4j.core.`object`.entity.Message
import org.starbunk.bunkbot.listeners.MessageDeleteListener
import reactor.core.publisher.Mono

abstract class DeleteBot: MessageDeleteListener() {
    abstract val botName: String
    abstract val avatar: String
    abstract val response: String
    abstract val pattern: String
    abstract val id: Long

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .doOnNext(::handleDelete)
            .then()

    abstract fun handleDelete(message: Message)
}