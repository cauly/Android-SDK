# 목차
1. [미디에이션 시작하기](#1-미디에이션-시작하기)
    * [AndroidManifest.xml 속성 지정](#androidmanifestxml-속성-지정)
    * [SDK 추가](#sdk-추가)
2. [Admob 광고 추가하기](#2-admob-광고-추가하기)
    * [Admob 설정](#admob-설정)
    * [Admob 배너 광고 추가하기](#admob-배너-광고-추가하기)
    * [Admob 전면 광고 추가하기](#admob-전면-광고-추가하기)
    * [Admob 네이티브 광고 추가하기](#admob-네이티브-광고-추가하기)
    * [Admob 보상형 광고 추가하기](#admob-보상형-광고-추가하기)
3. [파트너 통합 네트워크 추가하기](#3-파트너-통합-네트워크-추가하기)
    * [Inmobi 설정](#inmobi-설정)
    * [AppLovin 설정](#applovin-설정)
    * [Vungle 설정](#vungle-설정)
    * [DT Exchange 설정](#dt-exchange-설정)
    * [광고 요청 연결](#광고-요청-연결)
4. [커스텀 이벤트 네트워크 추가하기](#4-커스텀-이벤트-네트워크-추가하기)
    * [Cauly 광고 추가하기](#cauly-광고-추가하기)
        * [Cauly 배너 광고 추가하기](#cauly-배너-광고-추가하기)
        * [Cauly 전면 광고 추가하기](#cauly-전면-광고-추가하기)
        * [Cauly 네이티브 광고 추가하기](#cauly-네이티브-광고-추가하기)

- - - 

# 1. 미디에이션 시작하기

### AndroidManifest.xml 속성 지정

#### 필수 퍼미션 추가

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```


#### admob 연결

- APPLICATION_ID: 구글 AdMob 대시보드에 등록된 앱의 ID를 입력

    ```xml
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-xxxxxxxxxx" />
    ```



### SDK 추가

#### app level build.gradle 에 'dependencies'  추가

- inmobi 10.1.2.1 은 targetSdkVersion 32 이상에서 사용 가능합니다.
- com.google.android.ads:mediation-test-suite:3.0.0 은 앱 테스트 시 필수 항목으로, 상용화 시 반드시 제거해야합니다. 

    ```clojure
    dependencies {
        // google admob
        implementation 'com.google.android.gms:play-services-ads:21.4.0'

        // google admob mediation test
        implementation 'com.google.android.ads:mediation-test-suite:3.0.0'

        // cauly sdk
        implementation 'com.fsn.cauly:cauly-sdk:3.5.24'

        // inmobi mediation
        implementation 'com.google.ads.mediation:inmobi:10.1.2.1'

        // applobin mediation
        implementation 'com.google.ads.mediation:applovin:11.6.1.0'

        // vungle mediation
        implementation 'com.google.ads.mediation:vungle:6.12.0.1'

        // DT Exchange mediation
        implementation 'com.google.ads.mediation:fyber:8.2.1.0'
    }
    ```



#### 최상위 level build.gradle 에  maven repository 추가

- Cauly SDK 사용을 위해 필수

    ```clojure
    allprojects {
        repositories {
            google()
            jcenter()
            maven {
                url "s3://repo.cauly.net/releases"
                credentials(AwsCredentials) {
                    accessKey "AKIAWRZUK5MFKYVSUOLB"
                    secretKey "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"
                }
            }
        }
    }
    ```



# 2. Admob 광고 추가하기

### Admob 설정

#### Admob 광고 SDK 초기화

``` java
MobileAds.initialize(this, new OnInitializationCompleteListener() { 
    @Override 
    public void onInitializationComplete(InitializationStatus initializationStatus) { 
    } 
}); 
```



#### 테스트 기기 추가

##### `상용화 시 테스트 기기 ID를 설정하는 코드를 반드시 삭제해야 합니다.`

- 광고를 요청한 후 logcat 출력에서 테스트 기기 ID를 복사합니다.

    ``` clojure
    I/Ads: Use RequestConfiguration.Builder.setTestDeviceIds(Arrays.asList("33BE2250B43518CCDA7DE426D04EE231"))
    to get test ads on this device."
    ```



- `RequestConfiguration.Builder().setTestDeviceIds()`를 호출하고 테스트 기기 ID의 목록을 전달하도록 코드를 수정합니다.

    ``` java
    MobileAds.setRequestConfiguration(
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(Config.ADMOB_TEST_DEVICE_ID))
                .build());
    ```



#### 미디에이션 테스트 모음 실행

- 앱 실행 시 Splash 화면 이후 최초로 표시되는 Activity 화면에 아래 코드를 추가해야합니다.
    - 로그인 페이지가 있는 경우 로그인 Activity의 onCreate() 에 코드 추가
    - 로그인 페이지가 없는 경우 메인 화면 Activity의 onCreate() 에 코드 추가
- 상용화 시 해당 코드를 반드시 삭제해야 합니다.

    ``` java
    MediationTestSuite.launch(MainActivity.this);
    ```



### Admob 배너 광고 추가하기

#### ‘광고 삽입할 부분’.xml 

- adUnitId: 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.

``` xml
<com.google.android.gms.ads.AdView
        xmlns:ads="http://schemas.android.com/apk/res-auto"
        android:id="@+id/adView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        ads:adSize="BANNER"
        ads:adUnitId="ca-app-pub-xxxxxxxxxx">
    </com.google.android.gms.ads.AdView>
```



#### 광고를 삽입하고 싶은 layout에 소스를 삽입

``` java
private AdRequest adRequest;
private AdView adView;

 @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.i(TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
        }
    });
    //admob 레이아웃에 AdView 추가
    adView = (AdView) findViewById(R.id.adView);
    // 테스트 시 RequestConfiguration.Builder.setTestDeviceIds()를 호출하고 테스트 기기 ID 목록을 전달하도록 수정해야 합니다.
    // 상용화 시 반드시 삭제해야합니다.
    MobileAds.setRequestConfiguration(
            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(Config.ADMOB_TEST_DEVICE_ID))
                    .build());
    //admob 배너 리스너
    setAdmobBannerListener();
    //admob adRequest
    adRequest = new AdRequest.Builder().build();

    //배너 요청
    adView.loadAd(adRequest);

    MediationTestSuite.launch(MainActivity.this);       // 앱 테스트시 필수로 상용화시 삭제해야 합니다.
}

// AdListener 클래스를 통해 광고의 작동 방식을 추가로 맞춤설정할 수 있습니다.
private void setAdmobBannerListener() {
    adView.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG, "banner onAdLoaded");
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            // Code to be executed when an ad request fails.
            Log.d(TAG, "onAdFailedToLoad " + adError.getCode() + "  " + adError.getMessage());

        }

        @Override
        public void onAdImpression() {
            // Code to be executed when an impression is recorded
            // for an ad.
            Log.d(TAG, "onAdImpression");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG, "onAdOpened");
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.d(TAG, "onAdClicked");
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
            Log.d(TAG, "onAdClosed");
        }

    });
}
```



### Admob 전면 광고 추가하기

- InterstitialAd.load() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.
- InterstitialAdLoadCallback: InterstitialAd 로드와 관련된 이벤트를 처리합니다.
- FullScreenContentCallback: InterstitialAd 표시와 관련된 이벤트를 처리합니다. 
    - InterstitialAd를 표시하기 전에 콜백을 설정해야 합니다.

``` java
private InterstitialAd mInterstitialAd;
private Button request_interstitial_btn;
private Button show_interstitial_btn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.i(TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
        }
    });
    
    // 테스트 시 RequestConfiguration.Builder.setTestDeviceIds()를 호출하고 테스트 기기 ID 목록을 전달하도록 수정해야 합니다.
    // 상용화 시 반드시 삭제해야합니다.
    MobileAds.setRequestConfiguration(
            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(Config.ADMOB_TEST_DEVICE_ID))
                    .build());
    //admob adRequest
    adRequest = new AdRequest.Builder().build();

    //전면 요청
    request_interstitial_btn = findViewById(R.id.request_interstitial_btn);
    request_interstitial_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadInterstitialAd();
        }
    });

    // 전면 노출
    show_interstitial_btn = findViewById(R.id.show_interstitial_btn);
    show_interstitial_btn.setEnabled(false);
    show_interstitial_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showInterstitialAd();
        }
    });

    MediationTestSuite.launch(MainActivity.this);       // 앱 테스트시 필수로 상용화시 삭제해야 합니다.
}

