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

package com.huawei.hms.flutter.ads.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hms.ads.consent.bean.AdProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ToMap {
    private static final String TAG = "ToMap";

    public static Map<String, Object> argsToMap(Object... args) {
        Map<String, Object> arguments = new HashMap<String, Object>();
        int i = 0;
        if (args.length % 2 == 1) {
            arguments.put("id", args[0]);
            i++;
        }
        for (; i < args.length; i += 2) {
            arguments.put(args[i].toString(), args[i + 1]);
        }
        return arguments;
    }

    public static Map<String, Object> objectToMap(Object args) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        if (args instanceof Map) {
            for (Object entry : ((Map) args).entrySet()) {
                if (entry instanceof Map.Entry) {
                    resMap.put(((Map.Entry) entry).getKey().toString(), ((Map.Entry) entry).getValue());
                }
            }
        }
        return resMap;
    }

    public static List<Map<String, Object>> adProviderList(List<AdProvider> adProviders) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (AdProvider provider : adProviders) {
            mapList.add(adProvider(provider));
        }
        return mapList;
    }

    private static Map<String, Object> adProvider(AdProvider adProvider) {
        Map<String, Object> adMap = new HashMap<String, Object>();
        adMap.put("id", adProvider.getId());
        adMap.put("name", adProvider.getName());
        adMap.put("serviceArea", adProvider.getServiceArea());
        adMap.put("privacyPolicyUrl", adProvider.getPrivacyPolicyUrl());
        return adMap;
    }
}
