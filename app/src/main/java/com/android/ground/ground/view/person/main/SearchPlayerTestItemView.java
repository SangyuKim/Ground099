package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.naver.MovieItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchPlayerTestItemView extends RelativeLayout {
    public SearchPlayerTestItemView(Context context) {
        super(context);
        init();
    }

    public SearchPlayerTestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView iconView;
    TextView titleView, directorView, actorView, rateView;
    MovieItem mItem;
    LinearLayout mLinearLayout;


    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_player_test_item, this);
        iconView = (ImageView)findViewById(R.id.image_icon);
        titleView = (TextView)findViewById(R.id.text_title);
        directorView = (TextView)findViewById(R.id.text_director);
        actorView = (TextView)findViewById(R.id.text_actor);
        rateView= (TextView)findViewById(R.id.text_rate);
        Button btn;

        btn = (Button)findViewById(R.id.button21);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                   if (mRequestListener != null) {
                    mRequestListener.onRequestButtonClick(SearchPlayerTestItemView.this, mItem);
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
        actorView.setText(item.actor);
        rateView.setText(Float.toString(item.userRating));

        ImageLoader.getInstance().displayImage(item.image, iconView, options);
    }

    public interface OnRequestButtonClickListener {
        public void onRequestButtonClick(SearchPlayerTestItemView view, MovieItem data);
    }

    OnRequestButtonClickListener mRequestListener;
    public void setOnRequestButtonListener(OnRequestButtonClickListener listener) {
        mRequestListener = listener;
    }
}
