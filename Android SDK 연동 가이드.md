# 목차
1. [CAULY 시작하기](#1-CAULY-시작하기)
    * [build.gradle > aar 파일 추가, Google Play Services API 추가](#buildgradle--aar-파일-추가-google-play-services-api-추가)   
    * [앱 AndroidManifest.xml 속성 지정](#앱-androidmanifestxml-속성-지정)
    * [proguard 설정](#proguard-설정-하는-경우-cauly-sdk-포함된-class는-난독화-시키시면-안됩니다)
    * [AndroidX 설정](#androidx-사용하는-경우)
2. [배너 광고 추가하기](#2-배너-광고-추가하기)
    * [JAVA 방식 base](#java-방식-base-자세한-내용은-caulyexample-참조)
    * [XML 방식](#xml-방식--설정하지-않은-항목들은-기본값으로-설정됩니다)
3. [전면 광고 추가하기](#3-전면-광고-추가하기)
    * [전면광고 fullScreen Type](#전면광고-fullscreen-type)   
    * [전면광고 Close Ad Type](#전면광고-close-ad-type)   
4. [네이티브 광고 추가하기](#4-네이티브-광고-추가하기)
    * [네이티브광고 : BASE](#네이티브광고--base)   
    * [네이티브광고:Custom](#네이티브광고custom)   
5. [아동 대상 서비스 취급용 '태그' 설정](#5-아동-대상-서비스-취급용-태그-설정)
6. [Error Code](#6-error-code)
7. [Class Reference](#7-class-reference)

- - - 
# 1. CAULY 시작하기


### build.gradle > aar 파일 추가, Google Play Services API 추가

- CAULY SDK를 설치할 project 에 ‘libs’ 폴더를 생성한 후, ‘CaulySDK-3.x.xx.aar’ 파일 복사한다.	
- Gradle 설정 `app-level build.gradle`

	```clojure
 	repositories {
        flatDir {
            dirs 'libs'
        }
    }

	dependencies {
		implementation name: 'CaulySDK-3.x.xx', ext: 'aar'
		//Google Play Services
    	implementation 'com.google.android.gms:play-services-ads-identifier:xx.x.x'
	}
	```


### 앱 AndroidManifest.xml 속성 지정

#### 필수 퍼미션 추가
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

#### 네트워크 보안 설정 (targetSdkVersion 28 이상)

`광고 노출 및 클릭이 정상적으로 동작하기 위해서 cleartext 네트워크 설정 필요`

```xml
<application android:usesCleartextTraffic="true" />
```	

#### Hardware Acceleration

html5, video 광고 노출을 위해서는 설정 필요
```xml
<application android:hardwareAccelerated="true" />
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

### proguard 설정 하는 경우 cauly SDK 포함된 Class는 난독화 시키시면 안됩니다.

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
	
### AndroidX 사용하는 경우
```xml
gradle.properties ::
 * android.useAndroidX=true
 * android.enableJetifier=true
```

- 참고 : https://developer.android.com/jetpack/androidx/migrate

# 2. 배너 광고 추가하기

광고를 삽입하고 싶은 layout에 광고를 소스를 삽입(두 가지 방식 제공 : XML 방식, JAVA 방식)

#### `JAVA 방식` base [자세한 내용은 ‘CaulyExample’ 참조]
``` java
   private CaulyAdView javaAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        // Cauly 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
        //LogLevel.Verbose > LogLevel.Debug > LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
        Logger.setLogLevel(LogLevel.Debug);

        // CaulyAdInfo 상세 설정 방법은 하단 표 참조
        // 설정하지 않은 항목들은 기본값으로 설정됨
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).
                effect("RightSlide").
                bannerHeight("Fixed").
                build();

        // CaulyAdInfo를 이용, CaulyAdView 생성.
        javaAdView = new CaulyAdView(this);
        javaAdView.setAdInfo(adInfo);
        javaAdView.setAdViewListener(this);

        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.java_root_view);
        // 예시 : 화면 하단에 배너 부착
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rootView.addView(javaAdView, params);
    }

    // CaulyAdViewListener
    //	광고 동작에 대해 별도 처리가 필요 없는 경우,
    //	Activity의 "implements CaulyAdViewListener" 부분 제거하고 생략 가능.
    @Override
    public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
        // 광고 수신 성공 & 노출된 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free banner AD received.");
        } else {
            Log.d("CaulyExample", "normal banner AD received.");
        }
    }

    @Override
    public void onFailedToReceiveAd(CaulyAdView adView, int errorCode, String errorMsg) {
        // 배너 광고 수신 실패할 경우 호출됨.
        Log.d("CaulyExample", "failed to receive banner AD.");
    }

    @Override
    public void onShowLandingScreen(CaulyAdView adView) {
        // 광고 배너를 클릭하여 랜딩 페이지가 열린 경우 호출됨.
        Log.d("CaulyExample", "banner AD landing screen opened.");
    }

    @Override
    public void onCloseLandingScreen(CaulyAdView adView) {
        // 광고 배너를 클릭하여 열린 랜딩 페이지가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "banner AD landing screen closed.");
    }

    // Activity 버튼 처리
    // - Java 배너 광고 갱신 버튼
    public void onReloadJavaAdView(View button) {
        javaAdView.reload();
    }
```

#### ‘광고 삽입할 부분’.xml 

```xml
        <RelativeLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@+id/layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
        </RelativeLayout>
```

#### `XML 방식` : 설정하지 않은 항목들은 기본값으로 설정됩니다.

```xml
	<com.fsn.cauly.CaulyAdView
            xmlns:app="http://schemas.android.com/apk/res/[개발자 프로젝트 PACKAGENAME]"
            android:id="@+id/xmladview"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_alignParentBottom="true"
            app:appcode="CAULY"
            app:bannerHeight="Fixed" />
```

#### 'project > res > values'에 'attrs.xml' 파일 생성 후 아래 코드 추가

```xml
	<declare-styleable name="com.cauly.android.ad.AdView">
		<attr name="appcode" format="string" />
		<attr name="effect" format="string" />
		<attr name="dynamicReloadInterval" format="boolean" />
		<attr name="reloadInterval" format="integer" />
		<attr name="threadPriority" format="integer" />
		<attr name="bannerHeight" format="string" />
		<attr name=" enableDefaultBannerAd " format=" boolean " /> 
	</declare-styleable>
```

#### 주의사항  

- Lifecycle에 따라 CaulyAdView의 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.
- 미디에이션 사용시 카울리배너광고 호출하면 광고 삽입한 부분의 View를 removeView 및 CaulyAdView의 destroy, null 처리 해주시길 바랍니다.    또한 배너 광고 수신 실패할 경우 광고 삽입한 부분의 View를 removeView 해주세요.


[CaulyAdinfo 설정 방법]

Adinfo|설 명
---|---
Appcode|APP 등록 후 부여 받은 APP CODE[발급ID] 입력
Effect()|LeftSlide(기본값) : 왼쪽에서 오른쪽으로 슬라이드<br/>RightSlide : 오른쪽에서 왼쪽으로 슬라이드<br/>TopSlide : 위에서 아래로 슬라이드<br/>BottomSlide : 아래서 위로 슬라이드<br/>FadeIn : 전에 있던 광고가 서서히 사라지는 효과 <br/>Circle : 한 바퀴 롤링<br/>None : 애니메이션 효과 없이 바로 광고 교체
reloadInterval()|min(기본값) : 20초)<br/>max : 120 초
dynamicReloadInterval()|true(기본값) : 광고에 따라 노출 주기 조정할 수 있도록 하여 광고 수익 상승 효과 기대<br/>false : reloadInterval 설정 값으로 Rolling
bannerHeight()|Proportional(기본값) : 디바이스 긴방향 해상도의 10% <br/> Fixed : 50dp
enableDefaultBannerAd ()|false : 디폴트배너 미노출 <br/>true : 디폴트배너 노출
threadPriority()|스레드 우선 순위 지정 : 1~10(기본값 : 5)
tagForChildDirectedTreatment(boolean)	|14세 미만 일 때 true
gdprConsentAvailable(boolean)	|gdpr 동의 일 때 true

# 3. 전면 광고 추가하기

#### 전면광고 fullScreen Type

```java
    // 아래와 같이 전면 광고 표시 여부 플래그를 사용하면, 전면 광고 수신 후, 노출 여부를 선택할 수 있다.
    private boolean showInterstitial = false;

    // Activity 버튼 처리
    // - 전면 광고 요청 버튼
    public void onRequestInterstitial(View button) {

        // CaulyAdInfo 생성
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE)
                          // statusbarHide(boolean) : 상태바 가리기 옵션(true : 상태바 가리기)
                            .build();

        // 전면 광고 생성
        CaulyInterstitialAd interstial = new CaulyInterstitialAd();
        interstial.setAdInfo(adInfo);
        interstial.setInterstialAdListener(this);

        // 전면광고 노출 후 back 버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
        // 단, requestInterstitialAd 위에서 추가되어야 합니다.
        // interstitialAd.disableBackKey();

        // 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
        interstial.requestInterstitialAd(this);
        // 전면 광고 노출 플래그 활성화
        showInterstitial = true;
    }

    // - 전면 광고 노출 취소 버튼
    public void onCancelInterstitial(View button) {
        // 전면 광고 노출 플래그 비활성화
        showInterstitial = false;
    }

    // CaulyInterstitialAdListener
	//전면 광고의 경우, 광고 수신 후 자동으로 노출되지 않으므로,
	//반드시 onReceiveInterstitialAd 메소드에서 노출 처리해 주어야 한다.
    @Override
    public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
        // 광고 수신 성공한 경우 호출됨.
        // 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
        if (isChargeableAd == false) {
            Log.d("CaulyExample", "free interstitial AD received.");
        } else {
            Log.d("CaulyExample", "normal interstitial AD received.");
        }
        // 노출 활성화 상태이면, 광고 노출
        if (showInterstitial)
            ad.show();
        else
            ad.cancel();
    }

    @Override
    public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errorCode, String errorMsg) {
        // 전면 광고 수신 실패할 경우 호출됨.
        Log.d("CaulyExample", "failed to receive interstitial AD.");
    }

    @Override
    public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
        // 전면 광고가 닫힌 경우 호출됨.
        Log.d("CaulyExample", "interstitial AD closed.");
    }
```
		
#### 전면광고 Close Ad Type

```java
public class JavaActivity extends Activity implements CaulyCloseAdListener {

    private static final String APP_CODE = "CAULY"; // 광고 요청을 위한 App Code
    CaulyCloseAd mCloseAd;                         // CloseAd광고 객체

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        //CloseAd 초기화 
        CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE)
                          // statusbarHide(boolean) : 상태바 가리기 옵션(true : 상태바 가리기)
                            .build();	
        mCloseAd = new CaulyCloseAd();
					
		/*  Optional
		//원하는 버튼의 문구를 설정 할 수 있다.  
		mCloseAd.setButtonText("취소", "종료");
		//원하는 텍스트의 문구를 설정 할 수 있다.  
		mCloseAd.setDescriptionText("종료하시겠습니까?");
					*/
        mCloseAd.setAdInfo(closeAdInfo);
        mCloseAd.setCloseAdListener(this); // CaulyCloseAdListener 등록
        // 종료광고 노출 후 back버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
        // mCloseAd.disableBackKey();	
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCloseAd != null)
            mCloseAd.resume(this); // 필수 호출 
    }

    // Back Key가 눌러졌을 때, CloseAd 호출
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
            if (mCloseAd.isModuleLoaded()) {
                mCloseAd.show(this);
            } else {
                // 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
                showDefaultClosePopup();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDefaultClosePopup() {
        new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("아니요", null)
                .show();
    }

    // CaulyCloseAdListener
    @Override
    public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode, String errMsg) {
    }

    // CloseAd의 광고를 클릭하여 앱을 벗어났을 경우 호출되는 함수이다. 
    @Override
    public void onLeaveCloseAd(CaulyCloseAd ad) {
    }

    // CloseAd의 request()를 호출했을 때, 광고의 여부를 알려주는 함수이다. 
    @Override
    public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {

    }

    //왼쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다. 
    @Override
    public void onLeftClicked(CaulyCloseAd ad) {

    }

    //오른쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다. 
    //Default로는 오른쪽 버튼이 종료로 설정되어있다. 
    @Override
    public void onRightClicked(CaulyCloseAd ad) {
        finish();
    }

    @Override
    public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {
    }
}
```

#### 주의사항

Lifecycle에 따라 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.

# 4. 네이티브 광고 추가하기

#### 네이티브광고 : BASE

```java
public class JavaActivity extends Activity implements CaulyCloseAdListener {

  private static final String APP_CODE = "CAULY"; // 광고 요청을 위한 App Code
    private ViewGroup native_container;
    private CaulyNativeAdView nativeAd;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        native_container = (ViewGroup) findViewById(R.id.native_container);
        request_Native_Base(APP_CODE);
    }

    private void request_Native_Base(String APP_CODE) {
        if (nativeAd != null) {
            nativeAd.destroy();
            native_container.removeView(nativeAd);
        }
        if (webView != null) {
            webView.removeAllViews();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
        }
        // Request Native AD
        // 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
        // CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
        CaulyAdInfo  caulyNativeAdInfoBuilder = new CaulyNativeAdInfoBuilder(APP_CODE) // 광고 요청을 위한 App Code
                .layoutID(R.layout.activity_native_iconlist) // 네이티브애드에 보여질 디자인을 작성하여 등록한다.
                .mainImageID(R.id.main_image) //메인이미지
                .titleID(R.id.title)         // 제목 등록
                .subtitleID(R.id.subtitle)   // 부제목 등록
                .textID(R.id.description)
                .iconImageID(R.id.icon)      // 아이콘 등록
                .adRatio("720x720") //메인이미지 비율설정  안할경우, default: 720x480  or 480x720
                .build();
        nativeAd = new CaulyNativeAdView(this);
        nativeAd.setAdInfo(caulyNativeAdInfoBuilder);
        nativeAd.setAdViewListener(new CaulyNativeAdViewListener() {
            // 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
            @Override
            public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {
                adView.attachToView(native_container);
            }
            // 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다.
            @Override
            public void onFailedToReceiveNativeAd(CaulyNativeAdView adView, int errorCode, String errorMsg) {

            }
        });
        nativeAd.request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nativeAd.destroy();
    }
}
```
```xml
-activity_native_iconlist.xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">

    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        tools:ignore="WebViewLayout">

        <WebView
            android:id="@+id/main_image"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentTop="true" />

        <RelativeLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">

            <ImageView
                android:id="@+id/icon"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:scaleType="fitXY" />

            <ImageView
                android:id="@+id/sponsor"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerHorizontal="true"
                android:visibility="gone" />
        </RelativeLayout>

        <TextView
            android:id="@+id/title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="114dp"
            android:layout_marginTop="30dp"
            android:layout_marginRight="14dp"
            android:lines="1"
            android:textColor="#000000"
            android:textSize="13dp" />

        <TextView
            android:id="@+id/subtitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginLeft="114dp"
            android:layout_marginTop="50dp"
            android:layout_marginRight="14dp"
            android:lines="2"
            android:textColor="#8a837e"
            android:textSize="10dp" />

        <TextView
            android:id="@+id/description"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginRight="14dp"
            android:layout_marginBottom="9dp"
            android:lines="1"
            android:textColor="#e15052"
            android:textSize="15dp" />
    </RelativeLayout>

</RelativeLayout>
```



#### 네이티브광고:Custom

 1. CaulyAdInfo 생성
 2. CaulyCustomAd 객체 생성후, CaulyAdInfo 적용
 3. CaulyCustomAd의 CaulyCustomAdListener등록
 4. CaulyCustomAd의 JSON 타입의 광고정보 요청
 5. 수신한 JSON Data Format.
 6. 소재가 화면에 보여지게 되면 노출로그 전송
	- sendImpressInform(광고ID)
 7. 광고 클릭시, linkUrl 로 이동
	- CaulyBrowserUtil.openBrowser(Context, linkUrl)
 8. 구현 형태
	- WebView 형태로 구현해주세요
 9. 파싱방법
 	- List<HashMap<KEY,VALUE>> 방식으로 가져오는 경우, `mCaulyAdView.getAdsList();`
	- Raw JSON String을 직접가져 경우 `mCaulyAdView.getJsonString();`

```
    {
            "ads":[
            {
                "id":"광고ID",
                    "ad_charge_type":"0 : 유료광고, 100: 하우스 광고",
                    "icon":"아이콘 이미지",
                    "image":"메인 이미지",
                    "title":"제목",
                    "subtitle":"부제목",
                    "description":"설명"
                "optout_img_url":"optout 버튼 이미지(png)",
                "optout_url":"optout page url",
                "optout":"관심기반 광고 여부 , true or false",
		"linkUrl":랜딩 페이지 URL
            }
			]
        }
```

#### WebView 형태
```java
		WebView web = (WebView)findViewById(R.id.webbanner);
       		web.getSettings().setJavaScriptEnabled(true);
        	web.setVerticalScrollBarEnabled(false);
        	web.setHorizontalScrollBarEnabled(false);
        	web.getSettings().setUseWideViewPort(true);
        	web.getSettings().setLoadsImagesAutomatically(true);
        	web.getSettings().setLoadWithOverviewMode(true);
        	web.getSettings().setBuiltInZoomControls(true);
        	web.getSettings().setDomStorageEnabled(true);
        	web.loadUrl(item.image);
```

#### 파싱방법
```java
{
CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
                .iconImageID(R.id.main_image) // 아이콘 이미지를 사용할 경우  
                .mainImageID(R.id.icon)       //메인 이미지를 사용할 경우
                .adRatio("720x720")			  //메인이미지 비율설정  안할경우, default: 720x480  or 480x720
		.build();
        	mCaulyAdView = new CaulyCustomAd(this);
       		mCaulyAdView.setAdInfo(adInfo);
        	mCaulyAdView.setCustomAdListener(new CaulyCustomAdListener() {
            @Override
            public void onShowedAd() {
            }

            //광고 호출이 성공할 경우, 호출된다.
            @Override
            public void onLoadedAd(boolean isChargeableAd) {
                List<HashMap<String, Object>> adlist = caulyCustomAd.getAdsList();
                if (adlist != null && adlist.size() > 0) {
                    for (HashMap<String, Object> map : adlist) {
                        AdItem data = new AdItem();
                        data.id = (String) map.get("id");
                        data.image = (String) map.get("image");
                        data.linkUrl = (String) map.get("linkUrl");
                        data.image_content_type = (String) map.get("image_content_type");
                        addWebBanner(data, caulyCustomAd);
                    }
                }
            }

            // 광고 호출이 실패할 경우, 호출된다.
            @Override
            public void onFailedAd(int errCode, String errMsg) {
            }

            // 광고가 클릭된 경우, 호출된다.
            @Override
            public void onClikedAd() {
            }

        });
     // CaulyCustomAd.INTERSTITIAL_PORTRAIT,CaulyCustomAd.NATIVE_PORTRAIT,CaulyCustomAd.NATIVE_LANDSCAPE
     CaulyCustomAd.requestAdData (type, ad_count);
}

        private void addWebBanner(final AdItem item, CaulyCustomAd mCaulyAdView, boolean check) {

            WebView web = findViewById(R.id.webbanner);
            web.getSettings().setJavaScriptEnabled(true);
            web.setVerticalScrollBarEnabled(false);
            web.setHorizontalScrollBarEnabled(false);
            web.getSettings().setUseWideViewPort(true);
            web.getSettings().setLoadsImagesAutomatically(true);
            web.getSettings().setLoadWithOverviewMode(true);
            web.getSettings().setBuiltInZoomControls(false);
            web.getSettings().setDomStorageEnabled(true);
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            web.setScrollbarFadingEnabled(false);
            web.loadUrl(item.image);
            web.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        CaulyBrowserUtil.openBrowser(MainActivity.this, item.linkUrl);
                    }
                    return true;
                }
            });

        //광고 유효노출 호출
        mCaulyAdView.sendImpressInform(item.id);
}
```
#### 주의사항
광고영역에 WebView 권장 및 Lifecycle에 따라 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.

# 5. 아동 대상 서비스 취급용 '태그' 설정

아동대상 콘텐츠로 지정한 경우 관심 기반 광고 및 리마케팅 광고 등이 필터링 됩니다.
		
- google families policy : https://play.google.com/about/families/#!?zippy_activeEl=designed-for-families#designed-for-families
- coppa : https://www.ftc.gov/tips-advice/business-center/privacy-and-security/children's-privacy
	

##### COPPA에 따라 콘텐츠를 아동 대상으로 지정하려면 'tagForChildDirectedTreatment(true)' 로 호출 한다.

```java
 CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APPCODE).
 	tagForChildDirectedTreatment(true).
	build()
