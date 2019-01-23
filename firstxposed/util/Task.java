package com.handsomexi.firstxposed.util;

import android.os.Handler;
import android.os.Looper;

public class Task {
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void onMain(long msec, final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception ignored) {
            }
        };
        sMainHandler.postDelayed(run, msec);
    }

    public static void onMain(final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception ignored) {
            }
        };
        sMainHandler.post(run);
    }
}
