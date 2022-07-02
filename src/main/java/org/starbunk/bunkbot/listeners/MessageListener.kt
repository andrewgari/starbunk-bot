package org.starbunk.bunkbot.listeners

import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.starbunk.bunkbot.StarbunkProperties
import reactor.core.publisher.Mono

abstract class MessageListener {
    @Lazy
    @Autowired
    protected open var gateway: GatewayDiscordClient? = null

    open fun processMessage(eventMessage: Message): Mono<Void> {
        return Mono.just(eventMessage)
            .filter { message ->
                message.author.map { user -> !user.isBot }.orElse(false)
            }
            .then()
    }

    fun Message.isBot(): Boolean = author.map { user -> !user.isBot }.orElse(false)
}

