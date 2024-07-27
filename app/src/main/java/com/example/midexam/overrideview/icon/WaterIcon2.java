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

public class WaterIcon2 extends View {

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
        path.moveTo(x / 2f, 3);
        path.cubicTo(x, y / 2f, x - x / 8f, y - 3, x / 2f, y - 3);
        path.cubicTo(x / 8f, y - 3, 0, y / 2f, x / 2f, 3);
        path.close();
        canvas.drawPath(path, paint);
    }

    public WaterIcon2(Context context) {
        super(context);
    }

    public WaterIcon2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaterIcon2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
