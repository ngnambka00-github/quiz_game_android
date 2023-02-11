package com.example.quizme.models;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizme.R;

public class PlaySound {
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private SoundPool soundPool;
    private AudioManager audioManager;
    private AppCompatActivity appCompatActivity;
    private float volume;
    private boolean loaded;
    int soundIds[] = new int[10];

    public PlaySound(){};

    public PlaySound(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) this.appCompatActivity.getSystemService(appCompatActivity.AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.appCompatActivity.setVolumeControlStream(streamType);
        this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundIds[0] = soundPool.load(this.appCompatActivity, R.raw.correct, 1);
        soundIds[1] = soundPool.load(this.appCompatActivity, R.raw.trans, 1);
        soundIds[2] = soundPool.load(this.appCompatActivity, R.raw.wrong, 1);
        soundIds[3] = soundPool.load(this.appCompatActivity, R.raw.timer, 1);
        soundIds[4] = soundPool.load(this.appCompatActivity, R.raw.tada, 1);
        soundIds[5] = soundPool.load(this.appCompatActivity, R.raw.ting, 1);
        soundIds[6] = soundPool.load(this.appCompatActivity, R.raw.support, 1);


    }
    public void playSoundSupport( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[6],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSound5050( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[5],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundResult( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[4],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundCorrect( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[0],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundFail( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[2],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundTrans( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[1],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
    public void playSoundTimer( )  {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            int streamId = this.soundPool.play(soundIds[3],leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
}
