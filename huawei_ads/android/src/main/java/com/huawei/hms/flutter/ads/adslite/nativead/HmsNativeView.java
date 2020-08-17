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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hms.ads.Image;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeView;
import com.huawei.hms.flutter.ads.R;

import java.util.Map;

enum NativeAdType {
    banner, small, full, video
}

public class HmsNativeView extends LinearLayout {
    private NativeView nativeView;
    private NativeStyles nativeStyles;
    private MediaView media;
    private TextView flag;
    private TextView title;
    private TextView source;
    private TextView description;
    private Button callToAction;

    public HmsNativeView(Context context, NativeAdType type) {
        super(context);
        inflateNative(context, type);
    }

    public NativeView getNativeView() {
        return nativeView;
    }

    public void setNativeStyles(NativeStyles styles) {
        this.nativeStyles = styles;
        updateStyles();
    }

    private void inflateNative(Context context, NativeAdType type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = -1;
        switch (type) {
            case full:
                layout = R.layout.native_ad_full_template;
                break;
            case banner:
                layout = R.layout.native_ad_banner_template;
                break;
            case small:
                layout = R.layout.native_ad_small_template;
                break;
            case video:
                layout = R.layout.native_ad_video_template;
                break;
        }
        if (layout == -1) {
            return;
        }

        inflater.inflate(layout, this, true);
        setBackgroundColor(Color.TRANSPARENT);

        nativeView = findViewById(R.id.ad_view);

        media = nativeView.findViewById(R.id.ad_media);
        nativeView.setMediaView(media);

        flag = nativeView.findViewById(R.id.ad_flag);
        flag.setBackground(new ColorDrawable(Color.parseColor("#ECC159")));

        source = nativeView.findViewById(R.id.ad_source);
        nativeView.setAdSourceView(source);

        title = nativeView.findViewById(R.id.ad_title);
        nativeView.setTitleView(title);

        description = nativeView.findViewById(R.id.ad_description);
        nativeView.setDescriptionView(description);

        callToAction = nativeView.findViewById(R.id.ad_call_to_action);
        nativeView.setCallToActionView(callToAction);

        nativeView.setIconView(nativeView.findViewById(R.id.ad_icon));
    }

    void setNativeAd(NativeAd nativeAd) {
        if (nativeAd == null) {
            return;
        }

        if (nativeAd.getMediaContent() != null) {
            media.setMediaContent(nativeAd.getMediaContent());
            media.setImageScaleType(nativeStyles.mediaImageScaleType);
        }

        if (nativeAd.getAdSource() == null) {
            source.setVisibility(View.INVISIBLE);
        } else {
            source.setVisibility(View.VISIBLE);
            source.setText(nativeAd.getAdSource());
        }

        if (nativeAd.getTitle() != null) {
            title.setText(nativeAd.getTitle());
        }

        Image icon = nativeAd.getIcon();
        if (nativeView.getIconView() != null) {
            if (icon == null) {
                nativeView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) nativeView.getIconView()).setImageDrawable(icon.getDrawable());
                nativeView.getIconView().setVisibility(View.VISIBLE);
            }
        }


        if (nativeAd.getDescription() != null) {
            description.setText(nativeAd.getDescription());
        }

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        nativeView.setNativeAd(nativeAd);
    }

    private void updateStyles() {
        if (media != null) {
            media.setVisibility(nativeStyles.showMediaContent ? View.VISIBLE : View.GONE);
        }

        if (flag != null) {
            setNativeStyle(flag, nativeStyles.flag);
        }

        if (title != null) {
            setNativeStyle(title, nativeStyles.title);
        }

        if (source != null) {
            setNativeStyle(source, nativeStyles.source);
        }

        if (description != null) {
            setNativeStyle(description, nativeStyles.description);
        }

        if (callToAction != null) {
            setNativeStyle(callToAction, nativeStyles.callToAction);
        }
    }

    private void setNativeStyle(TextView textView, Map<String, Object> nativeStyle) {
        textView.setTextColor((int) nativeStyle.get(NativeStyles.Keys.COLOR));
        textView.setTextSize((float) nativeStyle.get(NativeStyles.Keys.FONT_SIZE));
        textView.setTypeface(null, (int) nativeStyle.get(NativeStyles.Keys.FONT_WEIGHT));
        textView.setVisibility((int) nativeStyle.get(NativeStyles.Keys.VISIBILITY));
        int bgColor = (int) nativeStyle.get(NativeStyles.Keys.BACKGROUND_COLOR);
        if (bgColor != 0) {
            textView.setBackground(new ColorDrawable(bgColor));
        }
    }
}
