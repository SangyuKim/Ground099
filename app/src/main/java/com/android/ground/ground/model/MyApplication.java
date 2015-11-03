package com.android.ground.ground.model;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;


public class MyApplication extends Application {
    private static Context mContext;
    private static InputMethodManager mIMM;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader(this);
        mIMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static InputMethodManager getmIMM() {
        return mIMM;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(options)
                .imageDownloader(new HttpClientImageDownloader(context, NetworkManager.getInstance().getHttpClient()))
                .build();
        ImageLoader.getInstance().init(config);
    }

}
