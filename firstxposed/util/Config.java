package com.handsomexi.firstxposed.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.handsomexi.firstxposed.BuildConfig;

public class Config {
    private static SharedPreferences sharedPreferences;
    public static void init(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".setting", Context.MODE_PRIVATE);
        }
    }
    public static boolean isOn1(){
        return sharedPreferences.getBoolean("switch_on1", true);
    }
    public static void setOn1(boolean on) {
        sharedPreferences.edit().putBoolean("switch_on1", on).apply();
    }

    public static boolean isOn2(){
        return sharedPreferences.getBoolean("switch_on2", false);
    }
    public static void setOn2(boolean on) {
        sharedPreferences.edit().putBoolean("switch_on2", on).apply();
    }

    public static boolean isOn3(){
        return sharedPreferences.getBoolean("switch_on3", false);
    }
    public static void setOn3(boolean on) {
        sharedPreferences.edit().putBoolean("switch_on3", on).apply();
    }



}
