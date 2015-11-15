package com.android.ground.ground.model.tmap;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;


public class TmapItemView extends RelativeLayout {
    public TmapItemView(Context context) {
        super(context);
        init();
    }

    public TmapItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView textView;
    private void init() {
        inflate(getContext(), R.layout.view_tmap_item, this);
        textView = (TextView)findViewById(R.id.textView81);

    }

    public void setTmapItem(DongInfo item) {
        textView.setText(Html.fromHtml(item.address));

    }

    public TextView getTextView() {
        return textView;
    }
}
