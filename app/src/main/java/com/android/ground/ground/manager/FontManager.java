package com.android.ground.ground.manager;

import android.content.Context;
import android.graphics.Typeface;


public class FontManager {

    private static FontManager instance;

    private static Object obj = new Object();

    public static FontManager getInstance() {
        synchronized (obj) {
            if (instance == null) {
                instance = new FontManager();
            }
        }
        return instance;
    }

    Typeface nanum, noto, roboto;

    public static final String NANUM = "nanum";
    public static final String NOTO = "noto";
    public static final String ROBOTO = "roboto";

    private FontManager() {

    }

    public Typeface getTypeface(Context context, String fontName) {
        if (NANUM.equals(fontName)) {
            if (nanum == null) {
                nanum = Typeface.createFromAsset(context.getAssets(), "nanumgothic.ttf");
            }
            return nanum;
        }
        if (NOTO.equals(fontName)) {
            if (noto == null) {
                noto = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular.otf");
            }
            return noto;
        }
        if (ROBOTO.equals(fontName)) {
            if (roboto == null) {
                roboto = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
            }
            return roboto;
        }
        return null;
    }
}
