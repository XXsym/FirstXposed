package com.handsomexi.firstxposed.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handsomexi.firstxposed.util.DpUtil;
import com.handsomexi.firstxposed.util.StyleUtil;
import com.handsomexi.firstxposed.util.ViewUtil;

public class DialogFrameLayout extends FrameLayout implements DialogInterface.OnDismissListener{

    private OnDismissListener mDismissListener;
    private DialogInterface.OnClickListener mOnNeutralButtonClickListener;
    private DialogInterface.OnClickListener mOnNegativeButtonClickListener;
    private DialogInterface.OnClickListener mOnPositiveButtonClickListener;
    private String mNeutralButtonText;
    private String mNegativeButtonText;
    private String mPositiveButtonText;

    public DialogFrameLayout(Context context) {
        super(context);
    }

    public DialogFrameLayout( Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogFrameLayout( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showInDialog() {
        setBackgroundColor(Color.WHITE);
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog))
                .setOnDismissListener(this);

        AlertDialog dialog;
        dialog = builder.create();
        dialog.setView(createDialogContentView(dialog));

        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();
    }

    private TextView createTitleTextView() {
        String title = getDialogTitle();
        Context context = getContext();
        TextView titleTextView = new TextView(context);
        titleTextView.setText(title);
        titleTextView.setTextColor(Color.BLACK);
        titleTextView.setBackgroundColor(Color.WHITE);
        int defHPadding = DpUtil.dip2px(context, 20);
        titleTextView.setPadding(defHPadding,  DpUtil.dip2px(context, 15), defHPadding,  DpUtil.dip2px(context, 8));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, StyleUtil.TEXT_SIZE_BIG);

        if (TextUtils.isEmpty(title)) {
            titleTextView.setVisibility(GONE);
        }
        return titleTextView;
    }

    private View createNeutralButton(AlertDialog dialog) {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setTextColor(0xFF009688);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        textView.setBackground(ViewUtil.genBackgroundDefaultDrawable());
        textView.setMinWidth(DpUtil.dip2px(context, 45));
        textView.setGravity(Gravity.CENTER);

        String text = mNeutralButtonText;
        int defHPadding = DpUtil.dip2px(context, 6);
        int defVPadding = DpUtil.dip2px(context, 10);
        textView.setPadding(defHPadding, defVPadding, defHPadding, defVPadding);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(text);
            textView.setOnClickListener(v -> {
                DialogInterface.OnClickListener listener = mOnNeutralButtonClickListener;
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
                } else {
                    dialog.dismiss();
                }
            });
        }
        LinearLayout btnCon = new LinearLayout(context);
        btnCon.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnCon.setGravity(Gravity.LEFT);
        return btnCon;
    }

    private View createNegativeButton(AlertDialog dialog) {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setTextColor(0xFF009688);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        textView.setBackground(ViewUtil.genBackgroundDefaultDrawable());
        textView.setMinWidth(DpUtil.dip2px(context, 45));
        textView.setGravity(Gravity.CENTER);

        String text = mNegativeButtonText;
        int defHPadding = DpUtil.dip2px(context, 6);
        int defVPadding = DpUtil.dip2px(context, 10);
        textView.setPadding(defHPadding, defVPadding, defHPadding, defVPadding);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(text);
            textView.setOnClickListener(v -> {
                DialogInterface.OnClickListener listener = mOnNegativeButtonClickListener;
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                } else {
                    dialog.dismiss();
                }
            });
        }
        LinearLayout btnCon = new LinearLayout(context);
        btnCon.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnCon.setGravity(Gravity.RIGHT);
        return btnCon;
    }

    private View createPositiveButton(AlertDialog dialog) {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setTextColor(0xFF009688);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        textView.setBackground(ViewUtil.genBackgroundDefaultDrawable());
        textView.setMinWidth(DpUtil.dip2px(context, 45));
        textView.setGravity(Gravity.CENTER);

        String text = mPositiveButtonText;
        int defHPadding = DpUtil.dip2px(context, 6);
        int defVPadding = DpUtil.dip2px(context, 10);
        textView.setPadding(defHPadding, defVPadding, defHPadding, defVPadding);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        } else {
            textView.setText(text);
            textView.setOnClickListener(v -> {
                DialogInterface.OnClickListener listener = mOnPositiveButtonClickListener;
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                } else {
                    dialog.dismiss();
                }
            });
        }
        LinearLayout btnCon = new LinearLayout(context);
        btnCon.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnCon.setGravity(Gravity.RIGHT);
        return btnCon;
    }

    private View createBtnAreaView(AlertDialog dialog) {
        Context context = getContext();
        LinearLayout btnLLayout = new LinearLayout(context);
        btnLLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLLayout.setWeightSum(1);

        View neutralButton = createNeutralButton(dialog);
        View negativeButton = createNegativeButton(dialog);
        View positiveButton = createPositiveButton(dialog);

        int defPadding = DpUtil.dip2px(context, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.leftMargin = defPadding;
        params.rightMargin = defPadding;
        btnLLayout.addView(neutralButton, params);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = defPadding;
        btnLLayout.addView(negativeButton, params);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = defPadding;
        btnLLayout.addView(positiveButton, params);

        if (hasButton()) {
            btnLLayout.setPadding(0, DpUtil.dip2px(context, 10), 0, DpUtil.dip2px(context, 15));
        }
        return btnLLayout;
    }

    private View createDialogContentView(AlertDialog dialog) {
        Context context = getContext();
        View titleView = createTitleTextView();
        View contentView = this;
        View btnView = createBtnAreaView(dialog);

        LinearLayout rootLLayout = new LinearLayout(context);
        rootLLayout.setOrientation(LinearLayout.VERTICAL);
        rootLLayout.setWeightSum(1);
        rootLLayout.setBackgroundColor(Color.WHITE);
        rootLLayout.addView(titleView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLLayout.addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        rootLLayout.addView(btnView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return rootLLayout;
    }

    private boolean hasButton() {
        return (!TextUtils.isEmpty(mNegativeButtonText))
                || (!TextUtils.isEmpty(mNeutralButtonText))
                || (!TextUtils.isEmpty(mPositiveButtonText));
    }

    public DialogFrameLayout withOnDismissListener(OnDismissListener listener) {
        mDismissListener = listener;
        return this;
    }

    public DialogFrameLayout withOnNeutralButtonClickListener(DialogInterface.OnClickListener listener) {
        mOnNeutralButtonClickListener = listener;
        return this;
    }

    public DialogFrameLayout withOnNegativeButtonClickListener(DialogInterface.OnClickListener listener) {
        mOnNegativeButtonClickListener = listener;
        return this;
    }

    public DialogFrameLayout withOnPositiveButtonClickListener(DialogInterface.OnClickListener listener) {
        mOnPositiveButtonClickListener = listener;
        return this;
    }

    public DialogFrameLayout withNeutralButtonText(String text) {
        mNeutralButtonText = text;
        return this;
    }

    public DialogFrameLayout withNegativeButtonText(String text) {
        mNegativeButtonText = text;
        return this;
    }

    public DialogFrameLayout withPositiveButtonText(String text) {
        mPositiveButtonText = text;
        return this;
    }

    public String getDialogTitle() {
        return null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        OnDismissListener listener = mDismissListener;
        if (listener != null) {
            listener.onDismiss(this);
        }
    }

    interface OnDismissListener {
        void onDismiss(View v);
    }
}
