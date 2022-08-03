package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Value
import org.starbunk.bunkbot.listeners.MessageCreateListener
import reactor.core.publisher.Mono

abstract class ReplyBot: MessageCreateListener() {
    abstract val botName: String
    abstract val avatar: String
    abstract val response: String
    abstract val pattern: String
    abstract val id: Long

    open fun writeMessage(channel: TextChannel?, message: String = response, avatarUrl: String = avatar, name: String = botName) {
        channel?.let { ch ->
            webhookComponent.writeMessage(
                channel = ch,
                content = message,
                nickname = name,
                avatarUrl = avatarUrl
            )
        }
    }

    fun replyPipeline(eventMessage: Message): Mono<Message> =
        Mono.just(eventMessage)
            .filter { it.isBot() }

    override fun processMessage(eventMessage: Message): Mono<Void> =
        replyPipeline(eventMessage)
            .filter {
                (if (pattern.isNotBlank()) it.matchesPattern(pattern) else true) && (if (id > 0) it.author.get().id.asLong() == id else true)
            }
            .flatMap { it.channel }
            .cast(TextChannel::class.java)
            .doOnNext(::writeMessage)
            .then()

    fun Message.matchesPattern(pattern: String): Boolean {
        val regex = Regex(pattern, RegexOption.IGNORE_CASE)
        return regex.containsMatchIn(content)
    }

    fun Message.findMatches(pattern: String): MatchResult? {
        val regex = Regex(pattern, RegexOption.IGNORE_CASE)
        return regex.find(content)
    }

    fun Message.getTextChannel(): TextChannel? {
        return channel
            .cast(TextChannel::class.java)
            .block()
    }
}
