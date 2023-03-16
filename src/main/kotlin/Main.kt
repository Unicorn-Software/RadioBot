import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES
import org.ini4j.Ini
import java.io.File


fun main(args: Array<String>) {
    val config = Ini(File(args[0]))
    val builder: JDABuilder = JDABuilder.createDefault(config.get("bot", "token"), listOf(GUILD_MEMBERS, GUILD_VOICE_STATES))
    builder.setActivity(Activity.listening(config.get("bot", "activity")))
    builder.addEventListeners(MainListener(config.get("bot", "channel").toLong(),
        config.get("bot", "url")))
    builder.build()
}

