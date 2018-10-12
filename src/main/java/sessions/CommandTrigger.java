package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import voice.IffuVoiceClip;
import voice.IffuVoiceManager;

public class CommandTrigger extends Command {

    private AudioPlayerManager voicePlayerManager;
    private AudioPlayer voiceClipPlayer;

    public CommandTrigger() {
        super();
        this.name = "Trigger";
        this.help = "Trigger a voice for playback.";
        this.guildOnly = true;
        this.ownerCommand = false;
        voicePlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(voicePlayerManager);
    }

    @Override
    protected void execute(CommandEvent event) {

        if (event.getArgs().startsWith("list")) {
            String msg = "The available voices are:\n";
            String[] actors = IffuVoiceManager.listVoiceActors();
            for (String s : actors)
                msg += s + "\n";
            event.reply(msg);
            return;
        }

        AudioManager instrument = event.getGuild().getAudioManager();

        if(instrument.getConnectedChannel() != null)
            return;

        voicePlayerManager.loadItem(IffuVoiceManager.getVoiceClip(event.getArgs()), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                VoiceChannel currentChannel = event.getMember().getVoiceState().getChannel();

                if (currentChannel == null) {
                    event.reply("Can't speak if you can't listen.");
                    return;
                }

                if (!event.getGuild().getSelfMember().hasPermission(currentChannel, Permission.VOICE_CONNECT)) {
                    event.reply("Agh! I can't speak!");
                    return;
                }

                voiceClipPlayer = voicePlayerManager.createPlayer();

                IffuVoiceClip handle = new IffuVoiceClip(voiceClipPlayer);

                voiceClipPlayer.addListener(new AudioEventAdapter() {
                    @Override
                    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                instrument.closeAudioConnection();
                                System.out.println("Closed Audio Connection");
                            }
                        }).start();
                    }
                });

                instrument.setSendingHandler(handle);
                voiceClipPlayer.playTrack(track);
                instrument.openAudioConnection(currentChannel);
                System.out.println("Opened Audio Connection");
            }

            @Override
            public void noMatches() {
                event.reply("I didn't quite understand. Please use ``iffu?trigger list`` to see all voice actors.");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }

            @Override
            public void playlistLoaded(AudioPlaylist arg0) {
            }
        });
    }

}
