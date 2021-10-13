package org.starbunk;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.starbunk.listeners.BunkEventListener;

import java.util.List;

@Configuration
public class BunkConfig {

    @Value("${token}")
    private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient  gatewayDiscordClient(List<BunkEventListener<T>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();
        for (BunkEventListener<T> listener : eventListeners) {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }
        return client;
    }

    @Bean
    public RestClient discordRestClient() {
        return RestClient.create(token);
    }
}
