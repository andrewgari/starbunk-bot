package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class SheeshBot: ReplyBot() {

    private val random = Random.Default

    override val botName: String
        get() = "yuG"

    override val avatar: String
        get() = "https://imgur.com/4CqBg7F.png"

    override val response: String
        get() = "sh${"e".repeat(random.nextInt(1,500))}sh"

    override val pattern: String
        get() = "sh(e+).sh"

    @Value("\${starbunk.users.guy.id}")
    override val id: Long = -1

    override fun processMessage(eventMessage: Message): Mono<Void> =
            Mono.just(eventMessage)
                    .filter { it.isBot() }
                    .filter { it.author.isPresent && it.matchesPattern(pattern) && it.author.get().id.asLong() == id }
                    .flatMap { it.channel }
                    .cast(TextChannel::class.java)
                    .doOnNext(::writeMessage)
                    .then()
}