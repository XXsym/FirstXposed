package com.handsomexi.firstxposed.util;

import android.graphics.Color;
import android.util.TypedValue;
import android.widget.TextView;

public class StyleUtil {
    public static final float TEXT_SIZE_DEFAULT = 15.0f;
    public static final float TEXT_SIZE_BIG = 17.0f;
    public static final float TEXT_SIZE_SMALL = 13.0f;

    public static final int TEXT_COLOR_DEFAULT = Color.BLACK;
    public static final int TEXT_COLOR_SECONDARY = 0xFF8A9899;

    public static final int LINE_COLOR_DEFAULT = 0xFFE5E5E5;

    public static void apply(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DEFAULT);
        textView.setTextColor(TEXT_COLOR_DEFAULT);
    }
}
