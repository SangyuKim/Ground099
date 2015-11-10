package com.android.ground.ground.manager;

import android.content.Context;
import android.net.http.Headers;
import android.util.Log;
import android.widget.Toast;

import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Utils;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
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
    public static final String SEARCH_CLUB_URL ="http://192.168.201.182:3000/searchClub";
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

    public static final String SEARCH_MEM_URL ="http://192.168.201.182:3000/searchMem";
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

     public void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }

}

