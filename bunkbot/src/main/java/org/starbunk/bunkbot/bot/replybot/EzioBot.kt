package org.starbunk.bunkbot.bot.replybot

import org.springframework.stereotype.Service

@Service
class EzioBot: ReplyBot() {
    override val botName: String
        get() = "Ezio Auditore Da Firenze"
    override val avatar: String
        get() = "https://miro.medium.com/max/1838/1*CXPsg1BV8fuPUKchM6Cp-A.png"
    override val response: String
        get() = "Nothing is true; everything is permitted. <@{135820819086573568}>"
    override val pattern: String
        get() = "ezio|assassin"
    override val id: Long = -1
}