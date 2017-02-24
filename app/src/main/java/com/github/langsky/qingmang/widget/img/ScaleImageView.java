package com.github.langsky.qingmang.widget.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.github.langsky.qingmang.R;


/**
 * Created by swd1 on 17-1-4.
 */

public class ScaleImageView extends ImageView {

    private float scale;

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        scale = a.getFloat(R.styleable.ScaleImageView_scale_float, 3 / 4);
        a.recycle();
    }

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fourThreeHeight = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * scale),
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, fourThreeHeight);
    }

    public float getScaleFloat() {
        return scale;
    }

    public void setScaleFloat(float scale) {
        this.scale = scale;
    }
}
