package org.starbunk.bunkbot.bot.replybot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SheeshBot: ReplyBot() {
    override val botName: String
        get() = "yuG"
    override val avatar: String
        get() = "https://imgur.com/4CqBg7F.png"
    override val response: String
        get() {
            val random = Random.Default.nextInt()
            return "sh${"e".repeat(random)}sh"
        }
    override val pattern: String
        get() = "sh(e+).sh"

    @Autowired
    @Qualifier(value = "guyId")
    override val id: Long = -1

//    @Autowired
//    @Qualifier(value = "guyId")
//    private var guyId: Long = -1

//    override fun processMessage(eventMessage: Message): Mono<Void> =
//        Mono.just(eventMessage)
//            .filter { it.isBot() }
//            .filter { it.matchesPattern("sh(e+).sh") }
//            .filter { it.author.get().id.asLong() == guyId }
//            .map { it.channel }
//            .cast(TextChannel::class.java)
//            .doOnNext(::writeMessage)
//            .then()
}