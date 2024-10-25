package kr.co.cauly.sdk.android.mediation.sample.natives

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import com.fsn.cauly.CaulyBrowserUtil
import com.fsn.cauly.CaulyCustomAd
import com.google.android.gms.ads.mediation.NativeAdMapper
import com.google.android.gms.ads.nativead.NativeAd

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