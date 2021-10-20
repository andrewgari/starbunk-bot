package org.starbunk.bunkbot.bot.voice

import discord4j.core.event.domain.VoiceStateUpdateEvent
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.listeners.VoiceListener
import reactor.core.publisher.Mono

@Service
class FeliBot: VoiceListener() {

    override fun processEvent(event: VoiceStateUpdateEvent): Mono<Void> =
        Mono.just(event)
            .filter { it.isJoinEvent }
            .map { it.current.channelId.get().asLong() }
            .filter {
                it == 753251583902482637
            }
            .map {
                event.current.userId.asLong()
            }
            .filter { it == 120263103366692868 }
            .doOnNext {
                event.current.user.map { it ->
                    it.privateChannel.map {
                        it.createMessage("Good Night, Sweet Prince :heart:")
                    }
                }
            }
            .then()
}