private void loadInterstitialAd() {
    AdRequest adRequest = new AdRequest.Builder().build();

    // "ca-app-pub-xxxxxxxxxx"에 광고 단위에 지정할 고유 식별자를 설정합니다.
    InterstitialAd.load(this, "ca-app-pub-xxxxxxxxxx", adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    show_interstitial_btn.setEnabled(true);
                    Log.i(TAG, "onAdLoaded");
                    Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was dismissed.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Log.e(TAG, "The ad failed to show.");
                            mInterstitialAd = null;
                            show_interstitial_btn.setEnabled(false);
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "The ad was shown.");
                        }
                    });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;

                    String error =
                            String.format(
                                    "domain: %s, code: %d, message: %s",
                                    loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                    Toast.makeText(
                            MainActivity.this, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                            .show();
                }
            });
}

private void showInterstitialAd() {
    show_interstitial_btn.setEnabled(false);
    mInterstitialAd.show(this);
}

```



### Admob 네이티브 광고 추가하기

- AdLoader.Builder() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.

``` java
private NativeAd nativeAd;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.i(TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
        }
    });
    // 테스트 시 RequestConfiguration.Builder.setTestDeviceIds()를 호출하고 테스트 기기 ID 목록을 전달하도록 수정해야 합니다.
    // 상용화 시 반드시 삭제해야합니다.
    MobileAds.setRequestConfiguration(
            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(Config.ADMOB_TEST_DEVICE_ID))
                    .build());
    //admob adRequest
    adRequest = new AdRequest.Builder().build();

    //네이티브 요청
    loadAdmobNativeAd();

    MediationTestSuite.launch(MainActivity.this);       // 앱 테스트시 필수로 상용화시 삭제해야 합니다.
}

