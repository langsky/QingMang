package com.github.langsky.qingmang.utils;

import android.content.res.TypedArray;
import android.graphics.Typeface;

import com.github.langsky.qingmang.QingMang;

/**
 * All constant field. Created by swd1 on 16-12-29.
 */

public class C {
    public static final String BASE_URL = "http://qingmang.me/magazines/";
    public static final int BTN_FLIP_DEFAULT_FRONT = 0;
    public static final int BTN_FLIP_DEFAULT_BACK = 1;
    public static final int BTN_FLIP_WORFDPRESS = 2;
    public static final int BTN_FLIP_GITHUB = 3;
    public static final int BTN_FLIP_PAYPAL = 4;
    public static final int BTN_FLIP_SETTINGS = 5;
    public static final int MAX_COUNT_ABOUT_ME = 5;
    public static final int BTN_SHARE = 6;
    public static final int BTN_DELETE = 7;
    public static final int BTN_EMAIL = 8;
    public static final int BTN_WECHAT = 15;
    public static final int BTN_WORFDPRESS = 9;
    public static final int BTN_GITHUB = 10;
    public static final int BTN_BEFORE = 11;
    public static final int BTN_AFTER = 12;
    public static final int BTN_ALIPAY = 13;
    public static final int BTN_IMAGE = 14;
    public static final int BTN_TRANSFORM_HISTORY = 100;

    public static final Typeface shouJin = Typeface.createFromAsset(QingMang.instance.getAssets(), "fonts/shoujinti.ttf");
}
