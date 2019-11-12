Cauly Android SDK Installation Guide
==============================

- Table Of Contents
	1. Cauly SDK
		- Release note
		- Cautions
		- Recommended environment
		- SDK Components
	
	2. SDK installation guide
	3. Class Reference
		- CaulyAdInfo[Class for ad settings]
		- Logger[Class for logging]
		- Banner-ads
		- Interstitial-ads
		- Interstitial _Floating Type


## Cauly SDK

----------

### Release note
  - The improvement of this version
    - AndroidX support Library

### Cautions
 - In case your app uses the AndroidX library
      ```java
      gradle.properties ::
	* android.useAndroidX=true
	* android.enableJetifier=true
      ```
      참고 : https://developer.android.com/jetpack/androidx/migrate
 - Starting with Android 9.0 (API level 28) In the AndroidManifest
	```java   
 	<application
         android:usesCleartextTraffic="true"
	 />
	```
      
- P/E Ad configurations
	- P/E Ad allowance can be controlled by App-wide. If you want to deliver P/E Ad, please contact the Cauly’s Customer Center. 
	- P/E Ad allowance can also be controlled by AdView object with a following API.
	- setShowPreExpandableAd(boolean)
- Floating Type Interstitial-ad configurations 
	- When KEYCODE_BACK key is pressed on onKeyDown() function, you should call show() method of CloseAd class. Before you call, please verify if the required resources are all downloaded to deliver advertisement with isModuleLoaded() method. In case of download failure, we recommend to implement standard close popup frame which is commonly used in Android. 
	- You should call resume(Activity) method on onResume() method of Activity class.    
- Interstitial-ad process has changed
	- XML-based type for interstitial-ads has removed. You must use JAVA-based type to deliver interstitial-ad.
	- To deliver interstitial-ad properly, you must call either show() or cancel() method of CaulyInterstitialAd class, after successful ad request.
	- show()          // If you want to show a received ad. 
	- cancel()         // If you want to discard a received ad.
