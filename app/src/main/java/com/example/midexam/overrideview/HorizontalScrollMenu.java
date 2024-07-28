package com.example.midexam.overrideview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class HorizontalScrollMenu extends HorizontalScrollView {
    private static final String TAG = "HorizontalScrollMenu";

    private int screenWidth;
    private int menuWidth;
    private int menuWidthHalf;

    private final VelocityTracker tracker = VelocityTracker.obtain();
    private final int minSpeed = 1;
    Long endTime = 0L;
    Long startTime = 0L;
    Long deltaTime = 0L;

    float startX = 0f;
    float endX = 0f;
    float deltaX = 0f;

    private boolean operateMenu = false;

    ConstraintLayout constraintLayout;
    ConstraintLayout centre;
    ConstraintLayout menu;

    public HorizontalScrollMenu(Context context) {
        super(context);
    }

    public HorizontalScrollMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HorizontalScrollMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取Layout，将用于获取宽度
        constraintLayout = (ConstraintLayout) getChildAt(0);
        centre = (ConstraintLayout) constraintLayout.getChildAt(0);
        menu = (ConstraintLayout) constraintLayout.getChildAt(1);

        //获取屏幕宽度和menu宽度
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        menuWidth = screenWidth / 2;
        menuWidthHalf = menuWidth / 2;

        //为centre和menu设置宽度
        centre.getLayoutParams().width = screenWidth;
        menu.getLayoutParams().width = menuWidth;

        tracker.computeCurrentVelocity(1000);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //if (changed) {
        //    this.scrollTo(menuWidth, 0);
        //    once = true;
        //}
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        ///位置判断和速度检测，模拟ItemTouchHelper.Callback的速度阈值
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startTime = System.currentTimeMillis();
            startX = ev.getX();
            Log.d(TAG, "startTime = " + startTime);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {

            endTime = System.currentTimeMillis();
            endX = ev.getX();
            Log.d(TAG, "endTime = " + endTime);

            //获取相对时间
            deltaTime = endTime - startTime;
            Log.d(TAG, "deltaTime = " + deltaTime);

            //获取相对位置
            deltaX = endX - startX;
            Log.d(TAG, "deltaX = " + deltaX);

            //速度检测，超过阈值则直接展开或关闭
            if(deltaX/deltaTime < -minSpeed){
                this.scrollTo(menuWidth, 0);
                operateMenu = true;
                return true;
            }else if (deltaX/deltaTime > minSpeed){
                this.scrollTo(0, 0);
                operateMenu = false;
                return true;
            }

            //位置判断，超过menu的一半就直接展开或关闭
            if (getScrollX() > menuWidthHalf && operateMenu) {
                this.scrollTo(menuWidth, 0);
                //Log.d(TAG, "Open menu");
            } else if (getScrollX() < menuWidthHalf && !operateMenu) {
                this.scrollTo(0, 0);
                //Log.d(TAG, "Close menu");
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        //menu展开的状态判断
        if (l > menuWidthHalf) operateMenu = true;
        else if (l < menuWidthHalf) operateMenu = false;
        //Log.d(TAG, "operateMen = " + operateMenu);
    }
}
