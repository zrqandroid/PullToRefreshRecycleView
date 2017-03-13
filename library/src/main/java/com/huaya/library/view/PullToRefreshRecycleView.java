package com.huaya.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.huaya.library.R;
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

    private View footer;
    private View header;

    private int scaledTouchSlop;

    private LinearLayoutManager layoutManager;

    private void init(Context mContext) {
        this.mContext = mContext;
        LayoutInflater from = LayoutInflater.from(mContext);
        footer = from.inflate(R.layout.item_footer, null, false);
        header = from.inflate(R.layout.item_header, null, false);
        header.setPadding(0, 0, 0, 0);
        footer.setPadding(0, 0, 0, 0);
        scaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        gestureDetectorCompat = new GestureDetectorCompat(mContext, mGestureListener);

    }


    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int firstItemPostion = layoutManager.findFirstVisibleItemPosition();

            if (distanceY > 0) {
                //顶部item向上移动

                moveUp(firstItemPostion);

            } else if (distanceY < 0) {
                //顶部item向下移动

                moveDown(firstItemPostion);

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void moveUp(int firstItemPostion) {
        if (firstItemPostion >= 1 && status == SHOW_HEADER) {
            status = NORMAL;
            Log.i("xiaoqiao", "移除头部");
            removeHeader();
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount + firstItemPostion) >= totalItemCount && status == NORMAL) {
            status = SHOW_FOOTER;
            Log.i("xiaoqiao", "显示尾部");

            addFooter();
        }

    }

    private void moveDown(int firstItemPostion) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (firstItemPostion == 0 && status == NORMAL) {
            status = SHOW_HEADER;
            Log.i("xiaoqiao", "显示头部");
            addHeader();
            return;
        }

        if ((visibleItemCount + firstItemPostion) < totalItemCount && status == SHOW_FOOTER) {
            status = NORMAL;
            removeFooter();

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
        if (dy > 0) {
            //向上滑动
        } else if (dy < 0) {
            //向下滑动
        }


    }


    private void removeFooter() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            ((HeaderAndFooterWrapper) adapter).removeFooter();

        }
    }

    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (layoutManager == null) {
            layoutManager = (LinearLayoutManager) getLayoutManager();
        }

        int firstItemPostion = layoutManager.findFirstCompletelyVisibleItemPosition();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == 0) {
                    startY = e.getRawY();
                }
                float disY = e.getRawY() - startY;
                if (disY > 0) {
                    //指尖向下滑动
                    moveDown(firstItemPostion);
                }
                if (disY < 0) {
                    //指尖向上滑动
                    moveUp(firstItemPostion);

                }

                break;
            case MotionEvent.ACTION_UP:
                startY = 0;
                break;

        }

        return super.onTouchEvent(e);
    }

    private void addFooter() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {

            ((HeaderAndFooterWrapper) adapter).addFootView(footer);

        }
    }

    private void addHeader() {
        Adapter adapter = getAdapter();
        if (adapter instanceof HeaderAndFooterWrapper) {
            ((HeaderAndFooterWrapper) adapter).addHeaderView(header);


        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private void measureView(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
        int height = layoutParams.height;
        int childHeightSpec;
        if (height > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);


    }

}
