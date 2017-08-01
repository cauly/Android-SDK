package com.fsn.cauly.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfo.Direction;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.CaulyNativeAdView;
import com.fsn.cauly.CaulyNativeAdViewListener;

public class NativeViewActivity extends Activity implements CaulyNativeAdViewListener  {

	String APP_CODE="CAULY";// your app code which you are assigned.
	ViewGroup native_container;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        native_container = (ViewGroup) findViewById(R.id.native_container);
		showNative();
    }
	
	// Request Native AD
	// 네이티브 애드에 보여질 디자인을 정의하고 세팅하는 작업을 수행한다. (icon, image, title, subtitle, description ...)
	// CaulyNativeAdViewListener 를 등록하여 onReceiveNativeAd or onFailedToReceiveNativeAd 로 네이티브광고의 상태를 전달받는다.
	public void showNative()
	{
		CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
		.layoutID(R.layout.activity_native_view)
		.iconImageID(R.id.icon)
		.titleID(R.id.title)
		.subtitleID(R.id.subtitle)
		.sponsorPosition(R.id.sponsor, Direction.CENTER)
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
		adView.attachToView(native_container);  //지정된 위치에 adView를 붙인다.
	}
}
