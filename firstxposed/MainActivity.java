package com.handsomexi.firstxposed;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.handsomexi.firstxposed.util.AutoCollectUtils;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isModuleActive3()){
            Toast.makeText(this,"模块未激活",Toast.LENGTH_SHORT).show();
        }else {
            AutoCollectUtils.startAlipay(MainActivity.this);
        }
        finish();
    }
    private boolean isModuleActive3(){
        return false;
    }
}
