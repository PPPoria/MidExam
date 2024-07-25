package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HalfCircleView extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE); // 设置画笔颜色
        paint.setStyle(Paint.Style.STROKE); // 设置画笔样式为描边
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(21);

        canvas.drawArc(10, 10, getWidth()-10, getHeight()-10, 120, 300, false, paint);
    }

    public HalfCircleView(Context context) {
        super(context);
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
