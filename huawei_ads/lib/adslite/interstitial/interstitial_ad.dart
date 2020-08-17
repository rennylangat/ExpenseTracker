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
import 'package:huawei_ads/hms_ads.dart';
import 'package:huawei_ads/adslite/reward/reward_ad.dart';
import 'package:huawei_ads/adslite/ad_param.dart';

class InterstitialAd {
  static final Map<int, InterstitialAd> _interstitialAds =
      <int, InterstitialAd>{};
  static Map<int, InterstitialAd> get interstitialAds => _interstitialAds;
  int get id => hashCode;

  static final String _adType = 'Insterstitial';
  final AdParam adParam;
  AdListener listener;
  String adSlotId;
  RewardAdListener _rewardAdListener;

  InterstitialAd({
    @required String adUnitId,
    AdParam adParam,
    AdListener listener,
    RewardAdListener rewardAdListener,
  }) : this.adParam = adParam ?? AdParam.build() {
    adSlotId = adUnitId;
    _interstitialAds[id] = this;
  }

  Future<bool> loadAd() {
    return Ads.instance
        .invokeBooleanMethod("loadInterstitialAd", <String, dynamic>{
      'id': id,
      'adSlotId': adSlotId,
      'adParam': adParam?.toJson(),
      "hasRewardAdListener": (_rewardAdListener != null)
    });
  }

  set setAdListener(AdListener listener) => this.listener = listener;
  AdListener get getAdListener => this.listener;

  set setRewardAdListener(RewardAdListener listener) =>
      _rewardAdListener = listener;

  Future<bool> isLoading() {
    return Ads.instance.invokeBooleanMethod("isAdLoading",
        <String, dynamic>{'id': id, 'adSlotId': adSlotId, "adType": _adType});
  }

  Future<bool> isLoaded() {
    return Ads.instance.invokeBooleanMethod(
        "isAdLoaded", <String, dynamic>{'id': id, "adType": _adType});
  }

  Future<bool> show() async {
    bool result = await Ads.instance.invokeBooleanMethod(
        "showInterstitialAd", <String, dynamic>{'id': id, "adType": _adType});
    return result;
  }

  Future<bool> destroy() async {
    _interstitialAds[id] = null;
    bool result = await Ads.instance.invokeBooleanMethod(
        "destroyAd", <String, dynamic>{'id': id, "adType": _adType});
    return result;
  }
}
