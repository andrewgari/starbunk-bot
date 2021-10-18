package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class HoldBot: ReplyBot() {
    override val botName: String
        get() = "HoldBot"
    override val avatar: String
        get() = "https://i.imgur.com/YPFGEzM.png"
    override val response: String
        get() = "Hold."
    override val pattern: String
        get() = "^hold\\.?\$"
    override val id: Long = -1
}