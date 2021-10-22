package org.starbunk.bunkbot.bot.voice

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.VoiceListener
import reactor.core.publisher.Mono

@Service
class FeliBot: VoiceListener() {

    @Autowired
    @Qualifier(value = "whaleWatchersId")
    private var whaleWatchersId: Long = -1

    @Autowired
    @Qualifier (value = "covaId")
    private var feliId: Long = -1

    override fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> =
        Mono.just(event)
            .log()
            .doOnNext {
                println("isJoinEvent: ${it.isJoinEvent}")
                println("old: ${ it.old }")
                println("current: ${ it.current }")
                println("is move event: ${ it.isMoveEvent}")
            }
            .filter { it.isJoinEvent } // make sure its a voice connect event
            .filter { it.old.isPresent } // and that feli was in a channel previously
            .filter { event.current.userId.asLong() == feliId } // and it's feli
            .map { event.current.channelId } // get current channel
            .filter { it.isPresent } // check for non-null
            .map { it.get().asLong() } // get it's id, cast to long
            .map { id ->
                event.client
                    .getGuildChannels(event.current.guildId) // get the starbunk guild obj
                    .log()
                    .flatMap { it.guild }   // cast to guild
                    .filter { it.afkChannelId.isPresent }   // make sure theres an afk channel
                    .filter { it.afkChannelId.get().asLong() == id }    // make sure the current channel is the afk channel
                    .blockFirst() // return the first guild that matches this
            }
            .map { starbunk ->
                starbunk.channels.filter { it.id.asLong() == whaleWatchersId }.blockFirst() // get whaleWatchers
            }
            .cast(TextChannel::class.java) // cast to text channel
            .doOnNext { whaleWatchers -> // write message
                event.current.user.map { it ->
                    it.privateChannel.map {
                        it.createMessage("Good Night, Sweet Prince :heart:")
                    }
                }
                webhookComponent?.writeMessage(
                    channel = whaleWatchers,
                    content = "Good Night, Sweet Prince :heart:",
                    nickname = "FeliBot",
                    avatarUrl = "https://pbs.twimg.com/profile_images/421461637325787136/0rxpHzVx.jpeg"
                )
            }
            .then()
}