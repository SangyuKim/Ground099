package com.android.ground.ground.model.naver;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class MovieItemView extends RelativeLayout {
    public MovieItemView(Context context) {
        super(context);
        init();
    }

    public MovieItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView iconView;
    TextView titleView, directorView;
    MovieItem mItem;
    LinearLayout mLinearLayout;


    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_movie_item, this);
        iconView = (ImageView)findViewById(R.id.memImage);
        titleView = (TextView)findViewById(R.id.memNameCountScr);
        directorView = (TextView)findViewById(R.id.text_director);
        mLinearLayout = (LinearLayout)findViewById(R.id.custom_extra);
        Button btn = (Button)findViewById(R.id.button20);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", "button clicked");
                if (mListener != null) {
                    mListener.onExtraButtonClick(MovieItemView.this, mItem);
                }
            }
        });

        btn = (Button)findViewById(R.id.button21);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", "requeset button clicked");
                if (mRequestListener != null) {
                    mRequestListener.onRequestButtonClick(MovieItemView.this, mItem);
                }
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();

    }

    public void setMovieItem(MovieItem item) {
        titleView.setText(Html.fromHtml(item.title));
        directorView.setText(item.director);

        ImageLoader.getInstance().displayImage(item.image, iconView, options);
    }
    public void setVisible(){
        mLinearLayout.setVisibility(View.VISIBLE);
    }
    public void setInvisible(){
        mLinearLayout.setVisibility(View.GONE);
    }

    public interface OnExtraButtonClickListener {
        public void onExtraButtonClick(MovieItemView view, MovieItem data);
    }

    OnExtraButtonClickListener mListener;
    public void setOnExtraButtonListener(OnExtraButtonClickListener listener) {
        mListener = listener;
    }
    public interface OnRequestButtonClickListener {
        public void onRequestButtonClick(MovieItemView view, MovieItem data);
    }

    OnRequestButtonClickListener mRequestListener;
    public void setOnRequestButtonListener(OnRequestButtonClickListener listener) {
        mRequestListener = listener;
    }
}
