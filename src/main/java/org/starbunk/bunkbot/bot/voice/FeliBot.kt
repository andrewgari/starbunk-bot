package org.starbunk.bunkbot.bot.voice

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.VoiceListener
import reactor.core.publisher.Mono

@Service
class FeliBot : VoiceListener() {

    @Value("\${starbunk.channel.whale-watchers.id}")
    private var whaleWatchersId: Long = -1

    @Value("\${starbunk.channel.afk.id}")
    private var afkChannel: Long = -1

    @Value("\${starbunk.users.feli.id}")
    private var feliId: Long = -1

    override fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> =
            Mono.just(event)
                    .filter { it.isMoveEvent } // make sure its a voice connect event
                    .filter { it.old.isPresent && it.current.channelId.get().asLong() == afkChannel } // moved from a voice channel to the afk channel
                    .map { event.current.userId.asLong() }
                    .filter { it == feliId } // and it's feli
                    .flatMap {
                        event.client.getChannelById(Snowflake.of(whaleWatchersId)) //get the server instance
                    }
                    .cast(TextChannel::class.java)
                    .doOnNext { whaleWatchers ->
                        webhookComponent.writeMessage(
                            channel = whaleWatchers,
                            content = "Good Night, Sweet Prince :heart:",
                            nickname = "FeliBot",
                            avatarUrl = "https://pbs.twimg.com/profile_images/421461637325787136/0rxpHzVx.jpeg"
                        )
                    }
                    .flatMap {
                        it.guild
                    }
                    .flatMap {
                        it.getMemberById(Snowflake.of(feliId))
                    }
                    .flatMap {
                        it.privateChannel
                    }
                    .doOnNext { directChannel ->
                        directChannel.createMessage("Good Night, Sweet Prince :heart:").block()
                    }
                    .then()
}