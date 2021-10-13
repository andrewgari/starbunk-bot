package org.starbunk.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener extends MessageListener implements BunkEventListener<MessageCreateEvent> {
    protected final Logger log = LoggerFactory.getLogger(MessageCreateListener.class.getName());

    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    public Mono<Void> execute(MessageCreateEvent event) {
        return processCommand(event.getMessage());
    }
}

