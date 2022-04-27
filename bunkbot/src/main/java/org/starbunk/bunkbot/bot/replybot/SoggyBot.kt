package org.starbunk.bunkbot.bot.replybot

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

//@Service
class SoggyBot: ReplyBot() {
    override val botName: String
        get() = "SoggyBot"
    override val avatar: String
        get() = "https://imgur.com/OCB6i4x.jpg"
    override val response: String
        get() = "Sounds like somebody enjoys Wet Bread."
    override val pattern: String
        get() = "wet bread"

    override val id: Long = 204326753215315968

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
                .filter { it.isBot() }
                .filter { it.author.isPresent && it.author.get().id.asLong() == id && it.matchesPattern(pattern) }
                .flatMap { it.channel }
                .cast(TextChannel::class.java)
                .doOnNext {
                    writeMessage(it, response)
                }
                .then()

    private fun findRole(member: Member, name: String): Role {
        val roles = member.roles;
        return roles.toStream()
                .filter{ it.name == name}
                .findFirst()
                .orElse(null)
    }
}