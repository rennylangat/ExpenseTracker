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
class RewardVerifyConfig {
  String _userId;
  String _data;

  RewardVerifyConfig({String userId, String data}) {
    _userId = userId;
    _data = data;
  }
  static RewardVerifyConfig build() => new RewardVerifyConfig();

  String get getUserId => _userId;
  String get getData => _data;

  set setUserId(String id) => _userId = id;
  set setData(String data) => _data = data;


  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};
    if (_userId != null)
      json['userId'] = _userId;
    if (_data != null)
      json['data'] = _data;

    return json;
  }
}