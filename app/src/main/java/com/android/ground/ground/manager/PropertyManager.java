package com.android.ground.ground.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;

public class PropertyManager {

	public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
	private static PropertyManager instance;
	public static PropertyManager getInstance() {
		if (instance == null) {
			instance = new PropertyManager();
		}
		return instance;
	}
	
	SharedPreferences mPrefs;
	SharedPreferences.Editor mEditor;
	
	private PropertyManager() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
		mEditor = mPrefs.edit();
		myPageResult = new MyPageResult();
	}

	private static final String REG_ID = "regToken";
	
	public void setRegistrationToken(String regId) {
		mEditor.putString(REG_ID, regId);
		mEditor.commit();
	}
	
	public String getRegistrationToken() {
		return mPrefs.getString(REG_ID, "");
	}

	MyPageResult myPageResult;
	public void setMyPageResult(MyPageResult mResult){
		myPageResult = mResult;
	}
	public MyPageResult getMyPageResult(){
		return myPageResult;
	}

	
}
