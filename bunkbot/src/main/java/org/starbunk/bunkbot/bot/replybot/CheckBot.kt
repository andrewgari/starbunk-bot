package org.starbunk.bunkbot.bot.replybot

import discord4j.core.`object`.entity.channel.TextChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class CheckBot: ReplyBot() {
    override val botName: String
        get() = "CzechBot"
    override val avatar: String
        get() = "https://m.media-amazon.com/images/I/21Unzn9U8sL._AC_.jpg"
    override val response: String
        get() = "I think you mean *check*"
    override val pattern: String
        get() = "czech"
    @Autowired
    @Qualifier(value = "guyId")
    override val id: Long = -1

    override fun writeMessage(channel: TextChannel?, message: String, avatarUrl: String, name: String) {
        super.writeMessage(channel, "I think you meant to say \"${ message.replace("czech", "check")}\"", avatarUrl, name)
    }
}