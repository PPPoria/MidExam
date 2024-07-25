package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShallowBlueCircleView extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#6bbef2"));

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float r = Math.min(centerY,centerX);

        canvas.drawCircle(centerX,centerY,r,paint);
    }

    public ShallowBlueCircleView(Context context) {
        super(context);
    }

    public ShallowBlueCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShallowBlueCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShallowBlueCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
