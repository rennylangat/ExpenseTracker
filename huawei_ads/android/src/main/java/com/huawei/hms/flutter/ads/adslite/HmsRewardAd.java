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

package com.huawei.hms.flutter.ads.adslite;

import android.app.Activity;
import android.util.Log;

import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.huawei.hms.ads.reward.RewardVerifyConfig;
import com.huawei.hms.flutter.ads.factory.AdParamFactory;
import com.huawei.hms.flutter.ads.utils.Constants;
import com.huawei.hms.flutter.ads.utils.FromMap;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class HmsRewardAd extends RewardAdStatusListener implements RewardAdListener {
    private static final String TAG = "HmsRewardAd";
    private final RewardAd rewardAdInstance;
    private final MethodChannel channel;
    private String status;

    public HmsRewardAd(MethodChannel channel) {
        this.rewardAdInstance = null;
        this.channel = channel;
    }

    public HmsRewardAd(Activity activity, MethodChannel channel) {
        this.channel = channel;
        this.rewardAdInstance = RewardAd.createRewardAdInstance(activity);
        Log.i(TAG, "Reward ad initialized");
        setStatus(Constants.AdStatus.CREATED);
        this.rewardAdInstance.setRewardAdListener(this);
    }

    public void setRewardVerifyConfig(Map<String, Object> options) {
        String userId = FromMap.toString("userId", options);
        String data = FromMap.toString("data", options);
        if (userId != null && data != null) {
            rewardAdInstance.setRewardVerifyConfig(
                new RewardVerifyConfig.Builder()
                    .setUserId(userId)
                    .setData(data)
                    .build()
            );
        }
    }

    void setStatus(String status) {
        this.status = status;
    }

    public boolean isCreated() {
        return this.status.equals(Constants.AdStatus.CREATED);
    }

    public boolean isLoaded() {
        return rewardAdInstance != null && rewardAdInstance.isLoaded();
    }

    public boolean isFailed() {
        return this.status.equals(Constants.AdStatus.FAILED);
    }

    public Reward getReward() {
        return rewardAdInstance.getReward();
    }

    public void setUserId(String userId) {
        rewardAdInstance.setUserId(userId);
    }

    public void setData(String data) {
        rewardAdInstance.setData(data);
    }

    public void loadAd(String adSlotId, Map<String, Object> adParam) {
        setStatus(Constants.AdStatus.LOADING);
        AdParamFactory factory = new AdParamFactory(adParam);
        rewardAdInstance.loadAd(adSlotId, factory.createAdParam());
    }

    public void show() {
        if (rewardAdInstance != null && rewardAdInstance.isLoaded()) {
            rewardAdInstance.show();
        }
    }

    public void pause() {
        if (rewardAdInstance != null && rewardAdInstance.isLoaded()) {
            rewardAdInstance.pause();
        }
    }

    public void resume() {
        if (rewardAdInstance != null && rewardAdInstance.isLoaded()) {
            rewardAdInstance.resume();
        }
    }

    @Override
    public void onRewardAdFailedToShow(int errorCode) {
        Log.w(TAG, "onRewardAdFailedToShow: " + errorCode);
        setStatus(Constants.AdStatus.FAILED);
        channel.invokeMethod("onRewardAdFailedToShow", ToMap.argsToMap("errorCode", errorCode));
    }

    @Override
    public void onRewarded(Reward reward) {
        Log.i(TAG, "onRewarded");
        channel.invokeMethod("onRewarded",
            ToMap.argsToMap("name", reward.getName(), "amount", reward.getAmount()));
    }

    @Override
    public void onRewardAdClosed() {
        Log.i(TAG, "onRewardAdClosed");
        channel.invokeMethod("onRewardAdClosed", null);
    }

    @Override
    public void onRewardAdFailedToLoad(int errorCode) {
        Log.w(TAG, "onRewardAdFailedToLoad: " + errorCode);
        setStatus(Constants.AdStatus.FAILED);
        channel.invokeMethod("onRewardAdFailedToLoad", ToMap.argsToMap("errorCode", errorCode));
    }

    @Override
    public void onRewardAdLeftApp() {
        Log.i(TAG, "onRewardAdLeftApp");
        channel.invokeMethod("onRewardAdLeftApp", null);
    }

    @Override
    public void onRewardAdLoaded() {
        Log.i(TAG, "onRewardAdLoaded");
        setStatus(Constants.AdStatus.LOADED);
        channel.invokeMethod("onRewardAdLoaded", null);
    }

    @Override
    public void onRewardAdOpened() {
        Log.i(TAG, "onRewardAdOpened");
        channel.invokeMethod("onRewardAdOpened", null);
    }

    @Override
    public void onRewardAdCompleted() {
        Log.i(TAG, "onRewardAdCompleted");
        channel.invokeMethod("onRewardAdCompleted", null);
    }

    @Override
    public void onRewardAdStarted() {
        Log.i(TAG, "onRewardAdStarted");
        channel.invokeMethod("onRewardAdStarted", null);
    }
}
