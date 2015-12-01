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
import cz.msebera.android.httpclient.message.BasicHeader;


public class NetworkManager{
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
        params.put("member_id", memberId);


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

//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                    _dialog  = new Dialog(context);
//                    _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    _dialog.setContentView(R.layout.fragment_sample_ripple);
//                    ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                    TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                    mTextView.setText("getNetworkSearchClub");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }
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
        params.put("member_id", memberId);


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

//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkSearchMem");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }


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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkMatchInfoMVP");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }


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
        params.put("member_id", memberId);
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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkMyPage");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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

//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkMyPageTrans");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }
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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkClubMain");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkClubAndMember");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkClubMatchList");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkLineupInfo");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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
//            @Override
//            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPreProcessResponse(instance, response);
//
//                _dialog  = new Dialog(context);
//                _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                _dialog.setContentView(R.layout.fragment_sample_ripple);
//                ((RubberLoaderView) (_dialog.findViewById( R.id.loader2))).startLoading();
//                TextView mTextView = (TextView)_dialog.findViewById(R.id.textDialog);
//                mTextView.setText("getNetworkLineupMatch");
//                _dialog.show();
//
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//                super.onPostProcessResponse(instance, response);
//                _dialog.dismiss();
//            }

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
//    //facebook
//    public static final String SEND_FACEBOOK_URL =GROND_SERVER_URL+"/member/login";
//    public void postNetworkFacebook(final Context context,String accesstoken) {
//
//        final RequestParams params = new RequestParams();
//        params.put("access_token",accesstoken);
//        client.post(context, SEND_FACEBOOK_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
//                String s = new String(responseBody, Charset.forName("UTF-8"));
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
//
//    }

    //login local
//    public static final String SEND_LOGIN_URL =GROND_SERVER_URL+"/member/login";
//    public void postNetworkLogin(final Context context) {
//
//        final RequestParams params = new RequestParams();
//        params.put("member_id","1");
//        params.put("password","234");
//        client.post(context, SEND_LOGIN_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
//                String s = new String(responseBody, Charset.forName("UTF-8"));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
//
//    }

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
                unShowWaitingDialog();
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
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }



        });


    }

    //login kakao
    public static final String SEND_LOGIN_KAKAO_URL =GROND_SERVER_URL+"/member/login/kakao";
    public void postNetworkLoginKakao(final Context context, String token , final OnResultListener<KakaoLogin> listener) {
        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
           client.post(context, SEND_LOGIN_KAKAO_URL, params, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    unShowWaitingDialog();
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
                   try{
                    unShowWaitingDialog();
                   }catch (IllegalArgumentException e){
                       e.printStackTrace();
                   }
                   Log.d("hello", "status code : " + statusCode);
                   listener.onFail(statusCode);
//                   String s = new String(responseBody, Charset.forName("UTF-8"));
//                   Log.d("hello", "kakao post error : " + s);
               }



           });

    }

    //login kakao after token send
    public void getNetworkLoginKakao(final Context context, String token , final OnResultListener<KakaoResponse> listener) {
        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
//        params.put("access_token",token);
        client.get(context, SEND_LOGIN_KAKAO_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
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
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

    //login facebook
    public static final String SEND_LOGIN_FACEBOOK_URL =GROND_SERVER_URL+"/member/login/facebook";
    public void postNetworkLoginFacebook(final Context context, String token, final OnResultListener<FacebookLogin> listener) {

        showWaitingDialog(context);

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
        client.post(context, SEND_LOGIN_FACEBOOK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
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
                unShowWaitingDialog();
                Log.d("hello", "status : " + statusCode);
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
        params.put("startTime",mPush401.startTime);
        params.put("endTime",mPush401.endTime);
        params.put("matchLocation",mPush401.matchLocation);
        params.put("etc",mPush401.etc);

        client.post(context, SEND_PUSH_401_URL, params, new AsyncHttpResponseHandler() {
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

    // message201
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
    // message300
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
    // push300
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


    // message301 경기일정(FC - >소속회원)
    public static final String SEND_MESSAGE_301_URL =GROND_SERVER_URL+"/message/301";
    public void postNetworkMessage301(final Context context , Push301 mPush301,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("home_id",mPush301.home_id);
        params.put("away_id",mPush301.away_id);
        params.put("match_id",mPush301.match_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_301_URL, params, new AsyncHttpResponseHandler() {
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
    // push303 response 매치 참석 승낙
    public static final String SEND_MESSAGE_301_RESPONSE_URL =GROND_SERVER_URL+"/message/301/response";
    public void postNetworkMessage301Response(final Context context , Push301Response mPush301Response,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mPush301Response.member_id);
        params.put("club_id",mPush301Response.club_id);
        params.put("match_id",mPush301Response.match_id);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MESSAGE_301_RESPONSE_URL, params, new AsyncHttpResponseHandler() {
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


    // match create
    public static final String SEND_MATCH_CREATE_URL =GROND_SERVER_URL+"/match/create";
    public void postNetworkMatchCreate(final Context context , MatchCreateData mMatchCreateData,  final OnResultListener<MatchCreateDataResponse> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mMatchCreateData.member_id);
        params.put("home_id",mMatchCreateData.home_id);
        params.put("away_id",mMatchCreateData.away_id);
        params.put("matchDate", mMatchCreateData.matchDate);
        params.put("startTime",mMatchCreateData.startTime);
        params.put("endTime",mMatchCreateData.endTime);
        params.put("matchLocation",mMatchCreateData.matchLocation);
//        params.put("contents",mPush300.contents);

        client.post(context, SEND_MATCH_CREATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                unShowWaitingDialog();
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
                unShowWaitingDialog();
                Log.d("hello", "status code : " + statusCode);
                listener.onFail(statusCode);
            }


        });

    }

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
    // club  manager 박탈
    public static final String SEND_CLUB_MANAGER_STOP_URL =GROND_SERVER_URL+"/club/manager/stop";
    public void postNetworkClubManagerStop(final Context context , ClubManagerData mClubManagerData,  final OnResultListener<EtcData> listener) {
        showWaitingDialog(context);
        final RequestParams params = new RequestParams();
        params.put("member_id",mClubManagerData.member_id);
        params.put("club_id",mClubManagerData.club_id);
//        params.put("manager_id",mClubManagerData.manager_id);

//        params.put("contents",mPush300.contents);

        client.post(context, SEND_CLUB_MANAGER_STOP_URL, params, new AsyncHttpResponseHandler() {
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

