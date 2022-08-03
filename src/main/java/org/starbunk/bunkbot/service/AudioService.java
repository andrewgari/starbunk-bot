package org.starbunk.bunkbot.service;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import org.springframework.stereotype.Service;
import org.starbunk.bunkbot.audio.LavaPlayerAudioProvider;

@Service
public class AudioService {
    private final LavaPlayerAudioProvider provider;
    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;

    public AudioService() {
        // Creates AudioPlayer instances and translates URLs to AudioTrack instances
        this.playerManager = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize. It is not important to understand
        this.playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager);
        // Create an AudioPlayer so Discord4J can receive audio data
        this.player = playerManager.createPlayer();
        // We will be creating LavaPlayerAudioProvider in the next step
        this.provider = new LavaPlayerAudioProvider(player);
    }

    public LavaPlayerAudioProvider getProvider() {
        return provider;
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
