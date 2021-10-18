package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.starbunk.bunkbot.listeners.MessageCreateListener

abstract class ReplyBot: MessageCreateListener() {
    abstract val botName: String
    abstract val avatar: String
    abstract val response: String

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
