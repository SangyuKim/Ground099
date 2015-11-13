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
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMain;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfo;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageTrans;
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

    public final static String GROND_SERVER_URL = "http://192.168.201.154:3000";
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


    private static final String SERVER = "http://openapi.naver.com";

    private static final String MOVIE_URL = SERVER + "/search";
    private static final String KEY = "dbe85dae5e1192a42aae51ea5b61cb2b";
    private static final String TARGET = "movie";

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



    public void getNetworkMelon(Context context, String keyword, int start, int display, final OnResultListener<NaverMovies> listener) {
        final RequestParams params = new RequestParams();
        params.put("key", KEY);
        params.put("query", keyword);
        params.put("display", display);
        params.put("start", start);
        params.put("target", TARGET);

        client.get(context, MOVIE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                ByteArrayInputStream bais = new ByteArrayInputStream(responseBody);
                NaverMovies movies = parser.fromXml(bais, "channel", NaverMovies.class);
                listener.onSuccess(movies);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);
            }
        });
    }
    public static final String TMAP_URL = "https://apis.skplanetx.com/tmap/poi/findPoiAreaDataByName";
    public static final String TMAP_POI_URL ="https://apis.skplanetx.com/tmap/pois";
//    private static final String SERVER = "http://openapi.naver.com";

//    private static final String MOVIE_URL = SERVER + "/search";
    private static final String TMAP_API_KEY = "ed4139de-5d31-31a3-aa1d-92a502350a6f";
//    private static final String TARGET = "movie";
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
        params.put("sort", filter);
        params.put("page", page);
        params.put("member_id", memberId);
        params.put("search", keyword);

        client.get(context, SEARCH_CLUB_URL, params, new AsyncHttpResponseHandler() {
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
        params.put("sort", filter);
        params.put("page", page);
        params.put("member_id", memberId);
        params.put("search", keyword);

        client.get(context, SEARCH_MEM_URL, params, new AsyncHttpResponseHandler() {
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
        params.put("sort", filter);
        params.put("page", page);
        params.put("member_id", memberId);
        params.put("search", keyword);

        client.get(context,SEARCH_MATCHINFO_URL , params, new AsyncHttpResponseHandler() {
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
    public void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }



}

