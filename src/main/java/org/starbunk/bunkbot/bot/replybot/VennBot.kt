package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.rest.util.Image
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class VennBot() : ReplyBot() {
    override val botName: String = "Venn"
    override val avatar: String = ""
    override val response: String = "Sorry, but that was über cringe..."
    override val pattern: String = ""
    @Value("\${starbunk.users.venn.id}")
    override val id: Long = -1

    private val random = Random.Default

    override fun processMessage(eventMessage: Message): Mono<Void> =
        replyPipeline(eventMessage)
            .filter { it.author.get().id.asLong() == id && roll20() }
            .flatMap { it.channel }
            .cast( TextChannel::class.java)
            .doOnNext { channel ->
                val avatarUrl = Mono.just(eventMessage)
                    .flatMap(Message::getAuthorAsMember)
                    .map{ it.effectiveAvatarUrl }
                    .block()
                val botName = Mono.just(eventMessage)
                    .flatMap(Message::getAuthorAsMember)
                    .map(Member::getDisplayName)
                    .block()
                if (avatarUrl != null && botName != null) {
                    writeMessage(channel, response, avatarUrl, botName)
                }
            }
            .then()

    private fun roll20(): Boolean {
        val roll = random.nextInt(1, 20)
        return roll > 15
    }
}