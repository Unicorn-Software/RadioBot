import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason


/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
class TrackScheduler(player: AudioPlayer, mainListener: MainListener) : AudioEventAdapter() {
    private val player: AudioPlayer = player
    private val mainListener: MainListener = mainListener

    override fun onPlayerPause(player: AudioPlayer) {
        // Player was paused
    }

    override fun onPlayerResume(player: AudioPlayer) {
        // Player was resumed
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        // A track started playing
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        mainListener.play()
//        if (endReason.mayStartNext) {
//            // Start next track
//        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack, exception: FriendlyException) {
        mainListener.play()
        // An already playing track threw an exception (track end event will still be received separately)
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack, thresholdMs: Long) {
        mainListener.play()
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}