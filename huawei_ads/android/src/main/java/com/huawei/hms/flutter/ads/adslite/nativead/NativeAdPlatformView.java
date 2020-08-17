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
import android.view.View;

import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.flutter.ads.utils.FromMap;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.util.Map;

import io.flutter.plugin.platform.PlatformView;

public class NativeAdPlatformView implements PlatformView, NativeAdChangedListener {
    private NativeAdController nativeAdController;
    private HmsNativeView hmsNativeView;

    public NativeAdPlatformView(Context context, Object args) {
        Map<String, Object> params = ToMap.objectToMap(args);
        if (params.isEmpty()) {
            return;
        }
        String type = FromMap.toString("type", params.get("type"));
        NativeAdType adType = NativeAdType.banner;
        if (type != null) {
            adType = NativeAdType.valueOf(type);
        }
        hmsNativeView = new HmsNativeView(context, adType);
        Map<String, Object> nativeStyles = ToMap.objectToMap(params.get("nativeStyles"));
        if (!nativeStyles.isEmpty()) {
            hmsNativeView.setNativeStyles(new NativeStyles().build(nativeStyles));
        }
        String id = FromMap.toString("id", params.get("id"));
        if (id != null) {
            NativeAdController controller = NativeAdControllerFactory.get(id);
            if (controller == null) {
                return;
            }
            this.nativeAdController = controller;
            nativeAdController.setNativeAdChangedListener(this);
        }
        if (nativeAdController != null && nativeAdController.getNativeAd() != null) {
            hmsNativeView.setNativeAd(nativeAdController.getNativeAd());
            nativeAdController.onNativeControllerSet(hmsNativeView);
        }
    }

    @Override
    public View getView() {
        return hmsNativeView;
    }

    @Override
    public void dispose() {
        if (hmsNativeView != null) {
            if (hmsNativeView.getNativeView() != null) {
                hmsNativeView.getNativeView().destroy();
                nativeAdController.onNativeViewDestroyed();
            }
        }
    }

    @Override
    public void onNativeAdChanged(NativeAd ad) {
        hmsNativeView.setNativeAd(ad);
    }
}