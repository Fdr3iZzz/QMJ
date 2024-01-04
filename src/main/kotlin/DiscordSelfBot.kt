package de.Franz3

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.on
import org.caffeine.octane.entities.Snowflake
import org.caffeine.octane.typedefs.ClientType
import org.caffeine.octane.typedefs.LoggerLevel
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class DiscordSelfBot {

    private fun getConfig (key : String) : String{
        val properties = Properties()
        val inputStream = DiscordSelfBot::class.java.getResourceAsStream("/config.properties")
        properties.load(inputStream)
        return properties.getProperty(key)
    }

    fun getMessageLink(guildID : String, channelID : String, messageID : String) = "https://canary.discord.com/channels/$guildID/$channelID/$messageID"

    suspend fun selfBot() {
        val token = getConfig("dcToken")
        val userID = "1055156047549046794"
        val QmjGuildID = "972069061506785330"
        val QmjChannelID = "972069061951361056"

        val client = Client.build {
            config {
                this.token = token
                clientType = ClientType.USER
                loggerLevel = LoggerLevel.ALL
            }
        }
        client.on<ClientEvent.MessageCreate> {
            // filter only specific channel, no bots, not myself
            if (message.channel.id == Snowflake(QmjChannelID) && message.guild?.id == Snowflake(QmjGuildID) && !message.author.bot && message.author.id != Snowflake(userID)) {
                // handle empty
                if(message.content.isEmpty()) {
                    print("message empty")
                    return@on
                }
                // get date
                val date = Date(message.timestamp)
                // TODO() format date

                val link = getMessageLink(message.guild!!.id.toString(), message.channel.id.toString(), message.id.toString())
                message.reply(link)
                RSS().appendToFeed("Title", link, message.content, date)
            }
        }
        client.on<ClientEvent.Ready> {
            println("working")
        }
        client.login()
    }
}