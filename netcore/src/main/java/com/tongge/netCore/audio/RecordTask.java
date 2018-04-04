package com.tongge.netCore.audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tongge.common.sysUtils.AppManger;
import com.tongge.common.sysUtils.STLog;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.tongge.common.dataUtils.AppDataUtilsKt.getAppDataFile;

/**
 * Created by DZ on 2017/7/12.
 * 录音并发送
 */
public class RecordTask extends AsyncTask<Void, Integer, Void> {
    private final int AUDIOSOURCE = MediaRecorder.AudioSource.MIC;
    private final int AUDIOSOURCE_AEC = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
    private final int SAMPLERATE = 8000;
    private final int CHANNELCONFIG = AudioFormat.CHANNEL_IN_MONO;
    private final int AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private final int MINBUFFSIZE = AudioRecord.getMinBufferSize(SAMPLERATE, CHANNELCONFIG, AUDIOFORMAT);

    private AudioRecord audioRecord;
    private volatile boolean isRecord = false;
    private static RecordTask recordTask;

    private AudioTrack audioTrack;
    private STRecordAEC recordAEC;

    private RecordTask(){
//        if (STRecordAEC.isSupportAEC()) {
//            Toast.makeText(AppManger.getInstance().currentActivity(), "支持", Toast.LENGTH_SHORT).show();

        AudioManager audioManager = (AudioManager)AppManger.getInstance().currentActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(true);


            audioRecord = new AudioRecord(AUDIOSOURCE, SAMPLERATE, CHANNELCONFIG, AUDIOFORMAT, MINBUFFSIZE);
            audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                    8000,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    AudioTrack.getMinBufferSize(8000,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT),
                    AudioTrack.MODE_STREAM,
                    audioRecord.getAudioSessionId());
            if (NoiseSuppressor.isAvailable()){
                STLog.d(this,"支持降噪");
                NoiseSuppressor.create(audioRecord.getAudioSessionId()).setEnabled(true);
            }else{
                STLog.d(this,"不支持降噪");
            }
//            recordAEC = new STRecordAEC(audioRecord.getAudioSessionId());
//            recordAEC.setEnabled(true);
//        }
//        else {
//            Toast.makeText(AppManger.getInstance().currentActivity(), "不支持", Toast.LENGTH_SHORT).show();
//            audioRecord = new AudioRecord(AUDIOSOURCE, SAMPLERATE, CHANNELCONFIG, AUDIOFORMAT, MINBUFFSIZE);
//            audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
//                    8000,
//                    AudioFormat.CHANNEL_OUT_MONO,
//                    AudioFormat.ENCODING_PCM_16BIT,
//                    AudioTrack.getMinBufferSize(8000,
//                            AudioFormat.CHANNEL_OUT_MONO,
//                            AudioFormat.ENCODING_PCM_16BIT),
//                    AudioTrack.MODE_STREAM);
//        }



        AcousticEchoCanceler acousticEchoCanceler;
        NoiseSuppressor noiseSuppressor;
    }

    private void release(){
        if (audioRecord != null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        isRecord = false;
        recordTask.cancel(true);
        recordTask = null;
        if (recordAEC != null)
            recordAEC.release();
    }

    public static void openSpeak(){
        if (recordTask != null){
            recordTask.release();
        }
        recordTask = new RecordTask();
        recordTask.execute();
    }

    public static void closeSpeak(){
        if (recordTask != null){
            recordTask.release();
        }
    }

    public boolean isRecord() {
        return isRecord;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        isRecord = true;
        try {
            //在这里我们创建一个文件，用于保存录制内容
            File fpath = new File(getAppDataFile(AppManger.getInstance().currentActivity()));
            File audioFile = null;
            try {
                audioFile = new File(getAppDataFile(AppManger.getInstance().currentActivity())+"recording.pcm");//   File.createNewFile("recording", ".pcm", fpath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //开通输出流到指定的文件
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));
            //定义缓冲
            short[] buffer = new short[MINBUFFSIZE];
            //开始录制
            audioRecord.startRecording();
            audioTrack.play();

            int r = 0; //存储录制进度
            while (isRecord && !isCancelled()) {
                int bufferReadResult = audioRecord.read(buffer, 0, buffer.length);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
                audioTrack.write(buffer, 0, buffer.length);
                publishProgress(new Integer(r)); //向UI线程报告当前进度
                r++; //自增进度值
            }
            //录制结束
            audioRecord.stop();
            dos.close();
        } catch (Exception e) {
        }
        return null;
    }

    //当在上面方法中调用publishProgress时，该方法触发,该方法在UI线程中被执行
    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Void result) {

    }

}

