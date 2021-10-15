package org.starbunk.bunkbot.listeners;

import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public interface BunkEventListener<T extends Event> {
    Logger LOG = LoggerFactory.getLogger(BunkEventListener.class);

    Class<T> getEventType();
    Mono<Void> execute(T event);

    default Mono<Void> handleError(Throwable error) {
        LOG.error("Unable to Process " + getEventType().getSimpleName(), error);
        return Mono.empty();
    }
}
