package org.starbunk.bunkbot.service

import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Webhook
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.EventDispatcher.log
import discord4j.core.`object`.Embed
import discord4j.core.`object`.entity.Attachment
import discord4j.core.`object`.entity.Message
import discord4j.core.spec.MessageCreateFields
import discord4j.discordjson.json.WebhookCreateRequest
import discord4j.discordjson.json.gateway.MessageCreate
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLEncoder
import javax.print.DocFlavor.URL

@Service
@org.springframework.context.annotation.Lazy
open class WebhookComponent {

    @Autowired
    @org.springframework.context.annotation.Lazy
    private lateinit var gatewayClient: GatewayDiscordClient

    @Autowired
    private lateinit var webClient: RestClient

    private fun getWebhook(channel: TextChannel): Webhook? {
        var webhook = channel.webhooks
            .filter { webhook ->
                webhook.name.get().contains("BunkBot")
            }
            .blockFirst()
        if (webhook == null) {
            log.debug("Creating WebHook")
            val webhookData = webClient.webhookService.createWebhook(
                channel.id.asLong(),
                WebhookCreateRequest.builder().name("BunkBot-${channel.name}").build(),
                "Creating Webhook for New Message"
            ).block()
            if (webhookData != null)
                webhook = Webhook(gatewayClient, webhookData)
        }
        return webhook
    }

    fun writeMessage(
        channel: TextChannel,
        content: String,
        nickname: String,
        avatarUrl: String,
        embedData: MutableList<Embed> = ArrayList(),
        attatchments: MutableList<Attachment> = ArrayList()
    ) {
        getWebhook(channel)?.apply {
            this.name.map {
                log.info("$it: $content")
            }
            val files = attatchments.map {
                MessageCreateFields.File.of(it.filename,java.net.URL(it.url).openStream())
            }
            val mono = execute()
                .withAvatarUrl(avatarUrl)
                .withUsername(nickname)
                .withContent(content.ifBlank { "a" })
                .withWaitForMessage(true)
            if (files.isNotEmpty())
                mono.withFiles(files)
            mono.block()
        }
    }
}