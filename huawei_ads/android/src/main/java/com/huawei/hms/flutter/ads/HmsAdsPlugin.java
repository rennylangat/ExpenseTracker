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

package com.huawei.hms.flutter.ads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.RequestOptions;
import com.huawei.hms.ads.consent.bean.AdProvider;
import com.huawei.hms.ads.consent.constant.ConsentStatus;
import com.huawei.hms.ads.consent.constant.DebugNeedConsent;
import com.huawei.hms.ads.consent.inter.Consent;
import com.huawei.hms.ads.consent.inter.ConsentUpdateListener;
import com.huawei.hms.ads.identifier.AdIdVerifyException;
import com.huawei.hms.ads.identifier.AdvertisingIdClient;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.splash.SplashView;
import com.huawei.hms.flutter.ads.adslite.Banner;
import com.huawei.hms.flutter.ads.adslite.HmsRewardAd;
import com.huawei.hms.flutter.ads.adslite.Interstitial;
import com.huawei.hms.flutter.ads.adslite.Splash;
import com.huawei.hms.flutter.ads.adslite.nativead.NativeAdControllerFactory;
import com.huawei.hms.flutter.ads.adslite.nativead.NativeAdPlatformViewFactory;
import com.huawei.hms.flutter.ads.installreferrer.HmsInstallReferrer;
import com.huawei.hms.flutter.ads.installreferrer.InstallReferrerSdkUtil;
import com.huawei.hms.flutter.ads.utils.Constants;
import com.huawei.hms.flutter.ads.utils.FromMap;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.platform.PlatformViewRegistry;

import static com.huawei.hms.flutter.ads.utils.Constants.NATIVE_VIEW_TYPE;

/**
 * Hms Ads Plugin
 *
 * @author Huawei Technologies
 * @since (4.0.4)
 */
