package org.anvei.novelreader.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.anvei.novelreader.R;

public class SimpleCircleView extends View {

    private Paint paint;

    private int color;
    private float radius;

    public SimpleCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleCircleView);
        color = typedArray.getColor(R.styleable.SimpleCircleView_color, Color.RED);
        radius = typedArray.getDimension(R.styleable.SimpleCircleView_radius, 10);
        typedArray.recycle();
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(radius, radius, radius, paint);
    }
}
