package org.starbunk.bunkbot.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starbunk.bunkbot.service.WebhookComponent;
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
        return processMessage(event.getMessage());
    }
}