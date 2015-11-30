package com.android.ground.ground.controller.etc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomRubberLoaderView;


/**
 * Created by greenfrvr
 */
public class RippleSampleFragment extends BaseFragment {



    CustomRubberLoaderView rubberLoader2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_ripple, container, false);

        rubberLoader2 = (CustomRubberLoaderView)view.findViewById(R.id.loader2);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         rubberLoader2.startLoading();

    }
}
