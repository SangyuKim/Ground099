package com.android.ground.ground.custom;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.post.push.Push401;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchRequestFragment extends DialogFragment {
    String time;

    EditText matchLocation;
    EditText etc;
    TimePicker startTime;
    TimePicker endTime;
    DatePicker matchDate;
    Button btnSend;
    TextView matchContent;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MatchRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchRequestFragment newInstance(String param1, String param2) {
        MatchRequestFragment fragment = new MatchRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_request, container, false);


         matchLocation = (EditText)view.findViewById(R.id.matchLocation);
         etc = (EditText)view.findViewById(R.id.etc);
         startTime = (TimePicker)view.findViewById(R.id.startTime);
         endTime = (TimePicker)view.findViewById(R.id.endTime);
         matchDate = (DatePicker)view.findViewById(R.id.matchDate);
         btnSend = (Button)view.findViewById(R.id.button4);
         matchContent = (TextView)view.findViewById(R.id.matchContent);
        final Push401 mPush401 =new Push401();
        mPush401.collectorClub_id = getActivity().getIntent().getIntExtra("collectorClub_id", 0);
        mPush401.sender_id = PropertyManager.getInstance().getUserId();

//        startTime.
//        startTime.ini
        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mPush401.startTime= "" + hourOfDay + ":" + minute +":00";
            }
        });

        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mPush401.endTime = "" + hourOfDay + ":" + minute +":00";
            }
        });
//        mPush401.matchDate= ""+matchDate.getYear()+"/"+ (matchDate.getMonth()+1)+"/"+ matchDate.getDayOfMonth();
//        matchDate.init(matchDate.getYear(), matchDate.getMonth(), matchDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                mPush401.matchDate = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
//            }
//        });
         String match_date =  getActivity().getIntent().getStringExtra("match_date");
        mPush401.matchDate = match_date;


        matchContent.setText(getActivity().getIntent().getStringExtra("club_name")+"팀에게 매치 신청");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPush401.matchLocation = matchLocation.getText().toString();
                mPush401.etc = etc.getText().toString();

                Log.d("hello", mPush401.matchDate );
                if(mPush401.startTime ==null){
                    Toast.makeText(MyApplication.getContext(), "시작 시간을 정해주세요.", Toast.LENGTH_SHORT).show();
                }else if(mPush401.endTime== null){
                    Toast.makeText(MyApplication.getContext(), "끝날 시간을 정해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                NetworkManager.getInstance().postNetworkMessage401(getContext(), mPush401, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {

                        NetworkManager.getInstance().postNetworkPush401(getContext(), mPush401, new NetworkManager.OnResultListener<EtcData>() {
                            @Override
                            public void onSuccess(EtcData result) {
                                Toast.makeText(MyApplication.getContext(), "매치 신청 하였습니다.", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(MyApplication.getContext(), "매치 신청 푸시를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(MyApplication.getContext(), "매치 신청 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
