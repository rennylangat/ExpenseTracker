package com.huawei.hms.flutter.ads.adslite;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.flutter.ads.factory.AdParamFactory;
import com.huawei.hms.flutter.ads.utils.Constants;
import com.huawei.hms.flutter.ads.utils.ToMap;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class Banner extends AdListener {
    private static final String TAG = "Banner";
    private static SparseArray<Banner> allBannerAds = new SparseArray<>();

    private final Activity activity;
    private final MethodChannel channel;
    private final int id;
    private BannerView bannerView;
    private BannerAdSize adSize;
    private Long bannerRefresh;
    private double offset;
    private int anchorType;
    private String status;

    public Banner(Integer id, BannerAdSize adSize, Long bannerRefresh, Activity activity, MethodChannel channel) {
        this.id = id;
        this.activity = activity;
        this.channel = channel;
        this.adSize = adSize;
        this.bannerRefresh = bannerRefresh;
        this.offset = 0.0;
        this.anchorType = Gravity.BOTTOM;
        allBannerAds.put(id, this);
        setStatus(Constants.AdStatus.CREATED);
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void setAnchorType(int anchorType) {
        this.anchorType = anchorType;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public boolean isCreated() {
        return this.status.equals(Constants.AdStatus.CREATED);
    }

    private boolean isPreparing() {
        return this.status.equals(Constants.AdStatus.PREPARING);
    }

    public boolean isLoading() {
        return this.status.equals(Constants.AdStatus.LOADING);
    }

    private boolean isLoaded() {
        return this.status.equals(Constants.AdStatus.LOADED);
    }

    public boolean isFailed() {
        return this.status.equals(Constants.AdStatus.FAILED);
    }

    public static Banner get(Integer id) {
        return allBannerAds.get(id);
    }

    public BannerView getBannerView() {
        return bannerView;
    }

    public void loadAd(String adSlotId, Map<String, Object> adParamMap, boolean listener) {
        if (!isCreated()) {
            return;
        }
        setStatus(Constants.AdStatus.LOADING);

        bannerView = new BannerView(activity);
        Log.i(TAG, "Banner view initialized");
        bannerView.setBannerAdSize(adSize);
        Log.i(TAG, "Banner ad size is set");
        bannerView.setAdId(adSlotId);
        Log.i(TAG, "Banner ad unit id is set");
        if (bannerRefresh != null) {
            bannerView.setBannerRefresh(bannerRefresh);
            Log.i(TAG, "bannerRefreshTime set : " + bannerRefresh);
        }

        AdParamFactory factory = new AdParamFactory(adParamMap);
        AdParam adParam = factory.createAdParam();
        bannerView.loadAd(adParam);
        bannerView.setAdListener(this);
    }

    public void show() {
        if (isLoading()) {
            Log.i(TAG, "Banner ad is being prepared.");
            setStatus(Constants.AdStatus.PREPARING);
            return;
        }

        if (!isLoaded()) {
            Log.e(TAG, "ERROR: Banner ad is not loaded!");
            return;
        }

        if (activity.findViewById(id) != null) {
            Log.i(TAG, "Banner ad already displayed");
            return;
        }

        LinearLayout content = new LinearLayout(activity);
        content.setId(id);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.BOTTOM);
        content.addView(bannerView);
        final float scale = activity.getResources().getDisplayMetrics().density;

        if (anchorType == Gravity.BOTTOM) {
            content.setPadding(0, 0, 0, (int) (offset * scale));
        } else {
            content.setPadding(0, (int) (offset * scale), 0, 0);
        }

        activity.addContentView(
            content,
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void destroy() {
        bannerView.destroy();
        Log.i(TAG, "Banner ad destroyed");
        allBannerAds.remove(id);

        View contentView = activity.findViewById(id);
        if (contentView == null) {
            Log.e(TAG, "ERROR: Banner ad view not found in activity!");
            return;
        }
        ViewGroup contentParent = (ViewGroup) (contentView.getParent());
        contentParent.removeView(contentView);
        allBannerAds.remove(id);
    }

    public static void destroyAll() {
        for (int i = 0; i < allBannerAds.size(); i++) {
            allBannerAds.valueAt(i).destroy();
        }
        allBannerAds.clear();
    }

    @Override
    public void onAdLoaded() {
        Log.i(TAG, "onBannerAdLoaded");
        boolean wasPreparing = isPreparing();
        setStatus(Constants.AdStatus.LOADED);
        channel.invokeMethod("onAdLoaded", ToMap.argsToMap(id));
        if (wasPreparing) {
            show();
        }
    }

    @Override
    public void onAdFailed(int errorCode) {
        Log.w(TAG, "onBannerAdFailed: " + errorCode);
        setStatus(Constants.AdStatus.FAILED);
        channel.invokeMethod("onAdFailed", ToMap.argsToMap(id, "errorCode", errorCode));
    }

    @Override
    public void onAdOpened() {
        Log.i(TAG, "onBannerAdOpened");
        channel.invokeMethod("onAdOpened", ToMap.argsToMap(id));
    }

    @Override
    public void onAdClicked() {
        Log.i(TAG, "onBannerAdClicked");
        channel.invokeMethod("onAdClicked", ToMap.argsToMap(id));
    }

    @Override
    public void onAdImpression() {
        Log.i(TAG, "onBannerAdImpression");
        channel.invokeMethod("onAdImpression", ToMap.argsToMap(id));
    }

    @Override
    public void onAdClosed() {
        Log.i(TAG, "onBannerAdClosed");
        channel.invokeMethod("onAdClosed", ToMap.argsToMap(id));
    }

    @Override
    public void onAdLeave() {
        Log.i(TAG, "onBannerAdLeave");
        channel.invokeMethod("onAdLeave", ToMap.argsToMap(id));
    }
}
