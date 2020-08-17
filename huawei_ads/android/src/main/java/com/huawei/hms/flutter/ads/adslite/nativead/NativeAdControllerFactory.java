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
package com.huawei.hms.flutter.ads.adslite.nativead;

import android.content.Context;

import java.util.ArrayList;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

public class NativeAdControllerFactory {
    private static ArrayList<NativeAdController> allControllers = new ArrayList<>();

    private NativeAdControllerFactory() {
    }

    public static void createController(String id, BinaryMessenger messenger, Context context) {
        if (get(id) == null) {
            MethodChannel channel = new MethodChannel(messenger, id);
            NativeAdController controller = new NativeAdController(id, channel, context);
            allControllers.add(controller);
        }
    }

    // Streams require minimum API Level of 24,
    //  therefore are not used to support a wider range of devices
    public static NativeAdController get(String id) {
        for (NativeAdController controller : allControllers) {
            if (controller.checkId(id)) {
                return controller;
            }
        }
        return null;
    }

    public static boolean dispose(String id) {
        NativeAdController controller = get(id);
        if (controller != null) {
            controller.getNativeAd().destroy();
            allControllers.remove(controller);
            return true;
        }
        return false;
    }
}