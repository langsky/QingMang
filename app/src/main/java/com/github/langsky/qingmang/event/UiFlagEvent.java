package com.github.langsky.qingmang.event;

/**
 * Created by swd1 on 17-1-25.
 */

public class UiFlagEvent {

    private boolean hasImage;

    public UiFlagEvent(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean isHasImage() {
        return hasImage;
    }
}
