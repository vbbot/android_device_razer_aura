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
import android.provider.Settings;

import static android.provider.Settings.System.MIN_REFRESH_RATE;
import static android.provider.Settings.System.PEAK_REFRESH_RATE;
import static com.razer.parts.Constants.CHROMA_SWITCH;

public class BootReceiver extends BroadcastReceiver {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            return;
        }

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        context.startService(new Intent(context, HolderService.class));

        /* Buggy */
        // String resolution = (String) sharedPreferenceUtil.get(context, SCREEN_RESOLUTION,
        //         "1440");
        // if(resolution.equals("1440")) {
        //     ShellUtils.execCommand("wm density 480", false);
        //     ShellUtils.execCommand("wm size 1440x2560", false);
        // } else {
        //     ShellUtils.execCommand("wm density 360", false);
        //     ShellUtils.execCommand("wm size 1080x1920", false);
        // }

        int refreshRate = Settings.System.getInt(context.getContentResolver(), PEAK_REFRESH_RATE, 120);
        Settings.System.putInt(context.getContentResolver(), MIN_REFRESH_RATE, refreshRate);
        Settings.System.putInt(context.getContentResolver(), PEAK_REFRESH_RATE, refreshRate);

        boolean chromaEnabled = (boolean) sharedPreferenceUtil.get(context, CHROMA_SWITCH, false);
        if (chromaEnabled) {
            ChromaManager tempManager = new ChromaManager();
            tempManager.systemReady();
            tempManager.loadMcuWithParams(context);
        }
    }
}