private void loadAdmobNativeAd() {
    native_btn.setEnabled(false);

    // "ca-app-pub-xxxxxxxxxx"에 광고 단위에 지정할 고유 식별자를 설정합니다.
    AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-xxxxxxxxxx")
            .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    boolean isDestroyed = false;
                    native_btn.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = isDestroyed();
                    }
                    if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                        nativeAd.destroy();
                        return;
                    }
                    if (MainActivity.this.nativeAd != null) {
                        MainActivity.this.nativeAd.destroy();
                    }
                    MainActivity.this.nativeAd = nativeAd;
                    FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                    NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, frameLayout, false);
                    populateNativeAdView(nativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            })
            .withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    native_btn.setEnabled(true);
                    String error =
                            String.format(
                                    Locale.getDefault(),
                                    "domain: %s, code: %d, message: %s",
                                    loadAdError.getDomain(),
                                    loadAdError.getCode(),
                                    loadAdError.getMessage());
                    Toast.makeText(
                            MainActivity.this,
                            "Failed to load native ad with error " + error,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            })
            .build();

    adLoader.loadAd(new AdRequest.Builder().build());
}

```



### Admob 보상형 광고 추가하기

- RewardedAd.load() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.
- RewardedAdLoadCallback: RewardedAd 로드와 관련된 이벤트를 처리합니다.
- FullScreenContentCallback: RewardedAd 표시와 관련된 이벤트를 처리합니다.
- OnUserEarnedRewardListener: 보상형 광고를 게재할 때 보상 이벤트를 처리합니다.

``` java
private RewardedAd mRewardedAd;
private Button request_rewarded_btn;
private Button show_rewarded_btn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.i(TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }
        }
    });

    // 테스트 시 RequestConfiguration.Builder.setTestDeviceIds()를 호출하고 테스트 기기 ID 목록을 전달하도록 수정해야 합니다.
    // 상용화 시 반드시 삭제해야합니다.
    MobileAds.setRequestConfiguration(
            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(Config.ADMOB_TEST_DEVICE_ID))
                    .build());
    //admob adRequest
    adRequest = new AdRequest.Builder().build();

    // 보상형 광고 요청
    request_rewarded_btn = findViewById(R.id.request_rewarded_btn);
    request_rewarded_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadRewardedAd();
        }
    });

    show_rewarded_btn = findViewById(R.id.show_rewarded_btn);
    show_rewarded_btn.setEnabled(false);
    show_rewarded_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showRewardedAd();
        }
    });

    MediationTestSuite.launch(MainActivity.this);       // 앱 테스트시 필수로 상용화시 삭제해야 합니다.
}

