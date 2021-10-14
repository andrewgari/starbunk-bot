package org.starbunk.bots.webhooks

import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Webhook
import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.discordjson.json.WebhookCreateRequest
import discord4j.rest.RestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WebhookComponent {

    @Autowired
    private lateinit var gatewayClient: GatewayDiscordClient

    @Autowired
    private lateinit var webClient: RestClient

    private fun getWebhook(channel: TextChannel): Webhook? {
        var webhook = channel.webhooks
            .filter { webhook ->
                !webhook.name.get().contains("BunkBot")
            }
            .blockFirst()
        if (webhook == null) {
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

    fun writeMessage(channel: TextChannel, content: String, nickname: String, avatarUrl: String) {
        getWebhook(channel)?.apply {
            execute()
                .withAvatarUrl(avatarUrl)
                .withUsername(nickname)
                .withContent(content)
                .block()
        }
    }
}