```
##### COPPA에 따라 콘텐츠를 아동 대상으로 지정하지 않으려면 'tagForChildDirectedTreatment(false)' 로 호출 한다.
```java
 CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APPCODE).
	 tagForChildDirectedTreatment(false).
 	 build()
```
 \* tagForChildDirectedTreatment을 호출하지 않으면 아동 대상 콘테츠가 아닌 것으로 간주 합니다.

# 6. Error Code

[error 코드 정의]
		
Code|Message|설명
---|---|---
0|OK|유료 광고
200|	No filled AD	|전면CPM 광고 없음
400|	The app code is invalid. Please check your app code!	|App code 불일치 또는default app code
500|	Server error	|cauly서버 에러
-100|	SDK error	|SDK 에러
-200|	Request Failed(You are not allowed to send requests under minimum interval)	|최소요청주기 미달


# 7. Class Reference

Logger 로그 생성 클래스
------------------------------
데이터 형||
---|---
LogLevel|로그 수준. enum Verbose, Debug, Info, Warn, Error, None

메소드||
---|---
setLogLevel(LogLevel)|로그 수준 지정
getLogLevel()	|현재 로그 수준




배너 광고
------------------------------
CaulyAdView[광고 뷰 클래스]||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setAdViewListener(CaulyAdViewListener)	|CaulyAdViewListener 지정
setShowPreExpandableAd(boolean)	|P/E 광고 허용 여부 설정
reload()	|광고 재요청
pause()	|광고 요청 중단
resume()	|광고 요청 재개
destroy()	|광고 소멸


CaulyAdViewListener||
---|---
onReceiveAd(CaulyAdView, boolean isChargeableAd)    |광고 노출 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailedToReceiveAd(CaulyAdView, int errorCode, String errorMsg)    |광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨
onShowLandingScreen(CaulyAdView)    |랜딩 페이지가 열린 경우 호출됨
onCloseLandingScreen(CaulyAdView)   |랜딩 페이지가 닫힌 경우 호출됨


전면 광고_풀스크린형
---------------------------------
CaulyInterstitialAd||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setInterstialAdListener(CaulyInterstitialAdListener)	|CaulyAdViewListener 지정
requestInterstitialAd(Activity)	전면| 광고 요청
show()	|수신한 전면 광고를 노출
cancel()	|수신한 전면 광고를 폐기
disableBackKey()	|전면광고 노출 후 back 버튼 막기

CaulyInterstitialAdListener||
---|---
onReceiveInterstitialAd(CaulyInterstitialAd, boolean isChargeableAd)	|광고 노출 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailedToReceiveInterstitialAd(CaulyInterstitialAd, int errorCode, String errorMsg)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨
onClosedInterstitialAd(CaulyInterstitialAd)	|전면 광고 페이지가 닫힌 경우 호출됨

전면 광고_플로팅형
--------------
CaulyCloseAd||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setCloseAdListener(CaulyCloseAdListener)	|CaulyCloseAdListener 지정
Boolean isModuleLoaded()	|최초 광고 리소스를 다운 받았는지 여부.
resume(Activity)	|광고 State 전달 (required)
request (Activity)	|전면 광고 요청 (optional)
show(Activity)	|수신한 광고를 노출 (required)
setButtonText(String left, String right)	|버튼의 텍스트를 변경 (optional)
setDescriptionText(String)	|설명 텍스트를 변경 (optional)
cancel()	|수신한 광고를 폐기
disableBackKey()	|전면광고 노출 후 back 버튼 막기

CaulyCloseAdListener||
---|---
onReceiveCloseAd(CaulyCloseAd, boolean isChargeableAd)	|광고 요청 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailedToReceiveCloseAd(CaulyCloseAd, int errorCode, String errorMsg)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨
onLeaveCloseAd(CaulyInterstitialAd)	|광고가 클릭되어 앱을 벗어났을 경우 호출됨
onShowedClosedAd(CaulyCloseAd, boolean isChargable)	|광고가 보여졌을 때 호출됨
onLeftClicked(CaulyCloseAd)	|왼쪽 버튼이 클릭되었을 때, 호출됨
onRightClicked(CaulyCloseAd)|	오른쪽 버튼이 클릭되었을 때, 호출됨 

네이티브광고
--------------
CaulyNativeAdView||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setAdViewListener(CaulyNativeAdViewListener)	|CaulyNativeAdViewListener 지정
request (Activity)	|네이티브 광고 요청 
attachToView(ViewGroup)	|원하는 위치(ViewGroup)에 수신한 광고를 붙인다. 
isAttachedtoView()	|광고가 ViewGroup에 노출되었는지 여부
destroy()	|광고 소멸

CaulyNativeAdHelper(ListView 에서 노출하기 위한 Helper)||
---|---
getInstance()	|Singleton 객체
init()|	초기화
add(Context, ViewGroup, int, CaulyNativeAdView)	|ListView의 지정한 위치에 광고를 등록한다.
remove(ViewGroup, int)	|ListView의 지정한 위치에 광고를 해지한다. 
isAdPosiont(ViewGroup, int)	|ListView의 지정한 위치가 네이티브광고인지 여부
getView(ViewGroup,int,convertView)	|BaseAdaper 의 getView에서 등록된 NativeAdView를 붙인다
getSize(ViewGroup)	|현재 등록된 네이티브애드의 사이즈를 반환한다.
destroy()	|광고 소멸 (필수호출)

CaulyNativeAdViewListener||
---|---
onReceiveNativeAd(CaulyNativeAd, boolean isChargeableAd)	|광고 요청 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailedToReceiveNativeAd(CaulyNativeAd, int errorCode, String errorMsg)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨

CaulyAdInfo[광고 설정 클래스]
-------------------------
CaulyNativeAdInfoBuilder[CaulyAdInfo 생성용 클래스]||
---|---
CaulyNativeAdInfoBuilder(Context, AttributeSet)	|지정한 Context 및 AttributSet으로 CaulyAdInfoBuilder 생성
CaulyAdInfoBuilder(String)	|지정한 App Code로 CaulyAdInfoBuilder 생성
layoutID(int)	|앱이 NativeAd로 사용할 XML Layout을 연결한다. 
mainImageID(int)	|Layout 중 메인 이미지가 나타날 영역을 지정한다. 
adRatio(String)	|Layout 중 메인 이미지 사이즈를 요청한다 ex) adRatio("720x480").
mainImageOrientation(Orientation)	|메인 이미지가 가로형인지 세로형인지 설정한다.
iconImageID(int)	|Layout 중 아이콘이미지가 나타날 영역을 지정한다.
sponsorVisible(boolean) |스폰서마크가 화면노출여부를 결정한다.
titleID(int)	|Layout 중 제목이 나타날 영역을 지정한다.
subtitleID(int)	|Layout 중 부제목이 나타날 영역을 지정한다.
textID(int)	|Layout 중 설명부분이 나타날 영역을 지정한다.
clickingView(int id)	|Layout 중 클릭영역이 나타날 영역을 지정한다.
build()	|설정한 정보에 따라 CaulyAdInfo 생성

> cauly SDK 설치 관련하여 문의 사항은 고객센터 **1544-8867**
> 또는 cauly_SDK@fsn.co.kr 로 문의주시기 바랍니다.
