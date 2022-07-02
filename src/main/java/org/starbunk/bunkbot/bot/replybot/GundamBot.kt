package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class GundamBot: ReplyBot() {
    override val botName: String
        get() = "That Famous Unicorn Robot, \"Gandam\""
    override val avatar: String
        get() = "https://a1.cdn.japantravel.com/photo/41317-179698/1440x960!/tokyo-unicorn-gundam-statue-in-odaiba-179698.jpg"
    override val response: String
        get() = "That's the famous Unicorn Robot, \"Gandam\". There, I said it."
    override val pattern: String
        get() = "(.*?(\\bg(u|a)ndam\\b)[^$]*)$"
    override val id: Long = -1
}