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
    private val react = listOf("virus", "viral", "corona", "sick", "ill", "krank", "flu", "grippe", """home\s*office""").map { Regex(it) }
    private val random = listOf("hähähä", "I'm going viral!", "Drink Corona", "dumdidumm", "Bored at the home office?", "May I help you?", "Or do you prefer Heineken!?", "Hatschi!", "Stay away from me!", "The cake is a lie!", "42", "How many roads must a man walk down?", "Let there be rock!", "Have you tried turning it off and on again?", "I'm socially very distant", "Don't worry, be happy!")
    private val beer = listOf("beer", "bier", "cerveza", "biere", "birra", "öl", "øl", "ale")
    private val country = listOf("mexican", "dutch", "swiss", "german", "czech", "spanish", "japanese", "chinese")
    private val hate = listOf("windows", "liferay", "wine", "whatsapp", "php", "jsf", ".net", "water", "microsoft", "heineken", "carlsberg", "architekt", "architect", "winter", "cold", "cool", "kalt")
    private val love = listOf("unix", "linux", """(mexic\w+)""", "intelliJ", "jvm", "kotlin", "scala", "cake", "sleep", "schlafen", "summer", "sommer", "warm").map { Regex(it) }
    private val taste = listOf("taste", "geschmack", "durst", "drink", "hungry", "hunger", "eat", "essen")
    private val joke = listOf(
            Joke("Prediction:\nThere will be a minor baby boom in 9 months,\nand then one day in 2033, we shall whitness the rise of the QUARANTEENS."),
            Joke("If you need 144 rolls of toilet paper for a 14 day quarantine,\nyou probably should have been seeing a Doctor long before COVID-19."),
            Joke("The worst thing about corona?\nCOVID-19 looks like a Jira issue."),
            Joke("What is Happiness?\nWhen you work at a bank and two guys with masks come in\nbut they're just robbing the place."),
            Joke("Quarantine diary\n\nDay 1: I have stocked up on enough non-perishable food and supplies to last me for months, maybe years, so that I can remain in isolation for as long as it takes to see out this pandemic.\n\nDay 1 + 45 minutes: I am in the supermarket because I wanted a Twix."),
            Joke("Kid: Hey mum, when is this corona virus thing gonna be over?\nMum: Just shut up and eat your toilet paper."),
            Joke("Chuck Norris has been exposed to the Coronavirus.\nThe virus is now in quarantine for a month."),
            Joke("Some people aren't shaking hands because of the Corona virus.\nI'm not shaking hands because everyone is out of toilet paper."),
            Joke("", "olympia.jpg"),
            Joke("", "clean-ass.jpg"),
            Joke("", "tom.jpg"),
            Joke("", "chipotle.jpg"),
            Joke("", "learning.jpg"),
            Joke("", "corona-case.jpg"),
            Joke("", "young.jpg"),
            Joke("", "charmin.jpg"),
            Joke("", "tarantino.jpg"),
            Joke("", "apocalypse.jpg"),
            Joke("", "paper-crisis.jpg"),
            Joke("", "titanic.jpg"),
            Joke("", "cry.jpg"),
            Joke("", "god.jpg"),
            Joke("Even panic buyers have some honor.", "panic-buyer.jpg"),
            Joke("", "homework.jpg"),
            Joke("", "lockdown.jpg"),
            Joke("", "spock.jpg"),
            Joke("", "shining.jpg"),
            Joke("", "hands.jpg"),
            Joke("", "ebola.jpg"),
            Joke("", "mask.jpg"),
            Joke("", "bigfoot.jpg"),
            Joke("", "buy-god.jpg"),
            Joke("", "touch-face.jpg"),
            Joke("", "flirt.jpg"),
            Joke("Social distancing in the 1750s.", "old-distance.jpg"),
            Joke("", "borg.jpg")
    )

    override fun getBotToken() = System.getenv("MEXICAN_BEER_BOT_TOKEN")
    override fun getBotUsername() = System.getenv("MEXICAN_BEER_BOT_USER")

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
                listOf("joke", "witz").any { it in text } -> {
                    joke.choose().let {
                        if (it.image == null) execute(SendMessage(chatId, it.text))
                        else {
                            execute(SendPhoto().apply {
                                setChatId(chatId)
                                setCaption(it.text)
                                setPhoto(it.image, Thread.currentThread().contextClassLoader.getResourceAsStream(it.image))
                            })
                        }
                    }
                }
                rnd.nextDouble() < .1 -> {
                    execute(SendMessage(chatId, random.choose()))
                }
            }
        }
    }

    private fun <T> List<T>.choose() = this[rnd.nextInt(size)]
}

class Joke(val text: String, val image: String? = null)
