package com.fsn.cauly.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyMediaView;
import com.fsn.cauly.CaulyNativeAd;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.CaulyNativeAdListener;
import com.fsn.cauly.CaulyNativeAdLoader;
import com.fsn.cauly.CaulyNativeAdView;
import com.fsn.cauly.CaulyNativeTemplateType;
import com.fsn.cauly.CaulyNativeTemplateView;

/**
 * 네이티브 V2 테스트
 * <p>
 * 1. CaulyNativeAdLoader 로 광고를 요청한다.
 * 2. onAdLoaded() 로 받은 CaulyNativeAd 를 화면에 연결한다.
 *    - 템플릿: CaulyNativeTemplateView.setNativeAd() 만 호출하면 SDK 가 배치·노출·클릭을 처리한다.
 *    - 커스텀 레이아웃: 매체 레이아웃의 뷰를 CaulyNativeAdView 에 등록한 뒤 setNativeAd() 를 호출한다.
 * 3. 광고 교체·화면 종료 시 loader.cancel(), 뷰와 광고의 destroy() 로 정리한다.
 */
public class NativeActivity extends Activity {

    private static final String TAG = "NativeActivity";
    private static final String APP_CODE = "CAULY"; // 발급받은 APP_CODE

    private RadioGroup displayGroup;
    private FrameLayout container;
    private TextView logView;

    private CaulyNativeAdLoader loader;
    private CaulyNativeAd currentAd;
    private CaulyNativeAdView currentAdView;
    private CaulyNativeTemplateView currentTemplateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        displayGroup = findViewById(R.id.radio_display);
        container = findViewById(R.id.native_ad_container);
        logView = findViewById(R.id.text_native_log);
        findViewById(R.id.btn_request_native).setOnClickListener(v -> requestAd());
    }

    private void requestAd() {
        CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE).build();

        if (loader != null)
            loader.cancel();
        loader = new CaulyNativeAdLoader(this);
        loader.setAdInfo(adInfo);
        loader.setAdListener(new CaulyNativeAdListener() {
            @Override
            public void onAdLoaded(CaulyNativeAd ad) {
                if (isFinishing()) {
                    ad.destroy();
                    return;
                }
                log("onAdLoaded chargeable=" + ad.isChargeable() + " headline=" + ad.getHeadline());
                showAd(ad);
            }

            @Override
            public void onAdFailedToLoad(int errorCode, String errorMessage) {
                log("onAdFailedToLoad " + errorCode + " " + errorMessage);
            }

            @Override
            public void onAdImpression() {
                log("onAdImpression");
            }

            @Override
            public void onAdClicked() {
                log("onAdClicked");
            }
        });
        log("request");
        loader.request();
    }

    private void showAd(CaulyNativeAd ad) {
        releaseAd();
        currentAd = ad;

        int displayId = displayGroup.getCheckedRadioButtonId();
        if (displayId == R.id.radio_display_small || displayId == R.id.radio_display_medium) {
            // 템플릿: 레이아웃·뷰 등록 없이 setNativeAd() 만 호출한다.
            CaulyNativeTemplateView template = new CaulyNativeTemplateView(this,
                    displayId == R.id.radio_display_small ? CaulyNativeTemplateType.SMALL : CaulyNativeTemplateType.MEDIUM);
            template.setNativeAd(ad);
            container.addView(template);
            currentTemplateView = template;
            return;
        }

        // 커스텀 레이아웃: 매체 레이아웃의 뷰를 CaulyNativeAdView 에 등록한 뒤 광고를 연결한다.
        CaulyNativeAdView adView = (CaulyNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, container, false);
        TextView bodyView = adView.findViewById(R.id.ad_body);
        ImageView iconView = adView.findViewById(R.id.ad_icon);
        CaulyMediaView mediaView = adView.findViewById(R.id.ad_media);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setCallToActionView(adView.findViewById(R.id.ad_cta));
        adView.setBodyView(bodyView);
        adView.setIconView(iconView);
        adView.setMediaView(mediaView);
        adView.setAdBadgeView(adView.findViewById(R.id.ad_badge));

        // 본문·아이콘·메인 이미지는 없을 수 있다.
        if (ad.getBody() == null || ad.getBody().isEmpty())
            bodyView.setVisibility(View.GONE);
        if (ad.getIcon() == null)
            iconView.setVisibility(View.GONE);
        if (ad.getMediaContent() == null)
            mediaView.setVisibility(View.GONE);

        adView.setNativeAd(ad);
        container.addView(adView);
        currentAdView = adView;
    }

    // 광고 교체·화면 종료 시 뷰와 광고를 모두 해제한다.
    private void releaseAd() {
        if (currentTemplateView != null) {
            currentTemplateView.destroy();
            currentTemplateView = null;
        }
        if (currentAdView != null) {
            currentAdView.destroy();
            currentAdView = null;
        }
        container.removeAllViews();
        if (currentAd != null) {
            currentAd.destroy();
            currentAd = null;
        }
    }

    private void log(String message) {
        Log.d(TAG, message);
        logView.setText(message + "\n" + logView.getText());
    }

    @Override
    protected void onDestroy() {
        if (loader != null)
            loader.cancel();
        releaseAd();
        super.onDestroy();
    }
}
