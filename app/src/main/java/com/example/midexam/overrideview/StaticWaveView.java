package com.example.midexam.overrideview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StaticWaveView extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        float x = getWidth();
        float y = getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#666BBEF2"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.moveTo(0,y/2f);
        path.cubicTo(x/5f,y+y/6f,x/2.2f,0,x,y/2);
        path.lineTo(x,y);
        path.lineTo(0,y);
        path.close();
        canvas.drawPath(path,paint);

        path.reset();
        path.moveTo(0,y/4f);
        path.cubicTo(x/1.8f,y+y/2f,x-x/5f,-y/8,x,y/4);
        path.lineTo(x,y);
        path.lineTo(0,y);
        path.close();
        canvas.drawPath(path,paint);
    }

    public StaticWaveView(Context context) {
        super(context);
    }

    public StaticWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StaticWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
