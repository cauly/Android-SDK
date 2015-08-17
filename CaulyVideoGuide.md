![Valid XHTML](http://cauly044.fsnsys.com:10010/images/logo_cauly_main.png) CAULY Video SDK 연동가이드
----

### 목차 
1. CAULY SDK v3.4
2. SDK 설치 방법
3. Class Reference
 
#### CAULY SDK v3.4

1. Release note
	- 이번 버전에서 향상된 점
		-CAULY Video 추가
2. 주의 사항
	- Proguard를 사용하고 있다면, 아래 내용을 추가해야 합니다.
```java
	-keep public class com.fsn.cauly.** {
	 	   public protected *;
	}
	-keep public class com.trid.tridad.** {
	  	  public protected *;
	}
	-dontwarn android.webkit.**
```
	- 권장 환경
		- Android 2.1 버전 이상 (API level 7 이상)
	- SDK 구성
		- caulySDK-x.y.z.jar

#### SDK 설치 방법

1. CAULY SDK를 설치할 project 에 ‘libs’ 폴더를 생성 한 후, ‘caulySDK-x.z.z.jar’ 파일 복사 한다
2. ‘caulySDK-x.y.z.jar’ 파일을 라이브러리에 import 한다
	- ’Properties’  ’javaBuild Path’  ’Libraries’  ’Add JARs…’‘caulySDK-x.y.zjar’
3. ‘AndroidManifest.xml’ 설정 방법 [자세한 내용은 ‘CaulyExample’ 참조]
	- 광고가 삽입되는 activity에 configChanges="keyboard|keyboardHidden|orientation” 설정
		- 만약, 설정하지 않으면 화면 전환 시 마다 광고view 가 초기화 됩니다.
			1. target Api Level 15 이상인 경우 
			```
				configChanges="keyboard|keyboardHidden|orientation|screenSize”
			```
			2. target Api Level 15 이하인 경우
			```
				configChanges="keyboard|keyboardHidden|orientation”
			```
	- 필수 퍼미션 추가
	```xml
	<!-- 예시 : keyboard|keyboardHidden|orientation 추가  -->
	<activity android:name=".Sample"
	          android:label="@string/title_activity_java_sample"
	       	  android:configChanges="keyboard|keyboardHidden|orientation" >
	<!-- 퍼미션 -->
	→ 필수 퍼미션
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	```

	
4. 광고를 삽입하고 싶은 layout에 광고를 소스를 삽입
	
	- CAULY Video  : BASE
		```java
		public class MainActivity extends Activity {
		
			 static final String APP_CODE = "CAULY";
			CaulyVideoAdView adVideoView;
			
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				Logger.setLogLevel(LogLevel.Verbose);
				requestVideo();
			}
			
			
			//CAULY Video 없애고,   본 영상을 재생한다
			public void loadMainVideo()
			 {
				 
				RelativeLayout rootView = (RelativeLayout) findViewById(R.id.container);
				 rootView.removeAllViews();
				 /*
				  * Do what you want to do.
				  * 
				  * */
				 
			 }
			//CAULY Video를 호출한다. 
			public void requestVideo()
			{
				CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).build();
				adVideoView = new CaulyVideoAdView(this);
				adVideoView.setAdInfo(adInfo);
				adVideoView.setKeywords("category1,category2,category3");//카테고리 설정 최대 3개까지 설정가능 
				adVideoView.setAdViewListener(new CaulyVideoAdViewListener() {
					
					//광고수신에 성공되었을 때 호출된다.
					public void onReceiveVideoAd(final CaulyVideoAdView adView, boolean isChargeableAd) {
						//광고컨테이너에 수신받은 CaulyVideoAdView를 attach한다.
						RelativeLayout rootView = (RelativeLayout) findViewById(R.id.container);
						adView.attachToView(rootView);
					}
					//광고수신에 실패되었을 때 호출된다.
					public void onFailToReceiveVideoAd(CaulyVideoAdView adView,	int errorCode, String errorMsg) {
						Log.i("CaulyVideo","onFailedToReceiveVideoAd");
						//타사의 비디오광고를 연동하거나, 본영상을 재생시키는 작업수행.
						loadMainVideo();
						
					}
					//CAULY Video 재생 중, 재생완료, 광고클릭, 스킵버튼클릭, 플레이에러 등으로 광고영상이 끝났을 때 호출
					public void onFinishVideoAd(int code, String msg) {
						Log.i("CaulyVideo","onFinishVideoAd  "+code+" "+msg);
						loadMainVideo();
						
						switch(code)
						{
						case CaulyVideoAdView.MSG_VIDEO_COMPLETED:
							break;
						case CaulyVideoAdView.MSG_VIDEO_CLICK:
							break;
						case CaulyVideoAdView.MSG_VIDEO_ERROR:
							break;
						case CaulyVideoAdView.MSG_VIDEO_SKIPED:
							break;
						}
					}
					//광고의 재생시작을 알려준다.
					public void onStartVideoAd() {
						Log.i("CaulyVideo","onStartedVideoAd");
					}
		
				});
				adVideoView.request();
				
			}
		}

		
		Xml – activity_native_iconlist
		<?xml version="1.0" encoding="utf-8"?>
		<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		android:id="@+id/root_view"  android:layout_width="match_parent"
    		android:layout_height="match_parent" >

    			<RelativeLayout
        		android:id="@+id/container"
			 android:layout_width="match_parent"
        		android:layout_height="230dp" >
    			</RelativeLayout>

		</RelativeLayout>
		```
	
> CAULY SDK 연동 관련한 문의는 카울리 홈페이지[고객지원 >> 1대1 문의] 
> 또는 cauly@fsn.co.kr 로 문의 주시면 빠르게 응대해 드리도록 하겠습니다.

Class Reference
=================

[error 코드 정의]
		
Code|Message|설명
---|---|---
0|OK|유료 광고
100|	Non-chargeable ad is supplied|무료 광고(속성 : 공익 광고, Cauly 기본 광고)
200|	No filled AD	|광고 없음
400|	The app code is invalid. Please check your app code!	|App code 불일치 또는default app code
500|	Server error	|cauly서버 에러
-100|	SDK error	|SDK 에러
-200|	Request Failed(You are not allowed to send requests under minimum interval)	|최소요청주기 미달

Logger 로그 생성 클래스
------------------------------
데이터 형||
---|---
LogLevel|로그 수준. enum Verbose, Debug, Info, Warn, Error, None

메소드||
---|---
setLogLevel(LogLevel)|로그 수준 지정
getLogLevel()	|현재 로그 수준


CAULY Video
--------------
CaulyVideoAdView||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setAdViewListener(CaulyVideoAdViewListener)	|CaulyVideoAdViewListener 지정
request ()	|광고 요청 
setKeywords	|광고 타켓팅에 활용되는 키워드 설정( 카테고리, 태그 등을 ','(컴마)로 구분하여 최대 3개까지 설정가능)
setSkipCount	|광고의 건너띄우기 버튼의 노출시간설정
attachToView(ViewGroup)	|원하는 위치(ViewGroup)에 수신한 광고를 붙인다. 
isAttachedtoView()	|광고가 ViewGroup에 노출되었는지 여부
destroy()	|광고 소멸


CaulyVideoAdViewListener||
---|---
onReceiveVideoAd(CaulyVideoAdView, boolean isChargeableAd)	|광고 요청 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailToReceiveVideoAd(CaulyVideoAdView, int errorCode, String errorMsg)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨
onStartVideoAd()	|광고의 재생시작을 알려준다.
onFinishVideoAd()	|카울리비디오 재생 중, 재생완료, 광고클릭, 스킵버튼클릭, 플레이에러 등으로 광고영상이 끝났을 때 호출

CaulyAdInfo[광고 설정 클래스]
-------------------------
CaulyAdInfoBuilder[CaulyAdInfo 생성용 클래스]||
---|---
CaulyAdInfoBuilder(Context, AttributeSet)	|지정한 Context 및 AttributSet으로 CaulyAdInfoBuilder 생성
CaulyAdInfoBuilder(String)	|지정한 App Code로 CaulyAdInfoBuilder 생성
build()	|설정한 정보에 따라 CaulyAdInfo 생성















