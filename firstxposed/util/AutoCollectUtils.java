package com.handsomexi.firstxposed.util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;


import com.handsomexi.firstxposed.util.Config;
import com.handsomexi.firstxposed.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AutoCollectUtils {

    private static List<String> canStealList = new ArrayList<>();
    private static List<String> canHelpList = new ArrayList<>();
    private static Integer stealEnergy = 0,helpEnergy = 0;
    private static Integer pageCount = 0;
    private static Object curH5PageImpl;
    public static Object curH5Fragment;
    private static Method rpcCallMethod = null;
    public static Activity h5Activity;
    private static Toast toast;

    //自动获取有能量的好友信息
    public static void autoGetCanCollectUserIdList(final ClassLoader loader, String response) {
        // 开始解析好友信息，循环把所有有能量的好友信息都解析完，判断是否读取完所有好友信息了
        boolean isSucc = parseFrienRankPageDataResponse(response);
        if (isSucc) {
            toast("开始获取可以收取能量的好友信息...");
            new Thread(() -> {
                // 发送获取下一页好友信息接口
                rpcCall_FriendRankList(loader);
            }).start();
        } else {
            pageCount = 0;
            //如果发现已经解析完成了，如果有好友能量收取，就开始收取

            if (canStealList.size() > 0 || canHelpList.size()>0) {
                toast("开始获取能量信息...");
                // 开始偷取每个用户的能量
                for (String userId : canStealList) {
                    rpcCall_CanCollectEnergy(loader, userId);
                }
                // 开始帮助收取每个用户快要消失的能量
                for (String userId : canHelpList) {
                    rpcCall_CanCollectEnergy(loader, userId);
                }

                if(stealEnergy == 0 && helpEnergy == 0){
                    toast("没有能量可以收取.");
                }else if(stealEnergy !=0 && helpEnergy == 0){
                    toast("一共偷取了" + stealEnergy + "g能量");
                }else if (stealEnergy == 0){
                    toast("未偷取到好友能量，帮助好友收取"+helpEnergy+"g能量");
                }else {
                    toast("一共偷取了" + stealEnergy + "g能量\n帮助好友收取"+helpEnergy+"能量");
                }
            }else {
                toast("没有能量可以收取.");
            }
            clear();
        }
    }

    //自动获取能收取的能量ID
    public static void autoGetCanCollectBubbleIdList(final ClassLoader loader, String response) {
        if (!TextUtils.isEmpty(response) && response.contains("collectStatus")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("bubbles");
                String userName = jsonObject.getJSONObject("userEnergy").getString("displayName");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String collectStatus = object.optString("collectStatus");
                        boolean canHelpCollect = object.optBoolean("canHelpCollect");
                        String userId = object.optString("userId");
                        int id = object.optInt("id");
                        if ("AVAILABLE".equals(collectStatus)) {
                            rpcCall_StealEnergy(loader, userId, id,userName);
                        }
                        if(Config.isOn2()&&canHelpCollect){
                            rpcCall_HelpEnergy(loader, userId, id,userName);
                        }
                    }
                }

            } catch (Exception ignored) {
            }
        }
    }

    public static boolean isRankList(String response) {
        return !TextUtils.isEmpty(response) && response.contains("friendRanking");
    }

    public static boolean isUserDetail(String response) {
        return !TextUtils.isEmpty(response) && response.contains("userEnergy");
    }


    //解析好友信息
    private static boolean parseFrienRankPageDataResponse(String response) {
        try {
            JSONArray optJSONArray = new JSONObject(response).optJSONArray("friendRanking");
            if (optJSONArray == null || optJSONArray.length() == 0) {
                return false;
            } else {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject jsonObject = optJSONArray.getJSONObject(i);
                    boolean b1 = jsonObject.optBoolean("canCollectEnergy");
                    boolean b2 = jsonObject.optBoolean("canHelpCollect");
                    String userId = jsonObject.optString("userId");
                    if (b1 && !canStealList.contains(userId)) {
                        canStealList.add(userId);
                    }
                    if(Config.isOn2()&&b2 && !canHelpList.contains(userId)){
                        canHelpList.add(userId);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    //获取分页好友信息命令
    private static void rpcCall_FriendRankList(final ClassLoader loader) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("pageSize", pageCount * 20);
            json.put("startPoint", "" + (pageCount * 20 + 1));
            pageCount++;
            jsonArray.put(json);
            if (rpcCallMethod != null) {
                rpcCallMethod.invoke(null, "alipay.antmember.forest.h5.queryEnergyRanking", jsonArray.toString(),
                        "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
            }
        } catch (Exception ignored) {
        }
    }

    //获取指定用户可以收取的能量信息
    private static void rpcCall_CanCollectEnergy(final ClassLoader loader, String userId) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("pageSize", 3);
            json.put("startIndex", 0);
            json.put("userId", userId);
            jsonArray.put(json);

            if (rpcCallMethod != null) {
                rpcCallMethod.invoke(null, "alipay.antmember.forest.h5.queryNextAction", jsonArray.toString(),
                        "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
                rpcCallMethod.invoke(null, "alipay.antmember.forest.h5.pageQueryDynamics", jsonArray.toString(),
                        "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
            }

        } catch (Exception ignored) {
        }
    }

    //偷取能量命令
    private static void rpcCall_StealEnergy(final ClassLoader loader, String userId, Integer bubbleId,String userName) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("userId", userId);
            json.put("bubbleIds", bubbleId);
            jsonArray.put(json);

            if (rpcCallMethod != null) {
                Object resp = rpcCallMethod.invoke(null, "alipay.antmember.forest.h5.collectEnergy", jsonArray.toString(),
                        "", true, null, null, false, curH5PageImpl, 0, "", false, -1);
                if (resp != null) {
                    Method method = resp.getClass().getMethod("getResponse");
                    String response = (String) method.invoke(resp, new Object[]{});
                    parseCEResponse(response,0,userName);
                }
            }
        } catch (Exception ignored) {
        }
    }

    //帮助好友收取能量命令
    private static void rpcCall_HelpEnergy(final ClassLoader loader, String userId, Integer bubbleId, String userName) {
        try {
            Method rpcCallMethod = getRpcCallMethod(loader);
            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("av", "5");
            json.put("ct", "android");
            json.put("targetUserId", userId);
            json.put("bubbleIds", bubbleId);
            jsonArray.put(json);

            if (rpcCallMethod != null) {
                String arrayString = new JSONArray().put(json).toString();
                Object resp = rpcCallMethod.invoke(null, "alipay.antmember.forest.h5.forFriendCollectEnergy",arrayString,
                        "", true, null, null,false, curH5PageImpl, 0, "", false, -1);
                if(resp!=null){
                    Method method = resp.getClass().getMethod("getResponse");
                    String response = (String) method.invoke(resp, new Object[]{});
                    parseCEResponse(response,1,userName);
                }

            }
        } catch (Exception ignored) {
        }
    }


    private static Method getRpcCallMethod(ClassLoader loader) {
        if(rpcCallMethod!=null)
            return rpcCallMethod;
        try {
            Field aF = curH5Fragment.getClass().getDeclaredField("a");
            aF.setAccessible(true);
            Object viewHolder = aF.get(curH5Fragment);
            Field hF = viewHolder.getClass().getDeclaredField("h");
            hF.setAccessible(true);
            curH5PageImpl = hF.get(viewHolder);
            Class<?> h5PageClazz = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
            Class<?> jsonClazz = loader.loadClass("com.alibaba.fastjson.JSONObject");
            Class<?> rpcClazz = loader.loadClass("com.alipay.mobile.nebulabiz.rpc.H5RpcUtil");
            if (curH5PageImpl != null) {
                rpcCallMethod = rpcClazz.getMethod("rpcCall", String.class, String.class, String.class,
                        boolean.class, jsonClazz, String.class, boolean.class, h5PageClazz,
                        int.class, String.class, boolean.class, int.class);
                return rpcCallMethod;

            }
        } catch (Exception ignored) {
        }
        return null;
    }

    //解析收取好友收取能量的返回数据
    private static void parseCEResponse(String response,int type,String userName) {
        if (!TextUtils.isEmpty(response) && response.contains("failedBubbleIds")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("bubbles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    int c = jsonArray.getJSONObject(i).optInt("collectedEnergy");
                    if(c!=0&&Config.isOn3()){
                        JSONObject object = new JSONObject();
                        object.put("time",new Date().getTime());
                        object.put("user",userName);
                        object.put("energy",c);
                        object.put("type",type);
                        LogUtil.log(object.toString());
                    }

                    if (type == 0){
                        stealEnergy+=c;
                    }else{
                        helpEnergy+=c;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void clear(){
        canStealList.clear();
        canHelpList.clear();
        stealEnergy = 0;
        helpEnergy = 0;
    }

    private static void toast(String str){
        if(h5Activity == null) return;
        h5Activity.runOnUiThread(() -> {
            if(toast == null)
                toast = Toast.makeText(h5Activity,str,Toast.LENGTH_LONG);
            else
                toast.setText(str);
            toast.show();
        });
    }

    public static void startAlipay(final Context mContext) {
        Toast.makeText(mContext, "正在打开蚂蚁森林",Toast.LENGTH_LONG).show();
        try {
            Intent intent = Intent.parseUri("alipays://platformapi/startapp?appId=20000067&url=https://60000002.h5app.alipay.com/app/src/home.html",
                    Intent.URI_INTENT_SCHEME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (URISyntaxException e) {
            Toast.makeText(mContext,"打开失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
