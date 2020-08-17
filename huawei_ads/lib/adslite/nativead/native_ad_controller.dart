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
import 'package:huawei_ads/adslite/video_operator.dart';
import 'package:huawei_ads/adslite/nativead/dislike_ad_reason.dart';
import 'package:huawei_ads/utils/bundle.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:huawei_ads/hms_ads.dart';

enum NativeAdLoadState { loading, loaded, failed }
typedef void NativeAdStateListener(NativeAdLoadState state);
typedef void DislikeAdListener(AdEvent event);

class NativeAdController {
  NativeAdStateListener loadListener;
  MethodChannel _channel;
  NativeAdConfiguration _adConfiguration;
  AdParam _adParam;
  AdListener _listener;
  DislikeAdListener _dislikeListener;
  VideoOperator _videoOperator;

  final _key = UniqueKey();
  String get id => _key.toString();

  NativeAdController(
      {NativeAdConfiguration adConfiguration,
      AdParam adParam,
      AdListener listener,
      DislikeAdListener dislikeListener}) {
    _channel = MethodChannel(id);
    _channel.setMethodCallHandler(_handleMethod);
    _adConfiguration = adConfiguration;
    _adParam = adParam;
    _listener = listener;
    _dislikeListener = dislikeListener;

    Ads.instance.channel.invokeMethod("initNativeAdController", {
      "id": id,
    });
  }

  NativeAdConfiguration get _configuration =>
      _adConfiguration ?? NativeAdConfiguration.build();
  AdParam get adParam => _adParam ?? AdParam.build();

  set setAdListener(AdListener listener) => _listener = listener;
  set setDislikeAdListener(DislikeAdListener listener) =>
      _dislikeListener = listener;

  Future<Null> _handleMethod(MethodCall call) async {
    final Map<dynamic, dynamic> argumentsMap = call.arguments;
    final AdEvent event = _methodToAdEvent[call.method];
    final VideoLifecycleEvent videoEvent =
        _methodToVideoLifecycleEvent[call.method];

    if (event != null && _listener != null)
      event == AdEvent.failed
          ? _listener(event, errorCode: argumentsMap['errorCode'])
          : _listener(event);

    if (event == AdEvent.disliked && _dislikeListener != null)
      _dislikeListener(event);

    if (videoEvent != null && _videoOperator.getVideoLifecycleListener != null)
      videoEvent == VideoLifecycleEvent.mute
          ? _videoOperator.getVideoLifecycleListener(videoEvent,
              isMuted: call.arguments["isMuted"])
          : _videoOperator.getVideoLifecycleListener(videoEvent);

    switch (call.method) {
      case "onAdLoading":
        loadListener(NativeAdLoadState.loading);
        break;

      case "onAdFailed":
        loadListener(NativeAdLoadState.failed);
        break;

      case "onAdLoaded":
        loadListener(NativeAdLoadState.loaded);
        break;
    }
  }

  void setAdSlotId(String adSlotId) {
    _channel.invokeMethod("setNativeAdSlotId", {
      "adSlotId": adSlotId,
      "adParam": adParam.toJson(),
      "adConfiguration": _configuration.toJson()
    });
  }

  Future<VideoOperator> getVideoOperator() async {
    bool hasOperator = await _channel.invokeMethod("getVideoOperator");
    if (hasOperator) _videoOperator = VideoOperator(_channel);
    return _videoOperator ?? null;
  }

  void gotoWhyThisAdPage() {
    _channel.invokeMethod("gotoWhyThisAdPage");
  }

  Future<bool> isLoading() async {
    bool isLoading = await _channel.invokeMethod("isLoading");
    return isLoading;
  }

  void setAllowCustomClick() {
    _channel.invokeMethod("setAllowCustomClick");
  }

  Future<bool> isCustomClickAllowed() async {
    bool isAllowed = await _channel.invokeMethod("isCustomClickAllowed");
    return isAllowed;
  }

  Future<String> getAdSource() async {
    String source = await _channel.invokeMethod("getAdSource");
    return source;
  }

  Future<String> getDescription() async {
    String description = await _channel.invokeMethod("getDescription");
    return description;
  }

  Future<String> getCallToAction() async {
    String callToAction = await _channel.invokeMethod("getCallToAction");
    return callToAction;
  }

  Future<String> getTitle() async {
    String title = await _channel.invokeMethod("getTitle");
    return title;
  }

  Future<bool> dislikeAd(DislikeAdReason reason) {
    return _channel
        .invokeMethod("dislikeAd", {"reason": reason.getDescription});
  }

  Future<bool> isCustomDislikeThisAdEnable() async {
    bool isEnabled = await _channel.invokeMethod("isCustomDislikeThisAdEnable");
    return isEnabled;
  }

  Future<bool> triggerClick(Bundle bundle) {
    return _channel.invokeMethod("triggerClick", bundle.bundle);
  }

  Future<bool> recordClickEvent() {
    return _channel.invokeMethod("recordClickEvent");
  }

  Future<bool> recordImpressionEvent(Bundle bundle) {
    return _channel.invokeMethod("recordImpressionEvent", bundle.bundle);
  }

  Future<List<DislikeAdReason>> getDislikeAdReasons() async {
    List<String> reasonsList = await _channel.invokeMethod("getDislikeReasons");
    List<DislikeAdReason> responseList = new List<DislikeAdReason>();
    if (reasonsList != null) {
      reasonsList.forEach((String reason) {
        responseList.add(new DislikeAdReason(reason));
      });
    }
    return responseList;
  }

  Future<bool> destroy() {
    return Ads.instance.invokeBooleanMethod("destroyNativeAdController", {
      "id": id,
    });
  }

  // Known duplicate
  static const Map<String, AdEvent> _methodToAdEvent = <String, AdEvent>{
    'onAdLoaded': AdEvent.loaded,
    'onAdFailed': AdEvent.failed,
    'onAdClicked': AdEvent.clicked,
    'onAdImpression': AdEvent.impression,
    'onAdOpened': AdEvent.opened,
    'onAdLeave': AdEvent.leave,
    'onAdClosed': AdEvent.closed,
    'onAdDisliked': AdEvent.disliked,
  };

  static const Map<String, VideoLifecycleEvent> _methodToVideoLifecycleEvent =
      <String, VideoLifecycleEvent>{
    "onVideoEnd": VideoLifecycleEvent.end,
    "onVideoMute": VideoLifecycleEvent.mute,
    "onVideoPause": VideoLifecycleEvent.pause,
    "onVideoPlay": VideoLifecycleEvent.play,
    "onVideoStart": VideoLifecycleEvent.start,
  };
}