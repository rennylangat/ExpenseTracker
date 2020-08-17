package com.huawei.hms.flutter.ads.adslite.nativead;

interface NativeAdViewListener {
    /**
     * Callback method that receives the nativeAdView as an argument when a controller is set for that native ad.
     *
     * @param hmsNativeView : The NativeAdView that which has its controller set
     */
    void onNativeControllerSet(HmsNativeView hmsNativeView);

    /**
     * Callback method that is called when a Native Platform View is disposed
     */
    void onNativeViewDestroyed();
}
