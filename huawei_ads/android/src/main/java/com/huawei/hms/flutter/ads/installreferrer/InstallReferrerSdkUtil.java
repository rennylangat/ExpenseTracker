/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.huawei.hms.flutter.ads.installreferrer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.huawei.hms.ads.installreferrer.api.InstallReferrerClient;
import com.huawei.hms.ads.installreferrer.api.InstallReferrerStateListener;
import com.huawei.hms.ads.installreferrer.api.ReferrerDetails;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.io.IOException;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class InstallReferrerSdkUtil extends HmsInstallReferrer {
    private static SparseArray<InstallReferrerSdkUtil> allSdkReferrers = new SparseArray<>();
    private static final String TAG = "InstallReferrerSdkUtil";
    private Context context;
    private MethodChannel channel;
    private InstallReferrerClient referrerClient;

    InstallReferrerSdkUtil(Integer id, Context context, MethodChannel channel) {
        super(id);
        this.context = context;
        this.channel = channel;
        allSdkReferrers.put(id, this);
    }

    public void startConnection(boolean isTest) {
        if (null == context) {
            Log.e(TAG, "connect context is null");
            return;
        }

        if (status == Status.CONNECTED) {
            return;
        }

        Log.i(TAG, "startConnection");
        Log.i(TAG, "Test mode : " + isTest);
        referrerClient = InstallReferrerClient.newBuilder(context).setTest(isTest).build();
        Log.i(TAG, "referrerClient built from context");
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(final int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        Log.i(TAG, "connect ads kit ok");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Log.i(TAG, "FEATURE_NOT_SUPPORTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Log.i(TAG, "SERVICE_UNAVAILABLE");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                        Log.i(TAG, "SERVICE_DISCONNECTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        Log.i(TAG, "DEVELOPER_ERROR");
                        break;
                    default:
                        Log.i(TAG, "responseCode: " + responseCode);
                        break;
                }
                status = Status.CONNECTED;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        channel.invokeMethod("onInstallReferrerSetupFinished", ToMap.argsToMap(id, "responseCode", responseCode));
                    }
                });
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                Log.i(TAG, "onInstallReferrerServiceDisconnected");
                status = Status.DISCONNECTED;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        channel.invokeMethod("onInstallReferrerSetupDisconnected", ToMap.argsToMap(id));
                    }
                });
            }
        });
    }

    public boolean isReady() {
        if (referrerClient != null) {
            return referrerClient.isReady();
        }
        return false;
    }

    public void endConnection() {
        Log.i(TAG, "endConnection");
        if (null != referrerClient && status == Status.CONNECTED) {
            referrerClient.endConnection();
            referrerClient = null;
            status = Status.DISCONNECTED;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    channel.invokeMethod("onInstallReferrerSetupDisconnected", ToMap.argsToMap(id));
                }
            });
        }
    }

    public void getReferrerDetails(final MethodChannel.Result result) {
        if (referrerClient != null) {
            try {
                ReferrerDetails referrerDetails = referrerClient.getInstallReferrer();
                Log.i(TAG, "Referrer details retrieved successfully");
                final Map<String, Object> response =
                    ToMap.argsToMap(
                        ReferrerDetails.KEY_INSTALL_REFERRER, referrerDetails.getInstallReferrer(),
                        ReferrerDetails.KEY_REFERRER_CLICK_TIMESTAMP, referrerDetails.getReferrerClickTimestampMillisecond(),
                        ReferrerDetails.KEY_INSTALL_BEGIN_TIMESTAMP, referrerDetails.getInstallBeginTimestampMillisecond());
                new ReferrerDetailsHandler(Looper.getMainLooper(), response, result).backToMain();
            } catch (RemoteException | IOException e) {
                Log.e(TAG, "getInstallReferrer exception: " + e.getClass() + " | " + e.getMessage());
            }
        }
    }

    @Override
    void destroy() {
        if (status == Status.CONNECTED) {
            endConnection();
        }
        allSdkReferrers.remove(id);
        super.destroy();
    }
}
