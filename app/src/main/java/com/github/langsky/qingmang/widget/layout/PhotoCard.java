package com.github.langsky.qingmang.widget.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.PhotoLayoutCardBinding;
import com.github.langsky.qingmang.mvp.model.abs.TextModel;

/**
 * Created by swd1 on 17-1-22.
 */

public class PhotoCard extends ViewGroup implements ArticleViewHandler {

    private final PhotoLayoutCardBinding binding;

    public PhotoCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.photo_layout_card, this, true);
    }

    private void layoutView(View view, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        view.layout(leftWithMargins, topWithMargins,
                leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed = 0;
        int heightUsed = 0;

        measureChildWithMargins(binding.title,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(binding.title);

        if (binding.cover.getVisibility() != View.GONE) {
            measureChildWithMargins(binding.cover,
                    widthMeasureSpec, widthUsed,
                    heightMeasureSpec, heightUsed);
            heightUsed += getMeasuredHeightWithMargins(binding.cover);
        }

        measureChildWithMargins(binding.summary,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(binding.summary);

        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int paddingLeft = getPaddingLeft();
        int currentTop = getPaddingTop();

        layoutView(binding.title, paddingLeft, currentTop,
                binding.title.getMeasuredWidth(), binding.title.getMeasuredHeight());
        currentTop += getHeightWithMargins(binding.title);

        if (binding.cover.getVisibility() != View.GONE) {
            layoutView(binding.cover, paddingLeft, currentTop,
                    binding.cover.getMeasuredWidth(), binding.cover.getMeasuredHeight());
            currentTop += getHeightWithMargins(binding.cover);
        }

        layoutView(binding.summary, paddingLeft, currentTop,
                binding.summary.getMeasuredWidth(), binding.summary.getMeasuredHeight());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setUiData(TextModel model) {
        binding.setItem(model);
    }
}