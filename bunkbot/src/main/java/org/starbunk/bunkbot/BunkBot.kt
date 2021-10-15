package org.starbunk.bunkbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = arrayOf("org.starbunk"))
open class BunkBot

fun main(args: Array<String>) {
    runApplication<BunkBot>(*args)
}