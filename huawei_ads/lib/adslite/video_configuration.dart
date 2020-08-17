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
class VideoConfiguration {
  int _audioFocusType;
  bool _customizeOperationRequested;
  bool _startMuted;

  VideoConfiguration({int audioFocusType, bool customizeOperationRequested, bool startMuted}) {
    this._audioFocusType = audioFocusType;
    this._customizeOperationRequested = customizeOperationRequested;
    this._startMuted = startMuted;
  }

  static VideoConfiguration build() => new VideoConfiguration();

  int get getAudioFocusType => _audioFocusType;
  bool get isCustomizeOperationRequested => _customizeOperationRequested;
  bool get isStartMuted => _startMuted;

  set setAudioFocusType(int focusType) => _audioFocusType = focusType;
  set setCustomizeOperationRequested(bool customRequested) => _customizeOperationRequested = customRequested;
  set setStartMuted(bool muted) => _startMuted = _startMuted;


  Map<String, dynamic> toJson() {
    final Map<String, dynamic> json = <String, dynamic>{};
    if (_audioFocusType != null) json['audioFocusType'] = _audioFocusType;
    if (_customizeOperationRequested != null) json['customizeOperationRequested'] = _customizeOperationRequested;
    if (_startMuted != null) json['startMuted'] = _startMuted;

    return json;
  }
}