public class HmsAdsPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    private static final String TAG = "HW_ADS_PLUGIN";
    private Activity activity;
    private Context context;
    private MethodChannel methodChannel;
    private BinaryMessenger messenger;
    private FlutterPluginBinding flutterPluginBinding;
    private HmsRewardAd hmsRewardAd;
    private Consent consentInfo;

    public static void registerWith(Registrar registrar) {
        final HmsAdsPlugin instance = new HmsAdsPlugin();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), Constants.LIBRARY_CHANNEL);
        registrar.publish(instance);
        instance.onAttachedToEngine(registrar.platformViewRegistry(), channel, registrar.context(), registrar.messenger(), registrar.activity());
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding;
    }

    private void onAttachedToEngine(PlatformViewRegistry registry, MethodChannel channel, Context applicationContext, BinaryMessenger messenger, Activity activity) {
        registry.registerViewFactory(NATIVE_VIEW_TYPE, new NativeAdPlatformViewFactory(messenger, activity));
        this.activity = activity;
        this.context = applicationContext;
        this.methodChannel = channel;
        this.messenger = messenger;
        this.methodChannel.setMethodCallHandler(this);
        this.hmsRewardAd = new HmsRewardAd(activity, channel);
        this.consentInfo = Consent.getInstance(activity);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "bannerSize-getHeightPx":
                callGetHeightPx(call, result);
                break;
            case "bannerSize-getWidthPx":
                callGetWidthPx(call, result);
                break;
            case "getCurrentDirectionBannerSize":
                callGetCurrentDirectionBannerSize(call, result);
                break;
            case "getLandscapeBannerSize":
                callGetLandScapeBannerSize(call, result);
                break;
            case "getPortraitBannerSize":
                callGetPortraitBannerSize(call, result);
                break;
            // HWAds Methods
            case "HwAds-init":
                callAdsInit(result);
                break;
            case "HwAds-initWithAppCode":
                callAdsInitWithAppCode(call, result);
                break;
            case "HwAds-getSdkVersion":
                result.success(HwAds.getSDKVersion());
                break;
            case "HwAds-getRequestOptions":
                callGetRequestOptions(result);
                break;
            case "HwAds-setRequestOptions":
                callSetRequestOptions(call, result);
                break;
            // AdvertisingId METHODS
            case "getAdvertisingIdInfo":
                callGetAdvertisingIdInfo(result);
                break;
            case "verifyAdId":
                callVerifyAdId(call, result);
                break;
            // Ad methods
            case "loadBannerAd":
                callLoadBannerAd(activity, call, result);
                break;
            case "loadInterstitialAd":
                callLoadInterstitialAd(call, result);
                break;
            case "loadRewardAd":
                callLoadRewardAd(call, result);
                break;
            case "loadSplashAd":
                callLoadSplashAd(activity, call, result);
                break;
            case "preloadSplashAd":
                callPreLoadSplashAd(activity, call, result);
                break;
            case "showBannerAd":
                callShowBannerAd(call, result);
                break;
            case "showInterstitialAd":
                callShowInterstitialAd(call, result);
                break;
            case "showRewardAd":
                callShowRewardAd(result);
                break;
            case "getRewardAdReward":
                callGetRewardAdReward(result);
                break;
            case "setRewardAdUserId":
                callSetRewardAdUserId(call, result);
                break;
            case "setRewardAdData":
                callSetRewardAdData(call, result);
                break;
            case "destroyAd":
                callDestroyAd(call, result);
                break;
            case "isAdLoaded":
                callIsAdLoaded(call, result);
                break;
            case "isAdLoading":
                callIsAdLoading(call, result);
                break;
            case "pauseAd":
                callPauseAd(call, result);
                break;
            case "resumeAd":
                callResumeAd(call, result);
                break;
            case "initNativeAdController":
                callInitNativeAdController(call, result);
                break;
            case "destroyNativeAdController":
                callDestroyNativeAdController(call, result);
                break;
            // InstallReferrer methods
            case "referrerStartConnection":
                callStartConnection(call, result);
                break;
            case "getInstallReferrer":
                callGetReferrerDetails(call, result);
                break;
            case "referrerEndConnection":
                callEndConnection(call, result);
                break;
            case "referrerIsReady":
                callIsReady(call, result);
                break;
            // Consent methods
            case "getTestDeviceId":
                callGetTestDeviceId(call, result);
                break;
            case "addTestDeviceId":
                callAddTestDeviceId(call, result);
                break;
            case "setDebugNeedConsent":
                callSetDebugNeedConsent(call, result);
                break;
            case "setUnderAgeOfPromise":
                callSetUnderAgeOfPromise(call, result);
                break;
            case "setConsentStatus":
                callSetConsentStatus(call, result);
                break;
            case "requestConsentUpdate":
                callRequestConsentUpdate();
                break;
            case "updateConsentSharedPreferences":
                callUpdateConsentSharedPreferences(call, result);
                break;
            case "getConsentSharedPreferences":
                callGetConsentSharedPreferences(call, result);
                break;
            default:
                result.notImplemented();
        }
    }

    private void callGetHeightPx(MethodCall call, Result result) {
        Integer width;
        Integer height;
        width = FromMap.toInteger("width", call.argument("width"));
        height = FromMap.toInteger("height", call.argument("height"));
        if (width != null && height != null) {
            result.success(new BannerAdSize(width, height).getHeightPx(activity));
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callGetWidthPx(MethodCall call, Result result) {
        Integer width;
        Integer height;
        width = FromMap.toInteger("width", call.argument("width"));
        height = FromMap.toInteger("height", call.argument("height"));
        if (width != null && height != null) {
            result.success(new BannerAdSize(width, height).getWidthPx(activity));
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callGetCurrentDirectionBannerSize(MethodCall call, Result result) {
        Integer width;
        width = FromMap.toInteger("width", call.argument("width"));
        if (width != null) {
            BannerAdSize bannerAdSize = BannerAdSize.getCurrentDirectionBannerSize(activity, width);
            result.success(ToMap.argsToMap("width", bannerAdSize.getWidth(), "height", bannerAdSize.getHeight()));
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callGetLandScapeBannerSize(MethodCall call, Result result) {
        Integer width;
        width = FromMap.toInteger("width", call.argument("width"));
        if (width != null) {
            BannerAdSize bannerAdSize = BannerAdSize.getLandscapeBannerSize(activity, width);
            result.success(ToMap.argsToMap("width", bannerAdSize.getWidth(), "height", bannerAdSize.getHeight()));
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callGetPortraitBannerSize(MethodCall call, Result result) {
        Integer width;
        width = FromMap.toInteger("width", call.argument("width"));
        if (width != null) {
            BannerAdSize bannerAdSize = BannerAdSize.getPortraitBannerSize(activity, width);
            result.success(ToMap.argsToMap("width", bannerAdSize.getWidth(), "height", bannerAdSize.getHeight()));
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callAdsInit(Result result) {
        try {
            HwAds.init(activity);
            Log.i(TAG, "HW Ads Kit initialized");
            result.success(Boolean.TRUE);
        } catch (Exception e) {
            Log.e(TAG, "HW Ads initialization failed. Code: " + AdParam.ErrorCode.INNER);
            result.error(Integer.toString(AdParam.ErrorCode.INNER), "HW Ads initialization failed", "");
        }
    }

    private void callAdsInitWithAppCode(MethodCall call, Result result) {
        String appCode = FromMap.toString("appCode", call.argument("appCode"));
        if (appCode != null) {
            HwAds.init(context, appCode);
            Log.i(TAG, "HW Ads Kit initialized with appCode " + appCode);
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter provided for the method", "");
        }
    }

    private void callSetRequestOptions(MethodCall call, Result result) {
        String adContentClassification = FromMap.toString("adContentClassification", call.argument("adContentClassification"));
        Integer tagForUnderAge = FromMap.toInteger("tagForUnderAgeOfPromise", call.argument("tagForUnderAgeOfPromise"));
        Integer tagForChildProtection = FromMap.toInteger("tagForChildProtection", call.argument("tagForChildProtection"));
        Integer nonPersonalizedAd = FromMap.toInteger("nonPersonalizedAd", call.argument("nonPersonalizedAd"));
        String appCountry = FromMap.toString("appCountry", call.argument("appCountry"));
        String appLang = FromMap.toString("appLang", call.argument("appLang"));

        RequestOptions options = new RequestOptions().toBuilder()
            .setAdContentClassification(adContentClassification)
            .setTagForUnderAgeOfPromise(tagForUnderAge)
            .setTagForUnderAgeOfPromise(tagForChildProtection)
            .setNonPersonalizedAd(nonPersonalizedAd)
            .setAppCountry(appCountry)
            .setAppLang(appLang)
            .build();

        HwAds.setRequestOptions(options);
        Log.i(TAG, "Request Options Set");
        result.success(Boolean.TRUE);
    }

    private void callGetRequestOptions(Result result) {
        RequestOptions options = HwAds.getRequestOptions();
        Map<String, Object> arguments = new HashMap<>();
        if (options != null) {
            arguments.put("adContentClassification", options.getAdContentClassification());
            arguments.put("tagForUnderAgeOfPromise", options.getTagForUnderAgeOfPromise());
            arguments.put("tagForChildProtection", options.getTagForChildProtection());
            arguments.put("nonPersonalizedAd", options.getNonPersonalizedAd());
            arguments.put("appCountry", options.getAppCountry());
            arguments.put("appLang", options.getAppLang());
            result.success(arguments);
        } else {
            result.success(null);
        }
    }

    private void callGetAdvertisingIdInfo(Result result) {
        Map<String, Object> arguments = new HashMap<>();
        try {
            AdvertisingIdClient.Info clientInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity);
            Log.i(TAG, "Ad id information retrieved successfully.");
            arguments.put("advertisingId", clientInfo.getId());
            arguments.put("limitAdTrackingEnabled", clientInfo.isLimitAdTrackingEnabled());
            result.success(arguments);
        } catch (IOException e) {
            Log.e(TAG, "ERROR: Ad id information retrieval failed.");
            result.error(Integer.toString(AdParam.ErrorCode.INNER), "Ad id information retrieval failed", "");
        }
    }

    private void callVerifyAdId(MethodCall call, Result result) {
        new VerifyAdIdThread(call, result).start();
    }

    private void callLoadBannerAd(Activity activity, MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adSlotId = call.argument("adSlotId");
        if (adSlotId == null || adSlotId.isEmpty()) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null or empty adSlotId was provided for ad id : " + id, "");
            return;
        }

        final Integer width = FromMap.toInteger("width", call.argument("width"));
        final Integer height = FromMap.toInteger("height", call.argument("height"));
        final Long bannerRefresh = FromMap.toLong("refreshTime", call.argument("refreshTime"));

        if ((width == null || height == null) || (width == 0 || height == 0)) {
            String errMsg =
                String.format(
                    Locale.ENGLISH,
                    "Invalid BannerSize was provided for ad id : " + id,
                    id);
            result.error(Constants.ErrorCodes.INVALID_PARAM, errMsg, "");
            return;
        }

        BannerAdSize adSize;
        if (width == -3 && height == -4) {
            adSize = BannerAdSize.BANNER_SIZE_SMART;
        } else if (width == -1 && height == -2) {
            adSize = BannerAdSize.BANNER_SIZE_DYNAMIC;
        } else {
            adSize = new BannerAdSize(width, height);
        }

        Banner banner = new Banner(id, adSize, bannerRefresh, activity, methodChannel);

        if (!banner.isCreated()) {
            if (banner.isFailed()) {
                result.error(Constants.ErrorCodes.LOAD_FAILED, "Cannot load a failed ad. Ad id : " + id, null);
            } else {
                result.success(Boolean.TRUE);
            }
            return;
        }

        Map<String, Object> adParam = ToMap.objectToMap(call.argument("adParam"));
        banner.loadAd(adSlotId, adParam, false);
        result.success(Boolean.TRUE);
    }

    private void callLoadInterstitialAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        Interstitial interstitialAd = new Interstitial(id, activity, methodChannel);
        if (!interstitialAd.isCreated()) {
            if (interstitialAd.isFailed()) {
                result.error(Constants.ErrorCodes.LOAD_FAILED, "Cannot load a failed interstitialAd. Ad id : " + id, "");
            } else {
                result.success(Boolean.TRUE);
            }
            return;
        }

        String adSlotId = call.argument("adSlotId");
        if (adSlotId == null || adSlotId.isEmpty()) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null or empty adSlotId was provided for interstitialAd id : " + id, "");
            return;
        }

        Map<String, Object> adParam = ToMap.objectToMap(call.argument("adParam"));
        Boolean hasRewardAdListener = FromMap.toBoolean("hasRewardAdListener", call.argument("hasRewardAdListener"));
        interstitialAd.loadAd(adSlotId, adParam, hasRewardAdListener);
        result.success(Boolean.TRUE);
    }

    private void callLoadRewardAd(MethodCall call, Result result) {
        if (!hmsRewardAd.isCreated() && !hmsRewardAd.isFailed()) {
            result.success(Boolean.TRUE);
            return;
        }

        String adSlotId = call.argument("adSlotId");
        if (adSlotId == null || adSlotId.isEmpty()) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null or empty adSlotId was provided for RewardAd", "");
            return;
        }

        Map<String, Object> adParam = ToMap.objectToMap(call.argument("adParam"));
        if (call.argument("adParam") == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null AdParam object was provided for RewardAd", "");
            return;
        }

        Map<String, Object> rewardVerifyConfig = ToMap.objectToMap(call.argument("rewardVerifyConfig"));
        if (!rewardVerifyConfig.isEmpty()) {
            hmsRewardAd.setRewardVerifyConfig(rewardVerifyConfig);
        }

        hmsRewardAd.loadAd(adSlotId, adParam);
        result.success(Boolean.TRUE);
    }

    private void callLoadSplashAd(Activity activity, MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adSlotId = call.argument("adSlotId");
        if (adSlotId == null || adSlotId.isEmpty()) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null or empty adSlotId was provided for ad id : " + id, "");
            return;
        }

        Splash splash = new Splash(id, activity, methodChannel);

        if (!splash.isCreated()) {
            if (splash.isFailed()) {
                result.error(Constants.ErrorCodes.LOAD_FAILED, "Cannot load a failed ad. Ad id : " + id, "");
            } else {
                result.success(Boolean.TRUE);
            }
            return;
        }

        String type = FromMap.toString("adType", call.argument("adType"));
        Map<String, Object> adParam = ToMap.objectToMap(call.argument("adParam"));
        Map<String, Object> resources = ToMap.objectToMap(call.argument("resources"));
        Integer orientation = FromMap.toInteger("orientation", call.argument("orientation"));
        Integer audioFocusType = FromMap.toInteger("audioFocusType", call.argument("audioFocusType"));
        Double margin = FromMap.toDouble("topMargin", call.argument("topMargin"));

        splash.loadAd(
            adSlotId,
            orientation != null ? orientation : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            margin,
            type,
            adParam,
            resources,
            audioFocusType);

        result.success(Boolean.TRUE);
    }

    private void callPreLoadSplashAd(Activity activity, MethodCall call, Result result) {
        String adSlotId = call.argument("adSlotId");
        if (adSlotId == null || adSlotId.isEmpty()) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null or empty adSlotId was provided", "");
            return;
        }

        Map<String, Object> adParam = ToMap.objectToMap(call.argument("adParam"));
        Integer orientation = FromMap.toInteger("orientation", call.argument("orientation"));

        Splash.preloadAd(
            activity,
            adSlotId,
            orientation != null ? orientation : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            adParam,
            result);
    }

    private void callShowBannerAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        Banner bannerAd = Banner.get(id);
        if (bannerAd == null) {
            result.error(Constants.ErrorCodes.LOAD_FAILED, "The specified bannerAd was not loaded, show failed. Ad id: " + id, null);
            return;
        }
        final String offset = FromMap.toString("offset", call.argument("offset"));
        final String anchorType = FromMap.toString("anchorType", call.argument("anchorType"));
        if (offset != null) {
            bannerAd.setOffset(Double.parseDouble(offset));
        }
        if (anchorType != null) {
            bannerAd.setAnchorType("bottom".equals(anchorType) ? Gravity.BOTTOM : Gravity.TOP);
        }
        bannerAd.show();
        result.success(Boolean.TRUE);
    }

    private void callShowInterstitialAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        Interstitial interstitialAd = Interstitial.get(id);
        if (interstitialAd == null) {
            result.error(Constants.ErrorCodes.LOAD_FAILED, "The specified interstitialAd was not loaded, show failed. Ad id: " + id, null);
            return;
        }
        interstitialAd.show();
        result.success(Boolean.TRUE);
    }

    private void callShowRewardAd(Result result) {
        if (hmsRewardAd.isLoaded()) {
            hmsRewardAd.show();
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.LOAD_FAILED, "The specified ad was not loaded, show failed for RewardAd", null);
        }
    }

    private void callGetRewardAdReward(Result result) {
        Reward reward = hmsRewardAd.getReward();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", reward.getName());
        arguments.put("amount", reward.getAmount());

        result.success(arguments);
    }

    private void callSetRewardAdUserId(MethodCall call, Result result) {
        String userId = call.argument("userId");

        hmsRewardAd.setUserId(userId);
        result.success(Boolean.TRUE);
    }

    private void callSetRewardAdData(MethodCall call, Result result) {
        String data = call.argument("data");

        hmsRewardAd.setData(data);
        result.success(Boolean.TRUE);
    }

    private void callIsAdLoaded(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adType = FromMap.toString("adType", call.argument("adType"));
        if (!"Reward".equals(adType) && id == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "isLoaded failed, null parameter was provided", "");
            return;
        }

        switch (adType) {
            case "Reward":
                result.success(hmsRewardAd.isLoaded());
                break;
            case "Interstitial":
                Interstitial interstitial = Interstitial.get(id);
                result.success(interstitial.isLoaded());
                break;
            case "Splash":
                SplashView splash = Splash.get(id).getSplashView();
                result.success(splash != null && splash.isLoaded());
                break;
            default:
                result.error(Constants.ErrorCodes.INVALID_PARAM, "Ad type parameter is invalid", "");
                break;
        }
    }

    private void callDestroyAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        Banner bannerAd = Banner.get(id);
        Interstitial interstitialAd = Interstitial.get(id);
        Splash splashAd = Splash.get(id);
        if (bannerAd == null && interstitialAd == null && splashAd == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "dispose failed, no add exists for given id : " + id, null);
            return;
        }

        if (bannerAd != null) {
            bannerAd.destroy();
        } else if (interstitialAd != null) {
            interstitialAd.destroy();
        } else {
            splashAd.destroy();
        }

        result.success(Boolean.TRUE);
    }

    private void callIsAdLoading(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adType = FromMap.toString("adType", call.argument("adType"));
        if (adType == null || id == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "isLoading failed, null parameter was provided", "");
            return;
        }

        if (!adType.equals("Splash")) {
            Banner banner = Banner.get(id);
            result.success(banner.isLoading());
        } else {
            Splash splash = Splash.get(id);
            result.success(splash.getSplashView() != null && splash.getSplashView().isLoaded());
        }
    }

    private void callPauseAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adType = FromMap.toString("adType", call.argument("adType"));
        if (adType == null || id == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "pauseAd failed, null parameter was provided", "");
            return;
        }

        switch (adType) {
            case "Banner":
                Banner banner = Banner.get(id);
                banner.getBannerView().pause();
                break;
            case "Reward":
                hmsRewardAd.pause();
                break;
            case "Splash":
                Splash splash = Splash.get(id);
                splash.getSplashView().pauseView();
                break;
            default:
                result.error(Constants.ErrorCodes.INVALID_PARAM, "Ad type parameter is invalid", "");
        }
        result.success(true);
    }

    private void callResumeAd(MethodCall call, Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String adType = FromMap.toString("adType", call.argument("adType"));
        if (adType == null || id == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "resumeAd failed, null parameter was provided", "");
            return;
        }

        switch (adType) {
            case "Banner":
                Banner banner = Banner.get(id);
                banner.getBannerView().resume();
                break;
            case "Reward":
                hmsRewardAd.resume();
                break;
            case "Splash":
                Splash splash = Splash.get(id);
                splash.getSplashView().resumeView();
                break;
            default:
                result.error(Constants.ErrorCodes.INVALID_PARAM, "Ad type parameter is invalid", "");
        }
        result.success(true);
    }

    private void callInitNativeAdController(MethodCall call, Result result) {
        String id = FromMap.toString("id", call.argument("id"));
        if (id != null) {
            NativeAdControllerFactory.createController(id, messenger, context);
            result.success(true);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Null parameter was provided", "");
        }
    }

    private void callDestroyNativeAdController(MethodCall call, Result result) {
        String id = FromMap.toString("id", call.argument("id"));
        if (id != null && NativeAdControllerFactory.dispose(id)) {
            result.success(true);
        } else {
            result.error(Constants.ErrorCodes.NOT_FOUND, "No controller was found for provided Id: " + id, "");
        }
    }

    private void callStartConnection(MethodCall call, MethodChannel.Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String callMode = FromMap.toString("callMode", call.argument("callMode"));
        if (callMode != null) {
            new StartConnectionThread(id, callMode, call).start();
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "startConnection failed for id: " + id + " and callMode: null", "");
        }
    }

    private void callEndConnection(MethodCall call, MethodChannel.Result result) {
        final Integer id = FromMap.toInteger("id", call.argument("id"));
        String callMode = FromMap.toString("callMode", call.argument("callMode"));
        final HmsInstallReferrer referrer = HmsInstallReferrer.getReferrerForId(id);
        if (referrer != null && callMode != null) {
            if (referrer.getStatus() == HmsInstallReferrer.Status.CONNECTED) {
                new EndConnectionThread(referrer).start();
            } else {
                Log.i(TAG, "Referrer already disconnected");
            }
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "EndConnection failed for id: " + id + " and callMode: " + callMode, "");
        }
    }

    private void callGetReferrerDetails(MethodCall call, final Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String callMode = FromMap.toString("callMode", call.argument("callMode"));
        final HmsInstallReferrer referrer = HmsInstallReferrer.getReferrerForId(id);
        if (referrer != null && callMode != null) {
            if (referrer.getStatus() == HmsInstallReferrer.Status.CONNECTED) {
                new ReferrerDetailsThread(referrer, result).start();
            } else {
                Log.i(TAG, "Referrer already is not connected");
            }
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "getReferrerDetails failed for id: " + id + " and callMode: " + callMode, "");
        }
    }

    private void callIsReady(MethodCall call, final Result result) {
        Integer id = FromMap.toInteger("id", call.argument("id"));
        String callMode = FromMap.toString("callMode", call.argument("callMode"));
        final HmsInstallReferrer referrer = HmsInstallReferrer.getReferrerForId(id);
        if (referrer == null || callMode == null) {
            result.error(Constants.ErrorCodes.NULL_PARAM, "isReady failed for id: " + id + " and callMode: " + callMode, "");
            return;
        }

        if (callMode.equals("sdk")) {
            new IsReadyThread(referrer, result).start();
        } else {
            result.error(Constants.ErrorCodes.INVALID_PARAM, "Call mode parameter is invalid", "");
        }
    }

    private void callGetTestDeviceId(MethodCall call, Result result) {
        if (consentInfo != null) {
            result.success(consentInfo.getTestDeviceId());
        } else {
            result.error(Constants.ErrorCodes.NOT_FOUND, "Consent instance was null", "");
        }
    }

    private void callAddTestDeviceId(MethodCall call, MethodChannel.Result result) {
        String deviceId = FromMap.toString("deviceId", call.argument("deviceId"));
        if (deviceId != null && consentInfo != null) {
            Log.i(TAG, "SDK addTestDeviceId begin");
            consentInfo.addTestDeviceId(deviceId);
            Log.i(TAG, "SDK addTestDeviceId end");
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM,
                "addTestDevice failed. deviceId: " + deviceId + " | Consent initialized: " + (consentInfo != null),
                "");
        }
    }

    private void callSetDebugNeedConsent(MethodCall call, MethodChannel.Result result) {
        String consentStr = FromMap.toString("needConsent", call.argument("needConsent"));
        if (consentStr != null) {
            DebugNeedConsent needConsent = DebugNeedConsent.valueOf(consentStr);
            Log.i(TAG, "SDK setDebugNeedConsent begin");
            consentInfo.setDebugNeedConsent(needConsent);
            Log.i(TAG, "SDK setDebugNeedConsent end");
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM,
                "Null parameter was provided. needConsent is null ",
                "");
        }
    }

    private void callSetUnderAgeOfPromise(MethodCall call, MethodChannel.Result result) {
        Boolean ageOfPromise = FromMap.toBoolean("ageOfPromise", call.argument("ageOfPromise"));
        consentInfo.setUnderAgeOfPromise(ageOfPromise);
        result.success(Boolean.TRUE);
    }

    private void callSetConsentStatus(MethodCall call, MethodChannel.Result result) {
        String status = FromMap.toString("status", call.argument("status"));
        if (status != null) {
            ConsentStatus consentStatus = ConsentStatus.valueOf(status);
            Log.i(TAG, "setConsentStatus begin");
            consentInfo.setConsentStatus(consentStatus);
            Log.i(TAG, "setConsentStatus end");
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM,
                "Null parameter was provided. status is null ",
                "");
        }
    }

    private void callRequestConsentUpdate() {
        consentInfo.requestConsentUpdate(new ConsentUpdateListener() {
            @Override
            public void onSuccess(ConsentStatus consentStatus, boolean isNeedConsent, List<AdProvider> list) {
                Log.i(TAG, "onConsentUpdateSuccess");
                Log.d(TAG, "ConsentStatus: " + consentStatus + ", isNeedConsent: " + isNeedConsent);
                methodChannel.invokeMethod("onConsentUpdateSuccess",
                    ToMap.argsToMap("consentStatus", consentStatus.getValue(),
                        "isNeedConsent", isNeedConsent,
                        "adProviders", ToMap.adProviderList(list)));
            }

            @Override
            public void onFail(String description) {
                Log.w(TAG, "onConsentUpdateFail");
                methodChannel.invokeMethod("onConsentUpdateFail", ToMap.argsToMap("description", description));
            }
        });
    }

    private void callUpdateConsentSharedPreferences(MethodCall call, MethodChannel.Result result) {
        String prefKey = FromMap.toString("key", call.argument("key"));
        if (prefKey != null &&
            (prefKey.equals(ConsentConst.SP_CONSENT_KEY) || prefKey.equals(ConsentConst.SP_PROTOCOL_KEY))) {
            Integer prefValue = FromMap.toInteger("value", call.argument("value"));
            if (prefValue != null) {
                SharedPreferences preferences =
                    context.getSharedPreferences(ConsentConst.SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(ConsentConst.SP_PROTOCOL_KEY, prefValue).commit();
                result.success(Boolean.TRUE);
            } else {
                result.error(Constants.ErrorCodes.NULL_PARAM,
                    "Value for the Shared Preference is null.",
                    "");
            }
        } else {
            result.error(Constants.ErrorCodes.INVALID_PARAM,
                "Key for the Shared Preference is either invalid or null. Key: " + prefKey,
                "");
        }
    }

    private void callGetConsentSharedPreferences(MethodCall call, MethodChannel.Result result) {
        String prefKey = FromMap.toString("key", call.argument("key"));
        int defValue;
        if (prefKey != null &&
            (prefKey.equals(ConsentConst.SP_CONSENT_KEY) || prefKey.equals(ConsentConst.SP_PROTOCOL_KEY))) {
            if (prefKey.equals(ConsentConst.SP_CONSENT_KEY)) {
                defValue = ConsentConst.DEFAULT_SP_CONSENT_VALUE;
            } else {
                defValue = ConsentConst.DEFAULT_SP_PROTOCOL_VALUE;
            }

            SharedPreferences preferences =
                context.getSharedPreferences(ConsentConst.SP_NAME, Context.MODE_PRIVATE);
            int value = preferences.getInt(prefKey, defValue);
            result.success(value);
        } else {
            result.error(Constants.ErrorCodes.INVALID_PARAM,
                "Key for the Shared Preference is either invalid or null. Key: " + prefKey,
                "");
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        Splash.destroyAll();
        Banner.destroyAll();
        Interstitial.destroyAll();
        HmsInstallReferrer.disposeAll();
        this.flutterPluginBinding = null;
        methodChannel = null;
        messenger = null;
        activity = null;
        hmsRewardAd = null;
        consentInfo = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        if (flutterPluginBinding != null) {
            onAttachedToEngine(
                flutterPluginBinding.getPlatformViewRegistry(),
                new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), Constants.LIBRARY_CHANNEL),
                flutterPluginBinding.getApplicationContext(),
                flutterPluginBinding.getBinaryMessenger(),
                binding.getActivity());
        }
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        Splash.destroyAll();
        Banner.destroyAll();
        Interstitial.destroyAll();
        HmsInstallReferrer.disposeAll();
        activity = null;
        hmsRewardAd = null;
        consentInfo = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        if (flutterPluginBinding != null) {
            onAttachedToEngine(
                flutterPluginBinding.getPlatformViewRegistry(),
                new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), Constants.LIBRARY_CHANNEL),
                flutterPluginBinding.getApplicationContext(),
                flutterPluginBinding.getBinaryMessenger(),
                binding.getActivity());
        }
    }

    @Override
    public void onDetachedFromActivity() {
        Splash.destroyAll();
        Banner.destroyAll();
        Interstitial.destroyAll();
        HmsInstallReferrer.disposeAll();
        activity = null;
        hmsRewardAd = null;
        consentInfo = null;
    }

    class VerifyAdIdThread extends Thread {
        private MethodCall call;
        private MethodChannel.Result result;

        VerifyAdIdThread(MethodCall call, MethodChannel.Result result) {
            super("verifyAdId");
            this.call = call;
            this.result = result;
        }

        @Override
        public void run() {
            String adId = FromMap.toString("adId", call.argument("adId"));
            Boolean limitTracking = FromMap.toBoolean("limitTracking", call.argument("limitTracking"));
            try {
                final boolean isVerified = AdvertisingIdClient.verifyAdId(activity, adId, limitTracking);
                Log.i(TAG, "AdvertisingIdClient - verifyAdId: " + isVerified);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(isVerified);
                    }
                });
            } catch (AdIdVerifyException e) {
                Log.e(TAG, "ERROR: Ad id verification failed");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        result.error(Integer.toString(AdParam.ErrorCode.INNER), "Ad id verification failed", "");
                    }
                });
            }
        }
    }

    class StartConnectionThread extends Thread {
        private final Integer id;
        private final String callMode;
        private MethodCall call;

        StartConnectionThread(final Integer id, final String callMode, MethodCall call) {
            super("startConnection");
            this.id = id;
            this.callMode = callMode;
            this.call = call;
        }

        @Override
        public void run() {
            final Boolean isTest = FromMap.toBoolean("isTest", call.argument("isTest"));
            if (callMode.equals("sdk")) {
                InstallReferrerSdkUtil sdkUtil = HmsInstallReferrer.createSdkReferrer(id, context, methodChannel);
                if (sdkUtil.getStatus() == HmsInstallReferrer.Status.CREATED ||
                    sdkUtil.getStatus() == HmsInstallReferrer.Status.DISCONNECTED) {
                    sdkUtil.startConnection(isTest);
                } else {
                    Log.i(TAG, "Referrer already connected");
                }
            } else {
                Log.e(Constants.ErrorCodes.INVALID_PARAM, "Call mode parameter is invalid");
            }
        }
    }

    static class EndConnectionThread extends Thread {
        private final HmsInstallReferrer referrer;

        EndConnectionThread(HmsInstallReferrer referrer) {
            super("endConnection");
            this.referrer = referrer;
        }

        @Override
        public void run() {
            referrer.endConnection();
        }
    }

    static class ReferrerDetailsThread extends Thread {
        private final HmsInstallReferrer referrer;
        private final MethodChannel.Result result;

        ReferrerDetailsThread(HmsInstallReferrer referrer, MethodChannel.Result result) {
            super("getReferrerDetails");
            this.referrer = referrer;
            this.result = result;
        }

        @Override
        public void run() {
            referrer.getReferrerDetails(result);
        }
    }

    static class IsReadyThread extends Thread {
        private final HmsInstallReferrer referrer;
        private final MethodChannel.Result result;

        IsReadyThread(HmsInstallReferrer referrer, MethodChannel.Result result) {
            super("isReady");
            this.referrer = referrer;
            this.result = result;
        }

        @Override
        public void run() {
            final boolean isReady = referrer.isReady();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    result.success(isReady);
                }
            });
        }
    }

    interface ConsentConst {
        /**
         * SharedPreferences name.
         */
        String SP_NAME = "HuaweiAdsSdkSharedPreferences";

        /**
         * The SP key of protocol.
         */
        String SP_PROTOCOL_KEY = "protocol";

        /**
         * The SP key of consent.
         */
        String SP_CONSENT_KEY = "consent";

        /**
         * The SP default value of protocol.
         */
        int DEFAULT_SP_PROTOCOL_VALUE = 0;

        /**
         * The SP default value of consent.
         */
        int DEFAULT_SP_CONSENT_VALUE = -1;
    }
}