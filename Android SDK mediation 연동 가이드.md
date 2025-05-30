# 목차
1. [미디에이션 시작하기](#1-미디에이션-시작하기)
    * [AndroidManifest.xml 속성 지정](#androidmanifestxml-속성-지정)
    * [SDK 추가](#sdk-추가)
    * [UMP 설정](#ump-user-messaging-platform-설정)
2. [Admob 광고 추가하기](#2-admob-광고-추가하기)
    * [Admob 설정](#admob-설정)
    * [타겟팅 설정](#타겟팅-설정)
    * [Admob 앱 오프닝 광고 추가하기](#admob-앱-오프닝-광고-추가하기)
    * [Admob 배너 광고 추가하기](#admob-배너-광고-추가하기)
    * [Admob 전면 광고 추가하기](#admob-전면-광고-추가하기)
    * [Admob 네이티브 광고 추가하기](#admob-네이티브-광고-추가하기)
    * [Admob 보상형 광고 추가하기](#admob-보상형-광고-추가하기)
3. [파트너 통합 네트워크 추가하기](#3-파트너-통합-네트워크-추가하기)
    * [Inmobi 설정](#inmobi-설정-옵션)
    * [AppLovin 설정](#applovin-설정-옵션)
    * [Vungle 설정](#vungle-설정-옵션)
    * [DT Exchange 설정](#dt-exchange-설정-옵션)
    * [Mintegral 설정](#mintegral-설정-옵션)
    * [Pangle 설정](#pangle-설정)
    * [Unity Ads 설정](#unity-ads-설정)
    * [Facebook(meta) 설정](#facebookmeta-설정)
    * [IronSource 설정](#ironsource-설정)
    * [광고 요청 연결](#광고-요청-연결)
4. [커스텀 이벤트 네트워크 추가하기](#4-커스텀-이벤트-네트워크-추가하기)
    * [Cauly 광고 추가하기](#cauly-광고-추가하기)
        * [Cauly 배너 광고 추가하기](#cauly-배너-광고-추가하기)
        * [Cauly 전면 광고 추가하기](#cauly-전면-광고-추가하기)
        * [Cauly 네이티브 광고 추가하기](#cauly-네이티브-광고-추가하기)
    * [Adfit 광고 추가하기](#adfit-광고-추가하기)
        * [Adfit 네이티브 광고 추가하기](#adfit-네이티브-광고-추가하기)
- - - 

# 1. 미디에이션 시작하기

### 최소 지원 사양
- minSdkVersion 21 이상
- compileSdkVersion 34 이상


### AndroidManifest.xml 속성 지정

#### 필수 퍼미션 추가

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```


#### admob 연결

- APPLICATION_ID: 구글 AdMob 대시보드에 등록된 앱의 ID를 입력

    ```xml
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-xxxxxxxxxx" />
    ```



#### 네트워크 보안 설정 (targetSdkVersion 28 이상)

`광고 노출 및 클릭이 정상적으로 동작하기 위해서 cleartext 네트워크 설정 필요`

```xml
<application android:usesCleartextTraffic="true" />
```	

#### 더 안전한 구성요소 내보내기 설정 (targetSdkVersion 31 이상)
`intent-filter를 사용하는 활동을 포함하는 경우 android:exported 속성 설정 필요 (MAIN/LAUNCHER activity는 ture 설정 필수)`

```xml
<activity android:exported="true" />
``` 


#### Activity orientation
- Activity 형식의 전체 화면 랜딩을 지원하기 위해선 아래의 설정으로 추가한다.
- 만약, 설정하지 않으면 화면 전환 시 마다 광고view 가 `초기화` 됩니다.

```xml
<activity
    android:name=".MainActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize">
</activity>
```

```xml
<activity
    android:name="com.fsn.cauly.blackdragoncore.LandingActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"> 
</activity>
```


	
#### AndroidX 사용하는 경우
```xml
gradle.properties ::
 * android.useAndroidX=true
 * android.enableJetifier=true
```

- 참고 : https://developer.android.com/jetpack/androidx/migrate



### SDK 추가

#### app level build.gradle 에 'dependencies'  추가

``` clojure
dependencies {
    // google admob
    implementation 'com.google.android.gms:play-services-ads:23.3.0'
    
    // cauly sdk
    implementation 'com.fsn.cauly:cauly-sdk:3.5.34'
    
    // inmobi mediation
    implementation 'com.google.ads.mediation:inmobi:10.7.5.0'
    
    // applovin mediation
    implementation 'com.google.ads.mediation:applovin:12.6.0.0'
    
    // vungle mediation
    implementation 'com.google.ads.mediation:vungle:7.4.1.0'
    
    // DT Exchange mediation
    implementation 'com.google.ads.mediation:fyber:8.3.1.0'
    
    // Mintegral mediation
    implementation 'com.google.ads.mediation:mintegral:16.8.31.0'
    
    // Pangle mediation
    implementation 'com.google.ads.mediation:pangle:6.2.0.4.0'
    
    // Unity ads mediation
    implementation 'com.unity3d.ads:unity-ads:4.12.2'
    implementation 'com.google.ads.mediation:unity:4.12.2.0'

    // Meta(facebook) mediation
    implementation 'com.google.ads.mediation:facebook:6.17.0.0'

    // IronSource mediation
    implementation 'com.google.ads.mediation:ironsource:8.3.0.0'
    
}
```


#### 최상위 level build.gradle 에  maven repository 추가

- Cauly SDK, Mintegral SDK, Pangle SDK, IronSource SDK 사용을 위해 필수
> 기존에 **`Cauly SDK 3.5.34 미만 버전을 사용`** 했다면, 기존에 설정한 **`Cauly SDK Repository 정보를 제거 후, 아래 정보로 적용`** 해주셔야 합니다.  
> Cauly SDK Repository에 대한 정보는 [Cauly SDK 가이드](https://cauly.gitbook.io/cauly/android/sdk/project-build-setting#cauly-sdk)에서 확인할 수 있습니다.

   ```clojure
   allprojects {
      repositories {
         google()
         jcenter()
         // Cauly SDK Repository Public Access Info
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/cauly/Android-SDK/SDK")
            credentials {
                username = 'Cauly SDK 가이드 내 키값 참조'
                password = 'Cauly SDK 가이드 내 키값 참조'
            }
        }
         // Mintegral mediation
         maven {
            url 'https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea'
         }
         // Pangle mediation
         maven {
            url 'https://artifact.bytedance.com/repository/pangle/'
         }
         // IronSource mediation
        maven {
            url 'https://android-sdk.is.com/'
        }
      }
   }
   ```


### UMP (User Messaging Platform) 설정

#### GDPR (General Data Protection Regulation)

`GDPR은 유럽 연합(이하 'EU')의 개인정보 보호 법령으로, 서비스 제공자는 EU 사용자의 개인정보 수집 및 활용에 대해 사용자에게 동의 여부를 확인받아야 합니다.`

##### 1. SDK 추가

- app level build.gradle 에 'dependencies' 추가

```clojure
   dependencies {
      // User Messaging Platform SDK (GDPR)
      implementation 'com.google.android.ump:user-messaging-platform:3.0.0' 
   }
```

##### 2. 구현

> Main Activity의 onCreate 에서 광고 관련 코드를 요청하기 전에 애드몹 UMP (User Messaging Platform)를 통하여 GDPR 동의를 처리해야 합니다.

Java
``` java
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

public class MainActivity extends AppCompatActivity {
    private ConsentInformation consentInformation;
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private final AtomicBoolean isMobileInitializeCalled = new AtomicBoolean(false);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        // Create a ConsentRequestParameters object.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }
    
     private void initializeMobileAdsSdk() {
        if (isMobileInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this);
        
        // TODO: Request an ad.
    }
}
```

Kotlin
``` kotlin
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

public class MainActivity extends AppCompatActivity {
    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Create a ConsentRequestParameters object.
        val params = ConsentRequestParameters
            .Builder()
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            ConsentInformation.OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainActivity,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        if (loadAndShowError != null) {
                            Log.w(TAG, String.format("%s: %s",
                                loadAndShowError.errorCode,
                                loadAndShowError.message))
                        }

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            ConsentInformation.OnConsentInfoUpdateFailureListener {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w(TAG, String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message))
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
    
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        
        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)
    
        // TODO: Request an ad.
    }
}
```

##### 3. 테스트
> 해당 설정은 테스트 목적으로만 사용할 수 있습니다.  
> 앱을 출시하기 전에 테스트 설정 코드를 반드시 삭제해야 합니다.

- UMP SDK의 Debug 설정은 테스트 기기에서만 작동합니다.
- ConsentDebugSettings.Builder의 setDebugGeography() 를 사용하여 기기가 EEA 또는 영국에 있는 것처럼 앱 동작을 테스트할 수 있습니다.
- ConsentInformation.reset() 을 사용하여 UMP SDK의 상태를 재설정할 수 있습니다.

``` clojure
애드몹 UMP의 GDPR 동의 화면을 테스트 목적으로 확인하기 위해서는 아래 단계에 따라 테스트 기기 등록이 필요합니다.

1. requestConsentInfoUpdate() 를 호출합니다.
2. 로그 출력에서 다음과 같은 메시지를 확인합니다. 메시지에는 기기 ID와 이를 테스트 기기로 추가하는 방법이 나와있습니다.

Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("33BE2250B43518CCDA7DE426D04EE231") to set this as a debug device.

3. ConsentDebugSettings.Builder().addTestDeviceHashedId() 를 설정하고 테스트 기기 ID 목록을 전달합니다.
```

Java
``` java
ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .addTestDeviceHashedId("33BE2250B43518CCDA7DE426D04EE231")
        .build();

// Create a ConsentRequestParameters object.
ConsentRequestParameters params = new ConsentRequestParameters
        .Builder()
        .setConsentDebugSettings(debugSettings)
        .build();

consentInformation = UserMessagingPlatform.getConsentInformation(this);
// 동의 상태 재설정
consentInformation.reset();
consentInformation.requestConsentInfoUpdate(
        this,
        params,
        ...
);
```

Kotlin
``` kotlin
val debugSettings = ConsentDebugSettings.Builder(this)
    .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
    .addTestDeviceHashedId("F66ED148DC4D9CEFB84226320F5BCBAB")
    .build()

// Create a ConsentRequestParameters object.
val params = ConsentRequestParameters
    .Builder()
    .setConsentDebugSettings(debugSettings)
    .build()

consentInformation = UserMessagingPlatform.getConsentInformation(this)
// 동의 상태 재설정
consentInformation.reset()
consentInformation.requestConsentInfoUpdate(
    this,
    params,
    ...
)
```


# 2. Admob 광고 추가하기

### 주의사항
- 모든 광고 표시는 `UI Thread`에서 진행해야합니다.
    - 별도로 Thread를 생성하지 않은 경우, 기본적으로 UI Thread를 사용하므로 영향이 없습니다.
    - 이외의 Thread에서 표시하는 경우, 광고가 표시되지 않거나 앱이 강제종료될 수 있으므로 반드시 UI Thread 에서 표시하도록 코드를 반영해야합니다.

#### `UI Thread 반영 예시`

Java

``` java
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        // 광고 노출 코드
        ....
    }
});
```

Kotlin

``` kotlin
runOnUiThread {
    // 광고 노출 코드
    ....
}
```

### Admob 설정

#### Admob 광고 SDK 초기화


``` java
Java ::
new Thread(
        () -> {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this, initializationStatus -> {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.i(TAG, String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }
            });
        }).start();
```


``` kotlin
Kotlin ::
val backgroundScope = CoroutineScope(Dispatchers.IO)
backgroundScope.launch {
    // Initialize the Google Mobile Ads SDK on a background thread.
    MobileAds.initialize(
        this@SplashActivity
    ) { initializationStatus ->
        val statusMap = initializationStatus.adapterStatusMap
        for (adapterClass in statusMap.keys) {
            val status = statusMap[adapterClass]
            Log.i(
                LOG_TAG, String.format(
                    "Adapter name: %s, Description: %s, Latency: %d",
                    adapterClass, status!!.description, status.latency
                )
            )
        }
    }
}
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
Java ::
MobileAds.setRequestConfiguration(
    new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("33BE2250B43518CCDA7DE426D04EE231"))
            .build());
```

``` kotlin
Kotlin ::
val testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231")
val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
MobileAds.setRequestConfiguration(configuration)
```



### 타겟팅 설정
- 광고 요청에 타겟팅 정보를 제공하는 방법입니다.
- `구글플레이 스토어의 콘텐츠 설정과 SDK 설정이 반드시 동일하게 설정되어야 합니다.`


#### 아동 대상 설정 방법
- [아동 온라인 개인 정보 보호법(COPPA)](https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)에 따라 '아동 대상 서비스 취급용 태그' 설정이 가능합니다.
- 광고를 요청할 때 콘텐츠를 아동 대상 서비스 취급 여부를 지정할 수 있습니다.
- 콘텐츠를 아동 대상으로 처리하도록 지정하면 해당 광고 요청에 대한 관심 기반 광고 및 리마케팅 광고가 사용 중지됩니다.


Java
``` java
RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
    .build();
MobileAds.setRequestConfiguration(requestConfiguration);
```


Kotlin
``` kotlin
var requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
    .build()
MobileAds.setRequestConfiguration(requestConfiguration)
```


 | 아동 대상 설정 항목 | 설명                              |
 |------|--------------------------------------|
 | TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE  | COPPA에 따라 콘텐츠를 아동 대상으로 처리하도록 지정하는 경우       |
 | TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE  | COPPA에 따라 콘텐츠를 아동 대상으로 처리하지 않도록 지정하는 경우 |
 | TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED  | 광고 요청에서 COPPA에 따른 콘텐츠 취급 방법을 지정하지 않는 경우 |



#### 동의 연령 미만 사용자 설정 방법
- 유럽 경제 지역(EEA)에 거주하는 동의 연령 미만의 사용자를 대상으로 하는 서비스로 취급하도록 광고 요청에 표시할 수 있습니다.
- TFUA(동의 연령 미만의 유럽 사용자가 대상임을 나타내는 태그) 매개변수가 광고 요청에 포함되며, 모든 광고 요청에서 리마케팅을 포함한 개인 맞춤 광고가 사용 중지됩니다.
- `TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE` 로 설정하는 경우 광고 식별자인 AAID 수집도 차단됩니다.
- [아동 대상 설정](#아동-대상-설정-방법)과 동시에 true 로 설정하면 안 되며, 이 경우 아동 대상 설정이 우선 적용됩니다.



Java
``` java
RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
    .build();
MobileAds.setRequestConfiguration(requestConfiguration);
```


Kotlin
``` kotlin
var requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
    .build()
MobileAds.setRequestConfiguration(requestConfiguration)
```



 | 동의 연령 미만 사용자 설정 항목 | 설명                              |
 |------|--------------------------------------|
 | TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE  | 광고 요청이 EEA에 거주하는 동의 연령 미만의 사용자를 대상으로 처리하도록 지정하는 경우  |
 | TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE  | 광고 요청이 EEA에 거주하는 동의 연령 미만의 사용자를 대상으로 처리하지 않도록 지정하는 경우 |
 | TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED  | 광고 요청이 EEA에 거주하는 동의 연령 미만의 사용자 취급 방법을 지정하지 않는 경우 |





#### 광고 콘텐츠 필터링
- 광고 내 관련 혜택이 포함된 Google Play의 [부적절한 광고 정책](https://support.google.com/googleplay/android-developer/answer/9857753?hl=ko#zippy=,examples-of-common-violations)을 준수하려면 콘텐츠 자체가 Google Play 정책을 준수하더라도 앱에 표시되는 모든 광고 및 관련 혜택은 앱의 [콘텐츠 등급](https://support.google.com/googleplay/android-developer/answer/9898843?hl=ko)에 적합해야 합니다.
- 광고 콘텐츠 등급 한도가 설정된 경우 콘텐츠 등급이 설정된 한도 이하인 광고가 게재되며, 다음 중 하나로 설정해야합니다.
- 콘텐츠 등급 한도 설정에 대해 [각 광고 요청에 대한 콘텐츠 등급 한도 설정하기](https://support.google.com/admob/answer/10477886?hl=ko) 또는 [앱 또는 계정의 광고 콘텐츠 등급 한도 설정하기](https://support.google.com/admob/answer/7562142?hl=ko) 를 참고 부탁드립니다.

| 광고 콘텐츠 등급 한도 | 
 |------| 
 | MAX_AD_CONTENT_RATING_G  | 
 | MAX_AD_CONTENT_RATING_PG  | 
 | MAX_AD_CONTENT_RATING_T  | 
 | MAX_AD_CONTENT_RATING_MA  | 

Java
``` java
RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
    .build();
MobileAds.setRequestConfiguration(requestConfiguration);
```


Kotlin
``` kotlin
var requestConfiguration = MobileAds.getRequestConfiguration()
    .toBuilder()
    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
    .build()
MobileAds.setRequestConfiguration(requestConfiguration)
```





### Admob 앱 오프닝 광고 추가하기
- 앱 오프닝 광고를 구현하는 자세한 내용은 [AdMob 앱 오프닝 광고 연동 가이드](https://developers.google.com/admob/android/app-open)에서 확인해주십시오.
- 앱 오프닝 광고를 구현하는 데 필요한 단계는 크게 다음과 같습니다.
    1. `Application` 클래스를 확장하여 Google 모바일 광고 SDK를 초기화합니다. 
    2. 광고를 게재하기 전에 광고를 요청하는 유틸리티 클래스인 `AppOpenAdManager` 클래스를 생성합니다.
    3. 광고를 요청합니다.
    4. `ActivityLifecycleCallbacks` 인터페이스를 구현하고 등록합니다.
    5. 광고를 표시하고 콜백을 처리합니다.
    6. foreground 이벤트 중에 광고를 표시하도록 `LifecycleObserver` 인터페이스를 구현하고 등록합니다.
- 다음은 앱 오프닝 광고 구현 예시입니다.

- Application 클래스를 확장하는 새 클래스를 만든 후 `AndroidManifest.xml`에 다음 코드를 추가해야합니다.

``` xml
<application
    android:name="com.google.android.gms.example.appopendemo.MyApplication" ...>
...
</application>
```

<details> <summary>Java</summary>

- MyApplication.java

``` java
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    public Boolean isSplash = false;

    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        if (isSplash) {
            appOpenAdManager.showAdIfAvailable(currentActivity);
        }
    }

    /** ActivityLifecycleCallback methods. */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}

    /**
     * Load an app open ad.
     *
     * @param activity the activity that shows the app open ad
     */
    public void loadAd(@NonNull Activity activity, @NonNull OnLoadAdCompleteListener onLoadAdCompleteListener) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity, onLoadAdCompleteListener);
    }

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /** Interface definition for a callback to be invoked when an app open ad is complete. */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public interface OnLoadAdCompleteListener {
        void onAdLoaded();
        void onAdFailedToLoad();
    }

    /** Inner class that loads and shows app open ads. */
    private class AppOpenAdManager {
        private static final String LOG_TAG = "AppOpenAdManager";
        private static final String AD_UNIT_ID = Config.ADMOB_APP_OPEN_ID;

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private long loadTime = 0;

        /** Constructor. */
        public AppOpenAdManager() {}

        /** Request an ad. */
        private void loadAd(Context context, OnLoadAdCompleteListener onLoadAdCompleteListener) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context, AD_UNIT_ID, request,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            // Called when an app open ad has loaded.
                            Log.d(LOG_TAG, "Ad was loaded.");
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();
                            onLoadAdCompleteListener.onAdLoaded();
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            // Called when an app open ad has failed to load.
                            Log.d(LOG_TAG, loadAdError.getMessage());
                            isLoadingAd = false;
                            onLoadAdCompleteListener.onAdFailedToLoad();
                        }
                    });
        }

        /** Show the ad if one isn't already showing. */
        public void showAdIfAvailable(Activity activity) {
            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            });
        }

        /** Shows the ad if one isn't already showing. */
        public void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener){
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity, null);
                return;
            }

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Set the reference to null so isAdAvailable() returns false.
                            Log.d(LOG_TAG, "Ad dismissed fullscreen content.");
                            appOpenAd = null;
                            isShowingAd = false;

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity, null);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            // Set the reference to null so isAdAvailable() returns false.
                            Log.d(LOG_TAG, adError.getMessage());
                            appOpenAd = null;
                            isShowingAd = false;

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity, null);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            Log.d(LOG_TAG, "Ad showed fullscreen content.");
                        }
                    });
            isShowingAd = true;
            appOpenAd.show(activity);
        }

        /** Utility method to check if ad was loaded more than n hours ago. */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - this.loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /** Check if ad exists and can be shown. */
        private boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }
    }
}
```

- SplashActivity.java

``` java
public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeMobileAdsSdk();
    }

    /** Start the MainActivity. */
    public void startMainActivity() {
        Application application = getApplication();
        ((MyApplication) application).isSplash = true;

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    private void initializeMobileAdsSdk() {
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {
                        Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                        for (String adapterClass : statusMap.keySet()) {
                            AdapterStatus status = statusMap.get(adapterClass);
                            Log.i(TAG, String.format(
                                    "Adapter name: %s, Description: %s, Latency: %d",
                                    adapterClass, status.getDescription(), status.getLatency()));
                        }
                    });
                }).start();

        // TODO: Request an ad.
        Application application = getApplication();
        ((MyApplication) application).loadAd(this, new MyApplication.OnLoadAdCompleteListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "onAdLoaded");
                ((MyApplication) application)
                        .showAdIfAvailable(
                                SplashActivity.this,
                                new MyApplication.OnShowAdCompleteListener() {
                                    @Override
                                    public void onShowAdComplete() {
                                        startMainActivity();
                                    }
                                });
            }

            @Override
            public void onAdFailedToLoad() {
                Log.d(TAG, "onAdFailedToLoad");
                startMainActivity();
            }
        });
    }
}
```

</details>

<details> <summary>Kotlin</summary>

- MyApplication.kt

``` kotlin
private const val LOG_TAG = "MyApplication"

