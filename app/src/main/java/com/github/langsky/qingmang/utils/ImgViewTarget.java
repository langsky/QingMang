package com.github.langsky.qingmang.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by swd1 on 17-1-22.
 */

public class ImgViewTarget extends BitmapImageViewTarget {

    private ImageView.ScaleType type;

    public ImgViewTarget(ImageView view, ImageView.ScaleType type) {
        super(view);
        this.type = type;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        if (resource != null && view.getScaleType() != type)
            view.setScaleType(type);
        super.onResourceReady(resource, glideAnimation);
    }

    @Override
    public void onLoadStarted(Drawable placeholder) {
        if (placeholder != null && view.getScaleType() != type)
            view.setScaleType(type);
        super.onLoadStarted(placeholder);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        if (errorDrawable != null && view.getScaleType() != type)
            view.setScaleType(type);
        super.onLoadFailed(e, errorDrawable);
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {
        if (placeholder != null && view.getScaleType() != type)
            view.setScaleType(type);
        super.onLoadCleared(placeholder);
    }

}
