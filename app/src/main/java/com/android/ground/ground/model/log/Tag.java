package com.android.ground.ground.model.log;

/**
 * Created by leoshin on 15. 4. 6..
 */
public enum Tag {
    DEFAULT("kakao.sdk");

    private final String tag;

    private Tag(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
