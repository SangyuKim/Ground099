package com.android.ground.ground.manager;

import android.content.Context;
import android.net.http.Headers;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Utils;
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
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfo;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.post.fcCreate.ClubProfile;
import com.android.ground.ground.model.post.fcUpdate.ClubProfileUpdate;
import com.android.ground.ground.model.post.signup.UserProfile;
import com.android.ground.ground.model.tmap.TmapItem;
import com.begentgroup.xmlparser.XMLParser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


public class NetworkManager {

//    public final static String GROND_SERVER_URL = "http://172.31.24.101:80";
//    public final static String GROND_SERVER_URL = "http://192.168.211.228:3001";
    public final static String GROND_SERVER_URL = "http://54.178.160.114";
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    AsyncHttpClient client;
    XMLParser parser;
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


        parser = new XMLParser();
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

        client.get(context, TMAP_URL, headers, params, new AsyncHttpResponseHandler() {
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
            }
        });

    }
    public static final String SEARCH_CLUB_URL =GROND_SERVER_URL+"/club/search";
    public void getNetworkSearchClub(final Context context, String filter,String keyword, int page, int memberId, final OnResultListener<SearchClub> listener) {


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
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                SearchClub items = gson.fromJson(s, SearchClub.class);
                if (items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);
            }
        });

    }

    public static final String SEARCH_MEM_URL =GROND_SERVER_URL+"/member/search";
    public void getNetworkSearchMem(final Context context, String filter,String keyword, int page, int memberId, final OnResultListener<SearchMem> listener) {



        final RequestParams params = new RequestParams();

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
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                SearchMem items = gson.fromJson(s, SearchMem.class);
                if (items != null)
                    listener.onSuccess(items);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));

                MatchInfo items = gson.fromJson(s, MatchInfo.class);
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
    //Lineup virtual fomation
    public static final String SEARCH_LINEUP_VIRTUAL_FOMATION_URL =GROND_SERVER_URL+"/lineup/virtual/fomation";
    public void getNetworkLineupVirtualFomation(final Context context,int clubId,  int matchId, final OnResultListener<LineupVirtualFomation> listener) {

        final RequestParams params = new RequestParams();
        params.put("match_id",matchId);
        params.put("club_id",clubId);

        client.get(context,SEARCH_LINEUP_VIRTUAL_FOMATION_URL , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                LineupVirtualFomation items = gson.fromJson(s, LineupVirtualFomation.class);
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
    public void postNetworkSignup(final Context context , UserProfile mUserProfile) {

        final RequestParams params = new RequestParams();
        try {
            params.put("file", mUserProfile.mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("gender",mUserProfile.gender);
        params.put("age",mUserProfile.age);
        params.put("memLocationName",mUserProfile.memLocationName);
        params.put("memMainDay_Mon",mUserProfile.memMainDay_Mon);
        params.put("memMainDay_Tue",mUserProfile.memMainDay_Tue);
        params.put("memMainDay_Wed",mUserProfile.memMainDay_Wed);
        params.put("memMainDay_Thu",mUserProfile.memMainDay_Thu);
        params.put("memMainDay_Fri",mUserProfile.memMainDay_Fri);
        params.put("memMainDay_Sat",mUserProfile.memMainDay_Sat);
        params.put("memMainDay_Sun",mUserProfile.memMainDay_Sun);
        params.put("memIntro",mUserProfile.memIntro);
        params.put("position",mUserProfile.position);
        params.put("skill",mUserProfile.skill);
        params.put("latitude",mUserProfile.latitude);
        params.put("longitude",mUserProfile.longitude);

        client.post(context, SEND_SIGNUP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("hello", "status code : " + statusCode);
            }
        });

    }

    //login kakao
    public static final String SEND_LOGIN_KAKAO_URL =GROND_SERVER_URL+"/member/login/kakao";
    public void postNetworkLoginKakao(final Context context, String token) {

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
           client.post(context, SEND_LOGIN_KAKAO_URL, params, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                   ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                   String s = new String(responseBody, Charset.forName("UTF-8"));
                   Log.d("hello", s);
               }

               @Override
               public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                   Log.d("hello", "status code : " + statusCode);
               }
           });

    }

    //login facebook
    public static final String SEND_LOGIN_FACEBOOK_URL =GROND_SERVER_URL+"/member/login/facebook";
    public void postNetworkLoginFacebook(final Context context, String token) {

        final RequestParams params = new RequestParams();
        params.put("access_token",token);
        client.post(context, SEND_LOGIN_FACEBOOK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("hello", "status : " +statusCode);
            }
        });

    }
    //make fc club
    public static final String SEND_CLUB_CREATE_URL =GROND_SERVER_URL+"/club/make";
    public void postNetworkMakeClub(final Context context , ClubProfile mClubProfile) {

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



        client.post(context, SEND_CLUB_CREATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("hello", "status code : " + statusCode);
            }
        });

    }
    //update fc club
    public static final String SEND_CLUB_UPDATE_URL =GROND_SERVER_URL+"/club";
    public void postNetworkUpdateClub(final Context context , ClubProfileUpdate mClubProfileUpdate) {

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
        params.put("clubIntro",mClubProfileUpdate.clubIntro);



        client.post(context, SEND_CLUB_UPDATE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                String s = new String(responseBody, Charset.forName("UTF-8"));
                Log.d("hello", s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("hello", "status code : " + statusCode);
            }
        });

    }


//===================================================
    public void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }



}

