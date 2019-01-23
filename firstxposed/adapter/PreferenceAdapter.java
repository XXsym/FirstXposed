package com.handsomexi.firstxposed.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handsomexi.firstxposed.util.DpUtil;
import com.handsomexi.firstxposed.util.StyleUtil;
import com.handsomexi.firstxposed.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import static com.handsomexi.firstxposed.util.StyleUtil.LINE_COLOR_DEFAULT;
import static com.handsomexi.firstxposed.util.StyleUtil.TEXT_COLOR_SECONDARY;
import static com.handsomexi.firstxposed.util.StyleUtil.TEXT_SIZE_SMALL;

public class PreferenceAdapter extends BaseAdapter {
    private final List<Data> mData;
    public PreferenceAdapter(List<Data> mData) {
        this.mData = new ArrayList<>(mData);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new ViewHolder(viewGroup.getContext()).itemView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.bind(position, (Data) getItem(position));
        return view;
    }
    private class ViewHolder {

        private final LinearLayout itemView;
        private final TextView titleText;
        private final TextView subTitleText;
        private final CheckBox selectBox;
        private final View lineView;

        public ViewHolder(Context context) {
            itemView = new LinearLayout(context);
            itemView.setBackground(ViewUtil.genBackgroundDefaultDrawable());
            itemView.setOrientation(LinearLayout.VERTICAL);

            LinearLayout rootHorizontalLayout = new LinearLayout(context);
            rootHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootHorizontalLayout.setWeightSum(1);
            rootHorizontalLayout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout verticalLayout = new LinearLayout(context);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);

            int defTextVPadding = DpUtil.dip2px(context, 2);
            int defVPadding = DpUtil.dip2px(context, 15);
            int defHPadding = DpUtil.dip2px(context, 5);

            titleText = new TextView(context);
            StyleUtil.apply(titleText);
            titleText.setPadding(0, defTextVPadding, 0, defTextVPadding);

            subTitleText = new TextView(context);
            StyleUtil.apply(subTitleText);
            subTitleText.setTextColor(TEXT_COLOR_SECONDARY);
            subTitleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_SMALL);
            subTitleText.setPadding(0, defTextVPadding, 0, defTextVPadding + defHPadding);


            verticalLayout.setPadding(defHPadding, defVPadding, defHPadding, 0);
            verticalLayout.addView(titleText);
            verticalLayout.addView(subTitleText);

            selectBox = new CheckBox(context);
            selectBox.setClickable(false);
            selectBox.setFocusable(false);
            selectBox.setFocusableInTouchMode(false);
            LinearLayout.LayoutParams selectBoxLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            selectBoxLayoutParams.setMargins(0,0,defHPadding,0);

            rootHorizontalLayout.addView(verticalLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            rootHorizontalLayout.addView(selectBox, selectBoxLayoutParams);

            lineView = new View(context);
            lineView.setBackgroundColor(LINE_COLOR_DEFAULT);

            itemView.addView(rootHorizontalLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.addView(lineView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            itemView.setTag(this);
        }

        public void bind(int position, Data data) {
            if (data == null) {
                return;
            }
            int count = getCount();
            if (position < count - 1) {
                lineView.setVisibility(View.VISIBLE);
            } else {
                lineView.setVisibility(View.GONE);
            }
            if (data.isSelection) {
                selectBox.setVisibility(View.VISIBLE);
            } else {
                selectBox.setVisibility(View.GONE);
            }

            selectBox.setChecked(data.selectionState);
            titleText.setText(data.title);
            subTitleText.setText(data.subTitle);
        }
    }

    public static class Data {

        public String title;
        public String subTitle;
        public boolean isSelection;
        public boolean selectionState;

        public Data(String title, String subTitle) {
            this(title, subTitle, false, false);
        }

        public Data(String title, String subTitle, boolean isSelection, boolean selectionState) {
            this.title = title;
            this.subTitle = subTitle;
            this.isSelection = isSelection;
            this.selectionState = selectionState;
        }
    }
}
