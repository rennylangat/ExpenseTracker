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

import 'package:flutter/foundation.dart';
import 'package:huawei_ads/adslite/ad_param.dart';
import 'package:flutter/cupertino.dart';
import 'package:huawei_ads/hms_ads.dart';
import 'package:huawei_ads/adslite/banner/banner_ad_size.dart';

enum AnchorType { bottom, top }

class BannerAd {
  static final Map<int, BannerAd> _bannerAds = <int, BannerAd>{};
  static Map<int, BannerAd> get bannerAds => _bannerAds;
  int get id => hashCode;

  static final String _adType = 'Banner';
  final AdParam adParam;
  AdListener listener;
  String adSlotId;
  BannerAdSize size;
  int bannerRefreshTime;

  BannerAd({
    @required String adUnitId,
    @required this.size,
    this.listener,
    this.bannerRefreshTime,
    AdParam adParam,
  }) : this.adParam = adParam ?? AdParam.build() {
    adSlotId = adUnitId;
    _bannerAds[id] = this;
  }

  set setAdListener(AdListener listener) => this.listener = listener;
  AdListener get getAdListener => this.listener;

  Future<bool> loadAd() {
    return Ads.instance.invokeBooleanMethod("loadBannerAd", <String, dynamic>{
      'id': id,
      'adSlotId': adSlotId,
      'adParam': adParam?.toJson(),
      'width': size.width,
      'height': size.height,
      'refreshTime': bannerRefreshTime,
    });
  }

  Future<bool> isLoading() {
    return Ads.instance.invokeBooleanMethod("isAdLoading",
        <String, dynamic>{'id': id, 'adSlotId': adSlotId, "adType": _adType});
  }

  Future<bool> pause() {
    return Ads.instance.invokeBooleanMethod("pauseAd",
        <String, dynamic>{'id': id, 'adSlotId': adSlotId, "adType": _adType});
  }

  Future<bool> resume() {
    return Ads.instance.invokeBooleanMethod("resumeAd",
        <String, dynamic>{'id': id, 'adSlotId': adSlotId, "adType": _adType});
  }

  Future<bool> show(
      {double offset = 0.0, AnchorType anchorType = AnchorType.bottom}) async {
    bool result = await Ads.instance.invokeBooleanMethod(
        "showBannerAd", <String, dynamic>{
      'id': id,
      'offset': offset.toString(),
      'anchorType': describeEnum(anchorType),
      "adType": _adType
    });
    return result;
  }

  Future<bool> destroy() async {
    _bannerAds[id] = null;
    bool result = await Ads.instance.invokeBooleanMethod(
        "destroyAd", <String, dynamic>{'id': id, "adType": _adType});
    return result;
  }
}