private void loadRewardedAd() {
    if (mRewardedAd == null) {
        AdRequest adRequest = new AdRequest.Builder().build();
        // "ca-app-pub-xxxxxxxxxx"에 광고 단위에 지정할 고유 식별자를 설정합니다.
        RewardedAd.load(this, "ca-app-pub-xxxxxxxxxx", adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        show_rewarded_btn.setEnabled(false);
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                        Toast.makeText(MainActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        show_rewarded_btn.setEnabled(true);
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdLoaded");
                        Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


private void showRewardedAd() {
    if (mRewardedAd == null) {
        Log.d("TAG", "The rewarded ad wasn't ready yet.");
        return;
    }
    show_rewarded_btn.setEnabled(false);

    mRewardedAd.setFullScreenContentCallback(
            new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "onAdShowedFullScreenContent");
                    Toast.makeText(MainActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.d(TAG, "onAdFailedToShowFullScreenContent");
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null;
                    Toast.makeText(MainActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                            .show();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null;
                    Log.d(TAG, "onAdDismissedFullScreenContent");
                    Toast.makeText(MainActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                            .show();
                    // Preload the next rewarded ad.
//                        MainActivity.this.loadRewardedAd();
                }
            });
    Activity activityContext = MainActivity.this;
    mRewardedAd.show(
            activityContext,
            new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            }
    );

}

```



# 3. 파트너 통합 네트워크 추가하기

### Inmobi 설정

``` java
Bundle inMobiextras= new Bundle();
inMobiextras.putString(InMobiNetworkKeys.AGE_GROUP, InMobiNetworkValues.BETWEEN_25_AND_29);
inMobiextras.putString(InMobiNetworkKeys.AREA_CODE, "12345");
InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
```


#### Inmobi 는 Android 번들로 어댑터에 전달할 수 있는 추가 요청 매개변수를 지원합니다.
요청 매개변수|값
---|---
InMobiNetworkKeys.AGE_GROUP<br/>사용자의 연령대|InMobiNetworkValues.BELOW_18<br/>InMobiNetworkValues.BETWEEN_18_AND_24<br/>InMobiNetworkValues.BETWEEN_25_AND_29<br/>InMobiNetworkValues.BETWEEN_30_AND_34<br/>InMobiNetworkValues.BETWEEN_35_AND_44<br/>InMobiNetworkValues.BETWEEN_45_AND_54<br/>InMobiNetworkValues.BETWEEN_55_AND_65<br/>InMobiNetworkValues.ABOVE_65
InMobiNetworkKeys.EDUCATION<br/>사용자의 학력|InMobiNetworkValues.EDUCATION_HIGHSCHOOLORLESS<br/>InMobiNetworkValues.EDUCATION_COLLEGEORGRADUATE<br/>InMobiNetworkValues.EDUCATION_POSTGRADUATEORABOVE
InMobiNetworkKeys.AGE<br/>사용자의 연령|문자열
InMobiNetworkKeys.POSTAL_CODE<br/>사용자의 우편번호(일반적으로 5자리 숫자)|문자열
InMobiNetworkKeys.AREA_CODE<br/>사용자의 지역 번호(전화번호의 일부)|문자열
InMobiNetworkKeys.LANGUAGE<br/> 사용자의 모국어(알려진 경우)|문자열
InMobiNetworkKeys.CITY<br/>사용자의 도시|문자열
InMobiNetworkKeys.STATE<br/>사용자의 상태|문자열
InMobiNetworkKeys.COUNTRY<br/>사용자의 국가|문자열
InMobiNetworkKeys.LOGLEVEL<br/>InMobi SDK의 로그 수준을 설정합니다.|InMobiNetworkValues.LOGLEVEL_NONE<br/>InMobiNetworkValues.LOGLEVEL_DEBUG<br/>InMobiNetworkValues.LOGLEVEL_ERROR


### AppLovin 설정

``` java
Bundle appLovinExtras = new AppLovinExtras.Builder()
        .setMuteAudio(true)
        .build();
```



### Vungle 설정

``` java
Bundle vungleExtras = new VungleExtrasBuilder(null)
        .setStartMuted(false)
        .build();
```



### DT Exchange 설정

``` java
Bundle fyberExtras = new Bundle();
fyberExtras.putInt(InneractiveMediationDefs.KEY_AGE, 10);
```



### 광고 요청 연결

``` java
adRequest = new AdRequest.Builder()
    .addNetworkExtrasBundle(InMobiAdapter.class, inMobiextras)
    .addNetworkExtrasBundle(ApplovinAdapter.class, appLovinExtras)
    .addNetworkExtrasBundle(VungleAdapter.class, vungleExtras)      // Rewarded
    .addNetworkExtrasBundle(VungleInterstitialAdapter.class, vungleExtras)      // Interstitial
    .addNetworkExtrasBundle(FyberMediationAdapter.class, fyberExtras)
    .build();
```



# 4. 커스텀 이벤트 네트워크 추가하기

### Cauly 광고 추가하기

#### `CaulyAdInfo 설정방법`

AdInfo|설 명
---|---
Appcode|APP 등록 후 부여 받은 APP CODE[발급ID] 입력
Effect()|LeftSlide(기본값) : 왼쪽에서 오른쪽으로 슬라이드<br/>RightSlide : 오른쪽에서 왼쪽으로 슬라이드<br/>TopSlide : 위에서 아래로 슬라이드<br/>BottomSlide : 아래서 위로 슬라이드<br/>FadeIn : 전에 있던 광고가 서서히 사라지는 효과 <br/>Circle : 한 바퀴 롤링<br/>None : 애니메이션 효과 없이 바로 광고 교체
reloadInterval()|min(기본값) : 20초)<br/>max : 120 초
dynamicReloadInterval()|true(기본값) : 광고에 따라 노출 주기 조정할 수 있도록 하여 광고 수익 상승 효과 기대<br/>false : reloadInterval 설정 값으로 Rolling
bannerHeight()|Adaptive : 적응형 높이 형태 </br> Fixed : 높이 고정 형태
setBannerSize ()| Adaptive 지원하는 배너 사이즈 : (기본값)320x50, 320x100<br/>Fixed 지원하는 배너 사이즈 : (기본값)320x50, 320x100,300x250<br/>* xml 방식의 경우 320x50만 지원합니다.
threadPriority()|스레드 우선 순위 지정 : 1~10(기본값 : 5)
tagForChildDirectedTreatment(boolean)	|14세 미만 일 때 true
gdprConsentAvailable(boolean)	|gdpr 동의 일 때 true
statusbarHide()|전면광고 상태가 가리기 옵션(true : 상태가 가리기)

#### Cauly 배너 광고 추가하기

- CaulyBanner

``` java
public class CaulyBanner extends Adapter{

    protected static final String TAG = CaulyBanner.class.getSimpleName();

    private CaulyBannerLoader bannerLoader;

    @Override
    public void loadBannerAd(@NonNull MediationBannerAdConfiguration mediationBannerAdConfiguration, @NonNull MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> callback) {
        bannerLoader = new CaulyBannerLoader(mediationBannerAdConfiguration, callback);
        bannerLoader.loadAd();
    }

    // This method won't be called for custom events.
    @Override
    public void initialize(@NonNull Context context, @NonNull InitializationCompleteCallback initializationCompleteCallback, @NonNull List<MediationConfiguration> list) {
        initializationCompleteCallback.onInitializationSucceeded();
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        String versionString = "1.0.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 4) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = "1.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 3) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

}
```

- CaulyBannerLoader

``` java
public class CaulyBannerLoader implements CaulyAdBannerViewListener, MediationBannerAd {
    final static String TAG = CaulyBannerLoader.class.getSimpleName();
    private final MediationBannerAdConfiguration mediationBannerAdConfiguration;
    private final MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> mediationAdLoadCallback;
    private MediationBannerAdCallback bannerAdCallback;

    private CaulyAdBannerView adView = null;
    private Activity bannerActivity = null;
    private ViewGroup layout = null;

    /** Error raised when the custom event adapter cannot obtain the ad unit id. */
    public static final int ERROR_NO_AD_UNIT_ID = 101;
    /** Error raised when the custom event adapter cannot obtain the activity context. */
    public static final int ERROR_NO_ACTIVITY_CONTEXT = 103;
    public static final String SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android";
    public static final String CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent";

    public CaulyBannerLoader(MediationBannerAdConfiguration mediationBannerAdConfiguration, MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> mediationAdLoadCallback) {
        this.mediationBannerAdConfiguration = mediationBannerAdConfiguration;
        this.mediationAdLoadCallback = mediationAdLoadCallback;
    }

    /** Loads a banner ad from the third party ad network. */
    public void loadAd() {
        Log.i(TAG, "Admob Mediation : Cauly Banner loadBannerAd.");
        String serverParameter =  mediationBannerAdConfiguration.getServerParameters().getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(new AdError(ERROR_NO_AD_UNIT_ID, "Ad unit id is empty", CUSTOM_EVENT_ERROR_DOMAIN));
            return;
        }
        Log.d(TAG, "Received server parameter.");

        Context context = mediationBannerAdConfiguration.getContext();

        this.bannerActivity = (Activity) context;
        if (adView != null) {
            adView.destroy();
            adView = null;
            adView.removeAllViews();
            adView = null;
        }

        //AdView의 parent layout
        layout = bannerActivity.findViewById(R.id.adView);

        // 광고 정보 설정
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter)
                .bannerHeight(CaulyAdInfoBuilder.FIXED)
                .banner_interval(false)
                .dynamicReloadInterval(false)
                .effect("None")
                .build();

        // Cauly 배너 광고 View 생성.
        adView = new CaulyAdBannerView(context);

        // 광고 정보 설정
        adView.setAdInfo(adInfo);
        adView.setAdViewListener(this);


        // 광고로드
        adView.load(context, layout);
    }

    @Override
    public void onReceiveAd(CaulyAdBannerView caulyAdBannerView, boolean isChargeableAd) {
        // 광고 요청과 동시에 add된 광고 view를 제거함
        layout.removeView(adView);

        if (adView != null) {
            Log.e(TAG, "onReceiveAd onAdLoaded-=-=-=-=-: ");
            adView.show();

            bannerAdCallback = mediationAdLoadCallback.onSuccess(this);
            bannerAdCallback.onAdOpened();
            bannerAdCallback.reportAdImpression();
        }
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdBannerView caulyAdBannerView, int i, String s) {
        Log.e(TAG, "Failed to fetch the banner ad.");
        mediationAdLoadCallback.onFailure(new AdError(i, s, SAMPLE_SDK_DOMAIN));
        layout.removeView(adView);
    }

    @Override
    public void onShowLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onShowLandingScreen");
        bannerAdCallback.onAdLeftApplication();
        bannerAdCallback.reportAdClicked();
    }

    @Override
    public void onCloseLandingScreen(CaulyAdBannerView caulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onCloseLandingScreen");
    }

    @Override
    public void onTimeout(CaulyAdBannerView caulyAdBannerView, String s) {

    }

    @NonNull
    @Override
    public View getView() {
        return adView;
    }
}

