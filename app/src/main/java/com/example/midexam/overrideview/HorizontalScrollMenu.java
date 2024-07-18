package com.example.midexam.overrideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class HorizontalScrollMenu extends HorizontalScrollView {
    private int screenWidth;
    private int menuWidth;
    private int menuWidthHalf;

    private boolean operateMenu = false;
    private boolean once = false;

    LinearLayout linearLayout;
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
        linearLayout = (LinearLayout) getChildAt(0);
        centre = (ConstraintLayout) linearLayout.getChildAt(0);
        menu = (ConstraintLayout) linearLayout.getChildAt(1);

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        menuWidth = screenWidth / 2;
        menuWidthHalf = menuWidth / 2;

        centre.getLayoutParams().width = screenWidth;
        menu.getLayoutParams().width = menuWidth;

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
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (getScrollX() > menuWidthHalf && operateMenu) {
                this.scrollTo(menuWidth, 0);
            } else if (getScrollX() < menuWidthHalf && !operateMenu) {
                this.scrollTo(0, 0);
            }
            return true;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (l > menuWidthHalf) operateMenu = true;
        else operateMenu = false;
    }
}
