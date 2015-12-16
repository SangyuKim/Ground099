package com.android.ground.ground.manager;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomRubberLoaderView;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMember;
import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchList;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMain;
import com.android.ground.ground.model.lineup.info.LineupInfo;
import com.android.ground.ground.model.lineup.match.LineupMatch;
import com.android.ground.ground.model.lineup.planLoc.LineupPlanLoc;
import com.android.ground.ground.model.lineup.result.LineupResult;
import com.android.ground.ground.model.lineup.scorer.LineupScorer;
import com.android.ground.ground.model.lineup.virtual.fomation.LineupVirtualFomation;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualRes;
import com.android.ground.ground.model.login.FacebookLogin;
import com.android.ground.ground.model.login.KakaoLogin;
import com.android.ground.ground.model.login.KakaoResponse;
import com.android.ground.ground.model.login.SignupData;
import com.android.ground.ground.model.message.ClubMessageData;
import com.android.ground.ground.model.message.MyMessageData;
import com.android.ground.ground.model.noti.NotiData;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfo;
import com.android.ground.ground.model.person.main.matchinfo.matchFormation.MatchFormation;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.post.ClubManagerData;
import com.android.ground.ground.model.post.lineup.InputResultMVP;
import com.android.ground.ground.model.post.lineup.InputResultScorerReal;
import com.android.ground.ground.model.post.lineup.InputResultSetting;
import com.android.ground.ground.model.post.lineup.LineupPlan;
import com.android.ground.ground.model.post.lineup.LineupVirtualFomationPost;
import com.android.ground.ground.model.post.push.MessageDeleteData;
import com.android.ground.ground.model.post.push.Push302Response;
import com.android.ground.ground.model.post.lineup.ScoreMannerSkill;
import com.android.ground.ground.model.post.fcCreate.ClubProfile;
import com.android.ground.ground.model.post.fcUpdate.ClubProfileUpdate;
import com.android.ground.ground.model.post.push.MatchCreateData;
import com.android.ground.ground.model.post.push.MatchCreateDataResponse;
import com.android.ground.ground.model.post.push.Push100;
import com.android.ground.ground.model.post.push.Push200;
import com.android.ground.ground.model.post.push.Push201Response;
import com.android.ground.ground.model.post.push.Push300;
import com.android.ground.ground.model.post.push.Push301;
import com.android.ground.ground.model.post.push.Push301Response;
import com.android.ground.ground.model.post.push.Push302;
import com.android.ground.ground.model.post.push.Push401;
import com.android.ground.ground.model.post.signup.UserProfile;
import com.android.ground.ground.model.tmap.TmapItem;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;


public class NetworkManager{
    Handler mDialogHandler;
    public List<Dialog> mDialogList= new ArrayList<Dialog>();
    public final static String GROND_SERVER_URL = "http://54.178.160.114";
    public final static String ImageUrl ="";
//    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    private static NetworkManager instance;
    public static NetworkManager getInstance() {

        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    AsyncHttpClient client;
    Gson gson;
    private NetworkManager() {
        mDialogHandler = new Handler();

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client = new AsyncHttpClient();
            client.setSSLSocketFactory(socketFactory);
            client.setCookieStore(new PersistentCookieStore(MyApplication.getContext()));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }


        gson = new Gson();
        client.setCookieStore(new PersistentCookieStore(MyApplication.getContext()));

    }



    public HttpClient getHttpClient() {
        return client.getHttpClient();
    }

    public interface OnResultListener<T> {
        public void onSuccess(T result);
        public void onFail(int code);
    }


