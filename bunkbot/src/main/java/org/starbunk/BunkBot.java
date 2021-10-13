package org.starbunk;

import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BunkBot {
    public static void main(String[] args) {
        ApplicationContext springCtx = new SpringApplicationBuilder(BunkBot.class)
                .build()
                .run();
    }
}
