package org.starbunk.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starbunk.bots.webhooks.WebhookComponent;
import reactor.core.publisher.Mono;

@Service
public class MessageCreateListener extends MessageListener implements BunkEventListener<MessageCreateEvent> {
    protected final Logger log = LoggerFactory.getLogger(MessageCreateListener.class.getName());

    @Autowired
    protected WebhookComponent webhookComponent;

    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    public Mono<Void> execute(MessageCreateEvent event) {
        return processCommand(event.getMessage());
    }
}