```



#### Cauly 전면 광고 추가하기

- CaulyInterstitial

``` java
public class CaulyInterstitial extends Adapter {

    protected static final String TAG = CaulyInterstitial.class.getSimpleName();

    private CaulyInterstitialLoader interstitialLoader;

    @Override
    public void loadInterstitialAd(@NonNull MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration, @NonNull MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> callback) {
        interstitialLoader = new CaulyInterstitialLoader(mediationInterstitialAdConfiguration, callback);
        interstitialLoader.loadAd();
    }

    @Override
    public void initialize(@NonNull Context context, @NonNull InitializationCompleteCallback initializationCompleteCallback, @NonNull List<MediationConfiguration> list) {
        initializationCompleteCallback.onInitializationSucceeded();
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        String versionString = "1.0.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 4) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = "1.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 3) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }
}
```




- CaulyInterstitialLoader

``` java
public class CaulyInterstitialLoader implements CaulyInterstitialAdListener, MediationInterstitialAd {
    final static String TAG = CaulyInterstitialLoader.class.getSimpleName();
    /** Configuration for requesting the interstitial ad from the third-party network. */
    private final MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration;
    /** Callback that fires on loading success or failure. */
    private final MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> mediationAdLoadCallback;
    /** Callback for interstitial ad events. */
    private MediationInterstitialAdCallback interstitialAdCallback;

