package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.temporal.ChronoUnit


@Service
class BluBot : ReplyBot() {

    @Autowired
    @Qualifier(value = "vennId")
    private var vennId: Long = -1

    override val botName: String
        get() = "BluBot"

    override val avatar: String
        get() = "https://imgur.com/WcBRCWn.png"

    override val response: String
        get() = "Did somebody say Blu?"

    private val pattern = "(blu(e)?)"

    private var lastBluMessage: Instant = Instant.MIN

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .filter(::handleRequestForBlu)
            .filter(::handleResponseToBlu)
            .doOnNext(::handleQueryForBlue)
            .then()

    private fun handleRequestForBlu(message: Message): Boolean {
        return Mono.just(message)
            .log()
            .map {
                log.info("Checking Nice Regex")
                message.findMatches("Blue?bot,? say something nice about (.*$)")
                    ?.groupValues ?: emptyList()
            }
            .filter {
                it.size == 2
            }
            .map {
                it.forEach { s -> log.info(s) }
                it[1]
            }
            .map {
                if (it == "me") {
                    message.author.map { it.username }.orElseGet { "Hey" }
                } else {
                    it
                }
            }
            .map { name ->
                message.getTextChannel()?.let { channel ->
                    log.info("Okay, I'll say something nice about $name")
                    writeMessage(channel, "$name, I think you're pretty Blu! :wink:")
                    false
                }
            }.block() ?: true
    }

    private fun handleResponseToBlu(message: Message): Boolean {
        if (message.referencedMessage.isPresent ||
            message.timestamp.isBefore(lastBluMessage?.plus(5, ChronoUnit.MINUTES))) {
            if (message.author.get().id.asLong() == vennId) {
                log.info("listen here you little shit")
                // if he says a lot of bad words
                // navy seal copy pasta
            }
            // if they mentioned the bot
            // lol somebody definitely said blu
            log.info("lol somebody definitely said blu")
            log.debug(message.referencedMessage.toString())
            log.debug("${message.timestamp.isBefore(lastBluMessage?.plus(5, ChronoUnit.MINUTES))}")
            return false
        }
        return true
    }

    private fun handleVennResponseToBlu(message: Message) {

    }

    private fun handleQueryForBlue(message: Message) {
        Mono.just(message)
            .filter { message.matchesPattern("blue?") }
            .subscribe {
                message.getTextChannel()?.let { channel ->
                    log.info("I think somebody said blu")
                    writeMessage(channel, "Did Somebody Say Blu?")
                }
            }
    }

    override fun writeMessage(channel: TextChannel, message: String) {
        lastBluMessage = Instant.now()
        super.writeMessage(channel, message)
    }
}