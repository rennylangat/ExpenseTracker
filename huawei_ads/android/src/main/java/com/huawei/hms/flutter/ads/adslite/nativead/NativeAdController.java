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
package com.huawei.hms.flutter.ads.adslite.nativead;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.DislikeAdReason;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.flutter.ads.factory.AdParamFactory;
import com.huawei.hms.flutter.ads.utils.Constants;
import com.huawei.hms.flutter.ads.utils.FromMap;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class NativeAdController extends AdListener implements MethodChannel.MethodCallHandler, NativeAd.NativeAdLoadedListener, NativeAdViewListener {
    private static final String TAG = "NativeAdController";
    private String id;
    private MethodChannel channel;
    private Context context;

    private NativeAd nativeAd;
    private NativeAdChangedListener nativeAdChangedListener;
    private NativeAdLoader nativeAdLoader;
    private HmsNativeView hmsNativeView;
    private String adSlotId;
    private Map<String, Object> adParam;

    NativeAdController(String id, MethodChannel channel, Context context) {
        this.id = id;
        this.channel = channel;
        this.context = context;
        channel.setMethodCallHandler(this);
    }

    NativeAd getNativeAd() {
        return nativeAd;
    }

    void setNativeAdChangedListener(NativeAdChangedListener listener) {
        nativeAdChangedListener = listener;
    }

    boolean checkId(String id) {
        return this.id.equals(id);
    }

    @Override
    public void onMethodCall(final MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "setNativeAdSlotId":
                callSetAdSlotId(call, result);
                break;
            case "dislikeAd":
                callDislikeAd(call, result);
                break;
            case "setAllowCustomClick":
                nativeAd.setAllowCustomClick();
                result.success(Boolean.TRUE);
                break;
            case "isCustomClickAllowed":
                result.success(nativeAd.isCustomClickAllowed());
                break;
            case "isCustomDislikeThisAdEnable":
                result.success(nativeAd.isCustomDislikeThisAdEnabled());
                break;
            case "triggerClick":
                nativeAd.triggerClick(FromMap.toBundle(call.arguments));
                result.success(Boolean.TRUE);
                break;
            case "recordClickEvent":
                nativeAd.recordClickEvent();
                result.success(Boolean.TRUE);
                break;
            case "recordImpressionEvent":
                nativeAd.recordImpressionEvent(FromMap.toBundle(call.arguments));
                result.success(Boolean.TRUE);
                break;
            case "isLoading":
                result.success(nativeAdLoader.isLoading());
                break;
            case "gotoWhyThisAdPage":
                callGoToWhy(result);
                break;
            default:
                onNativeGetterMethodCall(call, result);
        }
    }

    private void onNativeGetterMethodCall(final MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "getAdSource":
                result.success(nativeAd.getAdSource());
                break;
            case "getDescription":
                result.success(nativeAd.getDescription());
                break;
            case "getCallToAction":
                result.success(nativeAd.getCallToAction());
                break;
            case "getDislikeAdReasons":
                callGetDislikeAdReasons(call, result);
                break;
            case "getTitle":
                result.success(nativeAd.getTitle());
                break;
            case "getVideoOperator":
                result.success(nativeAd.getVideoOperator() != null);
                break;
            default:
                onVideoMethodCall(call, result);
        }
    }

    private void onVideoMethodCall(final MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "getAspectRatio":
                callGetAspectRatio(result);
                break;
            case "hasVideo":
                result.success(nativeAd.getVideoOperator() != null
                    && nativeAd.getVideoOperator().hasVideo());
                break;
            case "isCustomOperateEnabled":
                result.success(nativeAd.getVideoOperator() != null
                    && nativeAd.getVideoOperator().isCustomizeOperateEnabled());
                break;
            case "isMuted":
                result.success(nativeAd.getVideoOperator() != null
                    && nativeAd.getVideoOperator().isMuted());
                break;
            case "mute":
                callMute(call, result);
                break;
            case "pause":
                callPause(result);
                break;
            case "play":
                callPlay(result);
                break;
            case "stop":
                callStop(result);
                break;
            default:
                result.notImplemented();
        }
    }

    private void callGetAspectRatio(MethodChannel.Result result) {
        if (nativeAd.getVideoOperator() != null) {
            result.success(nativeAd.getVideoOperator().getAspectRatio());
        }
    }

    private void callGoToWhy(MethodChannel.Result result) {
        if (hmsNativeView != null && hmsNativeView.getNativeView() != null) {
            hmsNativeView.getNativeView().gotoWhyThisAdPage();
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "NativeView is null", "");
        }
    }

    private void callDislikeAd(final MethodCall call, MethodChannel.Result result) {
        nativeAd.dislikeAd(new DislikeAdListenerImpl(call));
        result.success(Boolean.TRUE);
    }

    private void callMute(MethodCall call, MethodChannel.Result result) {
        Boolean mute = FromMap.toBoolean("mute", call.argument("mute"));
        if (nativeAd.getVideoOperator() != null) {
            nativeAd.getVideoOperator().mute(mute);
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Video Operator or boolean parameter was null. isMute : " + mute, "");
        }
    }

    private void callPause(MethodChannel.Result result) {
        if (nativeAd.getVideoOperator() != null) {
            nativeAd.getVideoOperator().pause();
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Video Operator is null", "");
        }
    }

    private void callPlay(MethodChannel.Result result) {
        if (nativeAd.getVideoOperator() != null) {
            nativeAd.getVideoOperator().play();
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Video Operator is null", "");
        }
    }

    private void callStop(MethodChannel.Result result) {
        if (nativeAd.getVideoOperator() != null) {
            nativeAd.getVideoOperator().stop();
            result.success(Boolean.TRUE);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Video Operator is null", "");
        }
    }

    private void callSetAdSlotId(MethodCall call, MethodChannel.Result result) {
        String newAdId = FromMap.toString("adSlotId", call.argument("adSlotId"));
        boolean unitIdChanged = newAdId != null && newAdId.equals(adSlotId);
        Map<String, Object> adParamMap = ToMap.objectToMap(call.argument("adParam"));
        Map<String, Object> adConfigurationMap = ToMap.objectToMap(call.argument("adConfiguration"));
        if (newAdId != null) {
            if (unitIdChanged) {
                adSlotId = newAdId;
            }
            if (nativeAdLoader == null || unitIdChanged) {
                NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, newAdId);
                nativeAdLoader = builder
                    .setNativeAdOptions(generateNativeAdConfiguration(adConfigurationMap))
                    .setNativeAdLoadedListener(this)
                    .setAdListener(this).build();
                if (nativeAd == null || unitIdChanged) {
                    adParam = adParamMap;
                    loadAd();
                } else {
                    invokeNativeAdLoaded();
                }
            }
            result.success(true);
        } else {
            result.error(Constants.ErrorCodes.NULL_PARAM, "Unit id for the native ad was null", null);
        }
    }

    private void loadAd() {
        channel.invokeMethod("onAdLoading", null);
        if (nativeAdLoader != null) {
            AdParamFactory factory;
            if (adParam != null) {
                factory = new AdParamFactory(adParam);
            } else {
                factory = new AdParamFactory(new HashMap<String, Object>());
            }

            AdParam param = factory.createAdParam();
            nativeAdLoader.loadAd(param);
        }
    }

    private void invokeNativeAdLoaded() {
        if (nativeAdChangedListener != null) {
            nativeAdChangedListener.onNativeAdChanged(nativeAd);
        }
    }

    private void callGetDislikeAdReasons(MethodCall call, MethodChannel.Result result) {
        List<DislikeAdReason> reasonsList = nativeAd.getDislikeAdReasons();
        List<String> responseList = new ArrayList<String>();
        if (reasonsList != null) {
            for (DislikeAdReason dislikeAdReason : reasonsList) {
                responseList.add(dislikeAdReason.getDescription());
            }
        }
        result.success(responseList);
    }

    @Override
    public void onNativeAdLoaded(NativeAd nativeAd) {
        Log.i(TAG, "onNativeAdLoaded");
        nativeAd.setDislikeAdListener(new DislikeAdListener() {
            @Override
            public void onAdDisliked() {
                Log.i(TAG, "onAdDisliked");
                channel.invokeMethod("onAdDisliked", null);
            }
        });

        VideoOperator videoOperator = nativeAd.getVideoOperator();
        if (videoOperator != null && videoOperator.hasVideo()) {
            videoOperator.setVideoLifecycleListener(new VideoOperator.VideoLifecycleListener() {
                @Override
                public void onVideoStart() {
                    Log.i(TAG, "onVideoStart");
                    channel.invokeMethod("onVideoStart", null);
                }

                @Override
                public void onVideoPlay() {
                    Log.i(TAG, "onVideoPlay");
                    channel.invokeMethod("onVideoStart", null);
                }

                @Override
                public void onVideoPause() {
                    Log.i(TAG, "onVideoPause");
                    channel.invokeMethod("onVideoPause", null);
                }

                @Override
                public void onVideoEnd() {
                    Log.i(TAG, "onVideoEnd");
                    channel.invokeMethod("onVideoEnd", null);
                }

                @Override
                public void onVideoMute(boolean isMuted) {
                    Log.i(TAG, "onVideoMute");
                    channel.invokeMethod("onVideoMute", ToMap.argsToMap("isMuted", isMuted));
                }
            });
        }

        this.nativeAd = nativeAd;
        channel.invokeMethod("onAdLoaded", null);
    }

    @Override
    public void onAdOpened() {
        Log.i(TAG, "onAdOpened");
        channel.invokeMethod("onAdOpened", null);
    }

    @Override
    public void onAdClicked() {
        Log.i(TAG, "onAdClicked");
        channel.invokeMethod("onAdClicked", null);
    }

    @Override
    public void onAdImpression() {
        Log.i(TAG, "onAdImpression");
        channel.invokeMethod("onAdImpression", null);
    }

    @Override
    public void onAdClosed() {
        Log.i(TAG, "onAdClosed");
        channel.invokeMethod("onAdClosed", null);
    }

    @Override
    public void onAdLeave() {
        Log.i(TAG, "onAdLeave");
        channel.invokeMethod("onAdLeave", null);
    }

    @Override
    public void onAdFailed(int errorCode) {
        Log.e(TAG, "onAdFailed with error code: " + errorCode);
        channel.invokeMethod("onAdFailed", ToMap.argsToMap("errorCode", errorCode));
    }

    @Override
    public void onNativeControllerSet(HmsNativeView hmsNativeView) {
        this.hmsNativeView = hmsNativeView;
    }

    @Override
    public void onNativeViewDestroyed() {
        this.hmsNativeView = null;
    }

    private NativeAdConfiguration generateNativeAdConfiguration(Map<String, Object> adConfigurationMap) {
        NativeAdConfigurationFactory adConFactory;
        if (adConfigurationMap != null) {
            adConFactory = new NativeAdConfigurationFactory(adConfigurationMap);
        } else {
            adConFactory = new NativeAdConfigurationFactory(new HashMap<String, Object>());
        }

        return adConFactory.createNativeAdConfiguration().build();
    }

    static class DislikeAdListenerImpl implements DislikeAdReason {
        MethodCall call;

        DislikeAdListenerImpl(MethodCall call) {
            this.call = call;
        }

        @Override
        public String getDescription() {
            return call.argument("reason");
        }
    }
}