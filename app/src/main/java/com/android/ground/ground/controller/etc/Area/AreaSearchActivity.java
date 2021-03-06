package com.android.ground.ground.controller.etc.Area;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.tmap.DongInfo;
import com.android.ground.ground.model.tmap.TmapAdapter;
import com.android.ground.ground.model.tmap.TmapItem;
import com.android.ground.ground.model.tmap.TmapItemView;

public class AreaSearchActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    TmapAdapter mAdapter;
    CustomToolbar customToolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(AreaSearchActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(Exception e){
            e.printStackTrace();
        }
        setTitle("지역검색");

        editText = (EditText)findViewById(R.id.editText6);
        listView = (ListView)findViewById(R.id.listView_area);
        mAdapter = new TmapAdapter();
        listView.setAdapter(mAdapter);

        ImageView btn = (ImageView)findViewById(R.id.button44);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                searchTmaps(s.toString());
            }
        });
        if(editText.getText().toString().equals("")){
            mAdapter.clear();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userArea = ((TmapItemView)view).getTextView().getText().toString();

                Intent intent = new Intent();
                intent.putExtra("userArea", userArea);
                intent.putExtra("latitude", Double.parseDouble( ((TmapItemView) view).mItem.resLat));
                intent.putExtra("longitude", Double.parseDouble(((TmapItemView)view).mItem.resLon));
                //경도랑 위도도 보내기
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }

    private void searchTmaps(final String s) {
        if (!TextUtils.isEmpty(s)) {
            NetworkManager.getInstance().getNetworkTMAP(AreaSearchActivity.this, s, 200, 1, new NetworkManager.OnResultListener<TmapItem>() {
                @Override
                public void onSuccess(TmapItem result) {
                    mAdapter.setKeyword(s);
                    mAdapter.clear();
                    for (DongInfo item : result.findPoiAreaDataByNameInfo.items) {
                        mAdapter.add(item);
                    }
                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(AreaSearchActivity.this, "길 또는 동 단위로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.setKeyword(s);
        }

    }
    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        customToolbar.setTitle(title.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fake_main, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
