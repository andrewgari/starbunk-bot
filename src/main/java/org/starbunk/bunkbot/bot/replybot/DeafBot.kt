package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DeafBot: ReplyBot() {
    override val botName: String
        get() = "DeafBot"
    override val avatar: String
        get() = "https://www.reptilecentre.com/blog/wp-content/uploads/2020/02/leopard-gecko-header.jpg"
    override val response: String
        get() = "He is **Awake**\nhttps://giphy.com/gifs/come-at-me-im-here-big-bird-H6W9H29kVsUI2hJE90"
    override val pattern: String
        get() = ""

    @Value("\${starbunk.users.deaf.id}")
    override val id: Long = -1

    private var lastResponse: DateTime = DateTime(Long.MIN_VALUE)

    override fun processMessage(eventMessage: Message): Mono<Void> =
            Mono.just(eventMessage)
                    .filter { it.isBot() && it.author.isPresent && it.author.get().id.asLong() == id }
                    .flatMap { it.channel }
                    .cast( TextChannel::class.java)
                    .filter {
                        DateTime.now().isAfter(lastResponse.plusDays(5))
                    }
                    .doOnNext(::writeMessage)
                    .then()

    override fun writeMessage(channel: TextChannel?, message: String, avatarUrl: String, name: String) {
        if (lastResponse > DateTime(Long.MIN_VALUE)) {
            super.writeMessage(channel, message, avatarUrl, name)
            log.debug("WRITING TO DEAF: ${DateTime.now().toLocalDateTime()}")
        }
        lastResponse = DateTime.now()
    }
}