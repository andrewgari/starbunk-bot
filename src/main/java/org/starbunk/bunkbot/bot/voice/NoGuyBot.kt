package org.starbunk.bunkbot.bot.voice

import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class NoGuyBot: GuyMoveBot() {
    @Value("\${starbunk.channel.nobody-else-use-this-channel.id}")
    override var voiceChannel: Long = -1

    override fun filter(event: VoiceStateUpdateEvent): Boolean {
        val eventUserId = event.current.userId.asLong()
        val eventChannelId = event.current.channel.block()?.id?.asLong()
        return eventUserId != guyId && eventChannelId == voiceChannel
    }

    override fun handleMoveMember(event: VoiceStateUpdateEvent) {
        gateway.let {
            getGuild(it)?.let { guild ->
                guild.getMemberById(event.current.userId).block(Duration.ofSeconds(5)).let { guildMember ->
                    guildMember?.edit(moveToLoungeSpec)?.block(Duration.ofSeconds(5))
                }
            }
        }
    }
}