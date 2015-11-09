package com.android.ground.ground;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.ground.ground.manager.PropertyManager;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * GCM 서버에 기기를 등록하고 토큰을 발급받는 서비스다.
 */
public class RegistrationIntentService extends IntentService {
    static final private String TAG = "GCM_Example";

    private static final String[] TOPICS = {"global"};
    static public final String REGISTRATION_COMPLETE_BROADCAST = "REGISTRATION_COMPLETE_BROADCAST";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    // 파라미터 없는 public 생성자 꼭 필요
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "RegistrationIntentService Start!");
        try {
            // 발급받은 토큰
            InstanceID instanceID = InstanceID.getInstance(this);
            final String token = instanceID.getToken(getString(R.string.GCM_SenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "Token : " + token);

            Intent completeIntent = new Intent(REGISTRATION_COMPLETE_BROADCAST);
            completeIntent.putExtra("TOKEN", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(completeIntent);


            PropertyManager.getInstance().setRegistrationToken(token);
            // Subscribe to topic channels
            subscribeTopics(token);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Regist Exception", e);
        }
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
