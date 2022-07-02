package org.starbunk.bunkbot.bot.voice

import discord4j.common.util.Snowflake
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.VoiceStateUpdateEvent
import discord4j.core.`object`.entity.Guild
import discord4j.core.spec.GuildMemberEditSpec
import org.springframework.beans.factory.annotation.Value
import org.starbunk.bunkbot.listeners.JoinChannelListener
import reactor.core.publisher.Mono
import java.time.Duration

abstract class GuyMoveBot: JoinChannelListener() {

    @Value("\${starbunk.channel.lounge.id}")
    protected var loungeId: Long = 0

    @Value("\${starbunk.users.guy.id}")
    protected var guyId: Long = -1

    @Value("\${starbunk.guild.id}")
    protected var guildId: Long = -1

    abstract val voiceChannel: Long;

    protected val moveToLoungeSpec: GuildMemberEditSpec =
        GuildMemberEditSpec.builder()
            .newVoiceChannelOrNull(Snowflake.of(loungeId))
            .build()


    protected fun getGuild(gateway: GatewayDiscordClient): Guild? {
        return gateway.getGuildById(Snowflake.of(guildId)).block()
    }

    protected abstract fun filter(event: VoiceStateUpdateEvent): Boolean

    protected open fun handleMoveMember(event: VoiceStateUpdateEvent) {
        val client = event.client
        val guild = client.getGuildById(Snowflake.of(guildId)).block(Duration.ofSeconds(5))
        if (guild != null) {
            val guyAsGuildMember = guild.getMemberById(Snowflake.of(guyId)).block(Duration.ofSeconds(5))
            guyAsGuildMember?.edit(moveToLoungeSpec)?.block(Duration.ofSeconds(5))
        }
    }

    override fun execute(event: VoiceStateUpdateEvent): Mono<Void> {
        return Mono.just(event)
            .filter { filter(it) }
            .doOnNext {
                handleMoveMember(it)
            }
            .then()
    }
}