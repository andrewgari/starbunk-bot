package org.starbunk.bunkbot.bot.delete

import discord4j.common.util.Snowflake
import discord4j.core.`object`.audit.ActionType
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

class VennReBot : DeleteBot() {
    override val botName: String = ""
    override val avatar: String = ""
    override val pattern: String = ""
    override val response: String = ""

    @Autowired @Qualifier(value = "covaId")
    override var id: Long = -1

    override fun processMessage(eventMessage: Message): Mono<Void> {
        return Mono.just(eventMessage)
            .doOnNext(::handleDelete)
            .then()
    }

    override fun handleDelete(message: Message) {
        val channel = message.channel.block()
        if (channel is TextChannel) {
            message.author.takeIf { it.isPresent }?.get()?.let { author ->
                if (author.id.asLong() == id) {
                    val guild = message.guild.block()
                    val logs = guild.auditLog
                        .withUserId(Snowflake.of(id))
                        .withActionType(ActionType.MESSAGE_DELETE)
                        .collectList()
                        .block()
                        if (logs.size > 0) {
                            webhookComponent.writeMessage(
                                channel = channel,
                                content = message.content,
                                nickname = message.author.get().username,
                                avatarUrl = message.author.get().avatarUrl,
                                embedData = message.embeds,
                                attatchments = message.attachments
                            )
                            webhookComponent.writeMessage(
                                channel = channel,
                                content = message.content,
                                nickname = message.author.get().username,
                                avatarUrl = message.author.get().avatarUrl,
                                embedData = message.embeds,
                                attatchments = message.attachments
                            )
                        }

                }
            }
        }
    }
}

