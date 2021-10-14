package org.starbunk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication

@SpringBootApplication
open class BunkBot

fun main(args: Array<String>) {
    runApplication<BunkBot>(*args)
}
