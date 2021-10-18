package org.starbunk.bunkbot.utils

import java.time.*
import java.time.temporal.ChronoUnit


fun Instant.isToday(): Instant {
    return LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).with(LocalTime.MIN).toInstant(ZoneOffset.UTC)
}

fun Instant.isFiveMinutesAgo(): Boolean {
    return isBefore(Instant.now().plus(5, ChronoUnit.MINUTES))
}

fun Instant.isFiveMinutesBefore(time: Instant): Boolean {
    return isBefore(time.plus(5, ChronoUnit.MINUTES))
}

fun Instant.isBeforeToday(): Boolean {
    return isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).with(LocalTime.MIN).toInstant(ZoneOffset.UTC))
}

fun Instant.withinFiveMinutesOf(time: Instant): Boolean {
    println((epochSecond - time.epochSecond) < (20*60*1000))
    return (epochSecond - time.epochSecond) < (20*60*1000)
}