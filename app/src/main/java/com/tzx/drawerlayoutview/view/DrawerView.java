package com.tzx.drawerlayoutview.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import static android.R.attr.x;

/**
 * Created by tanzhenxing
 * Date: 2017/1/9.
 * Description:
 */

public class DrawerView extends FrameLayout {
    private final String TAG = "DrawerView";
    private Context mContext;
    private View menuView;
    private View mainView;
    private int mTouchSlop;
    private ViewDragHelper mHelper;
    private ViewDragHelper.Callback mViewDragCallback;

    private int menuViewWidth;
    public DrawerView(Context context) {
        super(context);
        init(context);
    }



    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mViewDragCallback = new ViewDragHelper.Callback() {
            int left = 0;

            /*如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获*/
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mainView;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                int x = (xvel > 300 || xvel > 0 && (menuViewWidth - this.left) < 200)
                        || !(xvel < -300 || xvel <= 0 && this.left < 200) ? menuViewWidth : 0;
                mHelper.smoothSlideViewTo(releasedChild, x, 0);
                DrawerView.this.postInvalidateOnAnimation();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left <= menuViewWidth && left >= 0) {
                    this.left = left;
                } else if (left >= menuViewWidth) {
                    this.left = menuViewWidth;
                } else {
                    this.left = 0;
                }
                return this.left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }

            /*当captureview的位置发生改变时回调*/
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                float scale = 1 - (Math.min(Math.abs((float) left / menuViewWidth), 1.0f)) / 3;
                changedView.setScaleX(scale);
                changedView.setScaleY(scale);
                menuView.offsetLeftAndRight(dx);
            }

            /*在边界拖动时回调*/
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                return super.onEdgeLock(edgeFlags);
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public int getOrderedChildIndex(int index) {
                return super.getOrderedChildIndex(index);
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }
        };
        mHelper = ViewDragHelper.create(this, mTouchSlop, mViewDragCallback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*滑动过程中菜单栏展示*/
        menuViewWidth = menuView.getMeasuredWidth() - 1;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            this.postInvalidateOnAnimation();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.getChildCount() != 2) {
            throw  new IllegalStateException("Drawer must have two direct child~!~!");
        }
        int viewHeight = menuView.getMeasuredHeight();
        Log.d(TAG, "onLayout: left=" + (left - menuViewWidth) + "    right=" + (left + 1));
        menuView.layout(left - menuViewWidth, top, left + 1, top + viewHeight);
    }
}
