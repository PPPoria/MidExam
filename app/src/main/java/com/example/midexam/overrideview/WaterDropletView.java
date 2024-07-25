package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WaterDropletView extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        float x = getWidth();
        float y = getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#6bbef2"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(7);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path  = new Path();
        path.moveTo(x/2f,3);
        path.cubicTo(x,y/2.5f,x,y-3,x/2f,y-3);
        path.cubicTo(0,y-3,0,y/2.5f,x/2f,3);
        path.close();
        canvas.drawPath(path,paint);

        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(11);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path.reset();
        path.moveTo(x/2f,5);
        path.cubicTo(x,y/2.5f,x,y-5,x/2f,y-5);
        path.cubicTo(0,y-5,0,y/2.5f,x/2f,5);
        canvas.drawPath(path,paint);

        path.reset();
        path.moveTo(x-x/4f,y/2f);
        path.quadTo(x-x/6f,y-y/6f,x/2f,y-y/9f);
        canvas.drawPath(path,paint);
    }

    public WaterDropletView(Context context) {
        super(context);
    }

    public WaterDropletView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterDropletView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaterDropletView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
