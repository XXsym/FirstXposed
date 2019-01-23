package com.handsomexi.firstxposed.util;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handsomexi.firstxposed.BuildConfig;
import com.handsomexi.firstxposed.util.Task;
import com.handsomexi.firstxposed.util.DpUtil;
import com.handsomexi.firstxposed.util.StyleUtil;
import com.handsomexi.firstxposed.util.ViewUtil;
import com.handsomexi.firstxposed.view.SettingsView;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class XposedAlipayPlugin {

    public static void main() {
        XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                String actName = activity.getClass().getName();
                XposedBridge.log(actName);
                if(actName.contains(".MySettingActivity")){
                    Task.onMain(100, () -> doSettingsMenuInject_10_1_38(activity));
                }else if(actName.contains(".UserSettingActivity")){
                    Task.onMain(100, () -> doSettingsMenuInject(activity));
                }
            }
        });
    }
    private static void doSettingsMenuInject_10_1_38(final Activity activity){
        int listViewId = activity.getResources().getIdentifier("setting_list", "id", "com.alipay.android.phone.openplatform");

        ListView listView = activity.findViewById(listViewId);

        View lineTopView = new View(activity);
        lineTopView.setBackgroundColor(0xFFEEEEEE);

        LinearLayout itemHlinearLayout = new LinearLayout(activity);
        itemHlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemHlinearLayout.setWeightSum(1);
        itemHlinearLayout.setBackground(ViewUtil.genBackgroundDefaultDrawable(Color.WHITE, 0xFFD9D9D9));
        itemHlinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        itemHlinearLayout.setClickable(true);
        itemHlinearLayout.setOnClickListener(view -> new SettingsView(activity).showInDialog());

        int defHPadding = DpUtil.dip2px(activity, 15);

        TextView itemNameText = new TextView(activity);
        StyleUtil.apply(itemNameText);
        itemNameText.setText("蚂蚁森林设置");
        itemNameText.setGravity(Gravity.CENTER_VERTICAL);
        itemNameText.setPadding(defHPadding, 0, 0, 0);
        itemNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, StyleUtil.TEXT_SIZE_BIG);

        TextView itemSummerText = new TextView(activity);
        StyleUtil.apply(itemSummerText);
        itemSummerText.setText(BuildConfig.VERSION_NAME);
        itemSummerText.setGravity(Gravity.CENTER_VERTICAL);
        itemSummerText.setPadding(0, 0, defHPadding, 0);
        itemSummerText.setTextColor(0xFF999999);

        try {
            View settingsView = ViewUtil.findViewByName(activity, "com.alipay.mobile.antui", "item_left_text");
            if (settingsView instanceof TextView) {
                TextView settingsTextView = (TextView) settingsView;
                float scale = itemNameText.getTextSize() / settingsTextView.getTextSize();
                itemNameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, settingsTextView.getTextSize());
                itemSummerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemSummerText.getTextSize() / scale);
                itemNameText.setTextColor(settingsTextView.getCurrentTextColor());
            }
        } catch (Exception e) {
            XposedBridge.log("doSettingsMenuInject_10_1_38:"+e.getMessage());
        }
        itemHlinearLayout.addView(itemNameText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        itemHlinearLayout.addView(itemSummerText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View lineBottomView = new View(activity);
        lineBottomView.setBackgroundColor(0xFFEEEEEE);

        LinearLayout rootLinearLayout = new LinearLayout(activity);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rootLinearLayout.addView(lineTopView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        rootLinearLayout.addView(itemHlinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(activity, 45)));
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineParams.bottomMargin = DpUtil.dip2px(activity, 20);
        rootLinearLayout.addView(lineBottomView, lineParams);

        listView.addHeaderView(rootLinearLayout);
    }
    private static void doSettingsMenuInject(final Activity activity) {
        int logout_id = activity.getResources().getIdentifier("logout", "id", "com.alipay.android.phone.openplatform");

        View logoutView = activity.findViewById(logout_id);
        LinearLayout linearLayout = (LinearLayout) logoutView.getParent();
        linearLayout.setPadding(0, 0, 0, 0);
        List<ViewGroup.LayoutParams> childViewParamsList = new ArrayList<>();
        List<View> childViewList = new ArrayList<>();
        int childViewCount = linearLayout.getChildCount();
        for (int i = 0; i < childViewCount; i++) {
            View view = linearLayout.getChildAt(i);
            childViewList.add(view);
            childViewParamsList.add(view.getLayoutParams());
        }

        linearLayout.removeAllViews();

        View lineTopView = new View(activity);
        lineTopView.setBackgroundColor(0xFFDFDFDF);

        LinearLayout itemHlinearLayout = new LinearLayout(activity);
        itemHlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemHlinearLayout.setWeightSum(1);
        itemHlinearLayout.setBackground(ViewUtil.genBackgroundDefaultDrawable(Color.WHITE));
        itemHlinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        itemHlinearLayout.setClickable(true);
        itemHlinearLayout.setOnClickListener(view -> new SettingsView(activity).showInDialog());

        int defHPadding = DpUtil.dip2px(activity, 15);

        TextView itemNameText = new TextView(activity);
        StyleUtil.apply(itemNameText);
        itemNameText.setText("蚂蚁森林设置");
        itemNameText.setGravity(Gravity.CENTER_VERTICAL);
        itemNameText.setPadding(defHPadding, 0, 0, 0);
        itemNameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, StyleUtil.TEXT_SIZE_BIG);

        TextView itemSummerText = new TextView(activity);
        StyleUtil.apply(itemSummerText);
        itemSummerText.setText(BuildConfig.VERSION_NAME);
        itemSummerText.setGravity(Gravity.CENTER_VERTICAL);
        itemSummerText.setPadding(0, 0, defHPadding, 0);
        itemSummerText.setTextColor(0xFF888888);

        //try use Alipay style
        try {
            View settingsView = ViewUtil.findViewByName(activity, "com.alipay.mobile.ui", "title_bar_title");
            if (settingsView instanceof TextView) {
                TextView settingsTextView = (TextView) settingsView;
                float scale = itemNameText.getTextSize() / settingsTextView.getTextSize();
                itemNameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, settingsTextView.getTextSize());
                itemSummerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemSummerText.getTextSize() / scale);
                itemNameText.setTextColor(settingsTextView.getCurrentTextColor());
            }
        } catch (Exception ignored) {
        }

        itemHlinearLayout.addView(itemNameText, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        itemHlinearLayout.addView(itemSummerText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View lineBottomView = new View(activity);
        lineBottomView.setBackgroundColor(0xFFDFDFDF);

        linearLayout.addView(lineTopView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        linearLayout.addView(itemHlinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(activity, 50)));
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineParams.bottomMargin = DpUtil.dip2px(activity, 20);
        linearLayout.addView(lineBottomView, lineParams);

        for (int i = 0; i < childViewCount; i++) {
            View view = childViewList.get(i);
            ViewGroup.LayoutParams params = childViewParamsList.get(i);
            linearLayout.addView(view, params);
        }
    }
}
