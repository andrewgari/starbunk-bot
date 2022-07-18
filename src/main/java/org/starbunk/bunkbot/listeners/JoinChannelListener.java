package org.starbunk.bunkbot.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public class JoinChannelListener extends VoiceListener implements BunkEventListener<VoiceStateUpdateEvent> {

    @NotNull
    @Override
    public Class<VoiceStateUpdateEvent> getEventType() {
        return VoiceStateUpdateEvent.class;
    }

    @NotNull
    @Override
    public Mono<Void> execute(@NotNull VoiceStateUpdateEvent event) {
        return Mono.just(event)
                .filter(VoiceStateUpdateEvent::isJoinEvent)
                .flatMap(super::processEvent);
    }
}

