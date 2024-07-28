package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeasureXYView extends View {
    private static final String TAG = "MeasureXYView";
    float x;
    float y;

    public float getMeasureX() {
        return x;
    }

    public float getMeasureY() {
        return y;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        x = getWidth();
        y = getHeight();
        Log.d(TAG, "x = " + x + ", y = " + y);
    }

    public MeasureXYView(Context context) {
        super(context);
    }

    public MeasureXYView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureXYView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeasureXYView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
