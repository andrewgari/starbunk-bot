package org.starbunk.bunkbot.bot.command

import discord4j.core.`object`.VoiceState
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import org.springframework.stereotype.Service
import org.starbunk.bunkbot.audio.TrackScheduler
import org.starbunk.bunkbot.service.AudioService
import reactor.core.publisher.Mono

@Service
class DJCovaBot(private val audioService: AudioService): CommandBot()  {
    override val command: String = "covaPlay"

    private val scheduler = TrackScheduler(audioService.player);

    override fun processMessage(eventMessage: Message): Mono<Void> =
        commandPipeline(eventMessage)
            .flatMap(Message::getAuthorAsMember)
            .flatMap(Member::getVoiceState)
            .flatMap(VoiceState::getChannel)
            .flatMap { channel ->
                channel.join {
                    it.setProvider(audioService.provider)
                }
            }
            .map {
                println(eventMessage.content)
                eventMessage.content.substringAfter("?${command}").trim()
            }
            .doOnNext { command ->
                println(command)
                audioService.playerManager.loadItem(command, scheduler)
            }
            .then()
}