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
import 'package:huawei_ads/installreferrer/referrer_details.dart';
import 'package:huawei_ads/utils/bundle.dart';

enum InstallReferrerStateEvent {
  setupFinished,
  disconnected
}

enum ReferrerResponse {
  disconnected,
  ok,
  unavailable,
  featureNotSupported,
  developerError,
}

enum ReferrerCallMode {
  sdk,
}

typedef InstallReferrerStateListener(InstallReferrerStateEvent event, {ReferrerResponse responseCode});

class InstallReferrerClient {
  static final Map<int, InstallReferrerClient> allReferrers = <int, InstallReferrerClient>{};
  final ReferrerCallMode _callMode = ReferrerCallMode.sdk;
  bool _test = false;
  InstallReferrerStateListener _listener;
  ReferrerDetails _referrerDetails;

  InstallReferrerClient({bool test, InstallReferrerStateListener stateListener}) {
    this._test = test;
    this._listener = stateListener;
    allReferrers[id] = this;
  }

  int get id => hashCode;
  InstallReferrerStateListener get stateListener => _listener;
  set setTest(bool test) => _test = test;

  void startConnection([bool isTest]) {
    Ads.instance.channel.invokeMethod('referrerStartConnection', {
      'id': id,
      'callMode': describeEnum(_callMode),
      'isTest': isTest?? _test,
    });
  }

  void endConnection() {
    Ads.instance.channel.invokeMethod('referrerEndConnection',  {
      'id': id,
      'callMode': describeEnum(_callMode),
    });
  }

  Future<bool> isReady() {
    return Ads.instance.invokeBooleanMethod('referrerIsReady',  {
      'id': id,
      "callMode": describeEnum(_callMode),
    });
  }

  Future<ReferrerDetails> get getInstallReferrer async {
    dynamic referrer = await Ads.instance.channel.invokeMethod('getInstallReferrer', {
      'id': id,
      'callMode': describeEnum(_callMode),
    });
    if (referrer != null) {
      Bundle bundle = new Bundle();
      bundle.putString(ReferrerDetails.keyInstallReferrer, referrer[ReferrerDetails.keyInstallReferrer]?? null);
      bundle.putInt(ReferrerDetails.keyReferrerClickTimeStamp, referrer[ReferrerDetails.keyReferrerClickTimeStamp]?? null);
      bundle.putInt(ReferrerDetails.keyInstallBeginTimeStamp, referrer[ReferrerDetails.keyInstallBeginTimeStamp]?? null);
      _referrerDetails = ReferrerDetails(bundle);
    }
    return _referrerDetails;
  }
}