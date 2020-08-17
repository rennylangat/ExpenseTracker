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

public interface Constants {
    String LIBRARY_CHANNEL = "com.huawei.hms.flutter.ads";
    String NATIVE_VIEW_TYPE = "com.huawei.hms.flutter.ads/nativeview";

    String INSTALL_REFERRER_FILE = "install_referrer";
    String SERVICE_PACKAGE_NAME = "com.huawei.hwid";
    String TEST_SERVICE_PACKAGE_NAME = "com.huawei.pps.hms.test";
    String SERVICE_ACTION = "com.huawei.android.hms.CHANNEL_SERVICE";

    interface AdStatus {
        String CREATED = "CREATED";
        String LOADING = "LOADING";
        String LOADED = "LOADED";
        String PREPARING = "PREPARING";
        String FAILED = "FAILED";
    }

    interface ErrorCodes {
        String NULL_PARAM = "NULL_PARAM";
        String NOT_FOUND = "NOT_FOUND";
        String INVALID_PARAM = "INVALID_PARAM";
        String LOAD_FAILED = "LOAD_FAILED";
    }
}
