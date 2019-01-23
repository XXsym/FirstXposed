package com.handsomexi.firstxposed;

import android.app.Activity;
import android.os.Bundle;

import com.handsomexi.firstxposed.util.AutoCollectUtils;

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoCollectUtils.startAlipay(this);
    }
}