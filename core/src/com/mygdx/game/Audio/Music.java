package com.mygdx.game.Audio;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public enum Music {
    MANAGER;

    private final Map<String, Sound> sounds;

    {
        sounds = new HashMap<>();
    }

    public void load(String path, String name) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(name, sound);
    }

    public void play(String name) {
        play(name, 1f);
    }

     //Plays a sound width volume control.

    public void play(String name, float volume) {
        sounds.get(name).play(volume);
    }

    public void loop(String name) {
        sounds.get(name).loop();
    }

    public void stop(String name) {
        sounds.get(name).stop();
    }

    public void stopAll() {
        for (Sound s : sounds.values()) {
            s.stop();
        }
    }
}

