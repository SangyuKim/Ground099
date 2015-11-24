package com.android.ground.ground.manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.person.profile.MyPageTransResult;

import java.util.ArrayList;
import java.util.List;

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
		listPageTransResult = new ArrayList<MyPageTransResult>();
	}

	private static final String REG_ID = "regToken";
	
	public void setRegistrationToken(String regId) {
		mEditor.putString(REG_ID, regId);
		mEditor.commit();
	}
	
	public String getRegistrationToken() {
		return mPrefs.getString(REG_ID, "");
	}


	private static final String FIELD_FACEBOOK_ID = "facebookId";
	public void setFacebookId(String id) {
		mEditor.putString(FIELD_FACEBOOK_ID, id);
		mEditor.commit();
	}
	public String getFaceBookId() {
		return mPrefs.getString(FIELD_FACEBOOK_ID, "");
	}


	private static final String FIELD_KAKAO_ID = "kakaoId";
	public void setKakaoId(String id) {
		mEditor.putString(FIELD_KAKAO_ID, id);
		mEditor.commit();
	}
	public String getKakaoId() {
		return mPrefs.getString(FIELD_KAKAO_ID, "");
	}



	MyPageResult myPageResult;
	public void setMyPageResult(MyPageResult mResult){
		myPageResult = mResult;
	}
	public MyPageResult getMyPageResult(){
		return myPageResult;
	}
	List<MyPageTransResult> listPageTransResult;
	public void setMyPageTransResult(List<MyPageTransResult> mResult){
		listPageTransResult = mResult;
	}
	public List<MyPageTransResult> getMyPageTransResult(){
		return listPageTransResult;
	}


	private static final String USER_ID = "userId";

	public void setUserId(int userId) {
		mEditor.putInt(USER_ID, userId);
		mEditor.commit();
	}

	public int getUserId() {
		return mPrefs.getInt(USER_ID, -1);
	}
	private static final String USER_NAME = "userName";

	public void setUserName(String userName) {
		mEditor.putString(USER_NAME, userName);
		mEditor.commit();
	}

	public String getUserName() {
		return mPrefs.getString(USER_NAME, "");
	}







}