/** Application class that initializes, loads and show ads when activities change states. */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleEventObserver {

    private lateinit var appOpenAdManager: AppOpenManager

    private var currentActivity: Activity? = null
    var isSplash = false

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenManager()
    }

    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START && isSplash) {
            // Show the ad (if available) when the app moves to foreground.
            currentActivity?.let {
                appOpenAdManager.showAdIfAvailable(it)
            }
        }
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // Updating the currentActivity only when an ad is not showing.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Load an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onLoadAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun loadAd(activity: Activity, onLoadAdCompleteListener: OnLoadAdCompleteListener) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity, onLoadAdCompleteListener)
    }

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    interface OnLoadAdCompleteListener {
        fun onAdLoaded()
        fun onAdFailedToLoad()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context, onLoadAdCompleteListener: OnLoadAdCompleteListener?) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context, Config().ADMOB_APP_OPEN_ID, request,
                object : AppOpenAdLoadCallback() {

                    override fun onAdLoaded(ad: AppOpenAd) {
                        // Called when an app open ad has loaded.
                        Log.d(LOG_TAG, "Ad was loaded.")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        onLoadAdCompleteListener?.onAdLoaded()
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Called when an app open ad has failed to load.
                        Log.d(LOG_TAG, loadAdError.message)
                        isLoadingAd = false;
                        onLoadAdCompleteListener?.onAdFailedToLoad()
                    }
                }
            )
        }

        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(activity, object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            })
        }


        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity, null)
                return
            }

            appOpenAd?.setFullScreenContentCallback(
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Called when full screen content is dismissed.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity, null)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        // Set the reference to null so isAdAvailable() returns false.
                        Log.d(LOG_TAG, adError.message)
                        appOpenAd = null
                        isShowingAd = false

                        onShowAdCompleteListener.onShowAdComplete()
                        loadAd(activity, null)
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d(LOG_TAG, "Ad showed fullscreen content.")
                    }
                })
            isShowingAd = true
            appOpenAd?.show(activity)
        }
    }
}
```

- SplashActivity.kt

``` kotlin
private const val LOG_TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initializeMobileAdsSdk()
    }

    /** Start the MainActivity. */
    fun startMainActivity() {
        val application = application as MyApplication
        application.isSplash = true

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun initializeMobileAdsSdk() {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(
                this@SplashActivity
            ) { initializationStatus ->
                val statusMap = initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.i(
                        LOG_TAG, String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
            }
            runOnUiThread {
                // TODO: Request an ad.
                val application = application as MyApplication
                application.loadAd(this@SplashActivity, object : MyApplication.OnLoadAdCompleteListener {
                    override fun onAdLoaded() {
                        Log.d(LOG_TAG, "onAdLoaded")
                        application.showAdIfAvailable(this@SplashActivity, object : MyApplication.OnShowAdCompleteListener {
                            override fun onShowAdComplete() {
                                startMainActivity()
                            }
                        })
                    }

                    override fun onAdFailedToLoad() {
                        Log.d(LOG_TAG, "onAdFailedToLoad")
                        startMainActivity()
                    }
                })
            }
        }

    }
}
```

</details>

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

<details> <summary>Java</summary>

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

</details>


<details> <summary>Kotlin</summary>

``` kotlin
private var adRequest: AdRequest? = null
private var adView: AdView? = null

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    MobileAds.initialize(
            this
    ) { initializationStatus ->
        val statusMap = initializationStatus.adapterStatusMap
        for (adapterClass in statusMap.keys) {
            val status = statusMap[adapterClass]
            Log.i(
                TAG, String.format(
                "Adapter name: %s, Description: %s, Latency: %d",
                adapterClass, status!!.description, status.latency
                )
            )
        }
    }
    //admob 레이아웃에 AdView 추가
    adView = findViewById<View>(R.id.adView) as AdView
    //admob 배너 리스너
    setAdmobBannerListener()
    MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList<String>(Config().ADMOB_TEST_DEVICE_ID))
                .build()
   )
    
    adRequest = AdRequest.Builder().build()

    adView!!.loadAd(adRequest!!)
}

