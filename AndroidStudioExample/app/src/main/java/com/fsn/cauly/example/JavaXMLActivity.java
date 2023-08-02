package com.fsn.cauly.example;

import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class JavaXMLActivity extends Activity implements CaulyAdViewListener {

	// Java Activity 전환 버튼
	public void switchToJavaActivity(View button) {
		finish();
	}

	//////////////////////////////////////////////////////////////////
	//
	// XML 기반 배너 광고
	//
	// - 이하 Java 코드는 CaulyAdView 상태에 따라 제어하기 위해 필요한 것으로,
	//    제어 필요가 없다면, 추가할 필요가 없으며,
	//    XML 파일 설정만으로 광고를 노출할 수 있다.
	//
	//////////////////////////////////////////////////////////////////

	private CaulyAdView xmlAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_xml);
       
        // 선택사항: XML의 AdView 항목을 찾아 Listener 설정
        xmlAdView = (CaulyAdView) findViewById(R.id.xmladview);
        xmlAdView.setAdViewListener(this);
    }

    // Activity 버튼 처리
	// - XML 배너 갱신
	public void onReloadJavaAdView(View button) {
		xmlAdView.reload();
	}
	
    // 선택사항: CaulyAdViewListener
    //	광고 동작에 대해 별도 처리가 필요 없는 경우,
    //	Activity의 "implements CaulyAdViewListener" 부분 제거하고 생략 가능.

	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
		// 광고 수신 성공 & 노출된 경우 호출됨.
		// 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
		if (isChargeableAd == false) {
			Log.d("CaulyExample", "free banner AD received.");
		}
		else {
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

	
}
