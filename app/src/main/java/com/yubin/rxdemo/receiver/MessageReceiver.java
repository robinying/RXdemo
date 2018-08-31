package com.yubin.rxdemo.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.yubin.rxdemo.bean.XGNotification;
import com.yubin.rxdemo.common.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * author : Yubin.Ying
 * time : 2018/8/27
 */
public class MessageReceiver extends XGPushBaseReceiver {


    public static final String TAG = "TPushReceiver";

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        Log.d(TAG,"onRegisterResult xgPushRegisterResult ="+xgPushRegisterResult.toString());

    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int i, String s) {
        Log.d(TAG,"onSetTagResult s ="+s);

    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        Log.d(TAG,"onDeleteTagResult s ="+s);

    }

    // 消息透传的回调
    //应用内消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        Log.d(TAG,"onTextMessage xgPushTextMessage ="+xgPushTextMessage.toString());

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        Log.d(TAG,"onNotifactionClickedResult xgPushClickedResult ="+xgPushClickedResult.toString());
        if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            Intent intent = new Intent(context,com.yubin.rxdemo.activity.SettingsActivity.class);
            context.startActivity(intent);

        } else if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
        }

    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        if (context == null || xgPushShowedResult == null) {
            return;
        }
        XGNotification notific = new XGNotification();
        notific.setMsg_id(xgPushShowedResult.getMsgId());
        notific.setTitle(xgPushShowedResult.getTitle());
        notific.setContent(xgPushShowedResult.getContent());
        // notificationActionType==1为Activity，2为url，3为intent
        notific.setNotificationActionType(xgPushShowedResult
                .getNotificationActionType());
        //Activity,url,intent都可以通过getActivity()获得
        notific.setActivity(xgPushShowedResult.getActivity());
        Log.d(TAG,"getNotificationActionType ="+xgPushShowedResult.getNotificationActionType());
        Log.d(TAG,"getActivity ="+xgPushShowedResult.getActivity());
        notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().getTime()));
        Intent intent = new Intent(Utils.UPDATE);
        intent.putExtra("title",xgPushShowedResult.getTitle());
        intent.putExtra("content",xgPushShowedResult.getContent());
        context.sendBroadcast(intent);
        Log.d(TAG,"notification string ="+xgPushShowedResult.toString());
    }
}
