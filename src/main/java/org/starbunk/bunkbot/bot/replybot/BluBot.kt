package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.utils.isBeforeToday
import org.starbunk.bunkbot.utils.isWithinFiveMinutesOf
import reactor.core.publisher.Mono
import java.time.Instant


@Service
class BluBot : ReplyBot() {

    @Value("\${starbunk.users.venn.id}")
    override var id: Long = -1

    override val botName: String
        get() = "BluBot"

    override val avatar: String
        get() = "https://imgur.com/WcBRCWn.png"

    override val response: String
        get() = "Did somebody say Blu?"

    override val pattern: String = ".*?\\b(blue?|bloo|b lu|eulb|azul|azulbot|cerulean)\\b[^$]*$"
    private val blueConfirmPattern = ".*?\\b(blue?(bot)?)|(bot)|yes|no|yep|(i did)|(you got it)|(sure did)\\b[^$]*$"
    private val blueNicePattern = "blue?bot,? say something nice about (.+$)"
    private val blueMeanPattern = "\\b(fuck(ing)?|hate|die|kill|worst|mom|shit|murder|bots?)\\b"

    private val bluMurderUrl = "https://imgur.com/Tpo8Ywd.jpg"
    private val bluSmirkUrl = "https://i.imgur.com/dO4a59n.png"

    private val bluCuriousResponse = "Did somebody say Blu?"
    private val blueHappyResponse = "Lol, Somebody definitely said Blu! :smile:"
    private val blueMurderResponse =
        "What the fuck did you just fucking say about me, you little bitch? I'll have you know I graduated top of my class in the Academia d'Azul, and I've been involved in numerous secret raids on Western La Noscea, and I have over 300 confirmed kills. I've trained with gorillas in warfare and I'm the top bombardier in the entire Eorzean Alliance. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Shard, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of tonberries across Eorzea and your IP is being traced right now so you better prepare for the storm, macaroni boy. The storm that wipes out the pathetic little thing you call your life. You're fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that's just with my bear-hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the Eorzean Blue Brigade and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn't, you didn't, and now you're paying the price, you goddamn idiot. I will fucking cook you like the little macaroni boy you are. You're fucking dead, kiddo."

    private var lastBluMessage: Instant = Instant.MIN
    private var lastBluMurder: Instant = Instant.MIN

    private fun isRequestToSayBlu(message: Message): Boolean {
        return message.matchesPattern(blueNicePattern)
    }

    private fun getNameFromBluRequest(message: Message): String {
        val name = message.findMatches(blueNicePattern)?.let { matches ->
            matches.groupValues.let { groups ->
                if (groups.size > 1) {
                    val n = groups[1]
                    if (n.lowercase() == "me")
                        message.author.get().username
                    else
                        groups[1]
                } else
                    "hey"
            }
        } ?: "hey"
        return name
    }

    private fun isVennInsultingBlu(message: Message): Boolean =
         message.author.get().id.asLong() == id && message.matchesPattern(blueMeanPattern) && isResponseToBlu(message) && lastBluMurder.isBeforeToday()

    private fun isResponseToBlu(message: Message): Boolean =
        if (message.referencedMessage.isPresent && message.referencedMessage.get().author.get().username == this.botName) {
            true
        } else
            message.timestamp.isWithinFiveMinutesOf(this.lastBluMessage) && (message.matchesPattern(this.blueConfirmPattern) || message.matchesPattern(this.blueMeanPattern))

    private fun didSomebodySayBlu(message: Message): Boolean =
        message.matchesPattern(pattern)

    private fun askIfSomebodySaidBlue(message: Message) {
        log.debug("REGULAR BLU PATTERN")
        lastBluMessage = Instant.now()
        writeMessage(message.getTextChannel(), avatarUrl = avatar, message = bluCuriousResponse)
    }

    private fun confirmSomebodySaidBlue(message: Message) {
        log.debug("SOMEBODY SAID BLU")
        lastBluMessage = Instant.MIN
        writeMessage(message.getTextChannel(), avatarUrl = bluSmirkUrl, message = blueHappyResponse)
    }

    private fun murderVenn(message: Message) {
        lastBluMessage = Instant.MIN
        lastBluMurder = message.timestamp // update murder tracker
        writeMessage(message.getTextChannel(), message = blueMurderResponse, avatarUrl = bluMurderUrl)
    }

    private fun saySomethingNiceToUser(message: Message, name: String = "Hey") {
        log.info("Okay, I'll say something nice about $name")
        log.debug("WAS ASKED TO SAY BLU")
        writeMessage(message.getTextChannel(), "$name, I think you're pretty Blu! :wink:")
    }

    private fun refuseToSaySomethingBlue(message: Message) {
        log.info("Blubot doesn't like Venn")
        writeMessage(message.getTextChannel(), "No way, Venn's a little blue loser. :unamused:")
    }

    private fun handleAllBlue(message: Message): Boolean {
        return if (isRequestToSayBlu(message)) {
            val name = getNameFromBluRequest(message)
            if (name.lowercase() == "venn")
                refuseToSaySomethingBlue(message)
            else
                saySomethingNiceToUser(message, name)
            false
        } else if (isVennInsultingBlu(message)) {
            murderVenn(message)
            false
        } else if (isResponseToBlu(message)) {
            confirmSomebodySaidBlue(message)
            false
        } else if (didSomebodySayBlu(message)) {
            askIfSomebodySaidBlue(message)
            false
        } else true
    }

    override fun processMessage(eventMessage: Message): Mono<Void> =
        Mono.just(eventMessage)
            .filter { it.isBot() }
            .doOnNext(::handleAllBlue)
            .then()
}