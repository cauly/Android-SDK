package kr.co.cauly.sdk.android.mediation.sample.natives

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.google.android.gms.ads.mediation.NativeAdMapper
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import com.google.android.gms.ads.nativead.NativeAd
import com.kakao.adfit.ads.na.AdFitNativeAdLayout
import kr.co.cauly.sdk.android.mediation.sample.databinding.ItemNativeAdBinding
import java.io.ByteArrayOutputStream

class SampleUnifiedNativeAdMapper: NativeAdMapper {
    var context: Context? = null

    constructor(context: Context, nativeAdView: ItemNativeAdBinding) {
        this.context = context

        if (context == null) {
            Log.e("AdfitNative", "Failed to load ad. Request must be for unified native ads.")
            return
        }

        setMediaView(nativeAdView.mediaView)
        icon = SampleNativeMappedImage(nativeAdView.profileIconView.drawable, getUri(context, (nativeAdView.profileIconView.drawable as BitmapDrawable).bitmap), 1.0)
        headline = nativeAdView.titleTextView.text as String
        body = nativeAdView.bodyTextView.text as String
        callToAction = nativeAdView.callToActionButton.text as String
        advertiser = nativeAdView.profileNameTextView.text as String


        overrideImpressionRecording = false
        overrideImpressionRecording = false
    }

    private fun getUri(context: Context, bitmap: Bitmap): Uri {
        var bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "icon", null)
        return Uri.parse(path)
    }
}