private fun setAdmobBannerListener() {
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "banner onAdLoaded")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad " + adError.code + "  " + adError.message)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "onAdClosed")
            }
        }
    }
```

</details>


### Admob 전면 광고 추가하기

- InterstitialAd.load() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.
- InterstitialAdLoadCallback: InterstitialAd 로드와 관련된 이벤트를 처리합니다.
- FullScreenContentCallback: InterstitialAd 표시와 관련된 이벤트를 처리합니다. 
    - InterstitialAd를 표시하기 전에 콜백을 설정해야 합니다.


<details> <summary>Java</summary>

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


</details>


<details> <summary>Kotlin</summary>

``` kotlin
private var mInterstitialAd: InterstitialAd? = null
lateinit var show_interstitial_btn: Button

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.i(
                    TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
        //admob 레이아웃에 AdView 추가
        adView = findViewById<View>(R.id.adView) as AdView
        

        //admob 배너 리스너
        setAdmobBannerListener()
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
            .setTestDeviceIds(Arrays.asList<String>(Config().ADMOB_TEST_DEVICE_ID))
            .build()
        )
    
        adRequest = AdRequest.Builder().build()


        //전면 요청
        val request_interstitial_btn = findViewById<Button>(R.id.request_interstitial_btn)
        request_interstitial_btn.setOnClickListener(View.OnClickListener { loadInterstitialAd() })

        // 전면 노출
        show_interstitial_btn = findViewById(R.id.show_interstitial_btn)
        show_interstitial_btn.setEnabled(false)
        show_interstitial_btn.setOnClickListener(View.OnClickListener { showInterstitialAd() })
    }

