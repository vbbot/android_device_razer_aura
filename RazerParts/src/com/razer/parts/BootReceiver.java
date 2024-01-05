/*
 * Copyright (c) 2021, Alcatraz323 <alcatraz32323@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.razer.parts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;

import static com.razer.parts.Constants.*;
import com.razer.parts.ShellUtils;
import com.razer.parts.ShellUtils.CommandResult;
import com.razer.parts.SharedPreferenceUtil;
import com.razer.parts.ChromaManager;
import com.razer.parts.R;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }

        SharedPreferenceUtil spfu = SharedPreferenceUtil.getInstance();
        String mode = (String) spfu.get(context, CHROMA_MODE,
                "color");
        String color = (String) spfu.get(context, CHROMA_COLOR,
                "#00FF00");
        String brightness = (String) spfu.get(context, CHROMA_BRIGHTNESS,
                "200");
        String resolution = (String) spfu.get(context, SCREEN_RESOLUTION,
                "1440");
        boolean chromaEnabled = (boolean) spfu.get(context, CHROMA_SWITCH,
                false);

        if(resolution.equals("1440")) {
            ShellUtils.execCommand("wm density 480", false);
            ShellUtils.execCommand("wm size 1440x2560", false);
        } else {
            ShellUtils.execCommand("wm density 360", false);
            ShellUtils.execCommand("wm size 1080x1920", false);
        }

        /* Buggy [WIP]*/
        // String refreshRate = (String) spfu.get(context, SCREEN_REFRESH_RATE,
        //         "120");
        // int parseInt = Integer.parseInt(refreshRate);
        // IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
        // if (windowManagerService != null) {
        //     try {
        //         windowManagerService.setDisplayRefreshRate(0, parseInt);
        //     } catch(RemoteException e) {
        //         e.printStackTrace();
        //     }
        // }

        if(chromaEnabled) {
            ChromaManager tempManager = new ChromaManager();
            tempManager.systemReady();
            tempManager.loadMcuWithParams(context);
        }
    }
}
