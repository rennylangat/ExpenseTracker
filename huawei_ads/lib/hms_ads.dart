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

import 'dart:async';
import 'package:huawei_ads/hms_ads_lib.dart';

import 'adslite/splash/splash_ad.dart';
import 'package:huawei_ads/adslite/reward/reward_ad.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/cupertino.dart';
import 'package:huawei_ads/adslite/ad_param.dart';

class Ads {
  MethodChannel _channel;
  Ads.private(MethodChannel channel) {
    _channel = channel;
    _channel.setMethodCallHandler(_handleMethod);
  }

  static final Ads _instance = Ads.private(
    const MethodChannel(LIBRARY_CHANNEL),
  );

  static Ads get instance => _instance;
  MethodChannel get channel => _channel;

  static const Map<String, ConsentUpdateEvent> _methodToConsentUpdateEvent =
      <String, ConsentUpdateEvent>{
    'onConsentUpdateSuccess': ConsentUpdateEvent.success,
    'onConsentUpdateFail': ConsentUpdateEvent.failed,
  };

  static const Map<String, SplashAdLoadEvent> _methodToSplashAdLoadEvent =
      <String, SplashAdLoadEvent>{
    'onSplashAdLoaded': SplashAdLoadEvent.loaded,
    'onSplashAdDismissed': SplashAdLoadEvent.dismissed,
    'onSplashAdFailedToLoad': SplashAdLoadEvent.failedToLoad,
  };

  static const Map<String, SplashAdDisplayEvent> _methodToSplashAdDisplayEvent =
      <String, SplashAdDisplayEvent>{
    'onSplashAdShowed': SplashAdDisplayEvent.showed,
    'onSplashAdClick': SplashAdDisplayEvent.click,
  };

  static const Map<String, AdEvent> _methodToHmsAdEvent = <String, AdEvent>{
    'onAdLoaded': AdEvent.loaded,
    'onAdFailed': AdEvent.failed,
    'onAdClicked': AdEvent.clicked,
    'onAdImpression': AdEvent.impression,
    'onAdOpened': AdEvent.opened,
    'onAdLeave': AdEvent.leave,
    'onAdClosed': AdEvent.closed,
  };

  static const Map<String, RewardAdEvent> _methodToRewardAdEvent =
      <String, RewardAdEvent>{
    'onRewarded': RewardAdEvent.rewarded,
    'onRewardAdClosed': RewardAdEvent.closed,
    'onRewardAdFailedToLoad': RewardAdEvent.failedToLoad,
    'onRewardAdLeftApp': RewardAdEvent.leftApplication,
    'onRewardAdLoaded': RewardAdEvent.loaded,
    'onRewardAdOpened': RewardAdEvent.opened,
    'onRewardAdStarted': RewardAdEvent.started,
    'onRewardAdCompleted': RewardAdEvent.completed,
    'onRewardAdFailedToShow': RewardAdEvent.failedToShow
  };

  static const Map<String, InstallReferrerStateEvent>
      _methodToReferrerStateEvent = <String, InstallReferrerStateEvent>{
    'onInstallReferrerSetupFinished': InstallReferrerStateEvent.setupFinished,
    'onInstallReferrerSetupDisconnected':
        InstallReferrerStateEvent.disconnected,
  };

  static const Map<int, ReferrerResponse> _codeToReferrerResponse =
      <int, ReferrerResponse>{
    -1: ReferrerResponse.disconnected,
    0: ReferrerResponse.ok,
    1: ReferrerResponse.unavailable,
    2: ReferrerResponse.featureNotSupported,
    3: ReferrerResponse.developerError,
  };

