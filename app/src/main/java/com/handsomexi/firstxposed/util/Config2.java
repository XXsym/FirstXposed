package com.handsomexi.firstxposed.util;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config2 {
    private static File file = new File(Environment.getExternalStorageDirectory(),"ACEnergy");
    private static File settingFile;
    public static SetBean bean;

    public static void init(){

        if(!file.exists()) file.mkdirs();
        settingFile = new File(file,"settings");
        settingFile2Bean();

    }
    //获取bean
    private static void settingFile2Bean(){
        try {
            FileReader fileReader = new FileReader(settingFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String tmp;
            StringBuilder builder = new StringBuilder();
            while ((tmp = reader.readLine())!=null){
                builder.append(tmp);
            }
            fileReader.close();
            reader.close();
            bean = new SetBean(builder.toString());
        } catch (IOException e) {
            bean = new SetBean();
        }
    }



    public static class SetBean{
        public boolean steal = true;
        public boolean help = true;
        public boolean stealWhite = false;
        public boolean recEnergy = true;
        public List<String> whiteList = new ArrayList<>();

        SetBean(){}
        SetBean(String json){
            try {
                JSONObject object = new JSONObject(json);
                steal = object.optBoolean("steal");
                help = object.optBoolean("help");
                stealWhite = object.optBoolean("stealWhite");
                recEnergy = object.optBoolean("recEnergy");
                JSONArray array = object.getJSONArray("whiteList");
                for (int i = 0; i < array.length(); i++) {
                    whiteList.add(array.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String toJson(){
            JSONObject object = new JSONObject();
            try {
                object.put("steal",steal);
                object.put("help",help);
                object.put("stealWhite",stealWhite);
                object.put("recEnergy",recEnergy);
                object.put("whiteList",whiteList);
                return object.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
