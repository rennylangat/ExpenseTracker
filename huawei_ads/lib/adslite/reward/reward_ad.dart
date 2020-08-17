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

import 'package:huawei_ads/adslite/ad_param.dart';
import 'package:huawei_ads/adslite/reward/reward_verify_config.dart';
import 'package:flutter/cupertino.dart';
import 'package:huawei_ads/hms_ads.dart';

enum RewardAdEvent {
  loaded,
  failedToLoad,
  opened,
  leftApplication,
  closed,
  rewarded,
  started,
  completed,
  failedToShow
}

typedef void RewardAdListener(RewardAdEvent event, {Reward reward, int errorCode});

class Reward {
  String _name;
  int _amount;

  Reward({String name, int amount}) {
    _name = name;
    _amount = amount;
  }

  String get getName => _name;
  int get getAmount => _amount;

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};
    if(_name != null)
      json['name'] = _name;
    if (_amount != null)
      json['amount'] = _amount;

    return json;
  }

  static Reward fromJson(Map<dynamic, dynamic> args) {
    String name = args['name']?? null;
    int amount = args['amount']?? 0;

    return new Reward(name: name, amount: amount);
  }
}

class RewardAd {
  static final String _adType = 'Reward';
  RewardAd._();
  static final RewardAd _instance = RewardAd._();
  static RewardAd get instance => _instance;

  String _userId;
  String _data;
  RewardVerifyConfig _rewardVerifyConfig;
  RewardAdListener _listener;

  String get getUserId => _userId;
  String get getData => _data;
  RewardAdListener get getRewardAdListener => _listener;
  Future<Reward> getReward() async {
    Map<dynamic, dynamic> args = await Ads.instance.channel.invokeMethod("getRewardAdReward");
    return Reward.fromJson(args);
  }

  set setRewardAdListener(RewardAdListener listener) => _listener = listener;
  set setRewardVerifyConfig(RewardVerifyConfig options) => _rewardVerifyConfig = options;

  Future<bool> setUserId(String userId) async {
    bool isSuccess = await Ads.instance.invokeBooleanMethod("setRewardAdUserId", <String, dynamic>{
      'userId': userId,
    });
    if(isSuccess) {
      _userId = userId;
      return true;
    } else {
      print('Failed to set userId');
      return false;
    }
  }
  Future<bool> setData(String data) async {
    bool isSuccess = await Ads.instance.invokeBooleanMethod("setRewardAdData", <String, dynamic>{
      'data': data,
    });
    if(isSuccess) {
      _data = data;
      return true;
    } else {
      print('Failed to set data');
      return false;
    }
  }

  Future<bool> show() {
    return Ads.instance.invokeBooleanMethod("showRewardAd");
  }

  Future<bool> loadAd(
      {@required String adUnitId, @required AdParam adParam}) {
    return Ads.instance.invokeBooleanMethod("loadRewardAd", <String, dynamic>{
      'adSlotId': adUnitId,
      'adParam': adParam?.toJson(),
      'rewardVerifyConfig' : _rewardVerifyConfig?.toJson(),
    });
  }

  Future<bool> isLoaded() {
    return Ads.instance.invokeBooleanMethod("isAdLoaded", <String, dynamic>{
      "adType": _adType
    });
  }

  Future<bool> pause() {
    return Ads.instance.invokeBooleanMethod("pauseAd", <String, dynamic>{
      "adType": _adType
    });
  }

  Future<bool> resume() {
    return Ads.instance.invokeBooleanMethod("resumeAd", <String, dynamic>{
      "adType": _adType
    });
  }
}

