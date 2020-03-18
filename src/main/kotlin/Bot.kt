import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Update
import kotlin.random.Random

fun main() {
    ApiContextInitializer.init()
    TelegramBotsApi().apply {
        registerBot(Bot())
    }
}

class Bot : TelegramLongPollingBot() {
    private val rnd = Random
    private val react = listOf("virus", "viral", "corona", "sick", "ill", "krank", "flu", "grippe", "fever", "fieber", "hust", "keuch", "cough", "gesundheit", "health", """home\s*office""").map { Regex(it) }
    private val beer = listOf("beer", "bier", "cerveza", "biere", "birra", "öl", "øl", "ale")
    private val country = listOf("mexican", "dutch", "swiss", "german", "czech", "spanish", "japanese", "chinese")
    private val taste = listOf("taste", "geschmack", "durst", "drink", "hungry", "hunger", "eat", "essen")

    override fun getBotToken() = System.getenv("MEXICAN_BEER_BOT_TOKEN") ?: System.getenv("TOKEN")
    override fun getBotUsername() = System.getenv("MEXICAN_BEER_BOT_USER") ?: System.getenv("USER")

    override fun onUpdateReceived(update: Update) {
        if (update.message?.text != null) {
            val text = update.message.text.toLowerCase()
            println(text)
            val hated = hates.find { it in text }
            val loved = loves.find { it in text }?.let { it.find(text)!!.value }
            val chatId = update.message.chatId

            fun send(text: String) = execute(SendMessage(chatId, text))

            fun sendImage(name: String, caption: String) = execute(SendPhoto().apply {
                setChatId(chatId)
                setCaption(caption)
                setPhoto(name, Thread.currentThread().contextClassLoader.getResourceAsStream(name))
            })

            when {
                text.startsWith("/help") -> send("Say something about me, beer or jokes. I'll try to answer...")
                react.any { it in text } -> send(quotes.choose())
                beer.any { it in text } -> send("Can I have a ${country.choose()} beer, please?")
                hated != null -> send("I hate ${hated}!")
                loved != null -> send("I love ${loved}!")
                taste.any { it in text } -> send("Try me! I have an excellent taste!")
                listOf("joke", "witz").any { it in text } -> jokes.choose().let {
                    if (it.image == null) send(it.text)
                    else sendImage(it.image, it.text)
                }
                rnd.nextDouble() < .1 -> send(quotes.choose())
            }
        }
    }

    private fun <T> List<T>.choose() = this[rnd.nextInt(size)]
}
