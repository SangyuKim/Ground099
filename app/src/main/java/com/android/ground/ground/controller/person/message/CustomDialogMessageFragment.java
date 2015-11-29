package com.android.ground.ground.controller.person.message;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.post.push.Push100;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomDialogMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomDialogMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomDialogMessageFragment extends DialogFragment {

    EditText inputView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomDialogMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomDialogMessageFragment newInstance(String param1, String param2) {
        CustomDialogMessageFragment fragment = new CustomDialogMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomDialogMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_custom_dialog_message, container, false);
        inputView = (EditText)view.findViewById(R.id.editText5);
        Button btn = (Button)view.findViewById(R.id.button18);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputView.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(getContext(), "no input" , Toast.LENGTH_SHORT).show();
                } else {
                    //collector id인지 clubId인지 확인하기
                    Push100 mPush100 = new Push100();
                    mPush100.member_id = PropertyManager.getInstance().getUserId();
                    mPush100.sender_id= PropertyManager.getInstance().getUserId();
                    mPush100.collector_id = getActivity().getIntent().getIntExtra("collector_id", 0);
                    mPush100.contents = text;

                    NetworkManager.getInstance().postNetworkMessage100(getContext(), mPush100, new NetworkManager.OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            Toast.makeText(getContext(), "메시지를 전송하였습니다." , Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "메시지 전송 실패" , Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }



            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog d = getDialog();

//        d.getWindow().setLayout(R.dimen.msg_dialog_width, R.dimen.msg_dialog_height);
        WindowManager.LayoutParams params = d.getWindow().getAttributes();
        params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
//        params.x = 100;
//        params.y =100;
        d.getWindow().setAttributes(params);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
