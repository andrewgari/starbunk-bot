package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class PickleBot: ReplyBot() {
    override val botName: String
        get() = "GremlinBot"
    override val avatar: String
        get() = "https://i.imgur.com/D0czJFu.jpg"
    override val response: String
        get() = "Could you repeat that? I don't speak gremlin..."
    override val pattern: String
        get() = "gremlin"
    @Autowired
    @Qualifier(value = "pickleId")
    override val id: Long = -1

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .map { it.author.get().id.asLong() }
            .filter { it == id }
            .filter {
                eventMessage.matchesPattern("gremlin") || roll20()
            }
            .map { eventMessage.channel }
            .cast( TextChannel::class.java)
            .doOnNext(::writeMessage)
            .then()

    private fun roll20(): Boolean {
        val random = Random.Default
        return random.nextInt(1, 20) > 17
    }
}