private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, Config().ADMOB_INTERSTITIAL_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    show_interstitial_btn!!.isEnabled = true
                    Log.i(TAG, "onAdLoaded")
                    Toast.makeText(this@MainActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    mInterstitialAd!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null
                            Log.d("TAG", "The ad was dismissed.")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Log.e(TAG, "The ad failed to show.")
                            mInterstitialAd = null
                            show_interstitial_btn!!.isEnabled = false
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "The ad was shown.")
                        }
                    })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                    val error = String.format(
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain, loadAdError.code, loadAdError.message
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "onAdFailedToLoad() with error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

private fun showInterstitialAd() {
        show_interstitial_btn!!.isEnabled = false
        mInterstitialAd!!.show(this)
}
```

</details>



### Admob 네이티브 광고 추가하기

- AdLoader.Builder() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.


<details> <summary>Java</summary>

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

</details>


<details> <summary>Kotlin</summary>

``` kotlin
private var nativeAd: NativeAd? = null
lateinit var native_btn: Button
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.i(
                    TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
        //admob 레이아웃에 AdView 추가
        adView = findViewById<View>(R.id.adView) as AdView
        
        //admob 배너 리스너
        setAdmobBannerListener()
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
            .setTestDeviceIds(Arrays.asList<String>(Config().ADMOB_TEST_DEVICE_ID))
            .build()
        )


        //네이티브 요청
        native_btn = findViewById<Button>(R.id.native_btn)
        native_btn.setOnClickListener(View.OnClickListener { loadAdmobNativeAd() })
    }

private fun loadAdmobNativeAd() {
        native_btn!!.isEnabled = false
        val adLoader = AdLoader.Builder(this, Config().ADMOB_NATIVE_ID)
            .forNativeAd(OnNativeAdLoadedListener { nativeAd ->
                var isDestroyed = false
                native_btn!!.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed()
                }
                if (isDestroyed || isFinishing || isChangingConfigurations) {
                    nativeAd.destroy()
                    return@OnNativeAdLoadedListener
                }
                if (this@MainActivity.nativeAd != null) {
                    this@MainActivity.nativeAd!!.destroy()
                }
                this@MainActivity.nativeAd = nativeAd
                val frameLayout = findViewById<FrameLayout>(R.id.fl_adplaceholder)
                val adView =
                    layoutInflater.inflate(R.layout.ad_unified, frameLayout, false) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            })
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    native_btn!!.isEnabled = true
                    val error = String.format(
                        Locale.getDefault(),
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain,
                        loadAdError.code,
                        loadAdError.message
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to load native ad with error $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
```

</details>


### Admob 보상형 광고 추가하기

- RewardedAd.load() 호출하고 광고가 게재되는 앱의 광고 단위에 지정할 고유 식별자를 설정합니다.
- RewardedAdLoadCallback: RewardedAd 로드와 관련된 이벤트를 처리합니다.
- FullScreenContentCallback: RewardedAd 표시와 관련된 이벤트를 처리합니다.
- OnUserEarnedRewardListener: 보상형 광고를 게재할 때 보상 이벤트를 처리합니다.


<details> <summary>Java</summary>

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

</details>


<details> <summary>Kotlin</summary>

``` kotlin
private var mRewardedAd: RewardedAd? = null
lateinit var show_rewarded_btn: Button
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.i(
                    TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
        //admob 레이아웃에 AdView 추가
        adView = findViewById<View>(R.id.adView) as AdView
        
        //admob 배너 리스너
        setAdmobBannerListener()
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
            .setTestDeviceIds(Arrays.asList<String>(Config().ADMOB_TEST_DEVICE_ID))
            .build()
        )


        // 보상형 광고 요청
        val request_rewarded_btn = findViewById<Button>(R.id.request_rewarded_btn)
        request_rewarded_btn.setOnClickListener(View.OnClickListener { loadRewardedAd() })

        show_rewarded_btn = findViewById<Button>(R.id.show_rewarded_btn)
        show_rewarded_btn.setEnabled(false)
        show_rewarded_btn.setOnClickListener(View.OnClickListener { showRewardedAd() })

    }

