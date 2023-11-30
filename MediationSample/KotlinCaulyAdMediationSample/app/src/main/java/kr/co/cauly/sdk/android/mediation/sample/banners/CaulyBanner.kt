package kr.co.cauly.sdk.android.mediation.sample.banners

import android.content.Context
import com.google.android.gms.ads.mediation.*

class CaulyBanner : Adapter() {
    private var bannerLoader: CaulyBannerLoader? = null

    override fun loadBannerAd(
        mediationBannerAdConfiguration: MediationBannerAdConfiguration,
        callback: MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback>
    ) {
        bannerLoader = CaulyBannerLoader(mediationBannerAdConfiguration, callback)
        bannerLoader!!.loadAd()
    }

    // This method won't be called for custom events.
    override fun initialize(
        context: Context,
        initializationCompleteCallback: InitializationCompleteCallback,
        list: List<MediationConfiguration>
    ) {
        initializationCompleteCallback.onInitializationSucceeded()
    }

    override fun getVersionInfo(): VersionInfo {
        val versionString = "1.0.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 4) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt() * 100 + splits[3].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }

    override fun getSDKVersionInfo(): VersionInfo {
        val versionString = "1.0.0"
        val splits = versionString.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (splits.size >= 3) {
            val major = splits[0].toInt()
            val minor = splits[1].toInt()
            val micro = splits[2].toInt()
            return VersionInfo(major, minor, micro)
        }
        return VersionInfo(0, 0, 0)
    }
}
