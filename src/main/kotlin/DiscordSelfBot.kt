package de.Franz3

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.on
import org.caffeine.octane.entities.Snowflake
import org.caffeine.octane.typedefs.ClientType
import org.caffeine.octane.typedefs.LoggerLevel
import org.caffeine.octane.typedefs.StatusType
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


class DiscordSelfBot {

    private fun getConfig (key : String) : String{
        val properties = Properties()
        val inputStream = DiscordSelfBot::class.java.getResourceAsStream("/config.properties")
        properties.load(inputStream)
        return properties.getProperty(key)
    }

    fun getMessageLink(guildID : String, channelID : String, messageID : String) = "https://canary.discord.com/channels/$guildID/$channelID/$messageID"
    fun handleMessage(s: String): Triple<String, String, String> {
        var message = s
        if (message.isEmpty()) throw IllegalArgumentException("Message somehow is empty")
        // remove tag
        message = message.replace("@Daily QMJ notifications", "")
        // get url
        var title = ""
        var imageUrl = ""
        // get YT URL (url)
        var pattern = Pattern.compile("https://www.youtube.com/watch\\?v=[^ ]*")
        var matcher = pattern.matcher(message)
        if (matcher.find()) {
            val url = matcher.group(0)
            title = handleYoutubeLink(url).first
            imageUrl = handleYoutubeLink(url).second
        } else {
            // get YT URL (share)
            pattern = Pattern.compile("https://youtu.be/[^?]*")
            matcher = pattern.matcher(message)
            if (matcher.find()) {
                val url = matcher.group(0)
                title = handleYoutubeLink(url).first
                imageUrl = handleYoutubeLink(url).second
            } else {
                // get Spotify URL (share)
                pattern = Pattern.compile("https://open.spotify.com/track/[^?]*")
                matcher = pattern.matcher(message)
                if (matcher.find()) {
                    val url = matcher.group(0)
                    title = handleSpotifyLink(url).first
                    imageUrl = handleSpotifyLink(url).second
                } else throw IllegalArgumentException("No matching URL found")
            }
        }
        return Triple(message, title, imageUrl)
    }
    fun handleYoutubeLink (url : String) : Pair<String, String>{
        val document = Jsoup.connect(url).get()
        val title = document.select("meta[property=og:title]").attr("content")
        val imageUrl = document.select("meta[property=og:image]").attr("content")
        return Pair(title, imageUrl)
    }
    fun handleSpotifyLink (url : String) : Pair<String, String>{
        val connection = Jsoup.connect(url)
        connection.userAgent("facebookexternalhit/1.1")
        val document = connection.get()
        val title = document.select("meta[property=og:title]").attr("content")
        val imageUrl = document.select("meta[property=og:image]").attr("content")
        return Pair(title, imageUrl)
    }

    suspend fun selfBot() {
        val token = getConfig("dcToken")
        val userID = getConfig("userID")
        val QmjGuildID = getConfig("guildID")
        val QmjChannelID = getConfig("channelID")

        val client = Client.build {
            config {
                this.token = token
                clientType = ClientType.USER
                loggerLevel = LoggerLevel.ALL
                statusType = StatusType.INVISIBLE
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
                // handle when message is just edited
                if (message.editedAt != null) {
                    print("message edited")
                    return@on
                }
                // get date
                val date = Date(message.timestamp)
                // TODO() format date
                // get link to message
                val link = getMessageLink(message.guild!!.id.toString(), message.channel.id.toString(), message.id.toString())
                // process message to get title, formatted content and image url
                val processedMessage = handleMessage(message.content)
                // write information to RSS feed
                RSS().appendToFeed(processedMessage.second, link, processedMessage.first, date, processedMessage.third)
            }
        }
        client.on<ClientEvent.Ready> {
            println("working")
        }
        client.login()
    }

}