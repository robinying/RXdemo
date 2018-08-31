package com.yubin.rxdemo.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.youdao.sdk.app.EncryptHelper;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.common.Constants;
import com.youdao.voicerecognize.online.ASRErrorCode;
import com.youdao.voicerecognize.online.ASRListener;
import com.youdao.voicerecognize.online.ASRParameters;
import com.youdao.voicerecognize.online.ASRRecognizer;
import com.youdao.voicerecognize.online.ASRResult;
import com.yubin.rxdemo.R;
import com.yubin.rxdemo.audio.AuditRecorderConfiguration;
import com.yubin.rxdemo.audio.ExtAudioRecorder;
import com.yubin.rxdemo.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class RecognizeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button recordButton;
    private Button stopButton;
    private Button mRecognizeButton;
    private TextView mTextView;
    private TextView resultText;
    private File mRecordFile;
    ExtAudioRecorder recorder;
    ASRParameters tps;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        recordButton = (Button) findViewById(R.id.start_record);
        recordButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop_record);
        stopButton.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.my_text);
        resultText =(TextView) findViewById(R.id.recog_text);
        mRecognizeButton = (Button) findViewById(R.id.recognizeBtn);
        mRecognizeButton.setOnClickListener(this);
        //输入音频为WAV格式
        tps = new ASRParameters.Builder()
                .source("youdaoocr")
                .timeout(100000)
                .lanType(Language.CHINESE.getCode())//langType支持中文和英文
                .rate(Constants.RATE_16000)
                .build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_record:
                record();
                break;
            case R.id.stop_record:
                recordstop();
                break;
            case R.id.recognizeBtn:
                Log.d("robin","start_recognize");
                recognize();
                break;
        }
    }

    public void record() {
        try {
            mRecordFile = File.createTempFile("record_", ".wav");
            AuditRecorderConfiguration configuration = new AuditRecorderConfiguration.Builder()
                    .recorderListener(listener)
                    .handler(handler)
                    .uncompressed(true)
                    .builder();

            recorder = new ExtAudioRecorder(configuration);
            recorder.setOutputFile(mRecordFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordstop() {
        try {
            int time = recorder.stop();
            if (time > 0) {
                if (mRecordFile != null) {
                    mTextView.setText(mRecordFile.getAbsolutePath());
                }
            }
            recorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ExtAudioRecorder.RecorderListener listener = new ExtAudioRecorder.RecorderListener() {
        @Override
        public void recordFailed(int failRecorder) {
            if (failRecorder == 0) {
                Toast.makeText(RecognizeActivity.this, "录音失败，可能是没有给权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RecognizeActivity.this, "发生了未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void recognize() {
        final String text = (String) mTextView.getText();
        Log.d("robin","recognize");
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(RecognizeActivity.this, "请录音或选择音频文件", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        try {
            resultText.setText("正在识别，请稍等....");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startRecognize(text);
                }
            }).start();
        } catch (Exception e) {
            Log.d("robin","Exception ="+e.toString());
        }
    }

    private void startRecognize(String filePath) {
        byte[] datas = null;
        try {
            datas = FileUtils.getContent(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("robin","startRecognize Exception ="+e.toString());
        }
        final String bases64 = EncryptHelper.getBase64(datas);
        ASRRecognizer.getInstance(tps).recognize(bases64,
                new ASRListener() {
                    @Override
                    public void onResult(final ASRResult result, String input, String requestid) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(result.getResult() != null && result.getResult().size() > 0){
                                    resultText.setText("识别完成:" +result.getResult().get(0));
                                }else{
                                    resultText.setText("未知错误");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final ASRErrorCode error, String requestid) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultText.setText("识别失败" + error.toString());
                            }
                        });
                    }
                }, "requestid");
    }
}
