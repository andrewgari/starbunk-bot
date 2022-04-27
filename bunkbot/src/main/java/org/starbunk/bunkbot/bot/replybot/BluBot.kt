package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.utils.isBeforeToday
import org.starbunk.bunkbot.utils.isWithinFiveMinutesOf
import reactor.core.publisher.Mono
import java.time.Instant


@Service
class BluBot : ReplyBot() {

    @Autowired
    @Qualifier(value = "vennId")
    override var id: Long = -1

    override val botName: String
        get() = "BluBot"

    override val avatar: String
        get() = "https://imgur.com/WcBRCWn.png"

    override val response: String
        get() = "Did somebody say Blu?"

    override val pattern = "\\b(blue?)|(bloo)|(specific color)|(primary color that'?s neither red n?or yellow bot)|(Green - yellow Bot)|(b lu)|(eulb)|(azul)|(azulbot)\\b"
    private val bluePattern = ".*?(\\bblue?(bot)?\\b)|(bot\\b)[^$]*$"
    private val blueNicePattern = "blue?bot,? say something nice about (.+$)"
    private val blueMeanPattern = ".*?\\bfuck|hate|die|kill|worst|mom|shit|bot\\b[^$]*$"

    private val bluMurderUrl = "https://imgur.com/Tpo8Ywd.jpg"
    private val bluSmirkUrl = "https://i.imgur.com/dO4a59n.png"

    private val bluCuriousResponse = "Did somebody say Blu?"
    private val blueHappyResponse = "Lol, Somebody definitely said Blu! :smile:"
    private val blueMurderResponse =
        "What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Academia d'Azul, and I've been involved in numerous secret raids on Western La Noscea, and I have over 300 confirmed kills. I've trained with gorillas in warfare and I'm the top bombardier in the entire Eorzean Alliance. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Shard, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of tonberries across Eorzea and your IP is being traced right now so you better prepare for the storm, macaroni boy. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bear-hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the Eorzean Blue Brigade and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will fucking cook you like the little macaroni boy you are. You're fucking dead, kiddo."

    private var lastBluMessage: Instant = Instant.MIN
    private var lastBluMurder: Instant = Instant.MIN

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .doOnNext(::handleAllBlue)
            .then()

    private fun handleAllBlue(message: Message): Boolean {
        if (message.findMatches(blueNicePattern)?.groupValues?.size ?: 0 == 2) {
            // respond with "I think you're pretty blue
            var name = message.findMatches(blueNicePattern)!!.groupValues[1] // replace 'me' with posters name
            if (name == "me") {
                name = message.author.map { it.username }.orElseGet { "Hey" }
            }
            message.getTextChannel()?.let { channel ->
                log.info("Okay, I'll say something nice about $name")
                writeMessage1(channel, "$name, I think you're pretty Blu! :wink:")
                false
            }
            return true;
        } else if (isResponseToBlu(message)) { // message was posted within 5 minutes of the last blue message or is a reply
            // determine how to respond
            if (message.author.get().id.asLong() == id && message.matchesPattern(blueMeanPattern) && lastBluMurder.isBeforeToday()) { // was sent by ven
                writeMessage1(message.getTextChannel(), message = blueMurderResponse, avatarUrl = bluMurderUrl, response = true)
                lastBluMurder = message.timestamp // update murder tracker
                return false
            } else {
                // somebody definitely said blu
                if (message.matchesPattern(bluePattern)) {
                    writeMessage1(message.getTextChannel(), avatarUrl = bluSmirkUrl, message = blueHappyResponse, response = true)
                    return false
                }
            }
        } else if (message.matchesPattern(pattern)) {
            writeMessage1(message.getTextChannel(), avatarUrl = avatar, message = bluCuriousResponse)
            return false
        }
        return true
    }

    private fun isResponseToBlu(message: Message): Boolean =
        if (message.referencedMessage.isPresent && message.referencedMessage.get().author.get().username == this.botName) {
            true;
        } else {
            message.timestamp.isWithinFiveMinutesOf(lastBluMessage)
        };


    private fun writeMessage1(channel: TextChannel?, message: String, avatarUrl: String = avatar, name: String = botName, response: Boolean = false) {
        channel?.let { ch ->
            lastBluMessage = if (response) Instant.MIN else Instant.now()
            super.writeMessage(ch, message, avatarUrl, name)
        }
    }
}