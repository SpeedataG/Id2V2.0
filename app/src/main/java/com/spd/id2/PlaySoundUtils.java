package com.spd.id2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

/**
 * Created by brxu on 2017/3/21.
 */

public class PlaySoundUtils {

    private static SparseIntArray mapSRC;
    private static SoundPool sp;

    public static void initSoundPool(Context context) {
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mapSRC = new SparseIntArray(16);
        mapSRC.put(1, sp.load(context, R.raw.error, 0));
        mapSRC.put(2, sp.load(context, R.raw.welcome, 0));
        mapSRC.put(3, sp.load(context, R.raw.msg, 0));
    }

    /**
     * 播放声音池的声音
     */
    public static void play(int sound, int number) {
        sp.play(mapSRC.get(sound), 1.0f, 1.0f, 0, number, 0);
    }
}
