package org.starbunk.bunkbot.utils

import java.time.*

fun Instant.isBeforeToday(): Boolean {
    return isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).with(LocalTime.MIN).toInstant(ZoneOffset.UTC))
}

fun Instant.isWithinFiveMinutesOf(time: Instant): Boolean {
    println((epochSecond - time.epochSecond) < (20*60*1000))
    // if time < time + 5s && time > time -5s
    val fiveMinutesAfter = time.plusSeconds(5 * 60)
    return this.isAfter(time) && this.isBefore(fiveMinutesAfter);
}