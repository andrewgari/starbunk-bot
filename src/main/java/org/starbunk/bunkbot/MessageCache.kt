package org.starbunk.bunkbot

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.Message
import org.springframework.stereotype.Repository

@Repository
class MessageCache {

    private val messageQueue = HashMap<Snowflake, Message>()

    fun isPresent(id: Snowflake): Boolean = messageQueue.keys.contains(id);

    fun getMessage(id: Snowflake): Message? = if(isPresent(id)) messageQueue[id] else null

    fun addMessage(message: Message) = messageQueue.put(message.channelId, message)

}