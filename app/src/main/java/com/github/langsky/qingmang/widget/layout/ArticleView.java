package com.github.langsky.qingmang.widget.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.ArticleLayoutViewBinding;
import com.github.langsky.qingmang.mvp.model.Article;


public class ArticleView extends ViewGroup implements ArticleViewHandler<Article> {

    private final ArticleLayoutViewBinding binding;

    public ArticleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.article_layout_view, this, true);
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

        if (binding.cover.getVisibility() != View.GONE) {
            measureChildWithMargins(binding.cover,
                    widthMeasureSpec, widthUsed,
                    heightMeasureSpec, heightUsed);
            heightUsed += getMeasuredHeightWithMargins(binding.cover);
        }

        measureChildWithMargins(binding.title,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(binding.title);

        measureChildWithMargins(binding.summary,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(binding.summary);

        measureChildWithMargins(binding.photo,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(binding.photo);

        measureChildWithMargins(binding.author,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(binding.photo);

        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int paddingLeft = getPaddingLeft();
        int currentTop = getPaddingTop();

        if (binding.cover.getVisibility() != View.GONE) {
            layoutView(binding.cover, paddingLeft, currentTop,
                    binding.cover.getMeasuredWidth(), binding.cover.getMeasuredHeight());
            currentTop += getHeightWithMargins(binding.cover);
        }


        layoutView(binding.title, paddingLeft, currentTop,
                binding.title.getMeasuredWidth(), binding.title.getMeasuredHeight());
        currentTop += getHeightWithMargins(binding.title);

        layoutView(binding.summary, paddingLeft, currentTop,
                binding.summary.getMeasuredWidth(), binding.summary.getMeasuredHeight());
        currentTop += getHeightWithMargins(binding.summary);

        layoutView(binding.photo, paddingLeft, currentTop,
                binding.photo.getMeasuredWidth(),
                binding.photo.getMeasuredHeight());

        final int contentLeft = getWidthWithMargins(binding.photo) + paddingLeft;
        final int contentWidth = r - l - contentLeft - getPaddingRight();

        layoutView(binding.author, contentLeft, currentTop,
                contentWidth, binding.author.getMeasuredHeight());

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
    public void setUiData(Article article) {
        binding.setArticle(article);
        /*binding.author.setText(article.getAuthor());
        binding.title.setText(article.getTitle());
        binding.summary.setText(article.getSummary());

        final Context context = getContext();
        UIUtils.loadImage(context, binding.photo, article.getPhoto(), flags);

        final boolean hasPostImage = !TextUtils.isEmpty(article.getCover());

        binding.cover.setVisibility(hasPostImage ? View.VISIBLE : View.GONE);
        if (hasPostImage) {
            UIUtils.loadImage(context, binding.cover, article.getCover(), flags);
        }*/
    }
}
