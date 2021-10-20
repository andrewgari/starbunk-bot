package org.starbunk.bunkbot.listeners;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class JoinChannelListener extends VoiceListener implements BunkEventListener<VoiceStateUpdateEvent> {

    @Override
    public Class<VoiceStateUpdateEvent> getEventType() {
        return VoiceStateUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(VoiceStateUpdateEvent event) {
        return Mono.just(event)
                .filter(VoiceStateUpdateEvent::isJoinEvent)
                .flatMap(super::processEvent);
    }
}

