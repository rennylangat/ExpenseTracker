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
import 'package:flutter/material.dart';
import 'package:huawei_ads/hms_ads_lib.dart';
import 'package:huawei_ads_example/utils/constants.dart';

class InterstitialAdPage extends StatefulWidget {
  @override
  _InterstitialAdPageState createState() => _InterstitialAdPageState();
}

class _InterstitialAdPageState extends State<InterstitialAdPage> {
  static final String _imageTestAdSlotId = "teste9ih9j0rc3"; // Image ad
  static final String _videoTestAdSlotId = "testb4znbuh3n2"; // Video ad
  final AdParam _adParam = AdParam.build();
  String adSlotId = _imageTestAdSlotId;
  InterstitialAd _interstitialAd;
  String _status = '';

  void changeUnitId(String unitId) {
    setState(() {
      _interstitialAd = null;
      adSlotId = unitId;
    });
  }

  InterstitialAd createInterstitialAd() {
    return InterstitialAd(
      adUnitId: adSlotId,
      adParam: _adParam,
      listener: (AdEvent event, {int errorCode}) {
        print("InterstitialAd event $event");
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueGrey,
        title: Text(
          'Huawei Ads - Interstitial',
          style: TextStyle(
            color: Colors.white,
          ),
        ),
      ),
      body: Column(
        children: <Widget>[
          Expanded(
            child: Column(
              children: <Widget>[
                SingleChildScrollView(
                  child: Container(
                    padding: EdgeInsets.symmetric(horizontal: 50),
                    child: Column(
                      children: <Widget>[
                        ListTile(
                          title: Text('Interstitial Image'),
                          trailing: Radio(
                            groupValue: adSlotId,
                            value: _imageTestAdSlotId,
                            onChanged: changeUnitId,
                          ),
                        ),
                        ListTile(
                          title: Text('Interstitial Video'),
                          trailing: Radio(
                            groupValue: adSlotId,
                            value: _videoTestAdSlotId,
                            onChanged: changeUnitId,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    RaisedButton(
                      child: Text(
                        'Load Interstitial',
                        style: Styles.adControlButtonStyle,
                      ),
                      onPressed: () {
                        _interstitialAd?.destroy();
                        _interstitialAd = createInterstitialAd()..loadAd();
                      },
                    ),
                    RaisedButton(
                      child: Text(
                        'Show Interstitial',
                        style: Styles.adControlButtonStyle,
                      ),
                      onPressed: () {
                        if (_interstitialAd == null) {
                          setState(() {
                            _status =
                                "Interstitial ad needs to be loaded first!";
                          });
                        } else {
                          setState(() {
                            _status = "";
                          });
                          _interstitialAd?.show();
                        }
                      },
                    )
                  ],
                )
              ],
            ),
          ),
          Expanded(
            child: Center(
              child: Container(
                child: Text(_status,
                style: Styles.warningTextStyle,),
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
    _interstitialAd?.destroy();
  }
}
