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
import 'package:huawei_ads/hms_ads.dart';
import 'package:huawei_ads/hms_ads_lib.dart';

enum ConsentUpdateEvent {
  success,
  failed,
}

typedef void ConsentUpdateListener(
  ConsentUpdateEvent event, {
  ConsentStatus consentStatus,
  bool isNeedConsent,
  List<AdProvider> adProviders,
  String description,
});

class Consent {
  Consent._();
  static final Consent _instance = Consent._();
  static Consent get getInstance => _instance;

  ConsentUpdateListener _listener;

  Future<String> getTestDeviceId() async {
    String deviceId =
        await Ads.instance.channel.invokeMethod('getTestDeviceId');
    return deviceId;
  }

  ConsentUpdateListener get listener => _listener;

  Future<bool> addTestDeviceId(String deviceId) {
    return Ads.instance
        .invokeBooleanMethod('addTestDeviceId', {'deviceId': deviceId});
  }

  // DebugNeedConsent constants
  Future<bool> setDebugNeedConsent(DebugNeedConsent needConsent) {
    return Ads.instance.invokeBooleanMethod('setDebugNeedConsent', {
      "needConsent": describeEnum(needConsent)
    });
  }

  Future<bool> setUnderAgeOfPromise(bool ageOfPromise) {
    return Ads.instance.invokeBooleanMethod(
        'setUnderAgeOfPromise', {'ageOfPromise': ageOfPromise});
  }

  Future<bool> setConsentStatus(ConsentStatus status) {
    return Ads.instance.invokeBooleanMethod("setConsentStatus",
        {"status": describeEnum(status)});
  }

  void requestConsentUpdate(final ConsentUpdateListener listener) {
    this._listener = listener;
    Ads.instance.channel.invokeMethod('requestConsentUpdate');
  }

  static Future<bool> updateSharedPreferences(String key, int value) {
    return Ads.instance.invokeBooleanMethod('updateConsentSharedPreferences', {
      'key': key,
      'value': value,
    });
  }

  static Future<int> getSharedPreferences(String key) async {
    int pref = await Ads.instance.channel.invokeMethod('getConsentSharedPreferences', {
      'key': key
    });
    return pref;
  }
}
