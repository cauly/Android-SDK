package kr.co.cauly.sdk.android.mediation.sample.natives;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyCustomAd;
import com.fsn.cauly.CaulyCustomAdListener;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationNativeAdCallback;
import com.google.android.gms.ads.mediation.MediationNativeAdConfiguration;
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import kr.co.cauly.sdk.android.mediation.sample.Config;
import kr.co.cauly.sdk.android.mediation.sample.R;

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

//    @Override
//    public void onDestroy() {
//        if (mCaulyAdView != null) {
//            mCaulyAdView = null;
//
//        }
//
//    }
//
//    @Override
//    public void onPause() {
//
//    }
//
//    @Override
//    public void onResume() {
//
//    }
}
