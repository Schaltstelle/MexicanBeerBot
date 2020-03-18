import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
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
    private val react = listOf("virus", "viral", "corona", "sick", "ill", "krank", "flu", "grippe", """home\s*office""").map { Regex(it) }
    private val random = listOf("hähähä", "I'm going viral!", "Drink Corona", "dumdidumm", "Bored at the home office?", "May I help you?", "Or do you prefer Heineken!?", "Hatschi!", "Stay away from me!", "The cake is a lie!", "42", "How many roads must a man walk down?", "Let there be rock!", "Have you tried turning it off and on again?", "I'm socially very distant", "Don't worry, be happy!")
    private val beer = listOf("beer", "bier", "cerveza", "biere", "birra", "öl", "øl", "ale")
    private val country = listOf("mexican", "dutch", "swiss", "german", "czech", "spanish", "japanese", "chinese")
    private val hate = listOf("windows", "liferay", "wine", "whatsapp", "jsf", ".net", "water", "microsoft", "heineken", "carlsberg")
    private val love = listOf("unix", "linux", """(mexic\w+)""", "intelliJ", "cake", "sleep", "schlafen").map { Regex(it) }
    private val taste = listOf("taste", "geschmack", "durst", "drink", "hungry", "hunger", "eat", "essen")

    override fun onUpdateReceived(update: Update) {
        if (update.message?.text != null) {
            val text = update.message.text.toLowerCase()
            println(text)
            val hated = hate.find { it in text }
            val loved = love.find { it in text }?.let { it.find(text)!!.value }
            val chatId = update.message.chatId
            when {
                react.any { it in text } -> {
                    execute(SendMessage(chatId, random.choose()))
                }
                beer.any { it in text } -> {
                    execute(SendMessage(chatId, "Can I have a ${country.choose()} Beer, please?"))
                }
                hated != null -> {
                    execute(SendMessage(chatId, "I hate ${hated}!"))
                }
                loved != null -> {
                    execute(SendMessage(chatId, "I love ${loved}!"))
                }
                taste.any { it in text } -> {
                    execute(SendMessage(chatId, "Try me! I have an excellent taste!"))
                }
                rnd.nextDouble() < .1 -> {
                    execute(SendMessage(chatId, random.choose()))
                }
            }
        }
    }

    fun <T> List<T>.choose() = this[rnd.nextInt(size)]
}