- A caution using `<supports-screens>`
		- android:anyDensity=["true”]	// recommend using “true”
		- If this value is false, fixed-height banner may not be displayed properly.

- If you are using proguard, following classes included in cauly SDK must not be obfuscated.
	```java
		-keep public class com.fsn.cauly.** {
		 	   public protected *;
		}
		-keep public class com.trid.tridad.** {
		  	  public protected *;
		}
		-dontwarn android.webkit.**
	
	- Starting with Android 3.4 (gradle build tool 3.4.0)
	
		-keep class com.fsn.cauly.** {
		 	   public *; protected *;
		}
		-keep class com.trid.tridad.** {
		  	  public *; protected *;
		}
		-dontwarn android.webkit.**
	```	

### Recommended environment
	Android 8 (API level 26) or above

### SDK Components
	caulySDK-3.5.x.jar
	CaulyExample project



## SDK installation guide


----------


1. After creating ‘libs’ folder to the project where cauly SDK will be installed, copy the ‘caulySDK-3.3.x.jar’ file.

2. Connect ‘caulySDK-3.3.x.jar’ with the project.  
: ’Properties’ ->  ’javaBuild Path’ -> ’Libraries’ -> ’Add JARs…’ -> ‘caulySDK-3.3.x.jar’



3. ‘AndroidManifest.xml’ configurations [See the ‘CaulyExample’ project for details]
	- Set the configChanges parameter of activity with CaulyAdView
		- Preventing activity restarts when orientation changes, which cause adview resets.
			- If target Api Level is equal or above 13
			 : configChanges="keyboard|keyboardHidden|orientation|screenSize”
			- If target Api Level is below 13
			: configChanges="keyboard|keyboardHidden|orientation”
	- Add mandatory permissions.
	```java
	<activity android:name=".Sample" 
				  android:label="@string/title_activity_java_sample"
			      android:configChanges="keyboard|keyboardHidden|orientation" >
	//mandatory permissions 
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	```

4. After creating ‘attrs.xml’ file at ‘ project -> res -> values’, add the codes below.
	```java
	<declare-styleable name="com.fsn.cauly.CaulyAdView">
		<attr name="appcode" format="string" /> <attr name="effect" format="string" />
		<attr name="dynamicReloadInterval" format="boolean" />
		<attr name="reloadInterval" format="integer" />
		<attr name="threadPriority" format="integer" />
		<attr name="bannerHeight" format="string" /> 
	</declare-styleable>
	```

5. Insert the ad source to the layout where ad wants to be placed.
	(Available in two types: XML-based, JAVA-based)
	- XML-based Type: unset attributes will use default values.
	```xml
	<!-- CPC -->
	<com.fsn.cauly.CaulyAdView
		xmlns:app="http://schemas.android.com/apk/res/[PROJECT PACKAGENAME]"
		android:id="@+id/xmladview"
		android:layout_width="fill_parent"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		app:appcode="CAULY"
		app:effect="RightSlide"
		app:bannerHeight="Proportional"
	/>
	```
	[How to set-up]
	
	| Attrs	| Description |
	|-------|-------------|
	| Appcode | Received code after registering the App. (Use ‘CAULY’ for a test)<br>Other available test app codes: CAULY-RICHADTEST CAULY-PETEST, CAULY-3DTEST" |
	| Effect | LeftSlide(default) : Banner slides from left to right.<br> RightSlide : Banner slides from right to left. <br> TopSlide : Banner slides from up to bottom. <br> BottomSlide : Banner slides from bottom to up. <br> FadeIn : Old banner fades out and new banner fades in. <br> Circle : Banner rolls once. <br> None : Banner changes instantly. <br> |
	| reloadInterval | min : 15 sec.(default)<br>max : 120 sec. |
	| dynamicReloadInterval	| true (default) or false <br> true : Reloading time could be set differently by Ads, thus higher profit would be expected.<br> false : Rolls according to reloadInterval set period. |
	| enableDefaultBannerAd	| false : default banner invisible <br> true : default banner visible |
	| bannerHeight | Proportional(10% of device’s longer edge) or Fixed_50 (default. 50dp) |
	| threadPriority | Priority of threads relating CaulyAdview스 : 1~10(default : 5) |
		
	- JAVA-based Type [See the ‘CaulyExample’ project for details]
		- ‘ res -> layout ->  ‘a name of the place where you want to display ads’.xml 
		```java
		<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
			android:id="@+id/layout"
			android:orientation="vertical"
			android:layout_width="fill_parent"
			android:layout_height="fill_parent">
		</RelativeLayout>
		```
		- Banner-ad
			- src >> ‘user package name’ >> ‘user project activity’.java
			```java
				private CaulyAdView javaAdView;
					
				@Override
				public void onCreate(Bundle savedInstanceState) {
				     super.onCreate(savedInstanceState);
				     setContentView(R.layout.activity_java);
					// Set Cauly log level.
				    // LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
					Logger.setLogLevel(LogLevel.Info);
					    
					// Build CaulyAdInfo
					CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).
							     effect("RightSlide").
					             bannerHeight("Proportional").
					             build();
					
					// Create CaulyAdView with CaulyAdInfo.
					javaAdView = new CaulyAdView(this);
					javaAdView.setAdInfo(adInfo);
					javaAdView.setAdViewListener(this);
					
					RelativeLayout rootView = (RelativeLayout) findViewById(R.id.java_root_view);
					// Attach at a bottom of activity.
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						     LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					rootView.addView(javaAdView, params);		
			    }
			    // CaulyAdViewListener
			    //	If you don’t want any special control on ads delivery,
			    //	you can omit follows with deletion of Activity’s "implements CaulyAdViewListener
				@Override
				public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
					// Called when an ad is received and displayed.
					// If a received ad is free ad, the value of isChargeableAd will be false.
					if (isChargeableAd == false) {
					Log.d("CaulyExample", "free banner AD received.");
					}
					else {
						Log.d("CaulyExample", "normal banner AD received.");
					}
				}
				@Override
				public void onFailedToReceiveAd(CaulyAdView adView, int errorCode, String errorMsg){
					// Called when failed to receive an ad.
					Log.d("CaulyExample", "failed to receive banner AD.");
				}
				
				@Override
				public void onShowLandingScreen(CaulyAdView adView) {
					// Called when banner-ad is clicked and the landing page is opened.
					Log.d("CaulyExample", "banner AD landing screen opened.");
				}    
				
				@Override
				public void onCloseLandingScreen(CaulyAdView adView) {
					// Called when the landing page is closed.
					Log.d("CaulyExample", "banner AD landing screen closed.");
				}	
			```
	
			[How to set-up CaulyAdinfo]
			
			| Adinfo | Description |
			| ------- | -------------- |
			|	Appcode	| Received code after registering the App. (Use ‘CAULY’ for a test)<br>Other available test app codes: CAULY-RICHADTEST CAULY-PETEST, CAULY-3DTEST |
			| Effect() | LeftSlide(default) : Banner slides from left to right.<br>RightSlide : Banner slides from right to left.<br>TopSlide : Banner slides from up to bottom.<br>BottomSlide : Banner slides from bottom to up.<br>FadeIn : Old banner fades out and new banner fades in. <br>Circle : Banner rolls once.<br>None : Banner changes instantly.|
			| reloadInterval() | min : 15 sec.(default)<br>max : 120 sec. |
			| dynamicReloadInterval() | true (default) or false<br>true : Reloading time could be set differently by Ads, thus higher profit would be expected.<br>false : Rolls according to reloadInterval set period. |
			| enableDefaultBannerAd | false : default banner invisible<br>true : default banner visible |
			| bannerHeight() | Proportional(10% of device’s longer edge) or  Fixed_50(50 dp) |
			| threadPriority() | Priority of threads relating CaulyAdview: 1~10(default : 5) |
			| disableDefaultBannerAd() | Disable default banner ad. |

		- Interstitial-ad
		```java
		// - Interstitial-ad request button
		public void onRequestInterstitial(View button) {
		
		// Build CaulyAdInfo
		CaulyAdInfo adInfo = new CaulyAdInfoBuilder(APP_CODE).build();
		
		// Create interstitial-ad
		CaulyInterstitialAd interstial = new CaulyInterstitialAd();
		interstial.setAdInfo(adInfo);
		interstial.setInterstialAdListener(this);
		
		// Request interstitial-ad. Actual impress will happens at onReceiveInterstitialAd of CaulyInterstitialAdListener.
		interstial. requestInterstitialAd (this);
		}
		
		  // CaulyInterstitialAdListener
		  // For interstitial-ad will not be shown automatically, you must call show() method at onReceiveInterstitialAd to show ads.
		
		@Override
		public void onReceiveInterstitialAd(CaulyInterstitialAd ad, boolean isChargeableAd) {
			// Called when an interstitial-ad is received.
			// If a received ad is free ad, the value of isChargeableAd will be false.
			if (isChargeableAd == false) {
				Log.d("CaulyExample", "free interstitial AD received.");
			}
			else {
			Log.d("CaulyExample", "normal interstitial AD received.");
			}		
			      ad.show();
		}	
		@Override
		public void onFailedToReceiveInterstitialAd(CaulyInterstitialAd ad, int errorCode, String errorMsg) {
			// Called when failed to receive an interstitial-ad.
			Log.d("CaulyExample", "failed to receive interstitial AD.");
			}
			@Override
			public void onClosedInterstitialAd(CaulyInterstitialAd ad) {
			// Called when an interstitial-ad is closed.
			Log.d("CaulyExample", "interstitial AD closed.");
			}
		}
		```
		[Error code definition]
		
		| Code | Message | Description |
		| ------ | ---------- | --------------|
		| 0 | OK | Paid AD |
		| 100 |	Non-chargeable ad is supplied |	Free AD (Public service ads, cauly’s basic ads) |
		| 200 |	No filled AD	| No proper ad is available. |
		| 400 |	The app code is invalid. Please check your app code! |	Discordance of app code or default app code. |
		| 500 |	Server error	| Cauly server error |
		| -100 |	SDK error	| SDK error |
		| -200 |	Request Failed(You are not allowed to send requests under minimum interval) | Minimum request interval has not passed. |
	
		- Floating Type Interstitial Ad 
			```java
			 
			public class JavaActivity extends Activity implements CaulyCloseAdListener {
					
				private static final String APP_CODE = "CAULY"; // App Code
			CaulyCloseAd mCloseAd ; // CaulyCloseAd Object variable 
				@Override
			    public void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        setContentView(R.layout.activity_java);

			        //CloseAd initiation
					CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
					mCloseAd = new CaulyCloseAd();
					mCloseAd.setAdInfo(closeAdInfo);
					mCloseAd.setCloseAdListener(this); 
					
		    	}
			    
				@Override
				protected void onResume() {
					super.onResume();
					if (mCloseAd != null)
						mCloseAd.resume(this); // Required
				}
			
				// when Back key is pressed, show CloseAd 
				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						// check if all resources are downloaded and ready to deliver advertisement when you initially request CloseAd
						if (mCloseAd.isModuleLoaded()){
							mCloseAd.show(this);
						} else {
							// You would need your own close popup to show, in case of download failure
							showDefaultClosePopup();
						}
						return true;
					}
					return super.onKeyDown(keyCode, event);
				}
			
				private void showDefaultClosePopup()
				{
					new AlertDialog.Builder(this).setTitle("").setMessage("finish?")
					   .setPositiveButton("YES", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					     finish();
					    }
					   })
					   .setNegativeButton("cancel",null)
					   .show();
				}
			
			
			    // CaulyCloseAdListener
				@Override
				public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,String errMsg) {
					
				}
				@Override
				public void onLeaveCloseAd(CaulyCloseAd ad) {
				}
				@Override
				public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {
				
				}	
				 
				@Override
				public void onLeftClicked(CaulyCloseAd ad) {
					
				}	
				 
				 
				@Override
				public void onRightClicked(CaulyCloseAd ad) {
					finish();
				}
				@Override
				public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {
					
				}
			}
			```

	- Native Ad : BASE
		```java
		public class JavaActivity extends Activity implements CaulyNativeAdViewListener {
		
			private static final String APP_CODE = "CAULY"; //App Code for Ad-Request
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
			// Define your own layout of NativeAd and assign layout id  (icon, image, title, subtitle, description ...)
			// register CaulyNativeAdViewListener and will know of the state of ad(onReceiveNativeAd or onFailedToReceiveNativeAd)
			public void requestNative(){
				CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
				.layoutID(R.layout.activity_native_iconlist)//assign layout id for NativeAd.
				.iconImageID(R.id.icon)       // assign id of icon image	
				.titleID(R.id.title)	      // assign id of title 
				.subtitleID(R.id.subtitle)    // assign id of subtitle
				.build();
				CaulyNativeAdView nativeAd = new CaulyNativeAdView(this);
				nativeAd.setAdInfo(adInfo);
				nativeAd.setAdViewListener(this);
				nativeAd.request();
				}
			
			
				// Called when NO-AD and Network is not available. 
				public void onFailedToReceiveNativeAd(CaulyNativeAdView adView,	int errorCode, String errorMsg) {
					
				}
			
				// Called when CaulyNativeAd is received.
				public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {
					//Add to your contents list and add the nativead to CaulyNativeAdHelper at the same position as yours. 
					mList.add(position that you want,null);																									CaulyNativeAdHelper.getInstance().add(this,listview,position that you want,adView);
					mAdapter.notifyDataSetChanged();
				}
			
			    // call destroy() when activity destroy 
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
			
				
				// Add one more type of listview  
				@Override
				public int getItemViewType(int position) {
					if(CaulyNativeAdHelper.getInstance().isAdPosition(listview,position))
						return YOUR_ITEM_TYPE+1;
					else 
						return YOUR_ITEM_TYPE;
				}
					
				// return Your LayoutType +1 
				@Override
				public int getViewTypeCount() {
					return YOUR_ITEM_COUNT+1;
				}
					
				
				public View getView(int position, View convertView, ViewGroup parent) {
					// Tell if the view is CaulyNativeAd of not.
					if(CaulyNativeAdHelper.getInstance().isAdPosition(listview, position) )
					{
						return CaulyNativeAdHelper.getInstance().getView(listview,position, convertView);
					}
					else
					{
						//  your original getView 
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
	- Native Ad :Custom.
		1. Create CaulyAdInfo 
		2. Create CaulyCustomAd and set CaulyAdInfo 
		3. register CaulyCustomAdListener to CaulyCustomAd
		4. Request JsonType of Ad to CaulyCustomAd
		5. Received JSON Data Format is like below.
		```
			{"ads":[
			      {"id":"AD ID",
			       "ad_charge_type":"0 : Chargable ad, 100: HouseAd",
			       "icon":"icon image",  //1 : 1 proportion
			       "image":"main image",  // 3 : 2  or 2 : 3 
			       "title":"title",
			       "subtitle":"subtitle",
			       "description":"description"
			       "linkUrl":Landing Page URL
			      }
			     ]
			 } 
		```
		6. When CaulyNativeAd is shown to screen, call sendImpressInform
			- sendImpressInform(AD ID)
		7. When Clicked, Move linkUrl to Internet Browser
			- BrowserUtil.openBrowser(Context, linkUrl)
		8. Parse Json String of Ad
			- In case of getting list by List<HashMap<KEY,VALUE>> ,mCaulyAdView.getAdsList();
			- In case of getting Raw JSON String , mCaulyAdView.getJsonString();
			```java
			CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE).build();
				mCaulyAdView = new CaulyCustomAd(this);
				mCaulyAdView.setAdInfo(adInfo);
				mCaulyAdView.setCustomAdListener(new CaulyCustomAdListener() {
					@Override
					public void onShowedAd() {
					}
			
					// Called when AD Received. 
					@Override
					public void onLoadedAd(boolean isChargeableAd) {
					}
	
					// Called when Ad is failed to receive.
					@Override
					public void onFailedAd(int errCode, String errMsg) {
					}
	
					// Called when ad is clicked.
					@Override
					public void onClikedAd() {
					}
	
				});
			// CaulyCustomAd.INTERSTITIAL_PORTRAIT,CaulyCustomAd.NATIVE_PORTRAIT,CaulyCustomAd.NATIVE_LANDSCAPE
				CaulyCustomAd requestAdData(type,  ad_count);
			```

If you need more informations to install cauly SDK, please give us a call to the customer center +82-1544-8867 or send an e-mail to cauly@futurestream.co.kr.

## Class Reference
### CaulyAdInfo[Class for ad settings] 
| CaulyAdInfoBuilder[CaulyAdInfo builder class]	||
| --------------------------------------------- | --------- |
| CaulyAdInfoBuilder(Context, AttributeSet)	| Create CaulyAdInfoBuilder with given Context and AttributeSet. |
| CaulyAdInfoBuilder(String) | Create CaulyAdInfoBuilder with given App Code. |
| appCode(String) | Set App Code. |
| effect(String) | Set ad change effect: “None”, “LeftSlide”, “RightSlide”, “TopSlide”, “BottomSlide”, “FadeIn”, “Circle” |
| dynamicReloadInterval(boolean)| Set server controlled reload-time. |
| reloadInterval(int) | Set reload-interval: min 15, max 120 (sec.) |
| threadPriority(int) | Set thread priority. |
| bannerHeight(BannerHeight) | Set banner height: Fixed_50, Proportional |
| enableDefaultBannerAd () | Disable default banner Ad. |
| build() | Create CaulyAdInfo with given parameters |

### Logger[Class for logging]
| Data types ||
| ---------- | ------------ |
| LogLevel | Log level enumeration type: Info, Warn, Error, None |

| Method ||
| ---------- | ------------ |
| setLogLevel(LogLevel) | Set log level. |
| getLogLevel()	| Get current log level. |

### Banner-ads	
| CaulyAdView[Ad-view class]	||
| --------------------------------- | ---------------------------------------- |
| setAdInfo(CaulyAdInfo) | Set ad settings. |
| setAdViewListener(CaulyAdViewListener) | Set CaulyAdViewListener. |
| setShowPreExpandableAd(boolean)	Set P/E Ad allowance.	 |
| reload()	| Resend ad-request. |
| pause()	| Pause ad-request. |
| resume()	| Resume ad-request. |
| destroy()	| Destroy an ad-view.	 |

| CaulyAdViewListener	 ||
| --------------------------------- | ---------------------------------------- |
| onReceiveAd(CaulyAdView, boolean isChargeableAd) | Called when an ad is received and displayed. isChargeableAd parameter indicates whether received ad is free or not. |
| onFailedToReceiveAd(CaulyAdView, int errorCode, String errorMsg) | Called when failed to receive an ad. errorCode, errorMsg shows error details. |
| onShowLandingScreen(CaulyAdView) | Called when the landing page is opened. |
| onCloseLandingScreen(CaulyAdView) | Called when the landing page is closed. |

### Interstitial-ads	

| CaulyInterstitialAd	 ||
| --------------------------------- | ---------------------------------------- |
| setAdInfo(CaulyAdInfo) | Set ad settings. |
| setInterstialAdListener(CaulyInterstitialAdListener) | Set CaulyAdViewListener. |
| requestInterstitialAd (Activity) | Request an interstitial-ad. |
| show() | Show a received interstitial-ad. |
| cancel() | Discard a received interstitial-ad. |

| CaulyInterstitialAdListener ||
| --------------------------------- | ---------------------------------------- |
| onReceiveInterstitialAd(CaulyInterstitialAd, boolean isChargeableAd) | Called when an interstitial-ad is received. isChargeableAd parameter indicates whether received ad is free or not. |
| onFailedToReceiveInterstitialAd(CaulyInterstitialAd, int errorCode, String errorMsg) | Called when failed to receive an interstitial-ad. errorCode, errorMsg shows error details. |
| onClosedInterstitialAd(CaulyInterstitialAd) | Called when an interstitial-ad is closed. |

### Interstitial _Floating Type	
| CaulyCloseAd ||
| --------------------------------- | ---------------------------------------- |
| setAdInfo(CaulyAdInfo) | Set ad settings. |
| setCloseAdListener(CaulyCloseAdListener) | Set CaulyCloseAdListener |
| Boolean isModuleLoaded() | Returns readiness of Advertisement |
| resume(Activity) | Send a status of activity |
| request (Activity) | Request an interstitial-ad.(optional) |
| show(Activity) | Show a received Ad(required) |
| setButtonText(String left, String right) | Change the Button Text (optional) |
| setDescriptionText(String) | Change the description (optional)
| cancel() | Discard a received ad |

### CaulyCloseAd	
| CaulyCloseAdListener ||
| --------------------------------- | ---------------------------------------- |
| onReceiveCloseAd(CaulyCloseAd, boolean isChargeableAd) | Called when an interstitial-ad is received. isChargeableAd parameter indicates whether received ad is free or not. |
| onFailedToReceiveCloseAd(CaulyCloseAd, int errorCode, String errorMsg) | Called when failed to receive an ad. errorCode, errorMsg shows error details. |
| onLeaveCloseAd(CaulyInterstitialAd) | Called when Activity moves to Ad landing page on clicking ad-content |
| onShowedClosedAd(CaulyCloseAd, boolean isChargable) | Called when CloseAd is successfully shown |
| onLeftClicked(CaulyCloseAd) | Called when left button is clicked |
| onRightClicked(CaulyCloseAd) | Called when right button is clicked |

### CaulyNativeAd	
|CaulyNativeAd||
| --------------------------------- | ---------------------------------------- |
setAdInfo(CaulyAdInfo)	|Set ad settings.|
setAdViewListener(CaulyNativeAdViewListener)	|Set CaulyNativeAdViewListener |
request (Activity)	|Request CaulyNativeAD |
attachToView(ViewGroup)	|Attach CaulyNativeAdView to Your ViewGroup. |
isAttachedtoView()	|Indicate if CaulyNativeAd is attached to ViewGroup|
destroy()	|Destroy a recieved ad|

|CaulyNativeAdHelper(Helper of showing CaulyNativeAD to ListView)||
|---|---|
|getInstance()	|Singleton Object|
|init()|	Initiation|
|add(Context, ViewGroup, int, CaulyNativeAdView)	| Add CaulyNativeAd to Helper.|
|remove(ViewGroup, int)	|Remove CaulyNativeAd of Helper . |
|isAdPosiont(ViewGroup, int)	| Indicate if The Helper contains the NativeAd ad the position |
|getView(ViewGroup,int,convertView)	|return the CaulyNativeAdView |
|getSize(ViewGroup)	| return total size of NativeAds in Helper.|
|destroy()	|destroy all NativeAd of Helper (necessary)|

|CaulyNativeAdViewListener||
|---|---|
|onReceiveNativeAd(CaulyNativeAd, boolean isChargeableAd)	|Called when ad received, isChargeableAd indicate whether the received ad is free or not.|
|onFailedToReceiveNativeAd(CaulyNativeAd, int errorCode, String errorMsg)	|Called when failed to receive an ad. errorCode, errorMsg shows error details.|

