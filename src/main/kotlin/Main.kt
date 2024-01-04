package de.Franz3


suspend fun main() {
    val string = "Today's quatroph musical journey concerns artist Clairo. The warm and fuzzy feeling of emerging from a warm bed on a bright and cold winter's morning, a dressing gown draped across your shoulders. The intense, aromatic embrace of a hot cup of your favourite morning beverage and the sounds the world waking up around you are the hallmarks of Clairo's sound- grown from the tiny embryo of a Bandcamp bedroom setup onto the world stage (via TikTok and Instagram) a truly modern female artist for the modern age. Clairo - Softly. \uD83C\uDFB6 https://www.youtube.com/watch?v=SQdgdWayPhQ My thanks to @demi for the tip. @Daily QMJ notifications"
    println(RSS().handleMessage(string).first)
    println(RSS().handleMessage(string).second)
     DiscordSelfBot().selfBot()
    // RSS().createFeed(true)

}
