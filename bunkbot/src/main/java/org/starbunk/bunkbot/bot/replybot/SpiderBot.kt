package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class SpiderBot: ReplyBot() {
    override val botName: String
        get() = "Spider-Bot"
    override val avatar: String
        get() = "https://i.dlpng.com/static/png/6569125_preview.png"
    override val response: String
        get() = "Hey, it's \"Spider-Man\"! Don't forget the hyphen!"
    override val pattern: String
        get() = "(.*?(\\bspiderman\\b)[^\$]*)\$"
    override val id: Long
        get() = -1
}