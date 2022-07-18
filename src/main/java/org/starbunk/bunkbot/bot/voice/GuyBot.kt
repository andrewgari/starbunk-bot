package org.starbunk.bunkbot.bot.voice

import discord4j.common.util.Snowflake
import discord4j.core.event.domain.VoiceStateUpdateEvent
import discord4j.core.`object`.entity.Guild
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class GuyBot(): GuyMoveBot() {
    @Value("\${starbunk.channel.guy-dont-use-this-channel.id}")
    override var voiceChannel: Long = -1

    override fun filter(event: VoiceStateUpdateEvent): Boolean {
        val eventUserId = event.current.userId.asLong()
        val eventChannelId = event.current.channel.block()?.id?.asLong()
        return eventUserId == guyId && eventChannelId == voiceChannel
    }

    override fun handleMoveMember(event: VoiceStateUpdateEvent) {
        getGuild().getMemberById(Snowflake.of(guyId)).block(Duration.ofSeconds(5)).let { guildMember ->
            guildMember?.edit(moveToLoungeSpec())?.block(Duration.ofSeconds(5))
        }
    }
}