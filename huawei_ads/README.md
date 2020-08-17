# Huawei Ads Kit Flutter Plugin

## Table of Contents
* [Introduction](#introduction)
* [Installation Guide](#installation-guide)
* [API Reference](#api-reference)
* [Configuration Description](#configuration-description)
* [Licensing and Terms](#licensing-and-terms)

## Introduction

This plugin enables communication between Huawei Ads SDK and Flutter platform. Huawei Ads Kit Plugin for Flutter provides the following 2 services:
- **Publisher Service**: HUAWEI Ads Publisher Service utilizes Huawei's vast user base and extensive data capabilities to deliver targeted, high quality ad content to users. With this service, your app will be able to generate revenues while bringing your users content which is relevant to them.
- **Identifier Service**: HUAWEI Ads Kit provides the open advertising identifier (OAID) for advertisers to deliver personalized ads and attribute conversions.

## Installation Guide

- Before you get started, you must register as a HUAWEI developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

- Create an app by referring to [Creating an AppGallery Connect Project](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started#h1-1587521853252) and [Adding an App to the Project](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started#h1-1587521946133).

- On your Flutter project directory find and open your **pubspec.yaml** file and add library to dependencies.

    ```yaml
    dependencies:
        huawei_ads:
            # Replace {library path} with actual library path of Huawei Ads Kit Plugin for Flutter.
            path: {library path}
    ```

- Run

    ```
    flutter pub get
    ```
    **or**

    From Android Studio/IntelliJ: Click **Packages get** in the action ribbon at the top of pubspec.yaml.
    From VS Code: Click **Get Packages** located in right side of the action ribbon at the top of pubspec.yaml.

- Add a corresponding *import* statement in the Dart code and start the app.

## API Reference

This section does not cover all of the API, to read more visit [HUAWEI Developer](https://developer.huawei.com/) website.

### **Publisher Service - Ads**

### AdListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| AdListener(AdEvent event, {int errorCode}) | Ad status listener. |

### AdEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| clicked | Ad is tapped. |
| closed | Ad is closed. |
| failed | Ad request failed. |
| impression | An impression is recorded for an ad. |
| leave | Ad leaves an app. |
| loaded | Ad is loaded successfully. |
| opened | Ad is opened. |
| disliked | Ad is disliked. |

### AdParam
#### Methods
| Return Type | Method| Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | setGender(int gender)                         | Sets the gender. |
| void | setAdContentClassification(String adContentClassification)                         | Sets the ad content rating. |
| void | setTagForUnderAgeOfPromise(Integer tagForUnderAgeOfPromise)                         | Specifies whether to process ad requests as directed to users under the age of consent. |
| void | setTagForChildProtection(Integer tagForChildProtection)                        | Specifies whether to process ad requests according to the COPPA. |
| void | setNonPersonalizedAd(Integer nonPersonalizedAd)                         | Specifies whether to request personalized ads. |
| void | setAppCountry(String appCountry)                         | Sets the country code corresponding to the language in which an ad needs to be returned for an app. |
| void | setAppLang(String appLang)                         | Sets the language in which an ad needs to be returned for an app. |
| void | setBelongCountryCode(String countryCode)                        | Sets the home country code. |

### AdSize
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| AdSize | AdSize({int width, int height})                         | Ad size constructor. |

#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| width | final int | Ad height. |
| height | final int | Ad width. |

### AnchorType
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| bottom | Ad is anchored to the bottom of the screen. |
| top | Ad is anchored to the top of the screen. |


### HwAds
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| Future\<void\> | init()                        | Initializes the HUAWEI Ads SDK. |
| Future\<void\> | initWithAppCode (String appCode)                         | Initializes the HUAWEI Ads SDK. |
| Future\<String\> | getSdkVersion                        | Obtains the version number of the HUAWEI Ads SDK. |
| Future\<RequestOptions\> | getRequestOptions                         | Obtains the global request configuration. |
| Future\<void\> | setRequestOptions(RequestOptions options)                         | Provides the global ad request configuration. |

### RequestOptions
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getAdContentClassification                        | Obtains the ad content rating. |
| int | getTagForUnderAgeOfPromise                        | Obtains the setting directed to users under the age of consent. |
| int | getTagForChildProtection                        | Obtains the child-directed setting. |
| int | getNonPersonalizedAd                        | Checks whether to request non-personalized ads. |
| String | getAppCountry                        | Obtains the country code corresponding to the language in which an ad needs to be returned for an app. |
| String | getAppLang                        | Obtains the language in which an ad needs to be returned for an app. |
| void | setAdContentClassification(String contentClassification)                        | Sets the ad content rating. |
| void | setTagForUnderAgeOfPromise(int tag)                        | Specifies whether to process ad requests as directed to users under the age of consent. |
| void | setTagForChildProtection(int tag)                        | Specifies whether to process ad requests according to the COPPA. |
| void | setNonPersonalizedAd(int ad)                        | Specifies whether to request non-personalized ads. |
| void | setAppCountry(String country)                        | Sets the country code corresponding to the language in which an ad needs to be returned for an app. |
| void | setAppLang(String lang)                        | Sets the language in which an ad needs to be returned for an app. |

### VideoConfiguration
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| int | getAudioFocusType                        | Obtains the video playback scenario where the audio focus needs to be obtained. |
| bool | isCustomizeOperationRequested                        | Checks whether a custom video control is used for a video ad. |
| bool | isStartMuted                        | Checks whether a video is initially muted. |
| void | setAudioFocusType                        | Sets the video playback scenario where the audio focus needs to be obtained. |
| void | setCustomizeOperationRequested                        | Specifies whether to use a custom video control for a video ad. |
| void | setStartMuted                        | Specifies whether to mute a video initially. |

### **Publisher Service - Banner**
### BannerAdSize
#### Constants
| Name | Type   | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| s360x57 | BannerAdSize | Banner ad size: 360 x 57 dp |
| s360x144 | BannerAdSize | Banner ad size: 360 x 144 dp |
| s320x50 | BannerAdSize | Banner ad size: 320 x 50 dp |
| s468x60 | BannerAdSize | Banner ad size: 468 x 60 dp |
| s320x100 | BannerAdSize | Banner ad size: 320 x 100 dp |
| s728x90 | BannerAdSize | Banner ad size: 728 x 90 dp |
| s300x250 | BannerAdSize | Banner ad size: 300 x 250 dp |
| s160x600 | BannerAdSize | Banner ad size: 160 x 600 dp |
| sInvalid | BannerAdSize | Invalid size. No ad can be requested using this size. |
| sDynamic | BannerAdSize | Dynamic banner ad size. The width of the parent layout and the height of the ad content are used. |
| sSmart | BannerAdSize | Dynamic banner ad size. The screen width and an adaptive height are used. |

#### Methods
| Return Type | Method | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| int | getWidth | Obtains the ad width. |
| Future\<int\> | getWidthPx | Obtains the ad width, in pixels. If it fails to be obtained, –1 is returned. |
| int | getHeight | Obtains the ad height. |
| Future\<int\> | getHeightPx | Obtains the ad height, in pixels. If it fails to be obtained, –1 is returned. |
| bool | isAutoHeightSize | Checks whether an adaptive height is used. |
| bool | isDynamicSize | Checks whether a dynamic size is used. |
| bool | isFullWidthSize | Checks whether a full-screen width is used. |
| Future \<BannerAdSize\> | getCurrentDirectionBannerSize | Creates a banner ad size based on the current device orientation and a custom width. |
| Future \<BannerAdSize\> | getLandscapeBannerSize | Creates a banner ad size based on a custom width in landscape orientation. |
| Future \<BannerAdSize\> | getPortraitBannerSize | Creates a banner ad size based on a custom width in portrait orientation. |

### BannerAd
#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| adUnitId | String | Ad slot ID. |
| size | BannerAdSize | Size of a banner ad. |
| bannerRefreshTime | int | Rotation interval for banner ads. |
| adParam | AdParam | Ad request. |
| listener | AdListener | Ad listener for an ad. |

#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | setAdListener(AdListener listener)                         | Sets an ad listener for an ad. |
| AdSize | getAdListener                         | Obtains an ad listener. |
| Future\<bool\> | loadAd()                         | Loads an ad. |
| Future\<bool\> | show({double offset, AnchorType anchorType})                         | Displays an ad. |
| Future\<bool\> | isLoading()                         | Checks whether ads are being loaded. |
| Future\<bool\> | pause()                         | Pauses any additional processing related to an ad. |
| Future\<bool\> | resume()                        | Resumes an ad after the pause() method is called last time. |
| Future\<bool\> | destroy()                        | Destroys an ad. |

### **Publisher Service - Interstitial**
### InterstitialAd
#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| adUnitId | String | Ad slot ID. |
| adParam | AdParam | Ad request. |
| listener | AdListener | Ad listener for an ad. |

#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | setAdListener(AdListener listener)                         | Sets an ad listener for an ad. |
| AdSize | getAdListener                         | Obtains an ad listener. |
| void | setAdListener(AdListener listener)                         | Sets an ad listener for an ad. |
| Future\<bool\> | loadAd()                         | Loads an ad. |
| Future\<bool\> | isLoading()                         | Checks whether ads are being loaded. |
| Future\<bool\> | isLoaded()                         | Checks whether ad loading is complete. |
| Future\<bool\> | show({double offset, AnchorType anchorType})                         | Displays an ad. |
| Future\<bool\> | destroy()                         | Destroys an ad. |

### **Publisher Service - Reward**
### Reward
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getName                        | Obtains the name of a reward item. |
| int | getAmount                        | Obtains the number of reward items. |


### RewardAd
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| RewardAd | instance                        | Returns the singleton RewardAd instance. |
| String | getUserId                        | Obtains a user ID. |
| Future\<bool\> | setUserId(String userId)                         | Sets a user ID. |
| String | getData                        | Obtains custom data. |
| Future\<bool\> | setData(String data)                         | Sets custom data. |
| RewardAdListener | getRewardAdListener                        | Obtains a rewarded ad loading listener. |
| void | setRewardListener(RewardAdListener listener)                         | Sets a rewarded ad listener. |
| void | setRewardVerifyConfig(RewardVerifyConfig parameters)                         | Sets server-side verification parameters. |
| Future\<bool\> | loadAd({String adUnitId, AdParam adParam})                        | Requests a rewarded ad. |
| Future\<bool\> | isLoaded()                         | Checks whether a rewarded ad is successfully loaded. |
| Future\<bool\> | show()                         | Displays a rewarded ad. |
| Future\<bool\> | pause()                         | Pauses a rewarded ad. |
| Future\<bool\> | resume()                        | Resumes a rewarded ad. |

### RewardAdListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| RewardAdListener(RewardAdEvent event, {Reward reward, int errorCode}) | Ad status listener. |

### RewardAdEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| loaded | Rewarded ad is successfully loaded. |
| failedToLoad | Rewarded ad fails to be loaded. |
| opened | Rewarded ad is opened. |
| leftApplication | User exits an app. |
| closed | Rewarded ad is closed |
| rewarded | Reward item when rewarded ad playback is completed. |
| started | Rewarded ad starts playing. |
| completed | Rewarded ad playback is completed. |
| failedToShow | Rewarded ad fails to be displayed. |

### RewardVerifyConfig
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getUserId                        | Obtains the current user ID. |
| String | setUserId(String id)                        | Sets a user ID. |
| String | getData                       | Obtains custom data. |
| String | setData(String data)                        | Sets custom data. |

### **Publisher Service - Splash**
### SplashAd
#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| adType | SplashAdType | Splash ad layout type. |
| ownerText | String | Ad owner text. |
| footerText | String | Ad footer text. |
| displayListener | SplashAdDisplayListener | Listener for the splash ad display or click event. |
| loadListener | SplashAdLoadListener | Splash ad loading listener. |

#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | setLogoResId(String logoResId)                        | Sets an app logo resource ID. |
| String | setLogoBgResId(String logoBgResId)                        | Sets an app background logo resource ID. |
| String | setMediaNameResId(String mediaNameResId)                        | Sets an app text resource ID. |
| String | setSloganResId(String resId)                        | Sets a default app launch image in portrait mode, which is displayed before a splash ad is displayed. |
| String | setWideSloganResId(String resId)                        | Sets a default app launch image in landscape mode, which is displayed before a splash ad is displayed. |
| String | setAudioFocusType(int audioFocusType)                        | Sets the audio focus preemption policy for a video splash ad. |
| Future\<bool\> | preloadAd({ String adUnitId, int orientation, AdParam adParam })                        | Preloads a splash ad. |
| Future\<bool\> | loadAd({ String adUnitId, int orientation, AdParam adParam, double topMargin})                        | Loads and displays a splash ad. |
| Future\<bool\> | isLoading()                        | Checks whether a splash ad is being loaded. |
| Future\<bool\> | isLoaded()                        | Checks whether a splash ad has been loaded. |
| Future\<bool\> | pause()                        | Suspends a splash ad. |
| Future\<bool\> | resume()                        | Resumes a splash ad. |
| Future\<bool\> | destroy()                        | Destroys a splash ad. |

### SplashAdLoadListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| SplashAdLoadListener(SplashAdLoadEvent event, {int errorCode}) | Splash ad loading listener. |

### SplashAdDisplayListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| SplashAdDisplayListener(SplashAdDisplayEvent event) | Splash ad display listener. |

### SplashAdLoadEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| loaded | Splash ad is loaded successfully. |
| dismissed | Splash ad disappears. |
| failedToLoad | Splash ad fails to be loaded. |

### SplashAdDisplayEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| showed | Splash ad is displayed. |
| click | Splash ad is clicked. |

### **Publisher Service - Native**
### NativeAd
#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| adUnitId | final String | Ad slot ID. |
| styles | final NativeStyles | Style options for the views present in a native ad. |
| type | final NativeAdType | Native ad layout type. |
| controller | final NativeAdController | Controller object that grants control over native ads. |

### NativeAdController
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | setAdListener(AdListener listener)                       | Sets an ad action listener. |
| void | setDislikeAdListener(DislikeAdListener listener)                       | Sets an ad closing listener. |
| Future\<VideoOperator\> | getVideoOperator()                       | Obtains the video controller of an ad. |
| void | gotoWhyThisAdPage()                       | Goes to the page explaining why an ad is displayed. |
| Future\<bool\> | isLoading()                       | Checks whether ads are being loaded. |
| void | setAllowCustomClick()                       | Enables custom tap gestures. |
| Future\<bool\> | isCustomClickAllowed()                       | Checks whether custom tap gestures are enabled. |
| Future\<String\> | getAdSource()                       | Obtains an ad source. |
| Future\<String\> | getDescription()                       | Obtains the description of an ad. |
| Future\<String\> | getCallToAction()                       | Obtains the text to be displayed on a button, for example, View Details or Install. |
| Future\<String\> | getTitle()                       | Obtains the title of an ad. |
| Future\<bool\> | dislikeAd(DislikeAdReason reason)                      | Does not display the current ad. After this method is called, the current ad is closed. |
| Future\<bool\> | isCustomDislikeThisAdEnable()                       | Checks whether custom tap gestures are enabled. |
| Future\<bool\> | triggerClick(Bundle bundle)                       | Reports a tap. |
| Future\<bool\> | recordClickEvent()                       | Reports a custom tap gesture. |
| Future\<bool\> | recordImpressionEvent(Bundle bundle)                       | Reports an ad impression. |
| Future\<List\<DislikeAdReason\>\> | getDislikeAdReasons()                       | Obtains the choice of not displaying the current ad. |
| void | destroy()                       | Destroys an ad object. |

### NativeAdConfiguration
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| AdSize | getAdSize                     | Obtains the requested ad size. |
| void | setAdSize(AdSize adSize)                     | Sets the size of the request ad. |
| int | getChoicesPosition                     | Obtains the position of an ad choice icon. |
| void | setChoicesPosition(int position)                      | Sets the position of an ad choice icon. |
| int | getMediaDirection                     | Obtains the direction of an ad image. |
| void | setMediaDirection(int direction)                     | Sets the direction of an ad image. |
| int | getMediaAspect                     | Obtains the aspect ratio of an ad image. |
| void | setMediaAspect(int aspect)                     | Sets an image aspect ratio. |
| bool | isRequestMultiImages                     | Checks whether multiple ad images are requested. |
| void | setRequestMultiImages(bool multiImage)                     | Specifies whether to request multiple ad images. |
| bool | isReturnUrlsForImages                     | Checks whether the SDK is allowed to download native ad images. |
| void |  setReturnUrlsForImages(bool returnUrls)                     | Specifies whether to allow the SDK to download native ad images. |
| VideoConfiguration | getVideoConfiguration                     | Obtains video configuration information. |
| void | setVideoConfiguration(VideoConfiguration configuration)                     | Sets the information about native video. |
| void | setRequestCustomDislikeThisAd(bool dislike)                     | Specifies whether to customize the function of not displaying the ad. |

### NativeStyles
#### Properties
| Name | Type | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| showMedia | bool | Whether to show the media content of the native ad. |
| mediaImageScaleType | String | Scale type of the native ad image. |

#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | void setFlag({ bool isVisible, double fontSize, FontWeight fontWeight, Color color, Color bgColor })                      | Sets the style for the ad flag text view. |
| void | void setTitle({ bool isVisible, double fontSize, FontWeight fontWeight, Color color, Color bgColor })                      | Sets the style for the ad title text view. |
| void | void setSource({ bool isVisible, double fontSize, FontWeight fontWeight, Color color, Color bgColor })                      | Sets the style for the ad owner text view. |
| void | void setDescription({ bool isVisible, double fontSize, FontWeight fontWeight, Color color, Color bgColor })                      | Sets the style for the ad description text view. |
| void | void setCallToAction({ bool isVisible, double fontSize, FontWeight fontWeight, Color color, Color bgColor })                      | Sets the style for the ad action button. |

### FontWeight
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| normal | Normal text. |
| bold | Bold text. |
| italic | Italic text. |
| boldItalic | Bold and italic text. |

### DislikeAdListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| DislikeAdListener(AdEvent event) | Listens for when a native ad is closed |

### DislikeAdReason
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getDescription                     | Obtains the reason why a user dislikes an ad. |

### **Publisher Service - Consent**
### Consent
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| Future\<String\> | getTestDeviceId()                     | Retrieves the test device id. |
| Future\<String\> | addTestDeviceId(String deviceId)                     | Adds a new test device id. |
| Future\<String\> | setDebugNeedConsent(DebugNeedConsent needConsent)                     | Set whether user consent is needed. |
| Future\<String\> | setUnderAgeOfPromise(bool ageOfPromise)                     | Whether to process ad requests as directed to users under the age of consent. |
| Future\<String\> | setConsentStatus(ConsentStatus status)                     | Whether to display personalized ads. |
| Future\<String\> | requestConsentUpdate(final ConsentUpdateListener listener)                     | Request consent information. |
| Future\<String\> | updateSharedPreferences(String key, int value)                     | Update the shared preferences related to user consent |
| Future\<String\> | getSharedPreferences(String key)                     | Retrieve a shared preference related to user consent. |

### ConsentUpdateListener
| Signature | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| ConsentUpdateListener(ConsentUpdateEvent event, {ConsentStatus consentStatus, bool isNeedConsent, List<AdProvider> adProviders, String description}) | Ad status listener. |

### ConsentUpdateEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| success | Consent update was successful. |
| failed | Consent update failed. |

### AdProvider
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getId                     | Retrieves ad provider id. |
| void | setId(String id)                     | Sets ad provider id. |
| String | getName                     | Retrieves ad provider name. |
| void | setName(String name)                     | Sets ad provider name. |
| String | getServiceArea                     | Retrieves service area information. |
| void | setServiceArea(String serviceArea)                     | Sets service area information. |
| String | getPrivacyPolicyUrl                     | Retrieves the url for privacy policy. |
| void | setPrivacyPolicy(String policyUrl)                     | Sets the url for privacy policy. |
| bool | valid()                     | Checks whether an ad provider object is valid. |

### **Identifier Service**
### AdvertisingIdClient
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| Future\<AdvertisingIdClientInfo\> | getAdvertisingIdInfo()                     | Obtains the OAID and setting of Disable personalized ads. |
| Future\<bool\> | verifyAdId (String adId, bool limitTracking)                     | Verifies the OAID and setting of Disable personalized ads. |

### AdvertisingIdClientInfo
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getId                    | Obtains the current OAID. |
| bool | isLimitAdTrackingEnabled                     | Obtains the current setting of Disable personalized ads. |

### **Identifier Service - InstallReferrer**
### InstallReferrerClient
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| void | setTest                     | Set whether to run the service in test mode. |
| void | startConnection([bool isTest])                     | Starts to connect to the install referrer service. |
| void | endConnection()                     | Ends the service connection and releases all occupied resources. |
| Future\<bool\> | isReady()                     | Indicates whether the service connection is ready. |
| Future\<bool\> | getInstallReferrer()                     | Obtains install referrer information. |

### InstallReferrerStateEvent
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| setupFinished | Service connection is complete. |
| disconnected | Service is disconnected. |

### InstallReferrerStateListener
| Signature | Description|
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| typedef InstallReferrerStateListener(InstallReferrerStateEvent event, {ReferrerResponse responseCode}) | Install referrer connection state listener. |

### ReferrerResponse
#### Enum Values
| Name | Description  |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|
| disconnected | Failed to connect to the service. |
| ok | Connected to the service successfully. |
| unavailable | The service does not exist. |
| featureNotSupported | The service is not supported. |
| developerError | A call error occurred. |

### ReferrerDetails
#### Methods
| Return Type | Method | Description |
|:-----------------------------------:|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------:|
| String | getInstallReferrer                     | Obtains install referrer information. |
| int | getReferrerClickTimestampMillisecond                     | Obtains the ad click timestamp, in milliseconds. |
| int | getInstallBeginTimestampMillisecond                    | Obtains the app installation timestamp, in milliseconds. |


## Configuration Description

No.

## Licensing and Terms

Huawei Ads Kit Flutter Plugin uses the Apache 2.0 license.