package com.handsomexi.firstxposed.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil {
    private static String path = Environment.getExternalStorageDirectory().getPath()+"/ACEnergy";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
    private static File file = new File(path+"/"+format.format(new Date())+".txt");

        public static synchronized void log(String content){
            File parf = new File(path);
            if(!parf.exists()){
                parf.mkdirs();
            }
            try {
                FileWriter fileWriter = new FileWriter(file,true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(content);
                printWriter.flush();
                fileWriter.close();
                printWriter.close();
            } catch (IOException e) {
                Log.e("LogUtilERROR",e.getMessage());
            }
        }
}
