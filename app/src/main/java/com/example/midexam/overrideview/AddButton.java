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

public class AddButton extends View {

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        float length = Math.min(getWidth(), getHeight()) / 3f;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#2983bb"));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(7);

        Path path = new Path();
        path.moveTo(centerX - length / 2f, centerY);
        path.rLineTo(length, 0);
        canvas.drawPath(path, paint);

        path.reset();
        path.moveTo(centerX, centerY - length / 2f);
        path.rLineTo(0, length);
        canvas.drawPath(path, paint);
    }

    public AddButton(Context context) {
        super(context);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AddButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