    Handler mHandler = new Handler(Looper.getMainLooper());
    public void loginFacebookToken(Context context, String accessToken, final String result , final OnResultListener<String> listener) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(result);
            }
        }, 1000);
    }

    public void signupFacebook(Context context, String message, final OnResultListener<String> listener) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess("OK");
            }
        }, 1000);
    }

    public static final String TMAP_URL = "https://apis.skplanetx.com/tmap/poi/findPoiAreaDataByName";
    private static final String TMAP_API_KEY = "ed4139de-5d31-31a3-aa1d-92a502350a6f";
    public void getNetworkTMAP(final Context context, String keyword, int count, int page, final OnResultListener<TmapItem> listener) {
        final RequestParams params = new RequestParams();

        params.put("area_dong", keyword);
        params.put("count", count);
        params.put("page", page);
        params.put("version",1);
//        params.put("searchKeyword",keyword);
        Header[] headers =new Header[2];
        headers[0] = new BasicHeader("Accept", "application/json" );
        headers[1] = new BasicHeader("appKey",TMAP_API_KEY);

        client.get(context, TMAP_URL,  headers, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                TmapItem items = gson.fromJson(s, TmapItem.class);
                if(items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                 listener.onFail(statusCode);
                listener.onFail(statusCode);
            }

        });

    }

    public static final String SEARCH_CLUB_URL =GROND_SERVER_URL+"/club/search";
    public void getNetworkSearchClub(final Context context, String filter,String keyword, int page, int memberId, final OnResultListener<SearchClub> listener) {
//        showWaitingDialog(context);
        final RequestParams params = new RequestParams();

        String url;
        if(keyword.equals("")){
            url = SEARCH_CLUB_URL + "/"+ filter;
        }else{
            url =   SEARCH_CLUB_URL + "/search";
            params.put("search", keyword);
        }

//        params.put("sort", filter);
        params.put("page", page);
//        params.put("member_id", memberId);


        client.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                SearchClub items = gson.fromJson(s, SearchClub.class);
                if (items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                       listener.onFail(statusCode);
//                unShowWaitingDialog();
            }

        });

    }
    public static final String SEARCH_MEM_URL =GROND_SERVER_URL+"/member/search";
    public void getNetworkSearchMem(final Context context, String filter,String keyword, int page, int memberId, final OnResultListener<SearchMem> listener) {
        final RequestParams params = new RequestParams();
//        showWaitingDialog(context);
        String url;
        if(keyword.equals("")){
            url = SEARCH_MEM_URL + "/"+ filter;
        }else{
            url =   SEARCH_MEM_URL + "/search";
            params.put("search", keyword);
        }
//        params.put("sort", filter);
        params.put("page", page);
//        params.put("member_id", memberId);


        client.get(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                SearchMem items = gson.fromJson(s, SearchMem.class);
                if (items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                unShowWaitingDialog();
                  listener.onFail(statusCode);
            }

        });

    }
    //MVP
    public static final String SEARCH_MATCHINFO_MVP_URL =GROND_SERVER_URL+"/match/MVP";
    public void getNetworkMatchInfoMVP(final Context context,  final OnResultListener<MVP> listener) {
         final RequestParams params = new RequestParams();

        client.get(context,SEARCH_MATCHINFO_MVP_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                MVP items = gson.fromJson(s, MVP.class);
                if (items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);
            }
        });

    }
    //Match
    public static final String SEARCH_MATCHINFO_URL =GROND_SERVER_URL+"/match/search";
    public void getNetworkMatchInfo(final Context context,  String filter,String keyword, int page, int memberId, final OnResultListener<MatchInfo> listener) {
              final RequestParams params = new RequestParams();
//        showWaitingDialog(context);
        String url;
        if(keyword.equals("")){
            url = SEARCH_MATCHINFO_URL + "/"+ filter;
        }else{
            url =  SEARCH_MATCHINFO_URL + "/search";
            params.put("search", keyword);
        }

//        params.put("sort", filter);
        params.put("page", page);
//        params.put("member_id", memberId);
//        params.put("search", keyword);

        client.get(context,url , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));

                MatchInfo items = gson.fromJson(s, MatchInfo.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                unShowWaitingDialog();
                  listener.onFail(statusCode);
            }



        });

    }
    //MyProfile
    public static final String SEARCH_MY_PAGE_URL =GROND_SERVER_URL+"/member/myPage";
    public void getNetworkMyPage(final Context context,  int memberId, final OnResultListener<MyPage> listener) {

        final RequestParams params = new RequestParams();
        params.put("member_id", memberId);

        client.get(context,SEARCH_MY_PAGE_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", "mypage : " + s);

                MyPage items = gson.fromJson(s, MyPage.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }
        });

    }
    //ProfileTrans
    public static final String SEARCH_MY_PAGE_TRANS_URL =GROND_SERVER_URL+"/member/myPage/trans";
    public void getNetworkMyPageTrans(final Context context,  int memberId, final OnResultListener<MyPageTrans> listener) {

        final RequestParams params = new RequestParams();
        params.put("member_id", memberId);

        client.get(context,SEARCH_MY_PAGE_TRANS_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                MyPageTrans items = gson.fromJson(s, MyPageTrans.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }
        });

    }
    //ClubMain
    public static final String SEARCH_CLUB_PAGE_URL =GROND_SERVER_URL+"/club";
    public void getNetworkClubMain(final Context context,  int clubId, final OnResultListener<ClubMain> listener) {
           final RequestParams params = new RequestParams();
        params.put("club_id", clubId);

        client.get(context,SEARCH_CLUB_PAGE_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));

                ClubMain items = gson.fromJson(s, ClubMain.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }
        });

    }
    //ClubAndMember
    public static final String SEARCH_CLUB_AND_MEMBER_PAGE_URL =GROND_SERVER_URL+"/club/member";
    public void getNetworkClubAndMember(final Context context,  int clubId, final OnResultListener<ClubAndMember> listener) {


        final RequestParams params = new RequestParams();
        params.put("club_id", clubId);

        client.get(context,SEARCH_CLUB_AND_MEMBER_PAGE_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                ClubAndMember items = gson.fromJson(s, ClubAndMember.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);

            }
        });

    }
    //ClubMatchList
    public static final String SEARCH_CLUB_AND_MATCHLSIT_PAGE_URL =GROND_SERVER_URL+"/club/matchList";
    public void getNetworkClubMatchList(final Context context,  int clubId, int page, final OnResultListener<ClubMatchList> listener) {


        final RequestParams params = new RequestParams();
        params.put("club_id", clubId);
        params.put("page",page);

        client.get(context,SEARCH_CLUB_AND_MATCHLSIT_PAGE_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));

                ClubMatchList items = gson.fromJson(s, ClubMatchList.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }

        });

    }
    //Lineup info
    public static final String SEARCH_LINEUP_INFO_URL =GROND_SERVER_URL+"/lineup/info";
    public void getNetworkLineupInfo(final Context context,  int clubId, int matchId, final OnResultListener<LineupInfo> listener) {


        final RequestParams params = new RequestParams();
        params.put("club_id", clubId);
        params.put("match_id",matchId);

        client.get(context, SEARCH_LINEUP_INFO_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupInfo items = gson.fromJson(s, LineupInfo.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }
        });

    }
    //Lineup match
    public static final String SEARCH_LINEUP_MATCH_URL =GROND_SERVER_URL+"/lineup/match";
    public void getNetworkLineupMatch(final Context context,  int matchId, final OnResultListener<LineupMatch> listener) {

        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);

        client.get(context,SEARCH_LINEUP_MATCH_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupMatch items = gson.fromJson(s, LineupMatch.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);
            }
        });

    }
    //Lineup planLoc
    public static final String SEARCH_LINEUP_PLANLOC_URL =GROND_SERVER_URL+"/lineup/planLoc";
    public void getNetworkLineupPlanLoc(final Context context,  int matchId, final OnResultListener<LineupPlanLoc> listener) {


        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);

        client.get(context,SEARCH_LINEUP_PLANLOC_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupPlanLoc items = gson.fromJson(s, LineupPlanLoc.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }


        });

    }

    //Lineup virtual res
    public static final String SEARCH_LINEUP_VIRTUAL_RES_URL =GROND_SERVER_URL+"/lineup/virtual/res";
    public void getNetworkLineupVirtualRes(final Context context,int clubId,  int matchId, final OnResultListener<LineupVirtualRes> listener) {


        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);
        params.put("club_id",clubId);

        client.get(context,SEARCH_LINEUP_VIRTUAL_RES_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupVirtualRes items = gson.fromJson(s, LineupVirtualRes.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }


        });

    }

    //Lineup virtual formation
    public static final String SEARCH_LINEUP_VIRTUAL_FOMATION_URL =GROND_SERVER_URL+"/lineup/virtual/formation";
    public void getNetworkLineupVirtualFomation(final Context context,int clubId,  int matchId, final OnResultListener<LineupVirtualFomation> listener) {


        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);
        params.put("club_id",clubId);

        client.get(context,SEARCH_LINEUP_VIRTUAL_FOMATION_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                LineupVirtualFomation items = gson.fromJson(s, LineupVirtualFomation.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
                Log.d("hello", "가상 lineup 에러 " +statusCode);
            }

        });

    }
    //Lineup Result
    public static final String SEARCH_LINEUP_RESULT_URL =GROND_SERVER_URL+"/lineup/result/result";
    public void getNetworkLineupResult(final Context context,  int matchId, final OnResultListener<LineupResult> listener) {


        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);

        client.get(context,SEARCH_LINEUP_RESULT_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupResult items = gson.fromJson(s, LineupResult.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }


        });

    }
    //Lineup Scorer
    public static final String SEARCH_LINEUP_SCORER_URL =GROND_SERVER_URL+"/match/Scorer";
    public void getNetworkLineupScorer(final Context context,int clubId,  int matchId, final OnResultListener<LineupScorer> listener) {

        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);
        params.put("club_id",clubId);

        client.get(context,SEARCH_LINEUP_SCORER_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupScorer items = gson.fromJson(s, LineupScorer.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                listener.onFail(statusCode);
            }


        });

    }
    //signup
    public static final String SEND_SIGNUP_URL =GROND_SERVER_URL+"/member";
    public void postNetworkSignup(final Context context , UserProfile mUserProfile,  final OnResultListener<SignupData> listener) {

        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        try {
            if(mUserProfile.mFile != null){
                params.put("file", mUserProfile.mFile);
            }else{
                params.put("file", new File(""));
            }

          } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("gender", mUserProfile.gender);
        params.put("age", mUserProfile.age);
        params.put("memLocationName", mUserProfile.memLocationName);
        params.put("memMainDay_Mon", mUserProfile.memMainDay_Mon);
        params.put("memMainDay_Tue", mUserProfile.memMainDay_Tue);
        params.put("memMainDay_Wed",mUserProfile.memMainDay_Wed);
        params.put("memMainDay_Thu", mUserProfile.memMainDay_Thu);
        params.put("memMainDay_Fri", mUserProfile.memMainDay_Fri);
        params.put("memMainDay_Sat", mUserProfile.memMainDay_Sat);
        params.put("memMainDay_Sun",mUserProfile.memMainDay_Sun);
        params.put("memIntro", mUserProfile.memIntro);
        params.put("position", mUserProfile.position);
//        Log.d("hello", "position : " + mUserProfile.position);
        params.put("skill", mUserProfile.skill);
//        Log.d("hello", "skill : " + mUserProfile.skill);
        params.put("latitude", mUserProfile.latitude);
//        Log.d("hello", "latitude : " + mUserProfile.latitude);
        params.put("longitude", mUserProfile.longitude);
        params.put("updateYN", mUserProfile.updateYN);
//        Log.d("hello", "longitude : " + mUserProfile.longitude);

//        params.setForceMultipartEntityContentType(true);
//        Header[] headers =new Header[2];
//        headers[0] = new BasicHeader("Connection", "Keep-Alive" );
//        headers[1] = new BasicHeader("Content-Type", "multipart/form-data" );
        params.setForceMultipartEntityContentType(true);
        client.post(context, SEND_SIGNUP_URL, params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", "sign up : " + s);
                SignupData items = gson.fromJson(s, SignupData.class);
                if (items != null){
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }



        });


    }

    //login kakao
    public static final String SEND_LOGIN_KAKAO_URL =GROND_SERVER_URL+"/member/login/kakao";
    public void postNetworkLoginKakao(final Context context, String token , final OnResultListener<KakaoLogin> listener) {
//        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
           client.post(context, SEND_LOGIN_KAKAO_URL, params, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    unShowWaitingDialog();
                   ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                   String s = new String(responseBody, Charset.forName("UTF-8"));
                   Log.d("hello", "kakao post : " + s);
                   KakaoLogin items = gson.fromJson(s, KakaoLogin.class);
                   if (items != null){
                       listener.onSuccess(items);
                   }
               }

               @Override
               public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    unShowWaitingDialog();
                   Log.d("hello", "status code : " + statusCode);
                   listener.onFail(statusCode);
               }



           });

    }
    //login kakao after token send
    public void getNetworkLoginKakao(final Context context, String token , final OnResultListener<KakaoResponse> listener) {
//        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
//        params.put("access_token",token);
        client.get(context, SEND_LOGIN_KAKAO_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", "kakao get : " + s);
                KakaoResponse items = gson.fromJson(s, KakaoResponse.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //login facebook
    public static final String SEND_LOGIN_FACEBOOK_URL =GROND_SERVER_URL+"/member/login/facebook";
    public void postNetworkLoginFacebook(final Context context, String token, final OnResultListener<FacebookLogin> listener) {

//        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
        client.post(context, SEND_LOGIN_FACEBOOK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                FacebookLogin items = gson.fromJson(s, FacebookLogin.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                unShowWaitingDialog();
                Log.d("hello", "facebook error status : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //make fc club
    public static final String SEND_CLUB_CREATE_URL =GROND_SERVER_URL+"/club/make";
    public void postNetworkMakeClub(final Context context , ClubProfile mClubProfile,  final OnResultListener<EtcData> listener) {


        final RequestParams params = new RequestParams();
        try {
            params.put("file", mClubProfile.mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("clubName",mClubProfile.clubName);
        params.put("clubLocationName",mClubProfile.clubLocationName);
        params.put("clubMainDay_Mon",mClubProfile.clubMainDay_Mon);
        params.put("clubMainDay_Tue",mClubProfile.clubMainDay_Tue);
        params.put("clubMainDay_Wed",mClubProfile.clubMainDay_Wed);
        params.put("clubMainDay_Thu",mClubProfile.clubMainDay_Thu);
        params.put("clubMainDay_Fri",mClubProfile.clubMainDay_Fri);
        params.put("clubMainDay_Sat",mClubProfile.clubMainDay_Sat);
        params.put("clubMainDay_Sun",mClubProfile.clubMainDay_Sun);
        params.put("memYN",mClubProfile.memYN);
        params.put("fieldYN",mClubProfile.fieldYN);
        params.put("clubField",mClubProfile.clubField);
        params.put("latitude",mClubProfile.latitude);
        params.put("longitude",mClubProfile.longitude);
        params.put("matchYN",mClubProfile.matchYN);
        params.put("clubIntro",mClubProfile.clubIntro);
//        params.setContentEncoding("multipart/form-data");
        params.setForceMultipartEntityContentType(true);

        client.post(context, SEND_CLUB_CREATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //update fc club
    public static final String SEND_CLUB_UPDATE_URL =GROND_SERVER_URL+"/club";
    public void postNetworkUpdateClub(final Context context , ClubProfileUpdate mClubProfileUpdate,  final OnResultListener<EtcData> listener) {

        final RequestParams params = new RequestParams();
        try {
            params.put("file", mClubProfileUpdate.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("club_id",mClubProfileUpdate.club_id);
        params.put("clubLocationName",mClubProfileUpdate.clubLocationName);
        params.put("clubMainDay_Mon",mClubProfileUpdate.clubMainDay_Mon);
        params.put("clubMainDay_Tue",mClubProfileUpdate.clubMainDay_Tue);
        params.put("clubMainDay_Wed",mClubProfileUpdate.clubMainDay_Wed);
        params.put("clubMainDay_Thu",mClubProfileUpdate.clubMainDay_Thu);
        params.put("clubMainDay_Fri",mClubProfileUpdate.clubMainDay_Fri);
        params.put("clubMainDay_Sat",mClubProfileUpdate.clubMainDay_Sat);
        params.put("clubMainDay_Sun",mClubProfileUpdate.clubMainDay_Sun);
        params.put("memYN",mClubProfileUpdate.memYN);
        params.put("fieldYN",mClubProfileUpdate.fieldYN);
        params.put("clubField",mClubProfileUpdate.clubField);
        params.put("latitude",mClubProfileUpdate.latitude);
        params.put("longitude",mClubProfileUpdate.longitude);
        params.put("matchYN",mClubProfileUpdate.matchYN);
        params.put("clubIntro", mClubProfileUpdate.clubIntro);
        params.setForceMultipartEntityContentType(true);

        client.post(context, SEND_CLUB_UPDATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //my message
    public static final String SEND_MY_MESSAGE_URL =GROND_SERVER_URL+"/message/member";
    public void getNetworkMessage(final Context context, int member_id, int page,  final OnResultListener<MyMessageData> listener ){


        final RequestParams params = new RequestParams();
        params.put("member_id",member_id);
        params.put("page",page);

        client.get(context, SEND_MY_MESSAGE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", "match id 확인용 :"+s);
                MyMessageData items = gson.fromJson(s, MyMessageData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //club message
    public static final String SEND_CLUB_MESSAGE_URL =GROND_SERVER_URL+"/message/club";
    public void getNetworkClubMessage(final Context context, int club_id, int page,  final OnResultListener<ClubMessageData> listener ){

        final RequestParams params = new RequestParams();
        params.put("club_id",club_id);
        params.put("page", page);

        client.get(context, SEND_CLUB_MESSAGE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                ClubMessageData items = gson.fromJson(s, ClubMessageData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }
        });

    }
    //match result postion fomation
    public static final String SEND_LINEUP_RESULT_FORMATION_URL =GROND_SERVER_URL+"/lineup/result/formation";
    public void getNetworkLineupResultFormation(final Context context, int match_id, int club_id,  final OnResultListener<MatchFormation> listener ){


        final RequestParams params = new RequestParams();
        params.put("club_id",club_id);
        params.put("match_id", match_id);

        client.get(context, SEND_LINEUP_RESULT_FORMATION_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                MatchFormation items = gson.fromJson(s, MatchFormation.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }

        });

    }

    //message noti
    public static final String GET_MESSAGE_NOTI_URL =GROND_SERVER_URL+"/message/noti";
    public void getNetworkNoti(final Context context, int member_id, int page,  final OnResultListener<NotiData> listener ){


        final RequestParams params = new RequestParams();
        params.put("member_id",member_id);
        params.put("page",page);

        client.get(context, GET_MESSAGE_NOTI_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                NotiData items = gson.fromJson(s, NotiData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }


        });

    }

    // SEND_MESSAGE_401_URL
    public static final String SEND_MESSAGE_401_URL =GROND_SERVER_URL+"/message/401";
    public void postNetworkMessage401(final Context context , Push401 mPush401,  final OnResultListener<EtcData> listener) {

        final RequestParams params = new RequestParams();
        params.put("sender_id",mPush401.sender_id);
        params.put("collectorClub_id",mPush401.collectorClub_id);
        params.put("matchDate",mPush401.matchDate);
        params.put("startTime",mPush401.startTime);
        params.put("endTime",mPush401.endTime);
        params.put("matchLocation",mPush401.matchLocation);
        params.put("etc",mPush401.etc);

        client.post(context, SEND_MESSAGE_401_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push401
    public static final String SEND_PUSH_401_URL =GROND_SERVER_URL+"/push/401";
    public void postNetworkPush401(final Context context , Push401 mPush401,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("sender_id",mPush401.sender_id);
        params.put("collectorClub_id",mPush401.collectorClub_id);
        params.put("matchDate",mPush401.matchDate);
        params.put("startTime", mPush401.startTime);
        params.put("endTime",mPush401.endTime);
        params.put("matchLocation",mPush401.matchLocation);
        params.put("etc",mPush401.etc);

        client.post(context, SEND_PUSH_401_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // gcm registration id token
    public static final String SEND_MEMBER_PUSH_URL =GROND_SERVER_URL+"/member/push";
    public void postNetworkMemberPush(final Context context , String device_id, String token,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("device_id",device_id);
        params.put("os","Android");
        params.put("token",token);

        client.post(context, SEND_MEMBER_PUSH_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    // message100
    public static final String SEND_MESSAGE_100_URL =GROND_SERVER_URL+"/message/100";
    public void postNetworkMessage100(final Context context , Push100 mPush100,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush100.member_id);
        params.put("sender_id",mPush100.sender_id);
        params.put("collector_id",mPush100.collector_id);
        params.put("contents",mPush100.contents);

        client.post(context, SEND_MESSAGE_100_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push100
    public static final String SEND_PUSH_100_URL =GROND_SERVER_URL+"/push/100";
    public void postNetworkPush100(final Context context , Push100 mPush100,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush100.member_id);
        params.put("sender_id",mPush100.sender_id);
        params.put("collector_id",mPush100.collector_id);
        params.put("contents",mPush100.contents);

        client.post(context, SEND_PUSH_100_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message200
    public static final String SEND_MESSAGE_200_URL =GROND_SERVER_URL+"/message/200";
    public void postNetworkMessage200(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
        params.put("contents",mPush200.contents);

        client.post(context, SEND_MESSAGE_200_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push200
    public static final String SEND_PUSH_200_URL =GROND_SERVER_URL+"/push/200";
    public void postNetworkPush200(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
        params.put("contents",mPush200.contents);

        client.post(context, SEND_PUSH_200_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message201 가입신청
    public static final String SEND_MESSAGE_201_URL =GROND_SERVER_URL+"/message/201";
    public void postNetworkMessage201(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
//        params.put("contents",mPush200.contents);

        client.post(context, SEND_MESSAGE_201_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push201
    public static final String SEND_PUSH_201_URL =GROND_SERVER_URL+"/push/201";
    public void postNetworkPush201(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
//        params.put("contents",mPush200.contents);

        client.post(context, SEND_PUSH_201_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message201 respeonse
    public static final String SEND_MESSAGE_201_RESPEONSE_URL =GROND_SERVER_URL+"/message/201/response";
    public void postNetworkMessage201Response(final Context context , Push201Response mPush201Response,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush201Response.member_id);
        params.put("sender_id", mPush201Response.sender_id);
        params.put("club_id",mPush201Response.club_id);
        params.put("accRej",mPush201Response.accRej);
        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_MESSAGE_201_RESPEONSE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message202 302번 거절
    public static final String SEND_MESSAGE_202_URL =GROND_SERVER_URL+"/message/202";
    public void postNetworkMessage202(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
        params.put("message_id",mPush202.message_id);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_MESSAGE_202_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push202 302번 거절
    public static final String SEND_PUSH_202_URL =GROND_SERVER_URL+"/push/202";
    public void postNetworkPush202(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_PUSH_202_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message203 301번 수락
    public static final String SEND_MESSAGE_203_URL =GROND_SERVER_URL+"/message/203";
    public void postNetworkMessage203(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
        params.put("match_id",mPush202.match_id);
        params.put("message_id",mPush202.message_id);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_MESSAGE_203_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push203 301번 수락
    public static final String SEND_PUSH_203_URL =GROND_SERVER_URL+"/push/203";
    public void postNetworkPush203(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
        params.put("match_id",mPush202.match_id);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_PUSH_203_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message204 302번 거절
    public static final String SEND_MESSAGE_204_URL =GROND_SERVER_URL+"/message/204";
    public void postNetworkMessage204(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
        params.put("match_id",mPush202.match_id);
        params.put("message_id",mPush202.message_id);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_MESSAGE_204_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push204 302번 거절
    public static final String SEND_PUSH_204_URL =GROND_SERVER_URL+"/push/204";
    public void postNetworkPush204(final Context context , Push202 mPush202,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush202.member_id);
        params.put("collectorClub_id", mPush202.collectorClub_id);
        params.put("accRej",mPush202.accRej);
        params.put("match_id",mPush202.match_id);
//        params.put("accRej",mPush201Response.accRej);
//        params.put("message_id",mPush201Response.message_id);

        client.post(context, SEND_PUSH_204_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 300
    public static final String SEND_MESSAGE_300_URL =GROND_SERVER_URL+"/message/300";
    public void postNetworkMessage300(final Context context , Push300 mPush300,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush300.member_id);
        params.put("sender_id",mPush300.sender_id);
        params.put("collector_id",mPush300.collector_id);
        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_300_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 300
    public static final String SEND_PUSH_300_URL =GROND_SERVER_URL+"/push/300";
    public void postNetworkPush300(final Context context , Push300 mPush300,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush300.member_id);
        params.put("sender_id",mPush300.sender_id);
        params.put("collector_id",mPush300.collector_id);
        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_300_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message301 경기일정(FC - >소속회원)
    public static final String SEND_MESSAGE_301_URL =GROND_SERVER_URL+"/message/301";
    public void postNetworkMessage301(final Context context , Push301 mPush301,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("home_id",mPush301.home_id);
        params.put("away_id",mPush301.away_id);
        params.put("match_id",mPush301.match_id);
        params.put("message_id",mPush301.message_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_301_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
     // push301 경기일정(FC - >소속회원)
    public static final String SEND_PUSH_301_URL =GROND_SERVER_URL+"/push/301";
    public void postNetworkPush301(final Context context , Push301 mPush301,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("home_id",mPush301.home_id);
        params.put("away_id",mPush301.away_id);
        params.put("match_id",mPush301.match_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_301_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message303 201 수락(가입신청)
    public static final String SEND_MESSAGE_303_URL =GROND_SERVER_URL+"/message/303";
    public void postNetworkMessage303(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id", mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
        params.put("sendered_id",mPush303.sendered_id);
        params.put("message_id",mPush303.message_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_303_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push303 201 수락(가입신청)
    public static final String SEND_PUSH_303_URL =GROND_SERVER_URL+"/push/303";
    public void postNetworkPush303(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id",mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
        params.put("sendered_id",mPush303.sendered_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_303_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message304 201 거절(가입신청)
    public static final String SEND_MESSAGE_304_URL =GROND_SERVER_URL+"/message/304";
    public void postNetworkMessage304(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id",mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
        params.put("sendered_id",mPush303.sendered_id);
        params.put("message_id",mPush303.message_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_304_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push304 201 거절(가입신청)
    public static final String SEND_PUSH_304_URL =GROND_SERVER_URL+"/push/304";
    public void postNetworkPush304(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id",mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
        params.put("sendered_id",mPush303.sendered_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_304_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message305 302 가입권유
    public static final String SEND_MESSAGE_305_URL =GROND_SERVER_URL+"/message/305";
    public void postNetworkMessage305(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id",mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
//        params.put("sendered_id",mPush303.sendered_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_305_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push305 302 가입권유
    public static final String SEND_PUSH_305_URL =GROND_SERVER_URL+"/push/305";
    public void postNetworkPush305(final Context context , Push303 mPush303,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush303.member_id);
        params.put("senderClub_id",mPush303.senderClub_id);
        params.put("accRej",mPush303.accRej);
//        params.put("sendered_id",mPush303.sendered_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_305_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message400
    public static final String SEND_MESSAGE_400_URL =GROND_SERVER_URL+"/message/400";
    public void postNetworkMessage400(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
        params.put("contents",mPush200.contents);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_400_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push400
    public static final String SEND_PUSH_400_URL =GROND_SERVER_URL+"/push/400";
    public void postNetworkPush400(final Context context , Push200 mPush200,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush200.member_id);
        params.put("sender_id",mPush200.sender_id);
        params.put("collectorClub_id",mPush200.collectorClub_id);
        params.put("contents",mPush200.contents);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_400_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message302 가입권유
    public static final String SEND_MESSAGE_302_URL =GROND_SERVER_URL+"/message/302";
    public void postNetworkMessage302(final Context context , Push302 mPush302,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush302.member_id);
        params.put("sender_id",mPush302.sender_id);
        params.put("collector_id",mPush302.collector_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_302_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push302 가입권유
    public static final String SEND_PUSH_302_URL =GROND_SERVER_URL+"/push/302";
    public void postNetworkPush302(final Context context , Push302 mPush302,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush302.member_id);
        params.put("sender_id",mPush302.sender_id);
        params.put("collector_id",mPush302.collector_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_302_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push301 response 매치 참석 승낙
    public static final String SEND_MESSAGE_301_RESPONSE_URL =GROND_SERVER_URL+"/message/301/response";
    public void postNetworkMessage301Response(final Context context , Push301Response mPush301Response,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush301Response.member_id);
        params.put("club_id",mPush301Response.club_id);
        params.put("match_id",mPush301Response.match_id);
        params.put("accRej",mPush301Response.accRej);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_301_RESPONSE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 306
    public static final String SEND_MESSAGE_306_URL =GROND_SERVER_URL+"/message/306";
    public void postNetworkMessage306(final Context context , Push301Response mPush301Response,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush301Response.member_id);
        params.put("sender_id",mPush301Response.sender_id);
        params.put("collector_id",mPush301Response.collector_id);

        client.post(context, SEND_MESSAGE_306_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 306
    public static final String SEND_PUSH_306_URL =GROND_SERVER_URL+"/push/306";
    public void postNetworkPush306(final Context context , Push301Response mPush301Response,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush301Response.member_id);
        params.put("sender_id",mPush301Response.sender_id);
        params.put("collector_id",mPush301Response.collector_id);

        client.post(context, SEND_PUSH_306_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    // match create 401 매치신청 승락
    public static final String SEND_MATCH_CREATE_URL =GROND_SERVER_URL+"/match/create";
    public void postNetworkMatchCreate(final Context context , MatchCreateData mMatchCreateData,  final OnResultListener<MatchCreateDataResponse> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mMatchCreateData.member_id);
        params.put("home_id",mMatchCreateData.home_id);
        params.put("away_id", mMatchCreateData.away_id);
        params.put("matchDate", mMatchCreateData.matchDate);
        params.put("startTime",mMatchCreateData.startTime);
        params.put("endTime",mMatchCreateData.endTime);
        params.put("matchLocation",mMatchCreateData.matchLocation);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MATCH_CREATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                MatchCreateDataResponse items = gson.fromJson(s, MatchCreateDataResponse.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // messaage 402 401 매치신청 거절
    public static final String SEND_MESSAGE_402_URL =GROND_SERVER_URL+"/message/402";
    public void postNetworkMessage402(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("matchDate",mPush402.matchDate);
        params.put("startTime",mPush402.startTime);
        params.put("endTime",mPush402.endTime);
        params.put("message_id",mPush402.message_id);
//        params.put("home_id",mMatchCreateData.home_id);
//        params.put("away_id", mMatchCreateData.away_id);
//        params.put("matchDate", mMatchCreateData.matchDate);
//        params.put("startTime",mMatchCreateData.startTime);
//        params.put("endTime",mMatchCreateData.endTime);
//        params.put("matchLocation",mMatchCreateData.matchLocation);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_402_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 402 401 매치신청 거절
    public static final String SEND_PUSH_402_URL =GROND_SERVER_URL+"/push/402";
    public void postNetworkPush402(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id", mPush402.collectorClub_id);
        params.put("matchDate",mPush402.matchDate);
        params.put("startTime",mPush402.startTime);
        params.put("endTime",mPush402.endTime);
//        params.put("home_id",mMatchCreateData.home_id);
//        params.put("away_id", mMatchCreateData.away_id);
//        params.put("matchDate", mMatchCreateData.matchDate);
//        params.put("startTime",mMatchCreateData.startTime);
//        params.put("endTime",mMatchCreateData.endTime);
//        params.put("matchLocation",mMatchCreateData.matchLocation);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_PUSH_402_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //// TODO: 2015-12-08
    // message 403 정정신청
    public static final String SEND_MESSAGE_403_URL =GROND_SERVER_URL+"/message/403";
    public void postNetworkMessage403(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_MESSAGE_403_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 403 정정신청
    public static final String SEND_PUSH_403_URL =GROND_SERVER_URL+"/push/403";
    public void postNetworkPush403(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_PUSH_403_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 404 정정신청(403) 거절
    public static final String SEND_MESSAGE_404_URL =GROND_SERVER_URL+"/message/404";
    public void postNetworkMessage404(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_MESSAGE_404_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 404 정정신청(403) 거절
    public static final String SEND_PUSH_404_URL =GROND_SERVER_URL+"/push/404";
    public void postNetworkPush404(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_PUSH_404_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 405 정정신청(403) 수락
    public static final String SEND_MESSAGE_405_URL =GROND_SERVER_URL+"/message/405";
    public void postNetworkMessage405(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id",mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_MESSAGE_405_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 405 정정신청(403) 수락
    public static final String SEND_PUSH_405_URL =GROND_SERVER_URL+"/push/405";
    public void postNetworkPush405(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush402.member_id);
        params.put("sender_id",mPush402.sender_id);
        params.put("collectorClub_id", mPush402.collectorClub_id);
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_PUSH_405_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 502
    public static final String SEND_MESSAGE_502_URL =GROND_SERVER_URL+"/message/502";
    public void postNetworkMessage502(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_MESSAGE_502_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 502
    public static final String SEND_PUSH_502_URL =GROND_SERVER_URL+"/push/502";
    public void postNetworkPush502(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_PUSH_502_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // message 504
    public static final String SEND_MESSAGE_504_URL =GROND_SERVER_URL+"/message/504";
    public void postNetworkMessage504(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_MESSAGE_504_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // push 504
    public static final String SEND_PUSHE_504_URL =GROND_SERVER_URL+"/push/504";
    public void postNetworkPush504(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mPush402.match_id);


        client.post(context, SEND_PUSHE_504_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }


    // match reInsert
    public static final String SEND_MATCH_REINSERT_URL =GROND_SERVER_URL+"/match/reInsert";
    public void postNetworkMatchReInsert(final Context context , Push402 mPush402,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mPush402.match_id);
        params.put("member_id", mPush402.member_id);
        params.put("club_id",mPush402.club_id);

        client.post(context, SEND_MATCH_REINSERT_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

//=================================================================================================================================
    // club  manager 임명
    public static final String SEND_CLUB_MANAGER_URL =GROND_SERVER_URL+"/club/manager";
    public void postNetworkClubManager(final Context context , ClubManagerData mClubManagerData,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mClubManagerData.member_id);
        params.put("club_id",mClubManagerData.club_id);
        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_CLUB_MANAGER_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // club  manager 박탈
    public static final String SEND_CLUB_MANAGER_STOP_URL =GROND_SERVER_URL+"/club/manager/stop";
    public void postNetworkClubManagerStop(final Context context , ClubManagerData mClubManagerData,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mClubManagerData.member_id);
        params.put("club_id",mClubManagerData.club_id);

        client.post(context, SEND_CLUB_MANAGER_STOP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
                try{
                    String s = new String(responseBody, Charset.forName("UTF-8"));
                    Log.d("hello", s);
                    EtcData items = gson.fromJson(s, EtcData.class);
                    if (items != null) {
                        listener.onSuccess(items);
                    }
                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }


        });

    }
    // 선수 삭제
    public static final String SEND_MEMBER_STOP_URL =GROND_SERVER_URL+"/member/delete";
    public void postNetworkMemberStop(final Context context,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
//        params.put("member_id",mClubManagerData.member_id);
//        params.put("club_id",mClubManagerData.club_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MEMBER_STOP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // 선수 추방
    public static final String SEND_CLUB_MEMBER_DROP_URL =GROND_SERVER_URL+"/club/memDrop";
    public void postNetworkClubMemberDrop(final Context context, ClubManagerData mClubManagerData,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mClubManagerData.member_id);
        params.put("club_id",mClubManagerData.club_id);
//        params.put("manager_id",mClubManagerData.manager_id);
        params.put("manager_id",mClubManagerData.drop_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_CLUB_MEMBER_DROP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    // 클럽 탈퇴
    public static final String SEND_CLUB_DROPOUT_URL =GROND_SERVER_URL+"/club/dropOut";
    public void postNetworkClubDropOut(final Context context,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",PropertyManager.getInstance().getUserId());
        params.put("club_id",PropertyManager.getInstance().getMyPageResult().club_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_CLUB_DROPOUT_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    // 로그아웃
    public static final String SEND_MEMBER_LOGOUT_URL = GROND_SERVER_URL+"/member/logout";
    public void postNetworkMemberLogout(final Context context,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",PropertyManager.getInstance().getUserId());
        params.put("device_id",PropertyManager.getInstance().getDeviceId());
        params.put("os","Android");
//        params.put("club_id",PropertyManager.getInstance().getMyPageResult().club_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MEMBER_LOGOUT_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //메시지 삭제
    public static final String SEND_MESSAGE_DELETE_URL =GROND_SERVER_URL+"/message/delete";
    public void postNetworkMessageDelete(final Context context,  MessageDeleteData mMessageDeleteData,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mMessageDeleteData.member_id);
        params.put("message_id",mMessageDeleteData.message_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_DELETE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //본 메시지 설정 , 읽음, 읽은 메시지
    public static final String SEND_MESSAGE_WATCH_URL =GROND_SERVER_URL+"/message/watch";
    public void postNetworkMessageWatch(final Context context,  MessageDeleteData mMessageDeleteData,final OnResultListener<EtcData> listener) {
//        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mMessageDeleteData.member_id);
        params.put("message_id",mMessageDeleteData.message_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_WATCH_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                mDialogHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        unShowWaitingDialog();
//                    }
//                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                mDialogHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        unShowWaitingDialog();
//                    }
//                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //메시지 302, 푸쉬 push message 302 response
    public static final String SEND_MESSAGE_302_RESPONSE_URL =GROND_SERVER_URL+"/message/302/response";
    public void postNetworkMessage302Response(final Context context,  Push302Response mPush302Response,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush302Response.member_id);
        params.put("club_id",mPush302Response.club_id);
        params.put("accRej",mPush302Response.accRej);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_302_RESPONSE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }


    //라인업 작전 입력
    public static final String SEND_LINEUP_PLAN_URL =GROND_SERVER_URL+"/lineup/plan";
    public void postNetworkLineupPlan(final Context context,  LineupPlan mLineupPlan,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mLineupPlan.member_id);
        params.put("club_id", mLineupPlan.club_id);
        params.put("match_id",mLineupPlan.match_id);
        params.put("plan",mLineupPlan.plan);
        params.put("homeAway", mLineupPlan.homeAway);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_LINEUP_PLAN_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //가상 라인업 포메이션 입력 실제 선수 이용
    public static final String SEND_LINEUP_VIRTUAL_FORMATION_REAL_URL =GROND_SERVER_URL+"/lineup/virtual/formation/real";
    public void postNetworkLineupVirtualFormationReal(final Context context,  LineupVirtualFomationPost mLineupVirtualFomationPost,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mLineupVirtualFomationPost.member_id);
        params.put("club_id",mLineupVirtualFomationPost.club_id);
        params.put("match_id",mLineupVirtualFomationPost.match_id);
        params.put("locMemInfo",mLineupVirtualFomationPost.itemslocMemInfo);
//        params.put("contents",mPush300.contents);



        String bodyAsJson = gson.toJson(mLineupVirtualFomationPost);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson,"UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_VIRTUAL_FORMATION_REAL_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //가상 라인업 포메이션 입력 가상 선수 이용
    public static final String SEND_LINEUP_VIRTUAL_FORMATION_VIR_URL =GROND_SERVER_URL+"/lineup/virtual/formation/vir";
    public void postNetworkLineupVirtualFormationVir(final Context context,  LineupVirtualFomationPost mLineupVirtualFomationPost,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mLineupVirtualFomationPost.member_id);
        params.put("club_id",mLineupVirtualFomationPost.club_id);
        params.put("match_id",mLineupVirtualFomationPost.match_id);
        params.put("locVirInfo", mLineupVirtualFomationPost.itemslocVirInfo);

//        params.put("contents",mPush300.contents);

        String bodyAsJson = gson.toJson(mLineupVirtualFomationPost);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson, "UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_VIRTUAL_FORMATION_VIR_URL, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //실제 라인업 포메이션 입력 실제 선수 이용
    public static final String SEND_LINEUP_INPUTRESULT_FORMATION_REAL_URL =GROND_SERVER_URL+"/lineup/inputResult/formation/real";
    public void postNetworkLineupInputResultFormationReal(final Context context,  LineupVirtualFomationPost mLineupVirtualFomationPost,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mLineupVirtualFomationPost.member_id);
        params.put("club_id",mLineupVirtualFomationPost.club_id);
        params.put("match_id",mLineupVirtualFomationPost.match_id);
        params.put("locMemInfo",mLineupVirtualFomationPost.itemslocMemInfo);

//        params.put("contents",mPush300.contents);


        String bodyAsJson = gson.toJson(mLineupVirtualFomationPost);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson, "UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_INPUTRESULT_FORMATION_REAL_URL, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //실제 라인업 포메이션 입력 가상 선수 이용
    public static final String SEND_LINEUP_INPUTRESULT_FORMATION_VIR_URL =GROND_SERVER_URL+"/lineup/inputResult/formation/vir";
    public void postNetworkLineupInputResultFormationVir(final Context context,  LineupVirtualFomationPost mLineupVirtualFomationPost,final OnResultListener<EtcData> listener) throws UnsupportedEncodingException {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mLineupVirtualFomationPost.member_id);
        params.put("club_id",mLineupVirtualFomationPost.club_id);
        params.put("match_id",mLineupVirtualFomationPost.match_id);
        params.put("locVirInfo",mLineupVirtualFomationPost.itemslocVirInfo);

//        params.put("contents",mPush300.contents);
        String bodyAsJson = gson.toJson(mLineupVirtualFomationPost);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson, "UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_INPUTRESULT_FORMATION_VIR_URL, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //양팀 점수, 매너, 스킬 입력
    public static final String SEND_LINEUP_SCORE_MANNER_SKILL_URL =GROND_SERVER_URL+"/lineup/inputResult/scoreMannerSkill";
    public void postNetworkLineupInputResultScoreMannerSkill(final Context context,  ScoreMannerSkill mScoreMannerSkill,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mScoreMannerSkill.match_id);
        params.put("club_id",mScoreMannerSkill.club_id);
        params.put("homeAway", mScoreMannerSkill.homeAway);
        params.put("score", mScoreMannerSkill.score);
        params.put("skill",mScoreMannerSkill.skill);
        params.put("manner",mScoreMannerSkill.manner);
        params.put("member_id", mScoreMannerSkill.member_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_LINEUP_SCORE_MANNER_SKILL_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }
    //득점자 입력 실제
    public static final String SEND_LINEUP_INPUTRESULT_SCORER_REAL_URL =GROND_SERVER_URL+"/lineup/inputResult/scorer/real";
    public void postNetworkLineupInputResultScorerReal(final Context context,  InputResultScorerReal mInputResultScorerReal,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mInputResultScorerReal.match_id);
        params.put("club_id",mInputResultScorerReal.club_id);
        params.put("member_id",mInputResultScorerReal.member_id);
        params.put("realScorer",mInputResultScorerReal.itemsrealScorer);

//        params.put("contents",mPush300.contents);

        String bodyAsJson = gson.toJson(mInputResultScorerReal);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson, "UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_INPUTRESULT_SCORER_REAL_URL, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }
        });

    }
    //득점자 입력 가상
    public static final String SEND_LINEUP_INPUTRESULT_SCORER_VIR_URL =GROND_SERVER_URL+"/lineup/inputResult/scorer/vir";
    public void postNetworkLineupInputResultScorerVir(final Context context,  InputResultScorerReal mInputResultScorerReal,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mInputResultScorerReal.match_id);
        params.put("club_id",mInputResultScorerReal.club_id);
        params.put("member_id",mInputResultScorerReal.member_id);
        params.put("virScorer",mInputResultScorerReal.itemsvirScorer);

//        params.put("contents",mPush300.contents);


        String bodyAsJson = gson.toJson(mInputResultScorerReal);
        StringEntity entity  = null;
        entity = new StringEntity(bodyAsJson, "UTF-8");

        Log.d("hello", entity.toString());
        client.post(context, SEND_LINEUP_INPUTRESULT_SCORER_VIR_URL, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });

                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mDialogHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unShowWaitingDialog();
                    }
                });
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }
        });

    }
    //mvp 입력
    public static final String SEND_LINEUP_INPUTRESULT_MVP_URL =GROND_SERVER_URL+"/lineup/inputResult/mvp";
    public void postNetworkLineupInputResultMVP(final Context context,  InputResultMVP mInputResultMVP,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mInputResultMVP.match_id);
        params.put("club_id",mInputResultMVP.club_id);
        params.put("member_id",mInputResultMVP.member_id);
        params.put("mvp",mInputResultMVP.mvp);
        params.put("homeAway",mInputResultMVP.homeAway);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_LINEUP_INPUTRESULT_MVP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //결과 입력 정리
    Push402 mPush402;
    public static final String SEND_LINEUP_INPUTRESULT_SETTING_URL =GROND_SERVER_URL+"/lineup/inputResult/setting";
    public static final String SEND_LINEUP_INPUTRESULT_SETTING_URL2 =GROND_SERVER_URL+"/lineup/inputResult/insertResultYN_2";
    public void postNetworkLineupInputResultSetting(final Context context,  final InputResultSetting mInputResultSetting,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mInputResultSetting.match_id);
        params.put("club_id",mInputResultSetting.club_id);
        params.put("member_id",mInputResultSetting.member_id);
        params.put("homeAway",mInputResultSetting.homeAway);
        params.put("accRej",mInputResultSetting.accRej);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_LINEUP_INPUTRESULT_SETTING_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
                if(items.state ==2){
                    Log.d("hello", "/setting state 2");
                    client.post(context, SEND_LINEUP_INPUTRESULT_SETTING_URL2, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                            String s = new String(responseBody, Charset.forName("UTF-8"));
                            Log.d("hello", s);
                            EtcData items = gson.fromJson(s, EtcData.class);
                            if (items != null) {
                                listener.onSuccess(items);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("hello", "status code : " + statusCode);
                            listener.onFail(statusCode);
                        }
                    });
                }else if(items.state ==3){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", "error in setting :  " + s);


                listener.onFail(statusCode);
            }


        });

    }
    public static final String SEND_LINEUP_INPUTRESULT_SETTING_URL3 =GROND_SERVER_URL+"/lineup/inputResult/insertResultYN_3";
    public void postNetworkInsertResultYN_3(final Context context, final InputResultSetting mInputResultSetting,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("match_id",mInputResultSetting.match_id);
        params.put("club_id", mInputResultSetting.club_id);
        params.put("member_id",mInputResultSetting.member_id);
        params.put("homeAway",mInputResultSetting.homeAway);
        // accRej 0 거절 , 1 수락
        params.put("accRej",mInputResultSetting.accRej);
        params.put("message_id",mInputResultSetting.message_id);
        // 0 이면 처음, 1이면 재입력
        params.put("checkInsert",mInputResultSetting.checkInsert);

//        params.put("contents",mPush300.contents);

        Log.d("hello", "/setting state 3");
        client.post(context, SEND_LINEUP_INPUTRESULT_SETTING_URL3, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }
                if (items.reInsert == 0 && items.matchEnd ==1) {
                    Log.d("hello", "/setting/reInsert reInsert 0");
                    Log.d("hello", "/setting/reInsert matchEnd 1");

                    mPush402 = new Push402();
                    mPush402.match_id = mInputResultSetting.match_id;
                    NetworkManager.getInstance().postNetworkMessage504(context, mPush402, new OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            NetworkManager.getInstance().postNetworkPush504(context, mPush402, new OnResultListener<EtcData>() {
                                @Override
                                public void onSuccess(EtcData result) {

                                }

                                @Override
                                public void onFail(int code) {

                                }
                            });
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                } else if(items.reInsert == 1 && items.matchEnd ==1){
                    Log.d("hello", "/setting/reInsert reInsert 1");
                    mPush402 = new Push402();
                    mPush402.match_id = mInputResultSetting.match_id;
                    mPush402.member_id = mInputResultSetting.member_id;
                    mPush402.club_id = mInputResultSetting.club_id;
                    NetworkManager.getInstance().postNetworkMatchReInsert(context, mPush402, new OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            NetworkManager.getInstance().postNetworkMessage502(context, mPush402, new OnResultListener<EtcData>() {
                                @Override
                                public void onSuccess(EtcData result) {
                                    NetworkManager.getInstance().postNetworkPush502(context, mPush402, new OnResultListener<EtcData>() {
                                        @Override
                                        public void onSuccess(EtcData result) {

                                        }

                                        @Override
                                        public void onFail(int code) {

                                        }
                                    });
                                }

                                @Override
                                public void onFail(int code) {

                                }
                            });
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }
        });

    }
    public static final String SEND_CLUB_DELETE =GROND_SERVER_URL+"/club/delete";
    public void postNetworkClubDelete(final Context context, final InputResultSetting mInputResultSetting,final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
          params.put("club_id",mInputResultSetting.club_id);
        params.put("member_id",mInputResultSetting.member_id);


        client.post(context, SEND_CLUB_DELETE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
                EtcData items = gson.fromJson(s, EtcData.class);
                if (items != null) {
                    listener.onSuccess(items);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }
        });

    }

    //===================================================
    public void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }
    public void showWaitingDialog(Context context){
        mDialogList.add(new Dialog(context));
        mDialogList.get(mDialogList.size()-1).requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogList.get(mDialogList.size()-1).setContentView(R.layout.fragment_sample_ripple);
        ((CustomRubberLoaderView) (mDialogList.get(mDialogList.size()-1).findViewById(R.id.loader2))).startLoading();
        TextView mTextView = (TextView)mDialogList.get(mDialogList.size()-1).findViewById(R.id.textDialog);
//        if(MyApplication.getCurrentActivity()!= null){
//            if(MyApplication.getCurrentActivity().isFinishing()){
                try {
                    mDialogList.get(mDialogList.size() - 1).show();
                }catch (WindowManager.BadTokenException e){
                    e.printStackTrace();
                }
//            }
//        }

    }
    public void unShowWaitingDialog(){
        try{
            for(Dialog item : mDialogList){
                item.dismiss();
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }
}

