package com.android.ground.ground.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.inputmethod.InputMethodManager;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.log.Logger;
import com.android.ground.ground.model.widget.KakaoSDKAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.kakao.auth.KakaoSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;


public class MyApplication extends Application {

    private Activity currentTopActivity;
    private static Context mContext;
    private static InputMethodManager mIMM;
    private static volatile MyApplication instance = null;
    private static volatile Activity currentActivity = null;
    private com.android.volley.toolbox.ImageLoader imageLoader;

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static MyApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader(this);
        mIMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements

        instance = this;

        KakaoSDK.init(new KakaoSDKAdapter());
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentTopActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (currentTopActivity != null && currentTopActivity == activity) {
                    currentTopActivity = null;
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        com.android.volley.toolbox.ImageLoader.ImageCache imageCache = new com.android.volley.toolbox.ImageLoader.ImageCache() {
            final LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>(3);

            @Override
            public void putBitmap(String key, Bitmap value) {
                imageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return imageCache.get(key);
            }
        };

        imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, imageCache);

    }


    /**
     * 이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
     */

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




    public static Activity getCurrentActivity() {
        Logger.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        MyApplication.currentActivity = currentActivity;
    }
    /**
     * 이미지 로더를 반환한다.
     * @return 이미지 로더
     */
    public com.android.volley.toolbox.ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

}
