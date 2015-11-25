package com.android.ground.ground.controller.etc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by greenfrvr
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