  Future<dynamic> _handleMethod(MethodCall call) {
    final Map<dynamic, dynamic> argumentsMap = call.arguments;
    final SplashAdLoadEvent splashLoadEvent =
        _methodToSplashAdLoadEvent[call.method];
    final SplashAdDisplayEvent splashDisplayEvent =
        _methodToSplashAdDisplayEvent[call.method];
    final RewardAdEvent rewardAdEvent = _methodToRewardAdEvent[call.method];
    final InstallReferrerStateEvent referrerEvent =
        _methodToReferrerStateEvent[call.method];
    final ConsentUpdateEvent consentEvent =
        _methodToConsentUpdateEvent[call.method];

    // SplashAd
    if (splashLoadEvent != null || splashDisplayEvent != null) {
      final int id = argumentsMap['id'];
      if (id != null && SplashAd.splashAds[id] != null) {
        final SplashAd ad = SplashAd.splashAds[id];
        if (ad.displayListener != null && splashDisplayEvent != null) {
          ad.displayListener(splashDisplayEvent);
        } else if (ad.loadListener != null) {
          splashLoadEvent == SplashAdLoadEvent.failedToLoad
              ? ad.loadListener(splashLoadEvent,
                  errorCode: argumentsMap['errorCode'])
              : ad.loadListener(splashLoadEvent);
        }
      }
    }
    // RewardAd
    else if (rewardAdEvent != null) {
      if (RewardAd.instance.getRewardAdListener != null) {
        if (rewardAdEvent == RewardAdEvent.failedToShow ||
            rewardAdEvent == RewardAdEvent.failedToLoad) {
          RewardAd.instance.getRewardAdListener(rewardAdEvent,
              errorCode: argumentsMap["errorCode"]);
        } else if (rewardAdEvent == RewardAdEvent.rewarded) {
          RewardAd.instance.getRewardAdListener(rewardAdEvent,
              reward: Reward.fromJson(call.arguments));
        } else {
          RewardAd.instance.getRewardAdListener(rewardAdEvent);
        }
      }
    } else {
      // BannerAd & InterstitialAd
      final int id = argumentsMap['id'];
      if (id != null) {
        final AdEvent event = _methodToHmsAdEvent[call.method];
        if(BannerAd.bannerAds[id] != null) {
          final BannerAd bannerAd = BannerAd.bannerAds[id];
          if (event != null && bannerAd.listener != null) {
            event == AdEvent.failed
                ? bannerAd.listener(event, errorCode: argumentsMap['errorCode'])
                : bannerAd.listener(event);
          }
        } else {
          final InterstitialAd interstitialAd = InterstitialAd.interstitialAds[id];
          if (event != null && interstitialAd.listener != null) {
            event == AdEvent.failed
                ? interstitialAd.listener(event, errorCode: argumentsMap['errorCode'])
                : interstitialAd.listener(event);
          }
        }
      }
    }

    // Install referrer
    if (referrerEvent != null) {
      final int id = argumentsMap['id'];
      if (id != null && InstallReferrerClient.allReferrers[id] != null) {
        final InstallReferrerClient client =
            InstallReferrerClient.allReferrers[id];
        if (client.stateListener != null) {
          if (referrerEvent == InstallReferrerStateEvent.setupFinished &&
              argumentsMap['responseCode'] != null) {
            ReferrerResponse response =
                _codeToReferrerResponse[argumentsMap['responseCode']];
            client.stateListener(referrerEvent, responseCode: response);
          } else
            client.stateListener(referrerEvent);
        }
      }
    }

    // Consent
    if (consentEvent != null && Consent.getInstance.listener != null) {
      ConsentUpdateListener listener = Consent.getInstance.listener;
      if (consentEvent == ConsentUpdateEvent.success) {
        int status = call.arguments['consentStatus'];
        bool isNeedConsent = call.arguments['isNeedConsent'];
        List<dynamic> mapList = call.arguments['adProviders'];
        List<AdProvider> adProviders = AdProvider.buildList(mapList);

        listener(consentEvent,
            consentStatus: ConsentStatus.values.elementAt(status),
            isNeedConsent: isNeedConsent,
            adProviders: adProviders);
      } else
        listener(consentEvent);
    }

    return Future<dynamic>.value(null);
  }

  Future<bool> invokeBooleanMethod(String method, [dynamic arguments]) async {
    final bool result = await Ads.instance._channel.invokeMethod(
      method,
      arguments,
    );
    return result;
  }
}

typedef void AdListener(AdEvent event, {int errorCode});
