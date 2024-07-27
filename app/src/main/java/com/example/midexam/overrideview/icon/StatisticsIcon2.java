package com.example.midexam.overrideview.icon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StatisticsIcon2 extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        float x = getWidth();
        float y = getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();
        path.moveTo(x/6f,y-y/10f);
        path.lineTo(x/6f,y/4f);
        path.lineTo(4f*x/9f,y/4f);
        path.lineTo(4f*x/9f,y-y/10f);
        path.close();
        canvas.drawPath(path, paint);

        path.reset();
        path.moveTo(x-x/6f,y-y/10f);
        path.lineTo(x-x/6f,y/10f);
        path.lineTo(x-4f*x/9f,y/10f);
        path.lineTo(x-4f*x/9f,y-y/10f);
        path.close();
        canvas.drawPath(path, paint);
    }

    public StatisticsIcon2(Context context) {
        super(context);
    }

    public StatisticsIcon2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatisticsIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StatisticsIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
