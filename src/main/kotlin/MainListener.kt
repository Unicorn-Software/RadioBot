import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class MainListener(private val channelId: Long, private val url: String) : ListenerAdapter() {
    private lateinit var playerManager: AudioPlayerManager
    private lateinit var player: AudioPlayer
    private var ready: Boolean = false

    private fun start(guild: Guild) {
        createPlayer()
        connect(guild)
        play()
    }

    private fun createPlayer() {
        playerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(playerManager)
        player = playerManager.createPlayer()
        player.addListener(TrackScheduler(player, this))
    }

    private fun connect(guild: Guild) {
        guild.audioManager.sendingHandler = AudioPlayerSendHandler(player)
        guild.audioManager.openAudioConnection(guild.getVoiceChannelById(channelId)!!)
    }

    fun play() {
        playerManager.loadItem(url, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                player.playTrack(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                for (track in playlist.tracks) {
                    player.playTrack(track)
                }
            }

            override fun noMatches() {
                // Notify the user that we've got nothing
            }

            override fun loadFailed(throwable: FriendlyException) {
                // Notify the user that everything exploded
            }
        })
    }

    override fun onReady(event: ReadyEvent) {
        super.onReady(event)
        println("wow!")
        if (!ready) {
            ready = true
            start(event.jda.getGuildById(622146911197200414)!!)
        }
    }
}