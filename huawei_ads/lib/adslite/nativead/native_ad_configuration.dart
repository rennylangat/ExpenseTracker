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
import 'package:huawei_ads/adslite/ad_size.dart';
import 'package:huawei_ads/adslite/video_configuration.dart';

class NativeAdConfiguration {
  AdSize _adSize;
  int _choicesPosition;
  int _direction;
  int _aspect;
  bool _requestCustomDislikeAd;
  bool _requestMultiImages;
  bool _returnUrlsForImages;
  VideoConfiguration _videoConfiguration;

  NativeAdConfiguration(
      {AdSize adSize,
        int choicesPosition,
        int mediaDirection,
        int mediaAspect,
        bool requestCustomDislikeAd,
        bool requestMultiImages,
        bool returnUrlsForImages,
        VideoConfiguration configuration
      }) {
    _adSize = adSize;
    _choicesPosition = choicesPosition;
    _direction = mediaDirection;
    _aspect = mediaAspect;
    _requestCustomDislikeAd = requestCustomDislikeAd;
    _requestMultiImages = requestMultiImages;
    _returnUrlsForImages = returnUrlsForImages;
    _videoConfiguration = configuration;
  }

  static NativeAdConfiguration build() {
    NativeAdConfiguration configuration = NativeAdConfiguration();
    configuration._videoConfiguration = new VideoConfiguration();
    return configuration;
  }

  AdSize get getAdSize => _adSize;
  int get getChoicesPosition => _choicesPosition;
  int get getMediaDirection => _direction;
  int get getMediaAspect => _aspect;
  bool get isRequestMultiImages => _requestMultiImages;
  bool get isReturnUrlsForImages => _returnUrlsForImages;
  VideoConfiguration get getVideoConfiguration => _videoConfiguration;

  set setAdSize(AdSize adSize) => _adSize = adSize;
  set setChoicesPosition(int position) => _choicesPosition = position;
  set setMediaDirection(int direction) => _direction = direction;
  set setMediaAspect(int aspect) => _aspect = aspect;
  set setRequestCustomDislikeThisAd(bool dislike) =>
      _requestCustomDislikeAd = dislike;
  set setRequestMultiImages(bool multiImage) =>
      _requestMultiImages = multiImage;
  set setReturnUrlsForImages(bool returnUrls) =>
      _returnUrlsForImages = returnUrls;
  set setVideoConfiguration(VideoConfiguration configuration) =>
      _videoConfiguration = configuration;

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};
    if(_adSize != null)
      json['adSize'] = _adSize.toJson();
    if (_choicesPosition != null)
      json['choicesPosition'] = _choicesPosition;
    if (_direction != null)
      json['direction'] = _direction;
    if (_aspect != null)
      json['aspect'] = _aspect;
    if (_requestCustomDislikeAd != null)
      json['requestCustomDislikeAd'] = _requestCustomDislikeAd;
    if (_requestMultiImages != null)
      json['requestMultiImages'] = _requestMultiImages;
    if (_returnUrlsForImages != null)
      json['returnUrlsForImages'] = _returnUrlsForImages;
    if (_videoConfiguration != null)
      json['videoConfiguration'] = _videoConfiguration.toJson();

    return json;
  }
}
