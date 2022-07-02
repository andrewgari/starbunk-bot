package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class SixtyNineBot: ReplyBot() {
    override val botName: String
        get() = "CovaBot"
    override val avatar: String
        get() = "https://pbs.twimg.com/profile_images/421461637325787136/0rxpHzVx.jpeg"
    override val response: String
        get() = "Nice."
    override val pattern: String
        get() = "(.*?(\\b69\\b)[^$]*)$"
    override val id: Long = -1
}