package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.naver.MovieItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchMatchTestItemView extends RelativeLayout implements Checkable {
    public SearchMatchTestItemView(Context context) {
        super(context);
        init();
    }

    public SearchMatchTestItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView titleView, directorView;
    MovieItem mItem;
    LinearLayout mLinearLayout;


    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_match_test_item, this);
        titleView = (TextView)findViewById(R.id.text_title);
        directorView = (TextView)findViewById(R.id.text_director);
        mLinearLayout = (LinearLayout)findViewById(R.id.linearLayout_extra);
        Button btn = (Button)findViewById(R.id.button36);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onExtraButtonClick(SearchMatchTestItemView.this, mItem);
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

    }

    public interface OnExtraButtonClickListener {
        public void onExtraButtonClick(SearchMatchTestItemView view, MovieItem data);
    }

    OnExtraButtonClickListener mListener;
    public void setOnExtraButtonListener(OnExtraButtonClickListener listener) {
        mListener = listener;
    }
    public void setVisible(){
        mLinearLayout.setVisibility(View.VISIBLE);
    }
    public void setInvisible(){
        mLinearLayout.setVisibility(View.GONE);
    }
    public int getVisibilityLayout(){
        return mLinearLayout.getVisibility();
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;
            drawCheck();
        }

    }
    boolean isChecked = false;
    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
    private void drawCheck() {
        if (isChecked) {
//            checkView.setImageResource(android.R.drawable.checkbox_on_background);
//            setBackgroundColor(Color.RED);
//           checkedImage.setVisibility(View.VISIBLE);
            setVisible();
        } else {
//            checkView.setImageResource(android.R.drawable.checkbox_off_background);
//            setBackgroundColor(Color.TRANSPARENT);
//            checkedImage.setVisibility(View.GONE);
            setInvisible();
        }

    }
}
