package org.starbunk.bunkbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

class StarbunkProperties {
    lateinit var users: List<User>
    lateinit var roles: List<Role>
    lateinit var channels: List<Channel>

    fun getUser(name: String): User? = users.find { it.name == name }

    fun getRole(name: String): Role? = roles.find { it.name == name }

    fun getChannel(name: String): Channel? = channels.find { it.name == name }

    class User {
        var name: String = ""
        var id: Long = -1
    }
    class Role {
        var name: String = ""
        var id: Long = -1
    }
    class Channel {
        var name: String = ""
        var type: String = ""
        var id: Long = -1
    }
}