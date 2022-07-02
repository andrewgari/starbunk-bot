package org.starbunk.bunkbot.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.AuditLogQueryFlux;
import discord4j.rest.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.starbunk.bunkbot.MessageCache;
import org.starbunk.bunkbot.service.WebhookComponent;
import reactor.core.publisher.Mono;

public class MessageDeleteListener extends MessageListener implements BunkEventListener<MessageDeleteEvent> {
    protected final Logger log = LoggerFactory.getLogger(MessageDeleteListener.class.getName());

    @Autowired
    protected WebhookComponent webhookComponent;
    @Autowired
    protected MessageCache messageCache;
    @Lazy  @Autowired
    protected GatewayDiscordClient gateway;

    public Class<MessageDeleteEvent> getEventType() {
        return MessageDeleteEvent.class;
    }

    public Mono<Void> execute(MessageDeleteEvent event) {
        try {
            Message message = event.getMessage().orElse(messageCache.getMessage(event.getMessageId()));
            if (message != null) {
                return processMessage(message);
            }
        } catch (Exception e) {
            log.error("Delete Event has No Message", e);
        }
        return null;
    }
}
