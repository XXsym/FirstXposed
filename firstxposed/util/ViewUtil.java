package com.handsomexi.firstxposed.util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
public class ViewUtil {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    private static Comparator<View> sYLocationLHCompator = new Comparator<View>() {
        @Override
        public int compare(View o1, View o2) {
            int y1 = getViewYPosInScreen(o1);
            int y2 = getViewYPosInScreen(o2);
            return Integer.compare(y1, y2);
        }
    };

    public static Drawable genBackgroundDefaultDrawable(int defaultColor, int pressedColor) {
        StateListDrawable statesDrawable = new StateListDrawable();
        statesDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        statesDrawable.addState(new int[]{}, new ColorDrawable(defaultColor));
        return statesDrawable;
    }
    public static Drawable genBackgroundDefaultDrawable() {
        return genBackgroundDefaultDrawable(Color.TRANSPARENT);
    }

    public static Drawable genBackgroundDefaultDrawable(int defaultColor) {
        StateListDrawable statesDrawable = new StateListDrawable();
        statesDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0xFFE5E5E5));
        statesDrawable.addState(new int[]{}, new ColorDrawable(defaultColor));
        return statesDrawable;
    }
    public static View findViewByName(Activity activity, String packageName, String... names) {
        Resources resources = activity.getResources();
        for (String name : names) {
            int id = resources.getIdentifier(name, "id", packageName);
            if (id == 0) {
                continue;
            }
            View rootView = activity.getWindow().getDecorView();
            List<View> viewList = new ArrayList<>();
            getChildViews((ViewGroup) rootView, id, viewList);
            sortViewListByYPosition(viewList);
            int outViewListSize = viewList.size();
            if (outViewListSize == 1) {
                return viewList.get(0);
            } else if (outViewListSize > 1) {
                for (View view : viewList) {
                    if (view.isShown()) {
                        return view;
                    }
                }
                return viewList.get(0);
            }
        }
        return null;
    }
    public static void getChildViews(ViewGroup parent, String text, List<View> outList) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child instanceof EditText) {
                if (text.equals(String.valueOf(((TextView) child).getText()))) {
                    outList.add(child);
                } else if (text.equals(String.valueOf(((EditText) child).getHint()))) {
                    outList.add(child);
                }
            } else if (child instanceof TextView) {
                if (text.equals(String.valueOf(((TextView) child).getText()))) {
                    outList.add(child);
                }
            }
            if (child instanceof ViewGroup) {
                getChildViews((ViewGroup) child, text, outList);
            } else {
            }
        }
    }

    public static void getChildViews(ViewGroup parent, int id, List<View> outList) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                continue;
            }
            if (id == child.getId()) {
                outList.add(child);
            }
            if (child instanceof ViewGroup) {
                getChildViews((ViewGroup) child, id, outList);
            } else {
            }
        }
    }
    public static void sortViewListByYPosition (List<View> viewList) {
        Collections.sort(viewList, sYLocationLHCompator);
    }
    public static int getViewYPosInScreen(View v) {
        int[] location = new int[]{0, 0};
        v.getLocationOnScreen(location);
        return location[1];
    }
}
