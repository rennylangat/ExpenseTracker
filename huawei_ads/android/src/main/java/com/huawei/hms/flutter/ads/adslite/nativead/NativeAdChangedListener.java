package com.huawei.hms.flutter.ads.adslite.nativead;

import com.huawei.hms.ads.nativead.NativeAd;

interface NativeAdChangedListener {
    /**
     * Callback method that is called when a native ad is loaded
     *
     * @param ad : The Native ad that has changed
     */
    void onNativeAdChanged(NativeAd ad);
}
