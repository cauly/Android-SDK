package kr.co.cauly.sdk.android.mediation.sample.natives

import android.graphics.drawable.Drawable
import android.net.Uri
import com.google.android.gms.ads.nativead.NativeAd

class SampleNativeMappedImage(
    private val drawable: Drawable,
    private val uri: Uri,
    private val scale: Double
) : NativeAd.Image() {

    override fun getDrawable(): Drawable {
        return drawable
    }

    override fun getUri(): Uri {
        return uri
    }

    override fun getScale(): Double {
        return scale
    }
}