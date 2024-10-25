package kr.co.cauly.sdk.android.mediation.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

private const val LOG_TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initializeUMP()
    }

    /** Start the MainActivity. */
    fun startMainActivity() {
        val application = application as MyApplication
        application.isSplash = true

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun initializeUMP() {
        // Debug Setting
        // 테스트 시 ConsentDebugSettings.Builder().TestDeviceHashedIds() 를 호출하고 테스트 기기 ID 목록을 전달하도록 수정합니다.
        // 상용화 시 반드시 삭제해야합니다.
        val debugSettings = ConsentDebugSettings.Builder(this)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId("33BE2250B43518CCDA7DE426D04EE231")
            .build()

        // Create a ConsentRequestParameters object.
        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)     // Debug Setting
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            ConsentInformation.OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        if (loadAndShowError != null) {
                            Log.w(
                                LOG_TAG, String.format("%s: %s",
                                    loadAndShowError.errorCode,
                                    loadAndShowError.message))
                        }

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            ConsentInformation.OnConsentInfoUpdateFailureListener {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w(
                    LOG_TAG, String.format("%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message))
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(
                this@SplashActivity
            ) { initializationStatus ->
                val statusMap = initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.i(
                        LOG_TAG, String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
            }
            runOnUiThread {
                // TODO: Request an ad.
                val application = application as MyApplication
                application.loadAd(this@SplashActivity, object : MyApplication.OnLoadAdCompleteListener {
                    override fun onAdLoaded() {
                        Log.d(LOG_TAG, "onAdLoaded")
                        application.showAdIfAvailable(this@SplashActivity, object : MyApplication.OnShowAdCompleteListener {
                            override fun onShowAdComplete() {
                                startMainActivity()
                            }
                        })
                    }

                    override fun onAdFailedToLoad() {
                        Log.d(LOG_TAG, "onAdFailedToLoad")
                        startMainActivity()
                    }
                })
            }
        }

    }
}