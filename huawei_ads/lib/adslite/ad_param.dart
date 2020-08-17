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
import 'package:huawei_ads/adslite/request_options.dart';

class AdParam {
  int _gender;
  String _countryCode;
  RequestOptions _requestOptions;

  AdParam({int gender, String countryCode, RequestOptions requestOptions}) {
    _gender = gender;
    _countryCode = countryCode;
    _requestOptions = requestOptions;
  }

  static AdParam build() {
    AdParam adParam = new AdParam();
    adParam._requestOptions = new RequestOptions();
    return adParam;
  }

  set setGender(int gender) => _gender = gender;
  set setAdContentClassification(String contentClassification) =>
      _requestOptions.setAdContentClassification = contentClassification;
  set setTagForUnderAgeOfPromise(int tag) =>
      _requestOptions.setTagForUnderAgeOfPromise = tag;
  set setTagForChildProtection(int tag) =>
      _requestOptions.setTagForChildProtection = tag;
  set setNonPersonalizedAd(int ad) => _requestOptions.setNonPersonalizedAd = ad;
  set setAppCountry(String country) => _requestOptions.setAppCountry = country;
  set setAppLang(String lang) => _requestOptions.setAppLang = lang;
  set setBelongCountryCode(String code) => _countryCode = code;

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = _requestOptions.toJson();
    if (_gender != null) json['gender'] = _gender;
    if (_countryCode != null) json['countryCode'] = _countryCode;

    return json;
  }
}