private fun loadRewardedAd() {
        if (mRewardedAd == null) {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(this, Config().ADMOB_REWARDED_ID, adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error.
                        show_rewarded_btn!!.isEnabled = false
                        Log.d(TAG, loadAdError.message)
                        mRewardedAd = null
                        Toast.makeText(this@MainActivity, "onAdFailedToLoad", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        show_rewarded_btn!!.isEnabled = true
                        mRewardedAd = rewardedAd
                        Log.d(TAG, "onAdLoaded")
                        Toast.makeText(this@MainActivity, "onAdLoaded", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    private fun showRewardedAd() {
        if (mRewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
            return
        }
        show_rewarded_btn!!.isEnabled = false
        mRewardedAd!!.setFullScreenContentCallback(
            object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "onAdShowedFullScreenContent")
                    Toast.makeText(
                        this@MainActivity,
                        "onAdShowedFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.d(TAG, "onAdFailedToShowFullScreenContent")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    Toast.makeText(
                        this@MainActivity,
                        "onAdFailedToShowFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    Log.d(TAG, "onAdDismissedFullScreenContent")
                    Toast.makeText(
                        this@MainActivity,
                        "onAdDismissedFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    // Preload the next rewarded ad.
                }
            })
        val activityContext: Activity = this@MainActivity
        mRewardedAd!!.show(
            activityContext,
            OnUserEarnedRewardListener { rewardItem -> // Handle the reward.
                Log.d("TAG", "The user earned the reward.")
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
            }
        )
    }
```

</details>


# 3. 파트너 통합 네트워크 추가하기

### Inmobi 설정 (옵션)
- Inmobi SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/inmobi#optional_steps)를 참고하여 옵션 설정이 가능합니다.

``` java
Java ::
Bundle inMobiextras= new Bundle();
inMobiextras.putString(InMobiNetworkKeys.AGE_GROUP, InMobiNetworkValues.BETWEEN_25_AND_29);
inMobiextras.putString(InMobiNetworkKeys.AREA_CODE, "12345");
InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
```

``` kotlin
Kotlin ::
val inMobiextras = Bundle()
inMobiextras.putString(InMobiNetworkKeys.AGE_GROUP, InMobiNetworkValues.BETWEEN_25_AND_29)
inMobiextras.putString(InMobiNetworkKeys.AREA_CODE, "12345")
InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)
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


### AppLovin 설정 (옵션)
- AppLovin SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/applovin#optional_steps)를 참고하여 옵션 설정이 가능합니다.

``` java
Java ::
Bundle appLovinExtras = new AppLovinExtras.Builder()
        .setMuteAudio(true)
        .build();
```

``` kotlin
Kotlin ::
val appLovinExtras = AppLovinExtras.Builder()
    .setMuteAudio(true)
    .build()
```


### Vungle 설정 (옵션)
- Vungle SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/liftoff-monetize#optional_steps)를 참고하여 옵션 설정이 가능합니다.

``` java
Java :: 
Bundle vungleExtras = new Bundle();
extras.putString(VungleConstants.KEY_USER_ID, "myUserID");
extras.putInt(VungleConstants.KEY_ORIENTATION, 1);
```

``` kotlin
Kotlin ::
val extras = Bundle()
extras.putString(VungleConstants.KEY_USER_ID, "myUserID")
extras.putInt(VungleConstants.KEY_ORIENTATION, 1)
```

옵션|설명
---|---
KEY_USER_ID | 보상형 광고와 상호작용하는 사용자를 식별하기 위한 사용자 ID
KEY_ORIENTATION | 전체 화면 광고의 표시 방향을 지정하는 정수<br/>광고 방향을 따르도록 되어 있는 경우: 0(Portrait) 또는 1(Landscape)<br/>광고가 자동 회전하도록 되어있는 경우: 2



### DT Exchange 설정 (옵션)
- DT Exchange SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/dt-exchange#optional_steps)를 참고하여 옵션 설정이 가능합니다.

``` java
Java :: 
Bundle fyberExtras = new Bundle();
fyberExtras.putInt(InneractiveMediationDefs.KEY_AGE, 10);
```

``` kotlin
Kotlin :: 
val fyberExtras = Bundle()
fyberExtras.putInt(InneractiveMediationDefs.KEY_AGE, 10)
```


### Mintegral 설정 (옵션)
- Mintegral SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/mintegral#optional_steps)를 참고하여 옵션 설정이 가능합니다.




### Pangle 설정
- ProGuard를 사용하여 Android 코드를 난독화하는 경우 [Pangle 설명서의 지침에 따라](https://www.pangleglobal.com/integration/integrate-pangle-sdk-for-android#as5ja83qobk0) Pangle SDK 코드가 난독화되지 않도록 하십시오.
- ProGuard 설정 이외에 Pangle SDK 설정을 위해 추가 코드가 필요하지 않습니다.

``` clojure
proguard-rules.pro ::
-keep class com.bytedance.sdk.** { *; }
```



### Unity Ads 설정
- Unity Ads SDK 설정을 위해 추가 코드가 필요하지 않습니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/unity#optional_steps)를 참고하여 옵션 설정이 가능합니다.



### Facebook(Meta) 설정
- Facebook(Meta) 네트워크를 사용하기 위해 Facebook 로그인 및 `read_audience_network_insights` 권한이 필요합니다.
    - Facebook 로그인은 [Android용 Facebook 로그인](https://developers.facebook.com/docs/facebook-login/android)을 참고하여 설정이 가능합니다.
- 필요한 경우 [여기](https://developers.google.com/admob/android/mediation/meta?hl=ko#optional_steps)를 참고하여 옵션 설정이 가능합니다.



### IronSource 설정
- `onPause()` 및 `onResume()` 메서드를 재정의하여 해당하는 ironSource 메서드를 호출합니다.

Java
``` java
@Override
public void onResume() {
    super.onResume();
    IronSource.onResume(this);
}

@Override
public void onPause() {
    super.onPause();
    IronSource.onPause(this);
}
```

Kotlin
``` kotlin
public override fun onResume() {
    super.onResume()
    IronSource.onResume(this)
}

public override fun onPause() {
    super.onPause()
    IronSource.onPause(this)
}
```


- ProGuard를 사용하여 Android 코드를 난독화하는 경우 [IronSource Android SDK 통합 가이드](https://developers.is.com/ironsource-mobile/android/android-sdk/#step-5)에 따라 IronSource SDK 코드가 난독화되지 않도록 하십시오.


``` clojure
proguard-rules.pro ::

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
#noinspection ShrinkerUnresolvedReference
#unity
-keep class com.google.android.gms.ads.** {public *;}
-keep class com.google.android.gms.appset.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
#adapters
-keep class com.ironsource.adapters.** { *; }
#sdk
-dontwarn com.ironsource.**
-dontwarn com.ironsource.adapters.**
```


### 광고 요청 연결
- 옵션 설정을 추가한 경우에만 AdRequest에 연결하시면 됩니다.

``` java
Java ::
adRequest = new AdRequest.Builder()
    .addNetworkExtrasBundle(InMobiAdapter.class, inMobiextras)
    .addNetworkExtrasBundle(ApplovinAdapter.class, appLovinExtras)
    .addNetworkExtrasBundle(VungleAdapter.class, vungleExtras)      // Rewarded
    .addNetworkExtrasBundle(VungleInterstitialAdapter.class, vungleExtras)      // Interstitial
    .addNetworkExtrasBundle(FyberMediationAdapter.class, fyberExtras)
    .build();
```

``` kotlin
Kotlin ::
adRequest = AdRequest.Builder()
    .addNetworkExtrasBundle(InMobiAdapter::class.java, inMobiextras)
    .addNetworkExtrasBundle(ApplovinAdapter::class.java, appLovinExtras)
    .addNetworkExtrasBundle(VungleAdapter::class.java, vungleExtras) // Rewarded
    .addNetworkExtrasBundle(VungleInterstitialAdapter::class.java, vungleExtras) // Interstitial
    .addNetworkExtrasBundle(FyberMediationAdapter::class.java, fyberExtras)
    .build()
```


# 4. 커스텀 이벤트 네트워크 추가하기

### Cauly 광고 추가하기

- proguard 설정 하는 경우 cauly SDK 포함된 Class는 난독화 시키시면 안됩니다.

```clojure
proguard-rules.pro ::
-keep public class com.fsn.cauly.** {
      public protected *;
}
-keep public class com.trid.tridad.** {
  	  public protected *;
}
-dontwarn android.webkit.**

- Android Studio 3.4 이상 부터 (gradle build tool 3.4.0)는 아래 와 같이 설정 해야합니다.

-keep class com.fsn.cauly.** {
	   public *; protected *;
}
-keep class com.trid.tridad.** {
  	  public *; protected *;
}
-dontwarn android.webkit.**
```

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


<details> <summary>Java</summary>

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

</details>


<details> <summary>Kotlin</summary>

- CaulyBanner

``` kotlin
class CaulyBanner : Adapter() {
    private var bannerLoader: CaulyBannerLoader? = null
    
    override fun loadBannerAd(
        mediationBannerAdConfiguration: MediationBannerAdConfiguration,
        callback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>
    ) {
        bannerLoader = CaulyBannerLoader(mediationBannerAdConfiguration, callback)
        bannerLoader!!.loadAd()
    }

    // This method won't be called for custom events.
    override fun initialize(
        context: Context,
        initializationCompleteCallback: InitializationCompleteCallback,
        list: List<MediationConfiguration>
    ) {
        initializationCompleteCallback.onInitializationSucceeded()
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString = "1.0.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString = "1.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    companion object {
        protected val TAG = CaulyBanner::class.java.simpleName
    }
}
```

- CaulyBannerLoader

``` kotlin

class CaulyBannerLoader(
    private val mediationBannerAdConfiguration: MediationBannerAdConfiguration,
    private val mediationAdLoadCallback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>
) :
    CaulyAdBannerViewListener, MediationBannerAd {
    private var bannerAdCallback: MediationBannerAdCallback? = null
    private var adView: CaulyAdBannerView? = null
    private var bannerActivity: Activity? = null

    /** Loads a banner ad from the third party ad network.  */
    fun loadAd() {
        Log.i(TAG, "Admob Mediation : Cauly Banner loadBannerAd.")
        //아래가 미디에이션 서버로부터 파라미터 설정한 값 받아오는 부분
        val serverParameter =
            mediationBannerAdConfiguration.serverParameters.getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)

        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(
                AdError(
                    ERROR_NO_AD_UNIT_ID,
                    "Ad unit id is empty",
                    CUSTOM_EVENT_ERROR_DOMAIN
                )
            )
            return
        }
        Log.d(TAG, "Received server parameter.$serverParameter")
        val context = mediationBannerAdConfiguration.context
        bannerActivity = context as Activity

        if (adView != null) {
            adView!!.destroy()
            adView!!.removeAllViews()
            adView = null
        }

        //AdView의 parent layout
        layout = bannerActivity!!.findViewById(R.id.adView)

        // 광고 정보 설정
        val adInfo = CaulyAdInfoBuilder(serverParameter)
            .bannerHeight(CaulyAdInfoBuilder.FIXED)
            .banner_interval(false)
            .dynamicReloadInterval(false)
            .effect("None")
            .build()

        // Cauly 배너 광고 View 생성.
        adView = CaulyAdBannerView(context)

        // 광고 정보 설정
        adView!!.setAdInfo(adInfo)
        adView!!.setAdViewListener(this)

        // 광고로드
        adView!!.load(context, layout)
    }

    override fun onReceiveAd(caulyAdBannerView: CaulyAdBannerView, isChargeableAd: Boolean) {
        // 광고 요청과 동시에 add된 광고 view를 제거함
        layout!!.removeView(adView)
        if (adView != null) {
            Log.e(TAG, "onReceiveAd onAdLoaded-=-=-=-=-: ")
            adView!!.show()
            bannerAdCallback = mediationAdLoadCallback.onSuccess(this)
            bannerAdCallback!!.onAdOpened()
            bannerAdCallback!!.reportAdImpression()
        }
    }

    override fun onFailedToReceiveAd(caulyAdBannerView: CaulyAdBannerView, i: Int, s: String) {
        Log.e(TAG, "Failed to fetch the banner ad.")
        mediationAdLoadCallback.onFailure(AdError(i, s, SAMPLE_SDK_DOMAIN))
        layout!!.removeView(adView)
    }

    override fun onShowLandingScreen(caulyAdBannerView: CaulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onShowLandingScreen")
        bannerAdCallback!!.onAdLeftApplication()
        bannerAdCallback!!.reportAdClicked()
    }

    override fun onCloseLandingScreen(caulyAdBannerView: CaulyAdBannerView) {
        Log.e("cauly", "Admob Mediation : Cauly Banner onCloseLandingScreen")
    }

    override fun onTimeout(caulyAdBannerView: CaulyAdBannerView, s: String) {}


    override fun getView(): View {
        return adView!!
    }

    companion object {
        val TAG = CaulyBannerLoader::class.java.simpleName
        private var layout: ViewGroup? = null

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val ERROR_NO_ACTIVITY_CONTEXT = 103
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
        fun init(viewGroup: ViewGroup?) {
            layout = viewGroup
        }
    }
}
```

</details>



#### Cauly 전면 광고 추가하기

<details> <summary>Java</summary>

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
        // 전면 광고를 클릭하여 앱을 벗어났을 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD onLeaveInterstitialAd.");
        interstitialAdCallback.onAdClosed();
    }
}
```

</details>


<details> <summary>Kotlin</summary>

- CaulyInterstitial

``` kotlin
class CaulyInterstitial : Adapter() {
    private var interstitialLoader: CaulyInterstitialLoader? = null
    override fun loadInterstitialAd(
        mediationInterstitialAdConfiguration: MediationInterstitialAdConfiguration,
        callback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>
    ) {
        interstitialLoader = CaulyInterstitialLoader(mediationInterstitialAdConfiguration, callback)
        interstitialLoader!!.loadAd()
    }

    override fun initialize(
        context: Context,
        initializationCompleteCallback: InitializationCompleteCallback,
        list: List<MediationConfiguration>
    ) {
        initializationCompleteCallback.onInitializationSucceeded()
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString = "1.0.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString = "1.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    companion object {
        protected val TAG = CaulyInterstitial::class.java.simpleName
    }
}
```

- CaulyInterstitialLoader

```kotlin
class CaulyInterstitialLoader(
    /** Configuration for requesting the interstitial ad from the third-party network.  */
    private val mediationInterstitialAdConfiguration: MediationInterstitialAdConfiguration,
    /** Callback that fires on loading success or failure.  */
    private var mediationAdLoadCallback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>
) :
    CaulyInterstitialAdListener, MediationInterstitialAd {
    /** Callback for interstitial ad events.  */
    private var interstitialAdCallback: MediationInterstitialAdCallback? = null
    var caulyInterstitialAd: CaulyInterstitialAd? = null
    private var interstitialActivity: Activity? = null

    /** Constructor.  */
    init {
        mediationAdLoadCallback = mediationAdLoadCallback
    }

    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : Cauly Interstitial loadInterstitialAd.")
        val serverParameter =
            mediationInterstitialAdConfiguration.serverParameters.getString(MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(
                AdError(
                    ERROR_NO_AD_UNIT_ID,
                    "Ad unit id is empty",
                    CUSTOM_EVENT_ERROR_DOMAIN
                )
            )
            return
        }
        Log.d(TAG, "Received server parameter.")
        val context = mediationInterstitialAdConfiguration.context
        interstitialActivity = context as Activity
        val adInfo = CaulyAdInfoBuilder(serverParameter)
            .build()

        // 전면 광고 생성
        val interstial = CaulyInterstitialAd()
        interstial.setAdInfo(adInfo)
        interstial.setInterstialAdListener(this)

        // 광고 요청.
        interstial.requestInterstitialAd(interstitialActivity)
    }

    override fun showAd(context: Context) {
        caulyInterstitialAd!!.show()
        interstitialAdCallback!!.reportAdImpression()
        interstitialAdCallback!!.onAdOpened()
    }

    /** CaulyInterstitialAdListener  */
    override fun onReceiveInterstitialAd(
        caulyInterstitialAd: CaulyInterstitialAd,
        isChargeableAd: Boolean
    ) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.")
        } else {
            Log.d("CaulyExample", "normal interstitial AD received.")
        }
        this.caulyInterstitialAd = caulyInterstitialAd
        interstitialAdCallback = mediationAdLoadCallback.onSuccess(this)
        interstitialAdCallback!!.reportAdImpression()
    }

    override fun onFailedToReceiveInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd, i: Int, s: String) {
        Log.e(TAG, "Failed to fetch the interstitial ad.")
        mediationAdLoadCallback.onFailure(AdError(i, s, SAMPLE_SDK_DOMAIN))
    }

    override fun onClosedInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.")
        interstitialAdCallback!!.onAdClosed()
    }

    override fun onLeaveInterstitialAd(caulyInterstitialAd: CaulyInterstitialAd) {
        // 전면 광고를 클릭하여 앱을 벗어났을 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD onLeaveInterstitialAd.")
        interstitialAdCallback!!.onAdClosed()
    }

    companion object {
        val TAG = CaulyInterstitialLoader::class.java.simpleName

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
    }
}
```
</details>




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

<details> <summary>Java</summary>

- CaulyNative.java

``` java
public class CaulyNative extends Adapter {

    private CaulyNativeLoader nativeLoader;

    @Override
    public void loadNativeAdMapper(@NonNull MediationNativeAdConfiguration mediationNativeAdConfiguration, @NonNull MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback> mediationAdLoadCallback) throws RemoteException {
        nativeLoader = new CaulyNativeLoader(mediationNativeAdConfiguration, mediationAdLoadCallback);
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

- CaulyNativeLoader.java

``` java
public class CaulyNativeLoader {
//    private CustomEventNativeListener nativeListener;
    final static String TAG = CaulyNativeLoader.class.getSimpleName();

    /** Configuration for requesting the native ad from the third-party network. */
    private final MediationNativeAdConfiguration mediationNativeAdConfiguration;

    /** Callback that fires on loading success or failure. */
    private final MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback> mediationAdLoadCallback;

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
    public CaulyNativeLoader(MediationNativeAdConfiguration mediationNativeAdConfiguration, MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback> mediationAdLoadCallback) {
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
            // 광고가 표시되었을 때 호출된다.
            @Override
            public void onShowedAd() {
                Log.d(TAG, "onShowedAd");
                nativeAdCallback.reportAdImpression();
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
                    final SampleNativeAdMapper nativeAdMapper =
                            new SampleNativeAdMapper(
                                    context,
                                    map.get("icon"),
                                    map.get("image"),
                                    data,
                                    mCaulyAdView
                            );
                    nativeAdCallback = mediationAdLoadCallback.onSuccess(nativeAdMapper);
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
                nativeAdCallback.reportAdClicked();
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

- SampleNativeAdMapper.java

``` java
public class SampleNativeAdMapper extends NativeAdMapper {
    private final kr.co.cauly.sdk.android.mediation.sample.natives.AdItem data;
    private final CaulyCustomAd caulyAdView;
    private final Context context;

    public SampleNativeAdMapper(final Context context, Drawable icon, Drawable image, final kr.co.cauly.sdk.android.mediation.sample.natives.AdItem data, CaulyCustomAd mCaulyAdView) {

        this.data = data;
        this.caulyAdView = mCaulyAdView;
        this.context = context;

        if (context == null) {
            Log.e("CaulyNative", "Failed to load ad. Request must be for unified native ads.");
            return;
        }
        List<NativeAd.Image> imagesList = new ArrayList<>();
        imagesList.add(new SampleNativeMappedImage(image, Uri.parse(data.image), 1.0));
        setImages(imagesList);

        setHeadline(data.subtitle);

        setIcon(new SampleNativeMappedImage(icon, Uri.parse(data.icon), 1.0));
        setBody(data.description);
        setOverrideImpressionRecording(false);
        setOverrideClickHandling(false);
    }

    @Override
    public void recordImpression() {
        super.recordImpression();
        caulyAdView.sendImpressInform(data.id);
    }

    @Override
    public void handleClick(View view) {
        super.handleClick(view);
        CaulyBrowserUtil.openBrowser(context, data.linkUrl);
    }

}
```

- SampleNativeMappedImage.java

``` java
/**
 * A simple class that fits the the {@link NativeAd.Image} interface and can be filled with assets
 * returned by the Sample SDK.
 */
public class SampleNativeMappedImage extends NativeAd.Image {
    private final Drawable drawable;
    private final Uri imageUri;
    private final double scale;

  public SampleNativeMappedImage(Drawable drawable, Uri imageUri, double scale) {
    this.drawable = drawable;
    this.imageUri = imageUri;
    this.scale = scale;
  }

    @Override
    public Drawable getDrawable() {
        return drawable;
    }
    @Override
    public Uri getUri() {
        return imageUri;
    }

    @Override
    public double getScale() {
        return scale;
    }
}
```

</details>

<details> <summary>Kotlin</summary>

- CaulyNative.kt

``` kotlin
class CaulyNative: Adapter() {

    private var nativeLoader: CaulyNativeLoader? = null

    override fun loadNativeAdMapper(
        mediationNativeAdConfiguration: MediationNativeAdConfiguration,
        callback: MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback>
    ) {
        nativeLoader = CaulyNativeLoader(mediationNativeAdConfiguration, callback)
        nativeLoader!!.loadAd()
    }

    override fun initialize(
        context: Context,
        initializationCompleteCallback: InitializationCompleteCallback,
        list: List<MediationConfiguration>
    ) {
        initializationCompleteCallback.onInitializationSucceeded()
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString = "1.0.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString = "1.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }
}
```

- CaulyNativeLoader.kt

``` kotlin
class CaulyNativeLoader(
    /** Configuration for requesting the native ad from the third-party network. */
    private val mediationNativeAdConfiguration: MediationNativeAdConfiguration,
    /** Callback that fires on loading success or failure. */
    private var mediationAdLoadCallback: MediationAdLoadCallback<NativeAdMapper, MediationNativeAdCallback>
) {
    /** Callback for native ad events. */
    private var nativeAdCallback: MediationNativeAdCallback? = null
    private var mCaulyAdView: CaulyCustomAd? = null
    private var data: AdItem? = null;


    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : Cauly Native loadAd")
        val serverParameter = mediationNativeAdConfiguration.serverParameters.getString(
            MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(
                AdError(
                    ERROR_NO_AD_UNIT_ID,
                    "Ad unit id is empty",
                    CUSTOM_EVENT_ERROR_DOMAIN
                )
            )
            return
        }

        Log.d(TAG, "Received server parameter.")
        val context = mediationNativeAdConfiguration.context

        val adInfo = CaulyNativeAdInfoBuilder(serverParameter)
            .iconImageID(R.id.ad_app_icon)  // 아이콘 이미지를 사용할 경우
            .mainImageID(R.id.ad_media)     // 메인 이미지를 사용할 경우
            .build()

        mCaulyAdView = CaulyCustomAd(context)
        mCaulyAdView?.setAdInfo(adInfo)
        mCaulyAdView?.setCustomAdListener(object : CaulyCustomAdListener {
            // 광고 호출이 성공할 경우 호출된다.
            override fun onLoadedAd(isChargeableAd: Boolean) {
                val adList: List<HashMap<String, Any>>? = mCaulyAdView?.adsList

                if (adList != null && adList.isNotEmpty()) {
                    for (map in adList) {
                        data = kr.co.cauly.sdk.android.mediation.sample.natives.AdItem().apply {
                            id = map["id"] as String
                            image = map["image"] as String
                            linkUrl = map["linkUrl"] as String
                            subtitle = map["subtitle"] as String
                            description = map["description"] as String
                            icon = map["icon"] as String
                            image_content_type = map["image_content_type"] as String
                        }
                    }
                }

                val map = HashMap<String, Drawable>()
                val uThread = Thread {
                    try {
                        if (data?.icon != null) {
                            map["icon"] = data!!.icon?.let { getImageFromURL(it) }!!
                        }
                        if (data?.image != null) {
                            map["image"] = data!!.image?.let { getImageFromURL(it) }!!
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onFailedAd(-100, "internal error");
                    }
                }
                uThread.start()

                try {
                    uThread.join()
                    val nativeAdMapper = map["icon"]?.let { icon ->
                        map["image"]?.let { image ->
                            data?.let { data ->
                                SampleNativeAdMapper(
                                    context,
                                    icon,
                                    image,
                                    data,
                                    mCaulyAdView!!
                                )
                            }
                        }
                    }

                    nativeAdCallback = mediationAdLoadCallback.onSuccess(nativeAdMapper!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onFailedAd(-100, "internal error")
                }
            }

            // 광고 호출이 실패할 경우 호출된다.
            override fun onFailedAd(errCode: Int, errMsg: String?) {
                Log.e(TAG, "errCode: $errCode errMsg: $errMsg")
                val errorCode = when (errCode) {
                    200 -> AdRequest.ERROR_CODE_NO_FILL
                    400 -> AdRequest.ERROR_CODE_INVALID_REQUEST
                    500 -> AdRequest.ERROR_CODE_NETWORK_ERROR
                    else -> AdRequest.ERROR_CODE_INTERNAL_ERROR
                }
                mediationAdLoadCallback.onFailure(AdError(errorCode, errMsg!!, SAMPLE_SDK_DOMAIN))
            }

            // 광고가 클릭된 경우 호출된다.
            override fun onClikedAd() {
                Log.d(TAG, "onClicked")
                nativeAdCallback!!.reportAdClicked()
            }

            // 광고가 표시되었을 때 호출된다.
            override fun onShowedAd() {
                Log.d(TAG, "onShowedAd")
                nativeAdCallback!!.reportAdImpression()
            }

        })

        mCaulyAdView?.requestAdData(CaulyCustomAd.NATIVE_LANDSCAPE, 2);
    }

    companion object {
        val TAG = CaulyNativeLoader::class.java.simpleName

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
    }

    @Throws(Exception::class)
    fun getImageFromURL(data: String): Drawable {
        val url = URL(data)
        val conn = url.openConnection() as HttpURLConnection
        conn.doInput = true
        conn.connect()

        val inputStream: InputStream = conn.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return BitmapDrawable(Resources.getSystem(), bitmap)
    }
}
```

- SampleNativeAdMapper.kt

``` kotlin
class SampleNativeAdMapper(
    private val context: Context?,
    icon: Drawable,
    image: Drawable,
    private val data: kr.co.cauly.sdk.android.mediation.sample.natives.AdItem,
    private val caulyAdView: CaulyCustomAd
) : NativeAdMapper() {

    init {
        if (context != null) {
            // 이미지 리스트 생성 및 설정
            val imagesList: MutableList<NativeAd.Image> = ArrayList()
            imagesList.add(SampleNativeMappedImage(image, Uri.parse(data.image), 1.0))
            setImages(imagesList)

            // 제목, 아이콘, 설명 및 클릭 처리 설정
            data.subtitle?.let { setHeadline(it) }
            setIcon(SampleNativeMappedImage(icon, Uri.parse(data.icon), 1.0))
            data.description?.let { setBody(it) }
            setOverrideImpressionRecording(false)
            setOverrideClickHandling(false)
        } else {
            Log.e("CaulyNative", "Failed to load ad. Request must be for unified native ads.")
        }
    }

    override fun recordImpression() {
        super.recordImpression()
        caulyAdView.sendImpressInform(data.id)
    }

    override fun handleClick(view: View) {
        super.handleClick(view)
        CaulyBrowserUtil.openBrowser(context, data.linkUrl)
    }
}
```

- SampleNativeAdMappedImage.kt

``` kotlin
class SampleNativeMappedImage(
    private val drawable: Drawable,
    private val uri: Uri,
    private val scale: Double
) : NativeAd.Image() {

    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun getUri(): Uri {
        return uri
    }

    override fun getScale(): Double {
        return scale
    }
}
```
</details>

### Adfit 광고 추가하기
#### Adfit 네이티브 광고 추가하기

- 광고 요청 정보 설 정

네이티브 광고를 요청하기 위해서는 `AdFitNativeAdLoader`와 `AdFitNativeAdRequest`를 통해 가능합니다.

* `AdFitNativeAdRequest.Builder`의 `setAdInfoIconPosition()`으로 광고 정보 아이콘 위치를 설정할 수 있습니다.<br/>
  기본값은 `RIGHT_TOP`(오른쪽 상단)입니다.<br/>

  | AdFitAdInfoIconPosition | 위치              |
  |-------------------------|------------------|
  | LEFT_TOP                | 왼쪽 상단          |
  | RIGHT_TOP               | 오른쪽 상단 (기본값) |
  | LEFT_BOTTOM             | 왼쪽 하단          |
  | RIGHT_BOTTOM            | 오른쪽 상단         |

* `AdFitNativeAdRequest.Builder`의 `setVideoAutoPlayPolicy()`로 비디오 광고 자동 재생 정책을 설정할 수 있습니다.<br/>
  기본값은 `WIFI_ONLY`(WiFi 연결 상태에만 자동 재생)입니다.<br/>
  
  | AdFitVideoAutoPlayPolicy | 설정                     |
  |--------------------------|-------------------------|
  | NONE                     | 자동 재생하지 않음          |
  | WIFI_ONLY                | WiFi 연결 상태에만 자동 재생 |
  | ALWAYS                   | 항상 자동 재생             |

* 요청에 성공하여 새로운 광고를 응답 받은 경우, 응답 받은 광고 소재 정보를 `AdFitNativeAdBinder`를 통해 <br/>
  `AdLoadListener.onAdLoaded()`로 전달받을 수 있습니다. <br/>
* 요청에 실패하거나 응답 받은 소재가 없는 경우, 오류 코드(`errorCode: Int`)를 <br/>
  `AdLoadListener.onAdLoadError()`로 전달받을 수 있습니다. <br/>
  | Code | 발생 상황                              |
  |------|--------------------------------------|
  | 202  | 네트워크 오류로 광고 요청에 실패한 경우       |
  | 301  | 요청에는 성공했으나 네이티브 유형에 맞는 광고가 없는 경우 |
  | 302  | 요청에는 성공했으나 노출 가능한 광고가 없는 경우 |
  | 그 외 | 기타 오류로 광고 요청에 실패한 경우           |
* `AdFitNativeAdLoader.load()`는 동시에 하나의 요청만 처리할 수 있으며, 이전 요청이 진행 중이면 새로운 호출은 무시됩니다.

- 네이티브 광고 레이아웃 구성

네이티브 광고 레이아웃은 서비스 컨텐츠와 어울리도록 구성되어야 하므로, 서비스에서 직접 광고 레이아웃을 구현하는 과정이 필요합니다.<br/>
네이티브 광고 레이아웃 구성 샘플은 아래와 같습니다.<br/>

<img src="https://t1.daumcdn.net/adfit_sdk/document-assets/ios/native-ad-components3.png" width="640" style="border:1px solid #aaa"/>

| 번호 | 설명                       | UI 클래스                | AdFitNativeAdLayout |
|-----|---------------------------|------------------------|---------------------|
| -   | 광고 영역                   | AdFitNativeAdView      | containerView       |
| 1   | 광고 제목                   | TextView               | titleView           |
| 2   | 행동유도버튼                 | Button                 | callToActionButton  |
| 3   | 광고주 이름 (브랜드명)         | TextView               | profileNameView     |
| 4   | 광고주 아이콘 (브랜드 로고)     | ImageView              | profileIconView     |
| 5   | 미디어 소재 (이미지, 비디오 등)  | AdFitMediaView         | mediaView           |
| 6   | 광고 정보 아이콘              | -                      | -                   |
| 7   | 광고 홍보문구                | TextView               | bodyView            |

- 네이티브 광고는 위의 요소들로 구성됩니다.
- 각 요소들은 위 표를 참고하여 대응하는 UI 클래스를 통해 표시되도록 구현해야 합니다.
- "제목 텍스트"와 "미디어 소재" 요소는 필수입니다.
- "광고 정보 아이콘 이미지"는 "광고 영역" 내에 `AdFitNativeAdRequest`에 설정한 위치에 표시됩니다.
- 사용자가 광고임을 명확히 인지할 수 있도록 "광고", "AD", "Sponsored" 등의 텍스트를 별도로 표시해주셔야 합니다.
- "광고 영역"은 `AdFitNativeAdView`, "미디어 소재"는 `AdFitMediaView`를 사용하셔야 합니다.

- 네이티브 광고 노출 및 해제

응답 받은 `AdFitNativeAdBinder`와 구성한 `AdFitNativeAdLayout`으로 네이티브 광고를 노출할 수 있습니다.

```kotlin
val nativeAdBinder: AdFitNativeAdBinder
val nativeAdLayout: AdFitNativeAdLayout

// 광고 노출 및 측정 시작
nativeAdBinder.bind(nativeAdLayout)

// 광고 노출 해제 및 측정 중단
nativeAdBinder.unbind()
```

* `bind()` 호출 시, `AdFitNativeAdLayout`에 광고 소재(텍스트, 이미지, 비디오 등의 리소스)를 적용합니다.
* `bind()` 호출 시부터 `AdFitNativeAdLayout`의 노출 상태를 측정하며, `unbind()`가 호출되면 측정을 중단합니다.
* `unbind()` 호출은 필수 사항이 아닙니다.
---|---

Adfit을 사용하기 위한 4개의 클래스 파일

- AdfitNative
```kotlin
class AdfitNative: Adapter() {
    private var nativeLoader: AdFitNativeLoader? = null

    override fun loadNativeAd(
        mediationNativeAdConfiguration: MediationNativeAdConfiguration,
        callback: MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback>
    ) {
        nativeLoader = AdFitNativeLoader(mediationNativeAdConfiguration, callback)
        nativeLoader!!.loadAd()
    }

    override fun initialize(
        context: Context,
        initializationCompleteCallback: InitializationCompleteCallback,
        list: List<MediationConfiguration>
    ) {
        initializationCompleteCallback.onInitializationSucceeded()
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString = "1.0.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString = "1.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }
```
- AdfitNativeLoader
```kotlin

class AdFitNativeLoader(
    /** Configuration for requesting the native ad from the third-party network. */
    private val mediationNativeAdConfiguration: MediationNativeAdConfiguration,
    /** Callback that fires on loading success or failure. */
    private var mediationAdLoadCallback: MediationAdLoadCallback<UnifiedNativeAdMapper, MediationNativeAdCallback>
) {
    /** Callback for native ad events. */
    private var nativeAdCallback: MediationNativeAdCallback? = null
    private var nativeAdBinder: AdFitNativeAdBinder? = null


    /** Loads the interstitial ad from the third-party ad network.  */
    fun loadAd() {
        // All custom events have a server parameter named "parameter" that returns
        // back the parameter entered into the UI when defining the custom event.
        Log.i(TAG, "Admob Mediation : AdFit Native loadAd")
        val serverParameter = mediationNativeAdConfiguration.serverParameters.getString(
            MediationConfiguration.CUSTOM_EVENT_SERVER_PARAMETER_FIELD)
        if (TextUtils.isEmpty(serverParameter)) {
            mediationAdLoadCallback.onFailure(
                AdError(
                    ERROR_NO_AD_UNIT_ID,
                    "Ad unit id is empty",
                    CUSTOM_EVENT_ERROR_DOMAIN
                )
            )
            return
        }

        Log.d(TAG, "Received server parameter.")
        val context = mediationNativeAdConfiguration.context

        // AdFitNativeAdLoader 생성
        val nativeAdLoader: AdFitNativeAdLoader = AdFitNativeAdLoader.create(context, serverParameter.toString())

        // 네이티브 광고 요청 정보 설정
        val request = AdFitNativeAdRequest.Builder()
            .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP)
            .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY)
            .build()

        nativeAdLoader.loadAd(request, object : AdFitNativeAdLoader.AdLoadListener {
            override fun onAdLoaded(binder: AdFitNativeAdBinder) {
                val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val nativeAdView = ItemNativeAdBinding.inflate(inflater)


                val nativeAdLayout: AdFitNativeAdLayout =
                    AdFitNativeAdLayout.Builder(nativeAdView.containerView)
                        .setTitleView(nativeAdView.titleTextView)
                        .setBodyView(nativeAdView.bodyTextView)
                        .setProfileIconView(nativeAdView.profileIconView)
                        .setProfileNameView(nativeAdView.profileNameTextView)
                        .setMediaView(nativeAdView.mediaView)
                        .setCallToActionButton(nativeAdView.callToActionButton)
                        .build()

                nativeAdBinder = binder
                binder.bind(nativeAdLayout)

                val unifiedNativeAdMapper = SampleUnifiedNativeAdMapper(context, nativeAdView)
                nativeAdCallback = mediationAdLoadCallback.onSuccess(unifiedNativeAdMapper)
                nativeAdCallback!!.reportAdImpression()
            }

            override fun onAdLoadError(errorCode: Int) {
                mediationAdLoadCallback.onFailure(AdError(errorCode, "AdFit native ad load failed", SAMPLE_SDK_DOMAIN))
            }
        })
    }

    companion object {
        val TAG = AdFitNativeLoader::class.java.simpleName

        /** Error raised when the custom event adapter cannot obtain the ad unit id.  */
        const val ERROR_NO_AD_UNIT_ID = 101

        /** Error raised when the custom event adapter cannot obtain the activity context.  */
        const val SAMPLE_SDK_DOMAIN = "kr.co.cauly.sdk.android"
        const val CUSTOM_EVENT_ERROR_DOMAIN = "com.google.ads.mediation.sample.customevent"
    }
```
- SampleNativeMappedImge
```kotlin
import android.graphics.drawable.Drawable
import android.net.Uri
import com.google.android.gms.ads.formats.NativeAd

class SampleNativeMappedImage(
    private val drawable: Drawable,
    private val uri: Uri,
    private val scale: Double
) : NativeAd.Image() {

    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun getUri(): Uri {
        return uri
    }

    override fun getScale(): Double {
        return scale
    }
}
```
- SampleUnifiedNativeAdMapper
```kotlin
class SampleUnifiedNativeAdMapper: UnifiedNativeAdMapper {
    var context: Context? = null

    constructor(context: Context, nativeAdView: ItemNativeAdBinding) {
        this.context = context

        if (context == null) {
            Log.e("AdfitNative", "Failed to load ad. Request must be for unified native ads.")
            return
        }

        setMediaView(nativeAdView.mediaView)
        icon = SampleNativeMappedImage(nativeAdView.profileIconView.drawable, getUri(context, (nativeAdView.profileIconView.drawable as BitmapDrawable).bitmap), 1.0)

        headline = nativeAdView.titleTextView.text as String
        body = nativeAdView.bodyTextView.text as String
        callToAction = nativeAdView.callToActionButton.text as String
        advertiser = nativeAdView.profileNameTextView.text as String


        overrideImpressionRecording = false
        overrideImpressionRecording = false
    }

    private fun getUri(context: Context, bitmap: Bitmap): Uri {
        var bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "icon", null)
        return Uri.parse(path)
    }
}
```
