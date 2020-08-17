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

class InstallReferrerPage extends StatefulWidget {
  @override
  _InstallReferrerPageState createState() => _InstallReferrerPageState();
}

class _InstallReferrerPageState extends State<InstallReferrerPage> {
  static final InstallReferrerClient sdkReferrer = new InstallReferrerClient(
      stateListener: (InstallReferrerStateEvent event,
          {ReferrerResponse responseCode}) {
    print("ReferrerStateEvent event $event | Code: $responseCode");
  });

  ReferrerDetails _referrerDetails;

  void getReferrerDetails() async {
    ReferrerDetails referrerDetails;
    referrerDetails = await sdkReferrer.getInstallReferrer;

    setState(() {
      _referrerDetails = referrerDetails;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.blueGrey,
          title: Text(
            'Huawei Ads - Referrer',
            style: TextStyle(
              color: Colors.white,
            ),
          ),
        ),
        body: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Column(
                children: <Widget>[
                  Container(
                    padding: EdgeInsets.only(top: 50),
                    width: 400,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: <Widget>[
                        RaisedButton(
                          child: Container(
                            child: Center(
                              child: Text(
                                'Connect',
                                style: Styles.adControlButtonStyle,
                              ),
                            ),
                            width: 90,
                            height: 40,
                          ),
                          onPressed: () {
                            sdkReferrer.startConnection(true);
                          },
                        ),
                        RaisedButton(
                          child: Container(
                            child: Center(
                              child: Text(
                                'Disconnect',
                                style: Styles.adControlButtonStyle,
                              ),
                            ),
                            width: 90,
                            height: 40,
                          ),
                          onPressed: () {
                            sdkReferrer.endConnection();
                          },
                        ),
                      ],
                    ),
                  ),
                  SizedBox(
                    height: 10,
                  ),
                  RaisedButton(
                    child: Container(
                      child: Center(
                        child: Text(
                          'Get Referrer Details',
                          style: Styles.adControlButtonStyle,
                        ),
                      ),
                      width: 150,
                      height: 40,
                    ),
                    onPressed: () {
                      getReferrerDetails();
                    },
                  ),
                ],
              ),
              Center(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(10, 50, 10, 30),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                      Text('Install Referrer', style: Styles.headerTextStyle),
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        '${_referrerDetails?.getInstallReferrer ?? " "}',
                        style: Styles.textContentStyle,
                      ),
                      SizedBox(
                        height: 30,
                      ),
                      Text('Referrer Click Timestamp Millisec',
                          style: Styles.headerTextStyle),
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        '${_referrerDetails?.getReferrerClickTimestampMillisecond ?? " "}',
                        style: Styles.textContentStyle,
                      ),
                      SizedBox(
                        height: 30,
                      ),
                      Text('Install Begin Timestamp Millisec',
                          style: Styles.headerTextStyle),
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        '${_referrerDetails?.getReferrerBeginTimeStampMillisecond ?? " "}',
                        style: Styles.textContentStyle,
                      ),
                    ],
                  ),
                ),
              )
            ],
          ),
        ));
  }
}
