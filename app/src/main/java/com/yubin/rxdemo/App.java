package com.yubin.rxdemo;

import android.app.Application;

import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.youdao.sdk.app.YouDaoApplication;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;


/**
 * author : Yubin.Ying
 * time : 2018/8/27
 */
public class App extends Application {
    private static  App instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance= this;
        //XGPushConfig.enableDebug(this, true);
        XGPushConfig.getToken(this);
        android.util.Log.d("robin","getToken ="+XGPushConfig.getToken(this));
        XGPushManager.registerPush(this);
        YouDaoApplication.init(this,"7348f6998f0b869a"); //创建应用，每个应用都会有一个Appid，绑定对应的翻译服务实例，即可使用

        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        SpeechUtility.createUtility(instance, "appid=" + getString(R.string.app_id));
    }

    public App getInstance(){
        return  instance;
    }
}
