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
import 'dart:collection';

class AdProvider {
  String _id;
  String _name;
  String _serviceArea;
  String _privacyPolicyUrl;

  AdProvider(
      {String id, String name, String serviceArea, String privacyPolicyUrl}) {
    this._id = id;
    this._name = name;
    this._serviceArea = serviceArea;
    this._privacyPolicyUrl = privacyPolicyUrl;
  }

  String get getId => _id;
  String get getName => _name;
  String get getServiceArea => _serviceArea;
  String get getPrivacyPolicyUrl => _privacyPolicyUrl;

  set setId(String id) => _id = id;
  set setName(String name) => _name = name;
  set setServiceArea(String serviceArea) => _serviceArea = serviceArea;
  set setPrivacyPolicy(String policyUrl) => _privacyPolicyUrl = policyUrl;

  bool valid() {
    return (_id != null && _id.isNotEmpty) &&
        (_name != null && _name.isNotEmpty) &&
        (_serviceArea != null && _serviceArea.isNotEmpty) &&
        (_privacyPolicyUrl != null && _privacyPolicyUrl.isNotEmpty);
  }

  static List<AdProvider> buildList(List<dynamic> args) {
    List<AdProvider> adProviders = new List<AdProvider>();
    if (args != null)
      args.forEach((dynamic providerMap) {
        adProviders.add(build(providerMap));
      });
    return adProviders;
  }

  static AdProvider build(LinkedHashMap<dynamic, dynamic> args) {
    AdProvider provider = new AdProvider();

    if (args['id'] != null) provider.setId = args['id'];
    if (args['name'] != null) provider.setName = args['name'];
    if (args['serviceArea'] != null)
      provider.setServiceArea = args['serviceArea'];
    if (args['privacyPolicyUrl'] != null)
      provider.setPrivacyPolicy = args['privacyPolicyUrl'];

    return provider;
  }

  Map<String, dynamic> toJson () {
    Map<String, dynamic> json = Map<String, dynamic>();
    if(_id != null) json['id'] = _id;
    if(_name != null) json['name'] = _name;
    if(_serviceArea != null) json['serviceArea'] = _serviceArea;
    if(_privacyPolicyUrl != null) json['privacyPolicyUrl'] = _privacyPolicyUrl;

    return json;
  }
}
