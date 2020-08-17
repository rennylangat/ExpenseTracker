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
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:huawei_ads/adslite/ad_param.dart';
import 'package:huawei_ads/hms_ads.dart';

typedef void SplashAdLoadListener(SplashAdLoadEvent event, {int errorCode});
typedef void SplashAdDisplayListener(SplashAdDisplayEvent event);

enum SplashAdLoadEvent {
  // Load Events
  loaded,
  dismissed,
  failedToLoad
}

enum SplashAdDisplayEvent {
  // Display Events
  showed,
  click,
}

enum SplashAdType { above, below, aboveNoLogo, belowNoLogo }

class SplashAd {
  static final Map<int, SplashAd> splashAds = <int, SplashAd>{};
  static final String _adType = 'Splash';
  final SplashAdType adType;
  String ownerText;
  String footerText;
  String _logoResId;
  String _logoBgResId;
  String _mediaNameResId;
  String _sloganResId;
  String _wideSloganResId;
  int _audioFocusType;
  SplashAdDisplayListener displayListener;
  SplashAdLoadListener loadListener;

  SplashAd({
    @required this.adType,
    this.displayListener,
    this.loadListener,
    this.ownerText,
    this.footerText,
    String logoResId,
    String logoBgResId,
    String mediaNameResId,
    String sloganResId,
    String wideSloganResId,
    int audioFocusType,
  }) {
    splashAds[id] = this;
   _logoResId = logoResId;
   _logoBgResId = logoBgResId;
   _mediaNameResId = mediaNameResId;
   _sloganResId = sloganResId;
   _wideSloganResId = wideSloganResId;
   _audioFocusType = audioFocusType;
  }

  int get id => hashCode;

  set setLogoResId(String logoResId) => _logoResId = logoResId;
  set setLogoBgResId(String logoBgResId) => _logoResId = logoBgResId;
  set setMediaNameResId(String mediaNameResId) => _mediaNameResId = _logoResId;
  set setSloganResId(String resId) => _sloganResId = resId;
  set setWideSloganResId(String resId) => _wideSloganResId = resId;
  set setAudioFocusType(int audioFocusType) => _audioFocusType = audioFocusType;

  static Future<bool> preloadAd(
      {@required String adSlotId, int orientation, AdParam adParam}) {
    return Ads.instance.invokeBooleanMethod("preloadSplashAd", <String, dynamic>{
      'adSlotId': adSlotId,
      'adParam': adParam?.toJson(),
    });
  }

  Future<bool> loadAd(
      {@required String adUnitId, @required int orientation, @required AdParam adParam, double topMargin = 0.0}) {
    return Ads.instance.invokeBooleanMethod("loadSplashAd", <String, dynamic>{
      'id': id,
      'adSlotId': adUnitId,
      'adType': describeEnum(adType),
      'orientation': orientation,
      'adParam': adParam?.toJson(),
      "resources": _resourcesToJson(),
      "audioFocusType": _audioFocusType,
      "topMargin": topMargin,
      "owner": ownerText,
      "footer": footerText,
    });
  }

  Future<bool> isLoading() {
    return Ads.instance.invokeBooleanMethod("isAdLoading", <String, dynamic>{
      'id': id,
      "adType": _adType
    });
  }

  Future<bool> isLoaded() {
    return Ads.instance.invokeBooleanMethod("isAdLoaded", <String, dynamic>{
      'id': id,
      "adType": _adType
    });
  }

  Future<bool> pause() {
    return Ads.instance.invokeBooleanMethod("pauseAd", <String, dynamic>{
      'id': id,
      "adType": _adType
    });
  }

  Future<bool> resume() {
    return Ads.instance.invokeBooleanMethod("resumeAd", <String, dynamic>{
      'id': id,
      "adType": _adType
    });
  }

  Future<bool> destroy() {
    splashAds[id] = null;
    return Ads.instance
        .invokeBooleanMethod("destroyAd", <String, dynamic>{'id': id});
  }

  Map<String, dynamic> _resourcesToJson() {
    final Map<String, dynamic> json = new Map<String, dynamic>();
    if (_logoResId != null) json['logoResId'] = _logoResId;
    if (_logoBgResId != null) json['logoBgResId'] = _logoBgResId;
    if (_mediaNameResId != null) json['mediaNameResId'] = _mediaNameResId;
    if (_sloganResId != null) json['sloganResId'] = _sloganResId;
    if (_wideSloganResId != null) json['wideSloganResId'] = _wideSloganResId;
    if (ownerText != null) json['ownerText'] = ownerText;
    if (footerText != null) json['footerText'] = footerText;
    return json;
  }
}