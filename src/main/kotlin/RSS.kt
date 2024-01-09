package de.Franz3

import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEnclosureImpl
import com.rometools.rome.feed.synd.SyndEntryImpl
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import com.rometools.rome.feed.synd.SyndImage
import com.rometools.rome.feed.synd.SyndImageImpl
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.SyndFeedOutput
import org.jsoup.Jsoup
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import java.util.*
import java.util.regex.Pattern


class RSS {
    fun createFeed (createFile : Boolean) : SyndFeed{
        val feed: SyndFeed = SyndFeedImpl()
        feed.feedType = "atom_0.3"
        feed.title = "QMJ"
        feed.link = "https://discord.gg/PdRjwtNRVs"
        feed.description = "the quatroph musical journey will be posted here, up to six times a week"
        feed.author = "Quatroph"
        feed.language = "en-us"
        if (createFile) writeToFile(feed)
        return feed
    }
    fun addEntry(feed : SyndFeed, title : String, link : String, text : String, date : Date, imageUrl : String) {
        val newEntry = SyndEntryImpl()
        newEntry.title = title
        newEntry.link = link
        // create description
        val description = SyndContentImpl()
        description.type = "text/html"
        description.value = text
        // set description
        newEntry.description = description
        // create image
        val enclosure = SyndEnclosureImpl()
        enclosure.url = imageUrl
        enclosure.type = "image/jpeg"
        // set image
        newEntry.enclosures = listOf(enclosure)
        // add new entry
        feed.entries = feed.entries + newEntry
        feed.publishedDate = date

        // write to file
        writeToFile(feed)
    }
    fun writeToFile(feed : SyndFeed){
        val writer: Writer = FileWriter("QMJ.xml")
        val output = SyndFeedOutput()
        output.output(feed, writer)
        writer.close()
    }
    fun appendToFeed(title : String, link : String, text : String, date : Date, imageUrl : String) {
        // Create a SyndFeedInput object
        val input = SyndFeedInput()
        // Read the existing feed from the file and convert it to a SyndFeed object
        val feed = input.build(FileReader("QMJ.xml"))
        // add to feed
        addEntry(feed, title, link, text, date, imageUrl)
    }
}