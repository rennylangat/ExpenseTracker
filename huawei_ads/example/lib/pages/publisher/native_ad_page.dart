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

class NativeAdPage extends StatefulWidget {
  @override
  _NativeAdPageState createState() => _NativeAdPageState();
}

class _NativeAdPageState extends State<NativeAdPage> {
  static final String _imageTestAdSlotId = "testu7m3hc4gvm"; // Image
  static final String _videoTestAdSlotId = "testy63txaom86"; // Video ad

  NativeAdController createVideoAdController() {
    NativeAdController controller = NativeAdController();
    controller.setAdListener = (AdEvent event, {int errorCode}) {
      if (event == AdEvent.loaded) {
        testNative(controller);
      }
    };
    return controller;
  }

  void testNative(NativeAdController controller) async {
    VideoOperator operator = await controller.getVideoOperator();
    bool hasVideo = await operator.hasVideo();
    print('Operator has video : $hasVideo');

    String title = await controller.getTitle();
    print('Ad Title : $title');

    String callToAction = await controller.getCallToAction();
    print('Ad action : $callToAction');

    String source = await controller.getAdSource();
    print('Ad source : $source');
  }

  @override
  Widget build(BuildContext context) {
    NativeStyles stylesSmall = NativeStyles();
    stylesSmall.setTitle(fontWeight: FontWeight.boldItalic);
    stylesSmall.setCallToAction(fontSize: 8);
    stylesSmall.setFlag(fontSize: 10);
    stylesSmall.setSource(fontSize: 11);

    NativeStyles stylesVideo = NativeStyles();
    stylesVideo.setCallToAction(fontSize: 10);
    stylesVideo.setFlag(fontSize: 10);

    NativeStyles stylesFull = NativeStyles();
    stylesFull.setSource(color: Colors.redAccent);
    stylesFull.setCallToAction(
        color: Colors.white, bgColor: Colors.redAccent);

    NativeAdConfiguration configuration = NativeAdConfiguration.build();
    configuration.setChoicesPosition = NativeAdChoicesPosition.bottomRight;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueGrey,
        title: Text(
          'Huawei Ads - Native',
          style: TextStyle(
            color: Colors.white,
          ),
        ),
      ),
      body: SingleChildScrollView(
        child: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              Container(
                padding: EdgeInsets.symmetric(vertical: 20),
                color: Colors.black12,
                child: Center(
                  child: Text(
                    "Native Banner Ad",
                    style: Styles.headerTextStyle,
                  ),
                ),
              ),
              Container(
                height: 330,
                padding: EdgeInsets.symmetric(horizontal: 10),
                margin: EdgeInsets.only(bottom: 20.0),
                child: NativeAd(
                  adUnitId: _imageTestAdSlotId,
                  controller: NativeAdController(
                      adConfiguration: configuration,
                      listener: (AdEvent event, {int errorCode}) {
                        print("NativeAd event $event");
                      }),
                  type: NativeAdType.banner,
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 20),
                color: Colors.black12,
                child: Center(
                  child: Text(
                    "Native Small Ad",
                    style: Styles.headerTextStyle,
                  ),
                ),
              ),
              Container(
                height: 100,
                margin: EdgeInsets.only(bottom: 20.0),
                child: NativeAd(
                  adUnitId: _imageTestAdSlotId,
                  controller: NativeAdController(),
                  type: NativeAdType.small,
                  styles: stylesSmall,
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 20),
                color: Colors.black12,
                child: Center(
                  child: Text(
                    "Native Full Ad",
                    style: Styles.headerTextStyle,
                  ),
                ),
              ),
              Container(
                height: 400,
                padding: EdgeInsets.all(10),
                margin: EdgeInsets.only(bottom: 20.0),
                child: NativeAd(
                  adUnitId: _imageTestAdSlotId,
                  controller: NativeAdController(),
                  type: NativeAdType.full,
                  styles: stylesFull,
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 20),
                color: Colors.black12,
                child: Center(
                  child: Text(
                    "Native Video Ad",
                    style: Styles.headerTextStyle,
                  ),
                ),
              ),
              Container(
                height: 500,
                padding: EdgeInsets.symmetric(horizontal: 10),
                margin: EdgeInsets.only(bottom: 20.0),
                child: NativeAd(
                  adUnitId: _videoTestAdSlotId,
                  controller: createVideoAdController(),
                  type: NativeAdType.video,
                  styles: stylesVideo,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
  }
}
