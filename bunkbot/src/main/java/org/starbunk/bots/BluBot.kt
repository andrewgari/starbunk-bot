package org.starbunk.bots

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.stereotype.Service
import org.starbunk.listeners.MessageCreateListener
import reactor.core.publisher.Mono


@Service
class BluBot : MessageCreateListener(), ReplyBot {

    override val botName: String
        get() = "BluBot"

    override val avatar: String
        get() = "https://imgur.com/WcBRCWn.png"

    override val response: String
        get() = "Did somebody say Blu?"

    private val pattern = "(blu(e)?)"

    override fun processCommand(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { message ->
                message.author.map { user -> !user.isBot }.orElse(false)
            }
            .filter { message ->
                val regex = Regex(pattern, RegexOption.IGNORE_CASE)
                regex.containsMatchIn(message.content)
            }
            .map { message ->
                log.info("I think somebody said blu...")
                somebodySaidBlu(message)
                message
            }
            .then()

    private fun somebodySaidBlu(message: Message): Boolean {
        message.channel.cast(TextChannel::class.java).block()?.let { channel ->
            webhookService.writeMessage(
                channel = channel,
                content = response,
                nickname = botName,
                avatarUrl = avatar
            )
            return true
        }
        return false
    }
}