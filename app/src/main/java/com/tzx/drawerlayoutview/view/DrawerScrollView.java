package com.tzx.drawerlayoutview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.tzx.drawerlayoutview.utils.DensityUtil;

/**
 * Created by tanzhenxing
 * Date: 2017/1/10.
 * Description:
 */

public class DrawerScrollView extends FrameLayout{

    private final String TAG = "DrawerScrollView";
    private Context mContext;
    private View menuView;
    private View mainView;
    private View maskView;
    private int mTouchSlop;
    /*初始按下位置的X坐标*/
    private float mTouchDownX = 0;
    /*一次滑动抬起手指位置的X坐标*/
    private float mTouchUpX = 0;
    private int menuViewWidth = 0;
    private int lastTouchDownX = 0;
    /*菜单的初始偏移量*/
    private int menuInitDrawLeft = 0;

    private Scroller mScroller = null;
    /*检测速度变化*/
    private VelocityTracker mVelocityTracker;

    private int mMaxVelocity;
    /*一次滑动过程中的最大速度*/
    private float mCurrentMaxVelocityX = 0;
    /*触发View滚动的最大滑动速度*/
    private final float mMaxVelocityX = 4500;
    /*View滚动的速度系数*/
    private final float viewScrollVelocityX = 1.35f;

    public DrawerScrollView(Context context) {
        super(context);
        init(context);
    }

    public DrawerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setWillNotDraw(false);
        mScroller = new Scroller(mContext);
        mMaxVelocity = ViewConfiguration.get(mContext).getScaledMaximumFlingVelocity();
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        Log.d(TAG, "init: mMaxVelocity=" + mMaxVelocity);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        menuViewWidth = menuView.getMeasuredWidth();
        menuInitDrawLeft = (int) -(menuViewWidth * 1.0 / 3);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) menuView.getLayoutParams();
        layoutParams.leftMargin = menuInitDrawLeft;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        maskView = getChildAt(2);
        maskView.setAlpha(0);
        maskView.setVisibility(GONE);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScroller != null) {
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, (int) (Math.abs(getScrollX()) * viewScrollVelocityX));
                    postInvalidateOnAnimation();
                }
            }
        });
    }

    private void acquireVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        boolean interceptParent = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                lastTouchDownX = (int) mTouchDownX;
                if (mScroller != null) {
                    mScroller.forceFinished(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - lastTouchDownX);
                if (Math.abs(deltaX) > mTouchSlop) {
                    interceptParent = true;
                }
                break;
        }
        return interceptParent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                //求伪瞬时速度
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = verTracker.getXVelocity();
                mCurrentMaxVelocityX = mCurrentMaxVelocityX > velocityX ? mCurrentMaxVelocityX : velocityX;
                int deltaX = (int) (event.getX() - lastTouchDownX);
                lastTouchDownX = (int) event.getX();
                if (deltaX > 0 || deltaX <= 0 && getScrollX() - deltaX <= 0) {
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) menuView.getLayoutParams();
                    layoutParams.leftMargin = menuInitDrawLeft + (int) (getScrollX() * 2.0 / 3);
                    menuView.setLayoutParams(layoutParams);
                    setOverScrollMode(OVER_SCROLL_ALWAYS);
                    overScrollBy(-deltaX, 0, getScrollX(), 0, 0, 0, menuViewWidth, 0, true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: MotionEvent.ACTION_UP | MotionEvent.ACTION_CANCEL");
                mTouchUpX = event.getX();
                int detailX = (int) (mTouchUpX - mTouchDownX);
                Log.d(TAG, "onTouchEvent: detailX=" + detailX);
                if (!mScroller.computeScrollOffset() && (getScrollX() < 0 && getScrollX() > -menuViewWidth)) {
                    Log.d(TAG, "onTouchEvent: mCurrentMaxVelocityX=" + mCurrentMaxVelocityX);
                    if (Math.abs(detailX) >= menuViewWidth * 1.0 / 4 || Math.abs(mCurrentMaxVelocityX) >= mMaxVelocityX) {
                        if (detailX > 0) {
                            mScroller.startScroll(getScrollX(), 0, -(menuViewWidth + getScrollX()), 0, (int) (Math.abs(menuViewWidth + getScrollX()) * viewScrollVelocityX));
                        } else {
                            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, (int) (Math.abs(getScrollX()) * viewScrollVelocityX));
                        }
                    } else {
                        if (detailX <= 0) {
                            mScroller.startScroll(getScrollX(), 0, -(menuViewWidth + getScrollX()), 0, (int) (Math.abs(menuViewWidth + getScrollX()) * viewScrollVelocityX));
                        } else {
                            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, (int) (Math.abs(getScrollX()) * viewScrollVelocityX));
                        }
                    }
                } else {
                    Log.d(TAG, "onTouchEvent: mScroller.computeScrollOffset is true");
                }
                postInvalidateOnAnimation();
                break;
        }
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        this.scrollTo(scrollX, scrollY);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d(TAG, "onScrollChanged() called with: l = [" + l + "], t = [" + t + "], oldl = [" + oldl + "], oldt = [" + oldt + "]");
        maskView.setAlpha(0.6f * Math.abs(l) / menuViewWidth * 1);
        maskView.setVisibility(VISIBLE);
        if (Math.abs(l) <= 1) {
            maskView.setVisibility(GONE);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: mScroller.getCurrX()=" + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            int leftMargin = menuInitDrawLeft + (int) (getScrollX() * 2.0 / 3);
            if (leftMargin + menuViewWidth > 0) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) menuView.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                requestLayout();
            }
        } else {
            Log.d(TAG, "computeScroll: computeScrollOffset is false~!~!");
        }
    }
}