    CaulyInterstitialAd caulyInterstitialAd = null;
    private Activity interstitialActivity = null;

    /** Error raised when the custom event adapter cannot obtain the ad unit id. */
    public static final int ERROR_NO_AD_UNIT_ID = 101;
    /** Error raised when the custom event adapter cannot obtain the activity context. */
    public static final String SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android";
    public static final String CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent";

    /** Constructor. */
    public CaulyInterstitialLoader(MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration, MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> mediationAdLoadCallback) {
        this.mediationInterstitialAdConfiguration = mediationInterstitialAdConfiguration;
        this.mediationAdLoadCallback = mediationAdLoadCallback;
    }

    /** Loads the interstitial ad from the third-party ad network. */
    public void loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : Cauly Interstitial loadInterstitialAd.");

        String serverParameter =  mediationInterstitialAdConfiguration.getServerParameters().getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(new AdError(ERROR_NO_AD_UNIT_ID, "Ad unit id is empty", CUSTOM_EVENT_ERROR_DOMAIN));
            return;
        }
        Log.d(TAG, "Received server parameter.");

        Context context = mediationInterstitialAdConfiguration.getContext();
        this.interstitialActivity = (Activity) context;

        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(serverParameter)
                .build();

        // 전면 광고 생성
        CaulyInterstitialAd interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(this);

        // 광고 요청.
        interstial.requestInterstitialAd(this.interstitialActivity);
    }


    @Override
    public void showAd(@NonNull Context context) {
        caulyInterstitialAd.show();
        interstitialAdCallback.reportAdImpression();
        interstitialAdCallback.onAdOpened();
    }

    /** CaulyInterstitialAdListener **/
    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, boolean isChargeableAd) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        } else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }

        this.caulyInterstitialAd = caulyInterstitialAd;
        interstitialAdCallback = mediationAdLoadCallback.onSuccess(this);
        interstitialAdCallback.reportAdImpression();
    }

    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd, int i, String s) {
        Log.e(TAG, "Failed to fetch the interstitial ad.");
        mediationAdLoadCallback.onFailure(new AdError(i, s, SAMPLE_SDK_DOMAIN));
    }

    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
        interstitialAdCallback.onAdClosed();
    }

    @Override
    public void onLeaveInterstitialAd(CaulyInterstitialAd caulyInterstitialAd) {

    }
}
```



#### Cauly 네이티브 광고 추가하기


#### `CaulyAdInfo 설정방법(Native)`

AdInfo|설 명
---|---
Appcode|APP 등록 후 부여 받은 APP CODE[발급ID] 입력
layoutID|Native AD에 보여질 디자인을 작성하여 등록
mainImageID|메인 이미지 등록
titleID|제목 등록
subtitleID|부제목 등록
textID|설명 등록
iconImageID|아이콘 등록
adRatio|메인이미지 비율 설정 (기본값 : 720x480)

- CaulyNative

``` java
public class CaulyNative extends Adapter {

