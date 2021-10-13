package com.starbunk.bot

import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono


fun main(args: Array<String>) {
    val token = args[0] // pulls token found on the Discord Bot Portal from the command line on startup
    val client = DiscordClient.create(token) // creates a DiscordClient using the provided Token

    client.withGateway { // creates a connection to Discord's API
        mono { // create a publisher object that emits the following
            it.on(MessageCreateEvent::class.java) // emits a MessageCreateEvent Object when the bot detects one
                .asFlow() // converts published events to a stream
                .collect {  // handles the events found in the stream
                    val message = it.message
                }
        }
    }.block()
}


