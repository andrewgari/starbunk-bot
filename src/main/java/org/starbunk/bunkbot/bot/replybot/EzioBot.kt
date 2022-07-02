package org.starbunk.bunkbot.bot.replybot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EzioBot: ReplyBot() {
    override val botName: String
        get() = "Ezio Auditore Da Firenze"
    override val avatar: String
        get() = "https://miro.medium.com/max/1838/1*CXPsg1BV8fuPUKchM6Cp-A.png"
    override val pattern: String
        get() = "ezio|assassin"
    @Value("\${starbunk.users.bender.id}")
    override val id: Long = -1
    override val response: String
        get() = "Nothing is true; everything is permitted. <@$id>"
}