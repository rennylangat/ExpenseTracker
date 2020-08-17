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
class RequestOptions {
  String _adContentClassification;
  int _tagForUnderAgeOfPromise;
  int _tagForChildProtection;
  int _nonPersonalizedAd;
  String _appCountry;
  String _appLang;

  RequestOptions({
    String adContentClassification,
    int tagForUnderAgeOfPromise,
    int tagForChildProtection,
    int nonPersonalizedAd,
    String appCountry,
    String appLang,
  }) {
    _adContentClassification = adContentClassification;
    _tagForChildProtection = tagForChildProtection;
    _tagForUnderAgeOfPromise = tagForUnderAgeOfPromise;
    _nonPersonalizedAd = nonPersonalizedAd;
    _appCountry = appCountry;
  }

  static RequestOptions build() => new RequestOptions();

  String get getAdContentClassification => _adContentClassification;
  int get getTagForUnderAgeOfPromise => _tagForUnderAgeOfPromise;
  int get getTagForChildProtection => _tagForChildProtection;
  int get getNonPersonalizedAd => _nonPersonalizedAd;
  String get getAppCountry => _appCountry;
  String get getAppLang => _appLang;

  set setAdContentClassification(String contentClassification) =>
      _adContentClassification = contentClassification;
  set setTagForUnderAgeOfPromise(int tag) => _tagForUnderAgeOfPromise = tag;
  set setTagForChildProtection(int tag) => _tagForChildProtection = tag;
  set setNonPersonalizedAd(int ad) => _nonPersonalizedAd = ad;
  set setAppCountry(String country) => _appCountry = country;
  set setAppLang(String lang) => _appLang = lang;

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};
    if (_adContentClassification != null)
      json['adContentClassification'] = _adContentClassification;
    if (_tagForUnderAgeOfPromise != null)
      json['tagForUnderAgeOfPromise'] = _tagForUnderAgeOfPromise;
    if (_tagForChildProtection != null)
      json['tagForChildProtection'] = _tagForChildProtection;
    if (_nonPersonalizedAd != null)
      json['nonPersonalizedAd'] = _nonPersonalizedAd;
    if (_appCountry != null) json['appCountry'] = _appCountry;
    if (_appLang != null) json['appLang'] = _appLang;

    return json;
  }

  static RequestOptions fromJson(Map<dynamic, dynamic> args) {
    RequestOptions options = new RequestOptions();
    options.setAdContentClassification = args['adContentClassification']?? null;
    options.setTagForUnderAgeOfPromise = args['tagForUnderAgeOfPromise']?? null;
    options.setTagForChildProtection = args['tagForChildProtection']?? null;
    options.setNonPersonalizedAd = args['nonPersonalizedAd']?? null;
    options.setAppCountry = args['appCountry']?? null;
    options.setAppLang = args['appLang']?? null;

    return options;
  }
}
