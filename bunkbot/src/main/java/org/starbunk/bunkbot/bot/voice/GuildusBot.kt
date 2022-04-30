package org.starbunk.bunkbot.bot.voice

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.BunkEventListener.LOG
import org.starbunk.bunkbot.listeners.VoiceListener
import reactor.core.publisher.Mono

@Service
class GuildusBot : VoiceListener() {

    @Autowired
    @Qualifier(value = "guildusId")
    private var guildusId: Long = -1

    @Autowired
    @Qualifier(value = "whaleWatchersId")
    private var whaleWatchersId: Long = -1

    override fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> =
        Mono.just(event)
            .filter { it.isJoinEvent }
            .filter { it.old.isEmpty }
            .map { event.current.userId.asLong() }
            .filter { it == guildusId }
            .flatMap {
                event.client.getChannelById(Snowflake.of(whaleWatchersId))
            }
            .cast(TextChannel::class.java)
            .doOnNext { whaleWatchers ->
                webhookComponent?.writeMessage(
                    channel = whaleWatchers,
                    content = ":wave:",
                    nickname = "GuildusBot",
                    avatarUrl = "https://i.imgur.com/v3E8yWY.jpg"
                )

            }
            .then()
}