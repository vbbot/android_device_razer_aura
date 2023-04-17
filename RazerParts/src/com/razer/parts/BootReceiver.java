/*
 * Copyright (c) 2023, Alcatraz323 <alcatraz32323@gmail.com>
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;

import java.util.Locale;

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
        context.startService(new Intent(context, BMSService.class));

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

        boolean stepChargingManualOverride = (boolean) sharedPreferenceUtil.get(context, "bms_step_charging_switch",
                true);
        ShellUtils.execCommand("echo " + (stepChargingManualOverride ? "1" : "0") + " > /sys/class/power_supply/battery/step_charging_enabled", false);
        ShellUtils.execCommand("echo " + (stepChargingManualOverride ? "1" : "0") + " > /sys/class/power_supply/battery/sw_jeita_enabled", false);

        boolean first_ref_shown = (boolean) sharedPreferenceUtil.get(context, "first_ref_shown",
                false);
        
        if(first_ref_shown) {
            return;
        }

        Locale locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        String tag = locale.toLanguageTag();
        if(tag.equals("zh-Hans-CN")) {
            Intent intent1 = new Intent(context, NoticeActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
