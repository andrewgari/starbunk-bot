package org.starbunk.bots

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.MessageChannel
import org.springframework.stereotype.Service
import org.starbunk.listeners.MessageCreateListener
import reactor.core.publisher.Mono


@Service
class BluBot: MessageCreateListener(), ReplyBot {

    override val botName: String
        get() = "BluBot"

    override val avatar: Any
        get() = ""

    override val response: String?
        get() = "Did somebody say Blu?"

    private val pattern = "(blu(e)?)"

    override fun processCommand(eventMessage: Message): Mono<Void> {
        return Mono.just(eventMessage)
            .filter { message ->
                message.author.map { user -> !user.isBot }.orElse(false)
            }
            .filter { message ->
                val regex = Regex(pattern)
                !regex.containsMatchIn(message.content)
            }.also {
                it.subscribe {
                    somebodySaidBlu(it)
                }
            }.then()
    }

    fun somebodySaidBlu(message: Message): Boolean {
        message.channel.block()?.let { channel ->
            if (message.content.equals("123123BluBlu")) {
                writeMessage("WriteMessage: Did somebody say BLU?", channel)
                writeWHMessage("Did somebody say BLU?", message.channelId)
            }
        }
        return false
    }

    private fun writeMessage(message: String, channel: MessageChannel) =
        channel.createMessage(message).block()


    private fun writeWHMessage(message: String, channelId: Snowflake) {

    }
}