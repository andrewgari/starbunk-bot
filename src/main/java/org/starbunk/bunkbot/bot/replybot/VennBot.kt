package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class VennBot() : ReplyBot() {
    override val botName: String = "Venn"
    override val avatar: String = ""
    override val response: String = "Sorry, but that was mondo cringe..."
    override val pattern: String = ""
    @Value("\${starbunk.users.cova.id}")
    override val id: Long = -1

    private val random = Random.Default


    override fun processMessage(eventMessage: Message): Mono<Void> =
        replyPipeline(eventMessage)
            .filter { it.author.get().id.asLong() == id && roll20() }
            .flatMap { it.channel }
            .cast( TextChannel::class.java)
            .doOnNext(::writeMessage)
            .then()

    private fun roll20(): Boolean {
        val roll = random.nextInt(1, 20)
        return roll > 15
    }
}