    private CaulyNativeLoader nativeLoader;

    @Override
    public void loadNativeAd(@NonNull MediationNativeAdConfiguration mediationNativeAdConfiguration, @NonNull MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> callback) {
        nativeLoader = new CaulyNativeLoader(mediationNativeAdConfiguration, callback);
        nativeLoader.loadAd();
    }

    @NonNull
    @Override
    public VersionInfo getSDKVersionInfo() {
        String versionString = "1.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 3) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        String versionString = "1.0.0.0";
        String[] splits = versionString.split("\\.");

        if (splits.length >= 4) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    @Override
    public void initialize(@NonNull Context context, @NonNull InitializationCompleteCallback initializationCompleteCallback, @NonNull List<MediationConfiguration> list) {
        initializationCompleteCallback.onInitializationSucceeded();
    }
}
```

- CaulyNativeLoader

``` java
public class CaulyNativeLoader {
//    private CustomEventNativeListener nativeListener;
    final static String TAG = CaulyNativeLoader.class.getSimpleName();

    /** Configuration for requesting the native ad from the third-party network. */
    private final MediationNativeAdConfiguration mediationNativeAdConfiguration;

    /** Callback that fires on loading success or failure. */
    private final MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> mediationAdLoadCallback;

    /** Callback for native ad events. */
    private MediationNativeAdCallback nativeAdCallback;

    private CaulyCustomAd mCaulyAdView;
    AdItem data = null;

    /** Error raised when the custom event adapter cannot obtain the ad unit id. */
    public static final int ERROR_NO_AD_UNIT_ID = 101;
    /** Error raised when the custom event adapter cannot obtain the activity context. */
    public static final int ERROR_NO_ACTIVITY_CONTEXT = 103;
    public static final String SAMPLE_SDK_DOMAIN = "io.admize.sdk.android";
    public static final String CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent";

