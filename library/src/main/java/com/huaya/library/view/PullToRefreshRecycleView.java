package com.huaya.library.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.huaya.library.adapter.HeaderAndFooterWrapper;

/**
 * Created by zhuruqiao on 2017/1/10.
 * e-mail:563325724@qq.com
 */

public class PullToRefreshRecycleView extends RecyclerView {

    private static final int SHOW_HEADER = 1;

    private static final int SHOW_FOOTER = 2;

    private static final int NORMAL = 0;


    private int status = NORMAL;


    private Context mContext;

    public PullToRefreshRecycleView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private GestureDetectorCompat gestureDetectorCompat;

    private void init(Context mContext) {
        this.mContext = mContext;
        gestureDetectorCompat = new GestureDetectorCompat(mContext, mGestureListener);

    }


    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int firstItemPostion = layoutManager.findFirstVisibleItemPosition();

            if (distanceY > 0) {
                //顶部item向上移动

                moveUp(layoutManager, firstItemPostion);

            } else if (distanceY < 0) {
                //顶部item向下移动

                moveDown(firstItemPostion);

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    };

    private void moveUp(LinearLayoutManager layoutManager, int firstItemPostion) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount + firstItemPostion) >= totalItemCount && status == NORMAL) {
            status = SHOW_FOOTER;
            addFooter();
        } else if ((visibleItemCount + firstItemPostion) < totalItemCount && status == SHOW_FOOTER) {
            status = NORMAL;
            removeFooter();

        }

    }

    private void moveDown(int firstItemPostion) {
        if (firstItemPostion == 0 && status == NORMAL) {
            status = SHOW_HEADER;
            addHeader();
        } else if (firstItemPostion >= 1 && status == SHOW_HEADER) {
            status = NORMAL;
            removeHeader();
        }
    }

    private void removeHeader() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            ((HeaderAndFooterWrapper) adapter).removeHeader();

        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

    }

    private void removeFooter() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            ((HeaderAndFooterWrapper) adapter).removeFooter();

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    private void addFooter() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            TextView textView = new TextView(mContext);
            textView.setHeight(100);
            textView.setText("我是脚布局");
            textView.setBackgroundColor(Color.GRAY);
            ((HeaderAndFooterWrapper) adapter).addFootView(textView);

        }
    }

    private void addHeader() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            TextView textView = new TextView(mContext);
            textView.setHeight(100);
            textView.setText("我是头布局");
            textView.setBackgroundColor(Color.GRAY);
            ((HeaderAndFooterWrapper) adapter).addHeaderView(textView);

        }
    }

}
