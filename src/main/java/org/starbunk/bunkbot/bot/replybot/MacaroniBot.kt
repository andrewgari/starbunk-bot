package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MacaroniBot: ReplyBot() {
    override val botName: String
        get() = "MacaroniBot"
    override val avatar: String
        get() = "https://i.imgur.com/fgbH6Xf.jpg"
    override val response: String
        get() = ""
    override val pattern: String
        get() = ""

    @Value("\${starbunk.users.venn.id}")
    override val id: Long = -1

    @Value("\${starbunk.roles.macaroni.id}")
    val role: Long = -1

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .doOnNext(::handleMacaroniMessage)
            .then()

    private fun handleMacaroniMessage(message: Message) {
        if (message.matchesPattern("(.*?(\\bvenn\\b)[^$]*)$")) {
            writeMessage(
                channel = message.getTextChannel(),
                avatarUrl = avatar,
                message = "Correction: you mean Venn \"Tyrone \"The \"Macaroni\" Man\" Johnson\" Caelum"
            )
        } else if (message.matchesPattern("(.*?(\\bmacaroni\\b)[^$]*)$")) {
            writeMessage(
                channel = message.getTextChannel(),
                avatarUrl =  avatar,
                message = "Are you trying to reach <@&$role}>"
            )
        }
    }
}