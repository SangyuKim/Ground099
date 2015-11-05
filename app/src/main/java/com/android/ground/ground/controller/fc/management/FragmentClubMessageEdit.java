package com.android.ground.ground.controller.fc.management;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCFragment;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.message.MyMessageAdapter;
import com.android.ground.ground.controller.person.profile.MyProfileFragment;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnAdapterNoListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterYesListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentClubMessageEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentClubMessageEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentClubMessageEdit extends Fragment {

    ListView listView;
    MyMessageAdapter mAdapter;
    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    Button btn, btn2;
    LinearLayout mLinearLayout;
    boolean isAllchecked= false;

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
     * @return A new instance of fragment FragmentClubMessageEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentClubMessageEdit newInstance(String param1, String param2) {
        FragmentClubMessageEdit fragment = new FragmentClubMessageEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentClubMessageEdit() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_fragment_club_message_edit, container, false);
        listView = (ListView)view.findViewById(R.id.listView_club_message);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        initData();

        mAdapter = new MyMessageAdapter(getContext(), items);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mItemClickListener);
        //전체선택
        btn2 = (Button)view.findViewById(R.id.button6);
        mLinearLayout = (LinearLayout)view.findViewById(R.id.linearLayout_clear_cancel);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAllchecked){

                    for(int i=0; i< mAdapter.getCount(); i++){

                        listView.setItemChecked(i, true);
                        if(!mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = true;
                    }
                }else{
                    for(int i=0; i< mAdapter.getCount(); i++){

                        listView.setItemChecked(i, false);
                        if(mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = false;

                    }
                }
            }
        });
        btn  = (Button)view.findViewById(R.id.button11);
        btn.setText("편집취소");
        btn2.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mAdapter.setCheckBoxVisible(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return view;
    }
    private void onChoiceItem() {
        SparseBooleanArray selection = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < selection.size(); index++) {
            int position = selection.keyAt(index);
            if (selection.get(position)) {
                sb.append(Integer.toString(position)).append(",");
            }
        }
        Toast.makeText(getContext(), "items : " + sb.toString(), Toast.LENGTH_SHORT).show();

    }

    public void initData(){
        for(int i=0; i< 20; i++){
            MyMessageItem item = new MyMessageItem();
            items.add(item);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
    private AdapterView.OnItemClickListener mItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    mAdapter.setChecked(position);
                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                    mAdapter.notifyDataSetChanged();

                }
            };

}
