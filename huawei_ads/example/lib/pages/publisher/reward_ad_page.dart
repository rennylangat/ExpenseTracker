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
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:huawei_ads/hms_ads_lib.dart';
import 'package:huawei_ads_example/utils/constants.dart';

class RewardPage extends StatefulWidget {
  @override
  _RewardPageState createState() => _RewardPageState();
}

class _RewardPageState extends State<RewardPage> {
  final String _testAdSlotId = "testx9dtjwj8hp";
  final AdParam _adParam = AdParam.build();
  String _status = '';
  int _score = 0;

  /* *
  * Alternatively, loading status can be set when a RewardAdEvent.loaded
  * event is caught.
  * *
  * NOTE: A reward is not issued every time
  * */
  @override
  void initState() {
    super.initState();
    RewardAd.instance.setRewardAdListener =
        (RewardAdEvent event, {Reward reward, int errorCode}) {
      print("RewardedVideoAd event $event");
      if (event == RewardAdEvent.rewarded) {
        print('Received reward : ${jsonEncode(reward.toJson())}');
        setState(() {
          _score += reward.getAmount != 0
              ? reward.getAmount
              : 10;
        });
      }
    };
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueGrey,
        title: Text(
          'Huawei Ads - Reward',
          style: TextStyle(
            color: Colors.white,
          ),
        ),
      ),
      body: Column(
        children: <Widget>[
          Expanded(
            flex: 1,
            child: Column(
              children: <Widget>[
                Expanded(child: Center(
                  child: Text(
                    _status,
                    style: Styles.warningTextStyle,
                  ),
                )),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    RaisedButton(
                      child: Text(
                        'Load Reward Ad',
                        style: Styles.adControlButtonStyle,
                      ),
                      onPressed: () {
                        RewardAd.instance.loadAd(
                            adUnitId: _testAdSlotId,
                            adParam: _adParam);
                      },
                    ),
                    RaisedButton(
                      child: Text(
                        'Show Reward Ad',
                        style: Styles.adControlButtonStyle,
                      ),
                      onPressed: () {
                        RewardAd.instance.isLoaded().then((isLoaded) {
                          if(isLoaded) {
                            setState(() {
                              _status = '';
                            });
                            RewardAd.instance.show();
                          } else {
                            setState(() {
                              _status = 'Reward ad must be loaded first.';
                            });
                          }
                        });
                      },
                    )
                  ],
                )
              ],
            ),
          ),
          Expanded(
            flex: 3,
            child: Center(
              child: Container(
                child: Text('Your score : $_score'),
              ),
            ),
          )
        ],
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
  }
}
