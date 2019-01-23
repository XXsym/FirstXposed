package com.handsomexi.firstxposed.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DpUtil {
    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDensity(context) + 0.5f);
    }

    public static float dip2pxF(Context context, float dipValue) {
        return dipValue * getDensity(context);
    }

    private static float getDensity(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }
}
