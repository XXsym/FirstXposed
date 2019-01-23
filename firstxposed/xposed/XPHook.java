package com.handsomexi.firstxposed.xposed;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.handsomexi.firstxposed.util.AutoCollectUtils;
import com.handsomexi.firstxposed.util.Config;
import com.handsomexi.firstxposed.util.XposedAlipayPlugin;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XPHook implements IXposedHookLoadPackage {
    private boolean first = false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        switch (lpparam.packageName){
            case "com.eg.android.AlipayGphone":{
                hookSecurity(lpparam);
                XposedAlipayPlugin.main();
                hookRpcCall();
                break;
            }
            case "com.handsomexi.firstxposed":{
                XposedHelpers.findAndHookMethod(
                        "com.handsomexi.firstxposed.MainActivity",
                        lpparam.classLoader,
                        "isModuleActive3",
                        XC_MethodReplacement.returnConstant(true));
                break;
            }
        }
    }

    private void hookSecurity(XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            Class loadClass = lpparam.classLoader.loadClass("com.alipay.mobile.base.security.CI");
            if (loadClass != null) {
                XposedHelpers.findAndHookMethod(loadClass, "a", loadClass, Activity.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) {
                        return null;
                    }
                });
            }
        } catch (Throwable ignored) {
        }
    }

    private void hookRpcCall() {
        try {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new ApplicationAttachMethodHook());
        } catch (Exception ignored) {
        }
    }

    private class ApplicationAttachMethodHook extends XC_MethodHook {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Context context = (Context) param.args[0];
            Config.init(context);
            if (first) return;
            final ClassLoader loader = context.getClassLoader();
            Class clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5FragmentManager");
            if (clazz != null) {
                Class<?> h5FragmentClazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Fragment");
                if (h5FragmentClazz != null) {
                    XposedHelpers.findAndHookMethod(clazz, "pushFragment", h5FragmentClazz,
                            boolean.class, Bundle.class, boolean.class, boolean.class, new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    super.afterHookedMethod(param);
                                    AutoCollectUtils.curH5Fragment = param.args[0];
                                }
                            });
                }
            }

            clazz = loader.loadClass("com.alipay.mobile.nebulacore.ui.H5Activity");
            if (clazz != null) {
                XposedHelpers.findAndHookMethod(clazz, "onResume", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        AutoCollectUtils.h5Activity = (Activity) param.thisObject;
                    }
                });
            }

            clazz = loader.loadClass("com.alipay.mobile.nebulabiz.rpc.H5RpcUtil");
            if (clazz != null) {
                first = true;
                Class<?> h5PageClazz = loader.loadClass("com.alipay.mobile.h5container.api.H5Page");
                Class<?> jsonClazz = loader.loadClass("com.alibaba.fastjson.JSONObject");
                if (h5PageClazz != null && jsonClazz != null) {
                    XposedHelpers.findAndHookMethod(clazz, "rpcCall", String.class, String.class, String.class,
                            boolean.class, jsonClazz, String.class, boolean.class, h5PageClazz,
                            int.class, String.class, boolean.class, int.class, new XC_MethodHook() {

                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    super.afterHookedMethod(param);
                                    Object resp = param.getResult();
                                    if (resp != null&&Config.isOn1()) {
                                        Method method = resp.getClass().getMethod("getResponse");
                                        String response = (String) method.invoke(resp, new Object[]{});
                                        if (AutoCollectUtils.isRankList(response)) {
                                            AutoCollectUtils.autoGetCanCollectUserIdList(loader, response);
                                        }
                                        // 第一次是自己的能量，比上面的获取用户信息还要早，所以这里需要记录当前自己的userid值
                                        if (AutoCollectUtils.isUserDetail(response)) {
                                            AutoCollectUtils.autoGetCanCollectBubbleIdList(loader, response);
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }
}
