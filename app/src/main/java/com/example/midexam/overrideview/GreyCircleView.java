package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GreyCircleView extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#f8f9fd"));

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float r = Math.min(centerY,centerX);

        canvas.drawCircle(centerX,centerY,r,paint);
    }
    public GreyCircleView(Context context) {
        super(context);
    }

    public GreyCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GreyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GreyCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
