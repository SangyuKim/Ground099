package com.android.ground.ground.controller.etc.Area;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.tmap.DongInfo;
import com.android.ground.ground.model.tmap.TmapAdapter;
import com.android.ground.ground.model.tmap.TmapItem;
import com.android.ground.ground.model.tmap.TmapItemView;

import java.util.List;

public class AreaSearchActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    TmapAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText)findViewById(R.id.editText6);
        listView = (ListView)findViewById(R.id.listView_area);
        mAdapter = new TmapAdapter();
        listView.setAdapter(mAdapter);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.equals("")){
//                    mAdapter.clear();
//                }else{
//                    searchTmaps(s.toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        Button btn = (Button)findViewById(R.id.button44);
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
//                    mAdapter.setTotalCount(result.totalCnt);
                    mAdapter.clear();
                    for (DongInfo item : result.findPoiAreaDataByNameInfo.items) {
                        mAdapter.add(item);
                    }

//                    refreshView.onRefreshComplete();

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(AreaSearchActivity.this, "error : " + code, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.setKeyword(s);
        }

    }



}
