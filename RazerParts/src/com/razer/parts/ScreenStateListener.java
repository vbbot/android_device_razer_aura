package com.razer.parts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.razer.parts.Constants.CHROMA_KILL_RGB_WHEN_LOCK_SCREEN;
import static com.razer.parts.Constants.CHROMA_SWITCH;

public class ScreenStateListener extends BroadcastReceiver {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        SharedPreferenceUtil spfu = SharedPreferenceUtil.getInstance();
        boolean chromaEnabled = (boolean) spfu.get(context, CHROMA_SWITCH,
                false);
        boolean killWhenScreenLock = (boolean) spfu.get(context, CHROMA_KILL_RGB_WHEN_LOCK_SCREEN,
                false);

        if(!chromaEnabled) {
            return;
        }

        if(killWhenScreenLock) {
            ChromaManager tempManager = new ChromaManager();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                tempManager.systemReady();
                tempManager.loadMcuWithParams(context);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                tempManager.setHardwareSuspend(true);
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {

            }
        }
    }
}
