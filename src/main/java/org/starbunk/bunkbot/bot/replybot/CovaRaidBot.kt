package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CovaRaidBot : ReplyBot() {
    override val botName: String
        get() = "BadBot"
    override val avatar: String
        get() = "https://i1.sndcdn.com/artworks-000634932757-x97qoi-t500x500.jpg"
    override val pattern: String
        get() = "(raid)+.*(min(ute)?s?|hours?|hrs?).*"
    @Value("\${starbunk.users.cova.id}")
    override val id: Long = -1
    @Value("\${starbunk.users.venn.id}")
    val vennId: Long = -1
    @Value("\${starbunk.channel.nebula-raid-announcements.id}")
    val nebulaAnnouncementsId: Long = -1
    @Value("\${starbunk.channel.nebula-chat.id}")
    val nebulaChatId: Long = -1

    override val response: String
        get() = "Yeah, and I'm sure <@$id> will bungle it all up again..."

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
                .filter { it.isBot() }
                .filter { it.author.isPresent && it.author.get().id.asLong() == vennId && it.matchesPattern(pattern)}
                .flatMap { it.channel }
                .cast(TextChannel::class.java)
                .filter { it.id.asLong() == nebulaAnnouncementsId || it.id.asLong() == nebulaChatId }
                .doOnNext(::writeMessage)
                .then()
}