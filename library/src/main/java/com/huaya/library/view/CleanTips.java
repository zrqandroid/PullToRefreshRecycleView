package com.huaya.library.view;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.huaya.library.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhuruqiao on 16/10/24.
 */

public class CleanTips {

    public static final int LENGTH_ALWAYS = 0;

    public static final int LENGTH_SHORT = 2;

    public static final int LENGTH_LONG = 4;


    public int animations = -1;

    public static final int TOAST_SHOW_DURATION = 5;

    public Context mContext;

    public LayoutInflater inflater;

    public int toastWidth;

    public int toastHeight;

    public Toast toast;

    public View dragView;

    public boolean isShow;

    public Handler handler = new Handler();

    public int mDuration = LENGTH_LONG;

    public DisplayMetrics metrics;


    private Object mTN;
    private Method show;
    private Method hide;

    public WindowManager mWM;
    public WindowManager.LayoutParams params;

    public CleanTips(Context context) {
        mContext = context;
        metrics = Utils.getMetrics(mContext);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toast = new Toast(context);
        dragView = initView(inflater);
        getToastSize();
    }

    private void getToastSize() {
        int[] size = Utils.getSize(dragView);
        toastWidth = size[0];
        toastHeight = size[1];
    }


    public void show() {
        if (isShow) return;
        toast.setView(dragView);
        initTN();
        try {
            show.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isShow = true;
        if (mDuration > LENGTH_ALWAYS) {
            handler.postDelayed(hideRunnable, mDuration * 1000);
        }

    }


    public void hide() {
        if (!isShow) return;
        try {
            hide.invoke(mTN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isShow = false;
    }


    private void initTN() {
        try {
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");
            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.windowAnimations = R.style.CleanToastAnimation;
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());
            mWM = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGravity(Gravity.LEFT | Gravity.TOP, metrics.widthPixels - Utils.getSize(dragView)[0], params.y + metrics.heightPixels * 3 / 4);

    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private View initView(LayoutInflater inflater) {

        return inflater.inflate(R.layout.item_footer, null, false);

    }


    public void setCleanCount(int cleanCount) {
    }
}
