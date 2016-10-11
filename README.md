CAULY Android SDK 연동 가이드
----

### 목차 
1. cauly SDK v3.4
2. SDK 설치 방법
3. Class Reference
 
#### CAULY SDK v3.4

1. Release note
	- 이번 버전에서 향상된 점
		- Android 6.0 Apache Http Client 지원 중단 대응
			- Apache Http Client >> HttpURLConnection Wrapper
		- 광고 기능 개선 및 버그 수정
2. 주의 사항
	- P/E광고 설정 관련
		- 앱 마다 P/E광고 허용 여부를 설정 할 수 있으며, P/E광고 노출을 원하는 경우 ‘cauly홈페이지>> APP관리’에서 ‘ON’ 으로 설정하면 됩니다.
			- cauly 홈페이지 >> app 관리 >> 수익구분 : 배너CPM >> ON
		- 아래 API를 활용하시면 ‘FALSE’로 설정된 ‘View’에서만 P/E광고가 호출 되지 않습니다.
			- setShowPreExpandableAd(boolean)
	- 전면광고 플로팅형_Close Ad Type 설정 관련
		-	onKeyDown() API에서 KEYCODE_BACK 키 입력시, Close Ad show() API를 호출합니다.  
			이 때 광고 구동을 위해 필요한 리소스를 다운받았는지 isModuleLoaded() API를 통해 확인 하고, 필요한 리소스를 						다운받는데 실패했을 경우를 대비해 기본 종료팝업을 구현하는 것을 추천합니다.
		-	Activity ‘onResume()’ 에서 ‘resume(this)’ 을 호출해야 한다.
	- 전면광고 호출방식이 변경되어 XML 방식에서는 전면광고를 부착할 수 없으며, Java방식으로 코딩을 해야 전면광고를 노출할 수 		있습니다.
		전면광고를 보여주기 위해서는 CaulyInterstitialAd 클래스로 광고를 받아와서, Listener 에서 명시적으로 show()를 호출해야 		광고가 보여지게 됩니다.
	- <supports-screens> 사용 시
		- android:anyDensity=["true”] 를 권장 합니다
		- false로 설정할 경우 bannerHeight를 ‘Fixed’로 설정한 높이 고정형 배너가 정상적인 크기로 표시되지 않을 수 있으니 					주의바랍니다.
	- proguard 설정 하는 경우 cauly SDK 포함된 Class 는 난독화 시키시면 안됩니다.
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
		- caulySDK-3.4.xx.jar
		- CaulyExample project

#### SDK 설치 방법

1. CAULY SDK를 설치할 project 에 ‘libs’ 폴더를 생성 한 후, ‘caulySDK-3.4.xx.jar’ 파일 복사 한다
2. 
	- Eclipse :  ‘caulySDK-3.4.xx.jar’ 파일을 라이브러리에 import 한다
		- ’Properties’  ’javaBuild Path’  ’Libraries’  ’Add JARs…’‘caulySDK-3.4.xx.jar’
	
	- Studio :
		-  ‘caulySDK-3.4.xx.jar’를 libs 폴더에 넣는다
		-  ‘caulySDK-3.4.xx.jar’를 선택하고 오른쪽 마우스 클릭하면, Add Ad Library를 클릭하고 자동으로 빌드되는것을 볼 수 있다. 
		- Project Structure > Module> 해당 모듈 선택 후 >Dependencues로 라이브러리가 등록되었는지 확인한다
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
	```
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	```
	
4. 'project > res > values'에 'attrs.xml' 파일 생성 후 아래 코드 추가	
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
	
5. 광고를 삽입하고 싶은 layout에 광고를 소스를 삽입
	(두 가지 방식 제공 : XML 방식, JAVA 방식)
	- XML 방식 : 설정하지 않은 항목들은 기본값으로 설정됩니다.
	```xml
	<com.fsn.cauly.CaulyAdView
		xmlns:app="http://schemas.android.com/apk/res/[개발자 프로젝트 PACKAGENAME]"
		android:id="@+id/xmladview"
		android:layout_width="fill_parent"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		app:appcode="CAULY"
		app:effect="RightSlide"
		app:dynamicReloadInterval="true"
		app:reloadInterval="20"
		app:bannerHeight="Fixed"  
	/>
	```
	[설정방법]
	
	Attrs|설 명
	---|---
	Appcode	APP|등록 후 부여 받은 APP CODE`[발급ID]`입력
	Effect|LeftSlide(기본값) : 왼쪽에서 오른쪽으로 슬라이드<br/>RightSlide : 오른쪽에서 왼쪽으로 슬라이드<br/>TopSlide : 위에서 아래로 슬라이드<br/>BottomSlide : 아래서 위로 슬라이드<br/>FadeIn : 전에 있던 광고가 서서히 사라지는 효과 <br/>Circle : 한 바퀴 롤링<br/>None : 애니메이션 효과 없이 바로 광고 교체
	reloadInterval|min(기본값) : 20 초 <br/>max : 120 초
	enableDefaultBannerAd|false : 디폴트배너 미노출<br/>true : 디폴트배너 노출
	bannerHeight|Proportional(기본값) : 디바이스 긴 방향 해상도의 10<br/>Fixed : 48dp
	threadPriority|스레드 우선 순위 지정 : 1~10(기본값 : 5)
	
	[XML 방식 끝]

	- JAVA 방식 base [자세한 내용은 ‘CaulyExample’ 참조]
		- 위치 : ‘ res  layout  ‘광고 삽입할 부분’.xml 
		```xml
		<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    android:id="@+id/layout"
		    android:orientation="vertical"
		    android:layout_width="fill_parent"
		    android:layout_height="fill_parent">
		</RelativeLayout>
		```
	- 배너 광고
		- 위치 : src >> ‘package name’ >> ‘광고를 붙일 Activity’.java
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
					bannerHeight("Fixed_50").
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
				}else {
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
			
			[CaulyAdinfo 설정 방법]
			
			Adinfo|설 명
			---|---
			Appcode|APP 등록 후 부여 받은 APP CODE[발급ID] 입력
			Effect()|LeftSlide(기본값) : 왼쪽에서 오른쪽으로 슬라이드<br/>RightSlide : 오른쪽에서 왼쪽으로 슬라이드<br/>TopSlide : 위에서 아래로 슬라이드<br/>BottomSlide : 아래서 위로 슬라이드<br/>FadeIn : 전에 있던 광고가 서서히 사라지는 효과 <br/>Circle : 한 바퀴 롤링<br/>None : 애니메이션 효과 없이 바로 광고 교체
			reloadInterval()|min(기본값) : 20초)<br/>max : 120 초
			dynamicReloadInterval()|true(기본값) : 광고에 따라 노출 주기 조정할 수 있도록 하여 광고 수익 상승 효과 기대<br/>false : reloadInterval 설정 값으로 Rolling
			bannerHeight()|Proportional(기본값) : 디바이스 긴방향 해상도의 10%<br/>Fixed : 48dp<br/>Fixed_50 : 50dp
			enableDefaultBannerAd ()|false : 디폴트배너 미노출 <br/>true : 디폴트배너 노출
			threadPriority()|스레드 우선 순위 지정 : 1~10(기본값 : 5)

	- 전면광고
		```java
		// 아래와 같이 전면 광고 표시 여부 플래그를 사용하면, 전면 광고 수신 후, 노출 여부를 선택할 수 있다.
		private boolean showInterstitial = false;
		
		// Activity 버튼 처리
		// - 전면 광고 요청 버튼
		public void onRequestInterstitial(View button) {
		
			// CaulyAdInfo 생성
			CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).build();
			
			// 전면 광고 생성
			CaulyInterstitialAd interstial = new CaulyInterstitialAd();
			interstial.setAdInfo(adInfo);
			interstial.setInterstialAdListener(this);
			
			// 전면광고 노출 후 back 버튼 사용을 막기 원할 경우 disableBackKey();을 추가한다
			// 단, requestInterstitialAd 위에서 추가되어야 합니다.
			// interstitialAd.disableBackKey();
			
			// 광고 요청. 광고 노출은 CaulyInterstitialAdListener의 onReceiveInterstitialAd에서 처리한다.
			interstial.showInterstitialAd(this);
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
			}else {
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
		
		[error 코드 정의]
		
		Code|Message|설명
		---|---|---
		0|OK|유료 광고
		100|	Non-chargeable ad is supplied|무료 광고(속성 : 공익 광고, Cauly 기본 광고)
		200|	No filled AD	|전면CPM 광고 없음
		400|	The app code is invalid. Please check your app code!	|App code 불일치 또는default app code
		500|	Server error	|cauly서버 에러
		-100|	SDK error	|SDK 에러
		-200|	Request Failed(You are not allowed to send requests under minimum interval)	|최소요청주기 미달

	- 전면광고 플로팅형_ Close Ad Type
		```java
		public class JavaActivity extends Activity implements CaulyCloseAdListener {
				
			private static final String APP_CODE = "CAULY"; // 광고 요청을 위한 App Code
			CaulyCloseAd mCloseAd ;                         // CloseAd광고 객체
				@Override
			    public void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        setContentView(R.layout.activity_java);
			        //CloseAd 초기화 
					CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
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
						if (mCloseAd.isModuleLoaded())
						{
							mCloseAd.show(this);
						} 
						else
						{
						// 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
							showDefaultClosePopup();
						}
						return true;
					}
					return super.onKeyDown(keyCode, event);
				}
			
				private void showDefaultClosePopup()
				{
					new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
					   .setPositiveButton("예", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					     finish();
					    }
					   })
					   .setNegativeButton("아니요",null)
					   .show();
			}
			
		    // CaulyCloseAdListener
			@Override
			public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,String errMsg) {
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

	- 네이티브광고 : BASE
		```java
		public class JavaActivity extends Activity implements CaulyNativeAdViewListener {
		
			private static final String APP_CODE = "CAULY"; // 광고 요청을 위한 App Code
			ArrayList<Item> mList ;
			ListView listview; 
			@Override
			public void onCreate(Bundle savedInstanceState) {
			    super.onCreate(savedInstanceState);
			    setContentView(R.layout.activity_java);
			    // 기존의 ListView-Adaper 구현
			        requestNative()
			}
			// Request Native AD
			// 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
			// CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
			public void requestNative(){
				CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
				.layoutID(R.layout.activity_native_iconlist)// 네이티브애드에 보여질 디자인을 작성하여 등록한다.
				.iconImageID(R.id.icon)       // 아이콘 등록
				.titleID(R.id.title)	        // 제목 등록
				.subtitleID(R.id.subtitle)	// 부제목 등록
		
				.build();
				CaulyNativeAdView nativeAd = new CaulyNativeAdView(this);
				nativeAd.setAdInfo(adInfo);
				nativeAd.setAdViewListener(this);
				nativeAd.request();
				}
			
			
				// 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다. 
				public void onFailedToReceiveNativeAd(CaulyNativeAdView adView,	int errorCode, String errorMsg) {
					
				}
			
				// 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
				public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {
					//우선  앱의 리스트에 등록을 하고, 똑같은 위치의 포지션에 수신한 네이티브애드를 등록한다. 
					mList.add(원하는포지션,null);																									CaulyNativeAdHelper.getInstance().add(this,listview,원하는포지션,adView);
					mAdapter.notifyDataSetChanged();
				}
			
			    // 화면이 종료될 때  Destory()필수 호출 
				@Override
				protected void onDestroy() {
					super.onDestroy();
					CaulyNativeAdHelper.getInstance().destroy();
				}
			}
			
			class ListAdapter extends BaseAdapter 
			{
				private static final int YOUR_ITEM_TYPE = 0;
				private static final int YOUR_ITEM_COUNT = 1;
					
				public int getCount() {
					return mList.size();
				}
			
				public Item getItem(int position) {
					return mList.get(position);
				}
			
				
				// 새로운 네이티브애드 타입이 존재하기 때문에 하나를 등록해준다. 
				@Override
				public int getItemViewType(int position) {
					if(CaulyNativeAdHelper.getInstance().isAdPosition(listview,position))
						return YOUR_ITEM_TYPE+1;
					else 
						return YOUR_ITEM_TYPE;
				}
					
				// 기존의 레이아웃타입 + 1 의 총개수 등록
				@Override
				public int getViewTypeCount() {
					return YOUR_ITEM_COUNT+1;
				}
					
				
				public View getView(int position, View convertView, ViewGroup parent) {
					// CaulyNativeAdHelper를 이용하여, 현재 리스트뷰와 등록한 포지션을 이용하여 , 현재 뷰가 NativeAd인지 아닌지를 반환한다.
					if(CaulyNativeAdHelper.getInstance().isAdPosition(listview, position) )
					{
						return CaulyNativeAdHelper.getInstance().getView(listview,position, convertView);
					}
					else
					{
						// 기존의 getView 코드 구현
					}
				}
		}
		
		Xml – activity_native_iconlist
		<?xml version="1.0" encoding="utf-8"?>
		<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    xmlns:tools="http://schemas.android.com/tools"
		    android:layout_width="fill_parent"
		    android:layout_height="wrap_content"
		    android:background="@drawable/listcolor" >
		
		    <RelativeLayout
		        android:layout_width="fill_parent"
		        android:layout_height="100dp" >
				 <ImageView
		            android:id="@+id/icon"
		            android:layout_width="100dp"
		            android:layout_height="100dp"
		            android:scaleType="fitXY"
		           />
				  
		        <TextView
		            android:id="@+id/title"
		            android:layout_width="fill_parent"
		            android:layout_height="wrap_content"
		            android:layout_marginLeft="114dp"
		            android:layout_marginRight="14dp"
		            android:layout_marginTop="30dp"
		            android:textColor="#000000"
		            android:lines="1"
		            android:textSize="13dp" 
		            />
		         <TextView
		            android:id="@+id/subtitle"
		            android:layout_width="fill_parent"
		            android:layout_height="wrap_content"
		            android:layout_marginLeft="114dp"
		            android:layout_marginRight="14dp"
		            android:layout_marginTop="50dp"
		            android:lines="2"
		            android:textColor="#8a837e"
		            android:textSize="10dp" 
		            />
		         
		          <TextView
		            android:id="@+id/description"
		            android:layout_width="wrap_content"
		            android:layout_height="wrap_content"
		            android:layout_marginRight="14dp"
		            android:layout_alignParentRight="true"
		            android:layout_alignParentBottom="true"
		            android:layout_marginBottom="9dp"
		            android:lines="1"
		            android:textColor="#e15052"
		            android:textSize="15dp" 
		            />
		    </RelativeLayout>
		
		</RelativeLayout>
		```
	- 네이티브광고:Custom.
		1. CaulyAdInfo 생성
		2. CaulyCustomAd 객체 생성후, CaulyAdInfo 적용
		3. CaulyCustomAd의 CaulyCustomAdListener등록
		4. CaulyCustomAd의 JSON 타입의 광고정보 요청
		5. 수신한 JSON Data Format.
		```
			{"ads":[
			      {"id":"광고ID",
			       "ad_charge_type":"0 : 유료광고, 100: 하우스 광고",
			  	   "icon":"아이콘 이미지",  
			       "image":"메인 이미지",  
			       "title":"제목",
			       "subtitle":"부제목",
			       "description":"설명"
			       "linkUrl":랜딩 페이지 URL
			      }
			     ]
			 } 
		```
		6. 소재가 화면에 보여지게 되면   노출로그 전송
			- sendImpressInform(광고ID)
		7. 광고 클릭시, linkUrl 로 이동
			- BrowserUtil.openBrowser(Context, linkUrl)
		8. 파싱방법
			- List<HashMap<KEY,VALUE>> 방식으로 가져오는 경우,<br/>mCaulyAdView.getAdsList();
			- Raw JSON String을 직접가져 경우<br/>mCaulyAdView.getJsonString();
			```java
			CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
				.iconImageID(-1)       // 아이콘 이미지를 사용할 경우  
				.mainImageID(-1)       //메인 이미지를 사용할 경우
				.adRatio("720x720")    //메인이미지 비율설정  안할경우, default: 720x480  or 480x720
				.build();
		
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
				CaulyCustomAd requestAdData(type,  ad_count);
			```
> cauly SDK 설치 관련하여 문의 사항은 고객센터 1544-8867
> 또는 cauly@fsn.co.kr 로 문의 주시면 빠르게 응대해 드리도록 하겠습니다.

Class Reference
=================

CaulyAdInfo[광고 설정 클래스]
------------------------------

CaulyAdInfoBuilder CaulyAdInfo 생성용 클래스||
---|---
CaulyAdInfoBuilder(Context, AttributeSet)|지정한 Context 및 AttributSet으로 CaulyAdInfoBuilder 생성
CaulyAdInfoBuilder(String)	|지정한 App Code로 CaulyAdInfoBuilder 생성
appCode(String)	|App Code 지정
gender(String)	|성별 지정 : “all", “male”, “female”
age(String)	|연령대 지정 : “all”, “age10”, “age20”, “age30”, “age40”, “age50”
effect(String)	|광고 교체 효과 지정 : “None”, “LeftSlide”, “RightSlide”, “TopSlide”, “BottomSlide”, “FadeIn”, “Circle”
dynamicReloadInterval(boolean)	|광고 노출 시간 서버 제어 허용 여부 지정
reloadInterval(int)	|광고 갱신 주기 지정 : min 15, max 120
threadPriority(int)	|스레드 우선 순위 지정
bannerHeight(BannerHeight)	|배너 높이 설정 : Fixed,Fixed_50, Proportional
enableDefaultBannerAd()	|광고 수신 실패 시 카울리 배너 노출 여부 선택
build()	|설정한 정보에 따라 CaulyAdInfo 생성

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
onReceiveAd(CaulyAdView, boolean isChargeableAd)	|광고 노출 성공 시 호출됨. 유,무료 광고 여부가 isChargeableAd 변수에 설정됨
onFailedToReceiveAd(CaulyAdView, int errorCode, String errorMsg)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 errorCode, errorMsg 변수에 설정됨
onShowLandingScreen(CaulyAdView)	|랜딩 페이지가 열린 경우 호출됨
onCloseLandingScreen(CaulyAdView)	|랜딩 페이지가 닫힌 경우 호출됨

전면 광고_풀스크린형
---------------------------------
CaulyInterstitialAd||
---|---
setAdInfo(CaulyAdInfo)	|광고 정보 설정
setInterstialAdListener(CaulyInterstitialAdListener)	|CaulyAdViewListener 지정
showInterstitialAd(Activity)	전면| 광고 요청
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
    setButtonText(String left, String right)|	버튼의 텍스트를 변경 (optional)
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















