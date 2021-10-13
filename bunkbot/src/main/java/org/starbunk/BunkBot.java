package org.starbunk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BunkBot {
    public static void main(String[] args) {
        ApplicationContext springCtx = new SpringApplicationBuilder(BunkBot.class)
                .build()
                .run();
    }
}
