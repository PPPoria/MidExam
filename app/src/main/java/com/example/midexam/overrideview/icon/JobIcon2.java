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

public class JobIcon2 extends View {

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
        path.moveTo(x/6.2f,y/2f);
        path.lineTo(x-x/2f,y-y/6.2f);

        path.cubicTo(x-x/3f,y-y/6.2f,x-x/2f,y-y/3.5f,x-x/5f,y/2.6f);

        path.lineTo(x-x/2.6f,y/5f);

        path.cubicTo(x/3.5f,y/2f,x/6.2f,y/3f,x/6.2f,y/2f);
        path.close();
        canvas.drawPath(path,paint);
    }

    public JobIcon2(Context context) {
        super(context);
    }

    public JobIcon2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JobIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JobIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