    /** Constructor */
    public CaulyNativeLoader(MediationNativeAdConfiguration mediationNativeAdConfiguration, MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback> mediationAdLoadCallback) {
        this.mediationNativeAdConfiguration = mediationNativeAdConfiguration;
        this.mediationAdLoadCallback = mediationAdLoadCallback;
    }

    /** Loads the native ad from the third-party ad network. */
    public void loadAd() {
        Log.i(TAG, "Admob Mediation : Cauly Native loadNativeAd");
        String serverParameter = mediationNativeAdConfiguration.getServerParameters().getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD);
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(new AdError(ERROR_NO_AD_UNIT_ID, "Ad unit id is empty", CUSTOM_EVENT_ERROR_DOMAIN));
            return;
        }
        Log.d(TAG, "Received server parameter.");
        Context context = mediationNativeAdConfiguration.getContext();

        CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(serverParameter)
                .iconImageID(R.id.ad_app_icon) // 아이콘 이미지를 사용할 경우
                .mainImageID(R.id.ad_media)       //메인 이미지를 사용할 경우
                .build();
        mCaulyAdView = new CaulyCustomAd(context);
        mCaulyAdView.setAdInfo(adInfo);
        mCaulyAdView.setCustomAdListener(new CaulyCustomAdListener() {
            @Override
            public void onShowedAd() {
            }

            //광고 호출이 성공할 경우, 호출된다.
            @Override
            public void onLoadedAd(boolean isChargeableAd) {

                List<HashMap<String, Object>> adlist = mCaulyAdView.getAdsList();

                if (adlist != null && adlist.size() > 0) {
                    for (HashMap<String, Object> map : adlist) {
                        data = new kr.co.cauly.sdk.android.mediation.sample.natives.AdItem();
                        data.id = (String) map.get("id");
                        data.image = (String) map.get("image");
                        data.linkUrl = (String) map.get("linkUrl");
                        data.subtitle = (String) map.get("subtitle");
                        data.description = (String) map.get("description");
                        data.icon = (String) map.get("icon");
                        data.image_content_type = (String) map.get("image_content_type");
                    }

                }

                HashMap<String, Drawable> map = new HashMap<>();
                Thread uThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            map.put("icon", getImageFromURL(data.icon));
                            map.put("image", getImageFromURL(data.image));
                        } catch (Exception e) {
                            e.printStackTrace();
                            onFailedAd(-100, "internal error");
                        }
                    }
                };
                uThread.start();

                try {
                    uThread.join();
                    final SampleUnifiedNativeAdMapper unifiedNativeAdMapper =
                            new SampleUnifiedNativeAdMapper(
                                    context,
                                    map.get("icon"),
                                    map.get("image"),
                                    data,
                                    mCaulyAdView
                            );
                    nativeAdCallback = mediationAdLoadCallback.onSuccess(unifiedNativeAdMapper);
                    nativeAdCallback.reportAdImpression();
                } catch (Exception e) {
                    e.printStackTrace();

                    onFailedAd(-100, "internal error");
                }

            }

            // 광고 호출이 실패할 경우, 호출된다.
            @Override
            public void onFailedAd(int errCode, String errMsg) {
                Log.e(TAG, "errCode: " + errCode + " errMsg: " + errMsg);
                switch (errCode) {
                    case 200:
                        errCode = AdRequest.ERROR_CODE_NO_FILL;
                        break;
                    case 400:
                        errCode = AdRequest.ERROR_CODE_INVALID_REQUEST;
                        break;
                    case 500:
                        errCode = AdRequest.ERROR_CODE_NETWORK_ERROR;
                        break;
                    default:
                        errCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;
                        break;
                }
                mediationAdLoadCallback.onFailure(new AdError(errCode, errMsg, SAMPLE_SDK_DOMAIN));
            }

            // 광고가 클릭된 경우, 호출된다.
            @Override
            public void onClikedAd() {
                Log.d(TAG, "onClicked");
            }

        });
        //caulyCustomAd.requestAdView(CaulyCustomAd.NATIVE_LANDSCAPE,1/*ad_count 광고요청개수*/);
        mCaulyAdView.requestAdData(CaulyCustomAd.NATIVE_LANDSCAPE, 2);
    }

    public Drawable getImageFromURL(String data) throws Exception {
        Bitmap bitmap = null;
        URL url = new URL(data);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();

        InputStream is = conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(is);
        return new BitmapDrawable(Resources.getSystem(), bitmap);
    }

}
```