package com.frank.ffmpeg.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.frank.ffmpeg.FFmpegCmd;
import com.frank.ffmpeg.R;
import com.frank.ffmpeg.util.FFmpegUtil;

/**
 * 使用ffmpeg处理音频
 * Created by frank on 2018/1/23.
 */

public class FFmpegConfigActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG = FFmpegConfigActivity.class.getSimpleName();
    private final static int MSG_BEGIN = 11;
    private final static int MSG_FINISH = 12;
    private ProgressBar progress_audio;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_BEGIN:
                    progress_audio.setVisibility(View.VISIBLE);
                    setGone();
                    break;
                case MSG_FINISH:
                    progress_audio.setVisibility(View.GONE);
                    setVisible();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_conf);

        initView();
    }

    private void initView() {
        progress_audio = (ProgressBar) findViewById(R.id.progress_conf);
        findViewById(R.id.btn_codecs).setOnClickListener(this);
        findViewById(R.id.btn_fmt).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
    }

    private void setVisible() {
        findViewById(R.id.btn_codecs).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_fmt).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_filter).setVisibility(View.VISIBLE);
    }

    private void setGone() {
        findViewById(R.id.btn_codecs).setVisibility(View.GONE);
        findViewById(R.id.btn_fmt).setVisibility(View.GONE);
        findViewById(R.id.btn_filter).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {


        int handleType;
        switch (v.getId()){
            case R.id.btn_codecs:
                handleType = 0;
                break;
            case R.id.btn_fmt:
                handleType = 1;
                break;
            case R.id.btn_filter:
                handleType = 2;
                break;
            default:
                handleType = 0;
                break;
        }
        if (handleType == 0) {
            FFmpegCmd.executeConfig(null);
            return;
        }
        doHandleAudio(handleType);
    }

    /**
     * 调用ffmpeg处理音频
     * @param handleType handleType
     */
    private void doHandleAudio(int handleType){
        String[] commandLine = null;
        switch (handleType){
            case 0://转码
                commandLine = FFmpegUtil.ffmpegCodecs();
                break;
            case 1://剪切
                commandLine = FFmpegUtil.ffmpegFormats();
                break;
            case 2:
                commandLine = FFmpegUtil.ffmpegFilters();
                break;
            default:
                break;
        }
        executeFFmpegCmd(commandLine);
    }

    /**
     * 执行ffmpeg命令行
     * @param commandLine commandLine
     */
    private void executeFFmpegCmd(final String[] commandLine){
        if(commandLine == null){
            return;
        }
        FFmpegCmd.execute(commandLine, new FFmpegCmd.OnHandleListener() {
            @Override
            public void onBegin() {
                Log.i(TAG, "handle audio onBegin...");
                mHandler.obtainMessage(MSG_BEGIN).sendToTarget();
            }

            @Override
            public void onEnd(int result) {
                Log.i(TAG, "handle audio onEnd...");
                mHandler.obtainMessage(MSG_FINISH).sendToTarget();
            }
        });
    }
}
