package org.starbunk.bunkbot.bot.command

import discord4j.core.`object`.entity.Message
import org.starbunk.bunkbot.listeners.MessageCreateListener
import reactor.core.publisher.Mono

abstract class CommandBot(): MessageCreateListener() {
    abstract val command: String

    fun commandPipeline(eventMessage: Message): Mono<Message> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .filter { it.content.startsWith("?${command}") }
}