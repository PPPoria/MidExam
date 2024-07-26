package com.example.midexam.overrideview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShallowBlueWaveView extends View {
    private static final String TAG = "ShadowBlueWaveView";
    private Paint clearPaint;
    private Path path;
    private Paint paint;
    private ValueAnimator animator;
    private int step = 0;
    private float a = 30f;
    private float f;
    private float lambdaH = 140f;
    private float lambdaQ = lambdaH / 2;

    public float d;
    public float y = 50f;

    public ShallowBlueWaveView(Context context) {
        super(context);
        init();
    }

    public void init() {
        d = Math.min(getWidth(), getHeight()) - 100;

        Log.d(TAG, "init: 波浪初始化");
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        clearPaint = new Paint();
        clearPaint.setStyle(Paint.Style.FILL);
        clearPaint.setColor(Color.parseColor("#f8f9fd"));
        clearPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#BF6BBEF2"));
        paint.setStrokeWidth(5f);
        paint.setAntiAlias(true);

        paint.setBlendMode(BlendMode.SRC_IN);

        path = new Path();

        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                if (step >= lambdaH * 2 - 2) step = 0;
                else step += 8;

                path.reset();

                path.moveTo(-(4 * lambdaH) + step, a +50+ y);
                for (int i = 0; i < 6; i++) {
                    path.rQuadTo(lambdaQ, -a, lambdaH, 0);
                    path.rQuadTo(lambdaQ, a, lambdaH, 0);
                }
                path.rLineTo(0, 600);
                path.lineTo(-(4 * lambdaH) + step, 600 + a + y);
                path.close();

                invalidate();
            }
        });


    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, Math.min(getWidth(), getHeight()) / 2f, clearPaint);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public ShallowBlueWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShallowBlueWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShallowBlueWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
