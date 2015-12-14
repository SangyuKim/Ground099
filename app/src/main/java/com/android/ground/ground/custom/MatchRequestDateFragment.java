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
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.model.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchRequestDateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchRequestDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchRequestDateFragment extends DialogFragment {
    String time;

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

    public MatchRequestDateFragment() {
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
    public static MatchRequestDateFragment newInstance(String param1, String param2) {
        MatchRequestDateFragment fragment = new MatchRequestDateFragment();
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
    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_date_request, container, false);

         matchDate = (DatePicker)view.findViewById(R.id.matchDate);
         btnSend = (Button)view.findViewById(R.id.button4);
         matchContent = (TextView)view.findViewById(R.id.matchContent);
        date= ""+matchDate.getYear()+"/"+ (matchDate.getMonth()+1)+"/"+ matchDate.getDayOfMonth();
        matchDate.init(matchDate.getYear(), matchDate.getMonth(), matchDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            }
        });

        matchContent.setText(getActivity().getIntent().getStringExtra("club_name")+"팀에게 매치 신청");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date= ""+matchDate.getYear()+"/"+ (matchDate.getMonth()+1)+"/"+ matchDate.getDayOfMonth();
                Log.d("hello", date);
                Toast.makeText(MyApplication.getContext(), "매치 날짜를 선택하였습니다.", Toast.LENGTH_SHORT).show();
                getActivity().getIntent().putExtra("match_date", date);
                MatchRequestFragment dialog = new MatchRequestFragment();
                dialog.show(getFragmentManager(), "dialog");
                dismiss();
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
