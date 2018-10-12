package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import voice.IffuVoiceClip;

public class CommandHonk extends Command {

    private AudioPlayerManager HONKManager;
    private AudioPlayer HONKPlayer;

    public CommandHonk() {
        super();
        this.name = "HONK";
        this.help = "HONK";
        this.guildOnly = true;
        this.ownerCommand = true;
        HONKManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(HONKManager);
    }

    @Override
    protected void execute(CommandEvent event) {

        AudioManager GOOSE = event.getGuild().getAudioManager();

        if(GOOSE.getConnectedChannel() != null)
            return;

        HONKManager.loadItem("https://www.youtube.com/watch?v=rgUNUUP1Ta0", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                VoiceChannel currentChannel = event.getMember().getVoiceState().getChannel();

                if (currentChannel == null) {
                    event.reply("HONK?!");
                    return;
                }

                if (!event.getGuild().getSelfMember().hasPermission(currentChannel, Permission.VOICE_CONNECT)) {
                    event.reply("HOOOOOONK!!!");
                    return;
                }

                HONKPlayer = HONKManager.createPlayer();

                IffuVoiceClip handle = new IffuVoiceClip(HONKPlayer);

                HONKPlayer.addListener(new AudioEventAdapter() {
                    @Override
                    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GOOSE.closeAudioConnection();
                                System.out.println("Closed HONK Connection");
                            }
                        }).start();
                    }
                });

                GOOSE.setSendingHandler(handle);
                HONKPlayer.playTrack(track);
                GOOSE.openAudioConnection(currentChannel);
                System.out.println("Opened HONK Connection");
            }

            @Override
            public void noMatches() {
                event.reply("HONK?!?!");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
            }
        });
    }

}
