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

public class PersonIcon1 extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        float x = getWidth();
        float y = getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();
        path.moveTo(x - x / 8f, y - y / 10f);
        path.lineTo(x / 8f, y - y / 10f);
        path.quadTo(x / 8f, y / 1.8f, x / 2f, y / 1.8f);
        path.quadTo(x - x / 8f, y / 1.8f, x - x / 8f, y - y / 10f);
        canvas.drawPath(path, paint);

        path.reset();
        canvas.drawCircle(x/2f,(y/1.8f)/2f,y/6f,paint);
    }

    public PersonIcon1(Context context) {
        super(context);
    }

    public PersonIcon1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonIcon1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PersonIcon1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
