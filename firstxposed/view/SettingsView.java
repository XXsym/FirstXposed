package com.handsomexi.firstxposed.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handsomexi.firstxposed.BuildConfig;
import com.handsomexi.firstxposed.adapter.PreferenceAdapter;
import com.handsomexi.firstxposed.util.Config;
import com.handsomexi.firstxposed.util.DpUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.handsomexi.firstxposed.util.StyleUtil.TEXT_SIZE_BIG;

public class SettingsView extends DialogFrameLayout implements AdapterView.OnItemClickListener {
    private List<PreferenceAdapter.Data> mSettingsDataList = new ArrayList<>();
    private PreferenceAdapter mListAdapter;
    private ListView mListView;

    public SettingsView(Context context) {
        super(context);
        init(context);
    }

    public SettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Config.init(context);
        LinearLayout rootVerticalLayout = new LinearLayout(context);
        rootVerticalLayout.setOrientation(LinearLayout.VERTICAL);

        View lineView = new View(context);
        lineView.setBackgroundColor(Color.TRANSPARENT);

        TextView settingsTitle = new TextView(context);
        settingsTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_BIG);
        settingsTitle.setText("蚂蚁森林设置"+BuildConfig.VERSION_NAME);
        settingsTitle.setTextColor(Color.WHITE);
        settingsTitle.setTypeface(null, Typeface.BOLD);
        settingsTitle.setBackgroundColor(0xFF3CB371);
        int defHPadding = DpUtil.dip2px(context, 15);
        int defVPadding = DpUtil.dip2px(context, 12);
        settingsTitle.setPadding(defHPadding, defVPadding, defHPadding, defVPadding);

        mListView = new ListView(context);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);
        mListView.setPadding(defHPadding, defVPadding, defHPadding, defVPadding);
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));

        mSettingsDataList.add(new PreferenceAdapter.Data("自动收取能量","自动收取自己和好友的能量",true,Config.isOn1()));
        mSettingsDataList.add(new PreferenceAdapter.Data("帮助好友收取能量","帮助好友收取快要消失的能量",true,Config.isOn2()));
        mSettingsDataList.add(new PreferenceAdapter.Data("收取能量记录","记录能量详情,请在ACEnergy记录应用中查看",true,Config.isOn3()));
        mSettingsDataList.add(new PreferenceAdapter.Data("捐赠","觉得好用就请我喝一瓶未来星吧(〃∀〃)"));

        mListAdapter = new PreferenceAdapter(mSettingsDataList);

        rootVerticalLayout.addView(settingsTitle);
        rootVerticalLayout.addView(lineView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px(context, 2)));
        rootVerticalLayout.addView(mListView);

        this.addView(rootVerticalLayout);
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PreferenceAdapter.Data data = mListAdapter.getItem(position);
        switch (data.title){
            case "自动收取能量":{
                data.selectionState = !data.selectionState;
                Config.setOn1(data.selectionState);
                mListAdapter.notifyDataSetChanged();
                break;
            }
            case "帮助好友收取能量":{
                data.selectionState = !data.selectionState;
                Config.setOn2(data.selectionState);
                mListAdapter.notifyDataSetChanged();
                break;
            }
            case "收取能量记录":{
                data.selectionState = !data.selectionState;
                Config.setOn3(data.selectionState);
                mListAdapter.notifyDataSetChanged();
                break;
            }
            case "捐赠":{
                try {
                    getContext().startActivity(Intent.parseUri("intent://platformapi/startapp?saId=10000007&" +
                            "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2FFKX06388UKNC5RZBY6EEA8%3F_s" +
                            "%3Dweb-other&_t=1472443966571#Intent;" +
                            "scheme=alipayqr;package=com.eg.android.AlipayGphone;end", Intent.URI_INTENT_SCHEME));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
