package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class ChaosBot: ReplyBot() {
    override val botName: String
        get() = "ChaosBot"
    override val avatar: String
        get() = "https://preview.redd.it/md0lzbvuc3571.png?width=1920&format=png&auto=webp&s=ff403a8d4b514af8d99792a275d2c066b8d1a4de"
    override val response: String
        get() = "All I know is...I'm here to kill chaos"
    override val pattern: String
        get() = "(.*?(\\bchaos\\b)[^\$]*)\$"
    override val id: Long = -1
}
