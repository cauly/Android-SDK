package kr.co.cauly.sdk.android.mediation.sample.natives

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import com.fsn.cauly.CaulyCustomAd
import com.fsn.cauly.CaulyCustomAdListener
import com.fsn.cauly.CaulyNativeAdInfoBuilder
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.mediation.MediationAdLoadCallback
import com.google.android.gms.ads.mediation.MediationConfiguration
import com.google.android.gms.ads.mediation.MediationNativeAdCallback
import com.google.android.gms.ads.mediation.MediationNativeAdConfiguration
import com.google.android.gms.ads.mediation.NativeAdMapper
import kr.co.cauly.sdk.android.mediation.sample.R
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.jvm.Throws

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