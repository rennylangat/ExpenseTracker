package com.huawei.hms.flutter.ads.adslite;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.flutter.ads.factory.AdParamFactory;
import com.huawei.hms.flutter.ads.utils.Constants;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class Interstitial extends AdListener {
    private static final String TAG = "Interstitial";
    private static SparseArray<Interstitial> allInterstitialAds = new SparseArray<>();

    private final Activity activity;
    private final MethodChannel channel;
    private final int id;
    private String status;

    private InterstitialAd interstitial = null;

    public Interstitial(int id, Activity activity, MethodChannel channel) {
        this.id = id;
        this.activity = activity;
        this.channel = channel;
        allInterstitialAds.put(id, this);
        setStatus(Constants.AdStatus.CREATED);
    }

    public static Interstitial get(Integer id) {
        return allInterstitialAds.get(id);
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public boolean isCreated() {
        return this.status.equals(Constants.AdStatus.CREATED);
    }

    private boolean isPreparing() {
        return this.status.equals(Constants.AdStatus.PREPARING);
    }

    private boolean isLoading() {
        return this.status.equals(Constants.AdStatus.LOADING);
    }

    public boolean isLoaded() {
        return this.status.equals(Constants.AdStatus.LOADED);
    }

    public boolean isFailed() {
        return this.status.equals(Constants.AdStatus.FAILED);
    }

    public void loadAd(String adSlotId, Map<String, Object> adParam, boolean hasRewardAdListener) {
        setStatus(Constants.AdStatus.LOADING);
        interstitial = new InterstitialAd(activity);
        Log.i(TAG, "Interstitial ad initialized");
        interstitial.setAdId(adSlotId);
        Log.i(TAG, "Interstitial ad slot id set");

        interstitial.setAdListener(this);
        if (hasRewardAdListener) {
            interstitial.setRewardAdListener(new HmsRewardAd(channel));
        }

        AdParamFactory factory = new AdParamFactory(adParam);
        interstitial.loadAd(factory.createAdParam());
    }

    public void show() {
        if (isLoading()) {
            Log.i(TAG, "Interstitial ad is being prepared.");
            setStatus(Constants.AdStatus.PREPARING);
            return;
        }
        interstitial.show();
    }

    public void destroy() {
        allInterstitialAds.remove(id);
    }

    public static void destroyAll() {
        for (int i = 0; i < allInterstitialAds.size(); i++) {
            allInterstitialAds.valueAt(i).destroy();
        }
        allInterstitialAds.clear();
    }

    @Override
    public void onAdLoaded() {
        Log.i(TAG, "onAdLoaded");
        boolean wasPreparing = isPreparing();
        setStatus(Constants.AdStatus.LOADED);
        channel.invokeMethod("onAdLoaded", ToMap.argsToMap(id));
        if (wasPreparing) {
            show();
        }
    }

    @Override
    public void onAdFailed(int errorCode) {
        Log.w(TAG, "onInterstitialAdFailed: " + errorCode);
        setStatus(Constants.AdStatus.FAILED);
        channel.invokeMethod("onAdFailed", ToMap.argsToMap(id, "errorCode", errorCode));
    }

    @Override
    public void onAdOpened() {
        Log.i(TAG, "onInterstitialAdOpened");
        channel.invokeMethod("onAdOpened", ToMap.argsToMap(id));
    }

    @Override
    public void onAdClicked() {
        Log.i(TAG, "onInterstitialAdClicked");
        channel.invokeMethod("onAdClicked", ToMap.argsToMap(id));
    }

    @Override
    public void onAdImpression() {
        Log.i(TAG, "onInterstitialAdImpression");
        channel.invokeMethod("onAdImpression", ToMap.argsToMap(id));
    }

    @Override
    public void onAdClosed() {
        Log.i(TAG, "onInterstitialAdClosed");
        channel.invokeMethod("onAdClosed", ToMap.argsToMap(id));
    }

    @Override
    public void onAdLeave() {
        Log.i(TAG, "onInterstitialAdLeave");
        channel.invokeMethod("onAdLeave", ToMap.argsToMap(id));
    }
}
