package kr.co.cauly.sdk.android.mediation.sample.interstitials

import android.content.Context
import com.google.android.gms.ads.mediation.*

class CaulyInterstitial : Adapter() {
    private var interstitialLoader: CaulyInterstitialLoader? = null
    override fun loadInterstitialAd(
        mediationInterstitialAdConfiguration: MediationInterstitialAdConfiguration,
        callback: MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback>
    ) {
        interstitialLoader = CaulyInterstitialLoader(mediationInterstitialAdConfiguration, callback)
        interstitialLoader!!.loadAd()
    }

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
