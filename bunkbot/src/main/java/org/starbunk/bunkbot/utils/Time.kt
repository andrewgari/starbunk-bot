package org.starbunk.bunkbot.utils

import discord4j.core.`object`.entity.Message
import java.time.*
import java.time.temporal.ChronoUnit

fun Instant.isBeforeToday(): Boolean {
    return isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).with(LocalTime.MIN).toInstant(ZoneOffset.UTC))
}

//fun Instant.isWithinFiveMinutesOf(oldMessage: Instant): Boolean {
//    val fiveMinutesAfter = oldMessage.plus(5, ChronoUnit.MINUTES)
//    return isBefore(fiveMinutesAfter)
//}

fun Instant.isWithinFiveMinutesOf(time: Instant): Boolean {
    println((epochSecond - time.epochSecond) < (20*60*1000))
    return (epochSecond - time.epochSecond) < (20*60*1000)
}

fun Message.isWithinFiveMinutesOf(message: Message): Boolean {
    with(this.timestamp) {
        println((epochSecond - message.timestamp.epochSecond) < (20 * 60 * 1000))
        return (epochSecond - message.timestamp.epochSecond) < (20 * 60 * 1000)
    }
}