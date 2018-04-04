package com.tongge.netCore.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import com.tongge.common.sysUtils.AppManger;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import static com.tongge.common.dataUtils.AppDataUtilsKt.getAppDataFile;

/**
 * Created by DZ on 2017/7/12.
 * 播放设备端传来的声音
 */

public class AudioPlayTask extends AsyncTask<Void, Integer, Void> {
    private volatile boolean isPlaying = false;
    private AudioTrack audioTrack;
    private static AudioPlayTask audioPlayTask;

    private AudioPlayTask(){
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(8000,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT),
                AudioTrack.MODE_STREAM);
    }

    private void release(){
        if (audioTrack != null){
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
        isPlaying = false;
        audioPlayTask.cancel(true);
        audioPlayTask = null;
    }

    public static void openSound(){
        if (audioPlayTask != null)
            audioPlayTask.release();
        audioPlayTask = new AudioPlayTask();
        audioPlayTask.execute();
    }

    public static void closeSound(){
        if (audioPlayTask != null)
            audioPlayTask.release();
    }


    @Override
    protected Void doInBackground(Void... arg0) {
        isPlaying = true;
        short[] buffer = new short[AudioTrack.getMinBufferSize(8000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT) / 4];
        try {
            File audioFile = null;
            File fpath = new File(getAppDataFile(AppManger.getInstance().currentActivity()));
            audioFile = new File(getAppDataFile(AppManger.getInstance().currentActivity())+"recording.pcm");
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
            audioTrack.play();
            while (isPlaying && dis.available() > 0 && !isCancelled()) {
                int i = 0;
                while (dis.available() > 0 && i < buffer.length) {
                    buffer[i] = dis.readShort();
                    i++;
                }
                audioTrack.write(buffer, 0, buffer.length);

            }

            audioTrack.stop();
            dis.close();
        } catch (Exception e) {
        }
        return null;
    }

    protected void onPostExecute(Void result) {
    }

    protected void onPreExecute() {

    }

}
