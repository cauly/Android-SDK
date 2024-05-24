package kr.co.cauly.sdk.android.mediation.sample

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.AppLovinExtras
import com.applovin.mediation.ApplovinAdapter
import com.fyber.inneractive.sdk.external.InneractiveMediationDefs
import com.google.ads.mediation.fyber.FyberMediationAdapter
import com.google.ads.mediation.inmobi.InMobiAdapter
import com.google.ads.mediation.inmobi.InMobiNetworkKeys
import com.google.ads.mediation.inmobi.InMobiNetworkValues
import com.google.ads.mediation.unity.UnityAdapter
import com.google.ads.mediation.unity.UnityAdsAdapterUtils
import com.google.ads.mediation.vungle.VungleConstants
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.inmobi.sdk.InMobiSdk
import com.vungle.mediation.VungleAdapter
import com.vungle.mediation.VungleInterstitialAdapter
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private var adRequest: AdRequest? = null
    private var adView: AdView? = null

    private var mInterstitialAd: InterstitialAd? = null

    lateinit var show_interstitial_btn: Button

    private var nativeAd: NativeAd? = null
    lateinit var native_btn: Button

    private var mRewardedAd: RewardedAd? = null
    lateinit var show_rewarded_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(
            this
        ) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.i(
                    TAG, String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }
        }
        //admob 레이아웃에 AdView 추가
        adView = findViewById<View>(R.id.adView) as AdView
        //admob 배너 리스너
        setAdmobBannerListener()
        //admob adRequest
        setAdmobAdRequest()

        //배너 요청
        val banner_btn = findViewById<Button>(R.id.banner_btn)
        banner_btn.setOnClickListener(View.OnClickListener {
            //배너초기화
            loadAdmobBannerAd()
        })

        //전면 요청
        val request_interstitial_btn = findViewById<Button>(R.id.request_interstitial_btn)
        request_interstitial_btn.setOnClickListener(View.OnClickListener {
            loadInterstitialAd()
        })


        // 전면 노출
        show_interstitial_btn = findViewById(R.id.show_interstitial_btn)
        show_interstitial_btn.setEnabled(false)
        show_interstitial_btn.setOnClickListener(View.OnClickListener {
            showInterstitialAd()
        })

        //네이티브 요청
        native_btn = findViewById<Button>(R.id.native_btn)
        native_btn.setOnClickListener(View.OnClickListener {
            loadAdmobNativeAd()
        })


        // 보상형 광고 요청
        val request_rewarded_btn = findViewById<Button>(R.id.request_rewarded_btn)
        request_rewarded_btn.setOnClickListener(View.OnClickListener {
            loadRewardedAd()
        })

        show_rewarded_btn = findViewById<Button>(R.id.show_rewarded_btn)
        show_rewarded_btn.setEnabled(false)
        show_rewarded_btn.setOnClickListener(View.OnClickListener {
            showRewardedAd()
        })
    }

    private fun setAdmobAdRequest() {
        // 테스트 시 RequestConfiguration.Builder.setTestDeviceIds()를 호출하고 테스트 기기 ID 목록을 전달하도록 수정해야 합니다.
        // 상용화 시 반드시 삭제해야합니다.
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList<String>(Config().ADMOB_TEST_DEVICE_ID))
                .build()
        )

        // inmobi setting
        val inMobiextras = Bundle()
        //inmobi size 320x50 adsize banner
        inMobiextras.putString(InMobiNetworkKeys.AGE_GROUP, InMobiNetworkValues.BETWEEN_25_AND_29)
        inMobiextras.putString(InMobiNetworkKeys.AREA_CODE, "12345")
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)

        // appLovin setting
        val appLovinExtras = AppLovinExtras.Builder()
            .setMuteAudio(true)
            .build()

        // vungle setting
        val vungleExtras = Bundle()
        vungleExtras.putString(VungleConstants.KEY_USER_ID, "myUserID")
        vungleExtras.putInt(VungleConstants.KEY_ORIENTATION, 1)

        // DT Exchange setting
        val fyberExtras = Bundle()
        fyberExtras.putInt(InneractiveMediationDefs.KEY_AGE, 10)

        adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(InMobiAdapter::class.java, inMobiextras)
            .addNetworkExtrasBundle(ApplovinAdapter::class.java, appLovinExtras)
            .addNetworkExtrasBundle(VungleAdapter::class.java, vungleExtras) // Rewarded
            .addNetworkExtrasBundle(VungleInterstitialAdapter::class.java, vungleExtras) // Interstitial
            .addNetworkExtrasBundle(FyberMediationAdapter::class.java, fyberExtras)
            .build()
    }

    private fun setAdmobBannerListener() {
        adView!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "banner onAdLoaded")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad " + adError.code + "  " + adError.message)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG, "onAdOpened")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG, "onAdClosed")
            }
        }
    }

    private fun loadAdmobBannerAd() {
        adView!!.loadAd(adRequest!!)
    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, Config().ADMOB_INTERSTITIAL_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    show_interstitial_btn!!.isEnabled = true
                    Log.i(TAG, "onAdLoaded")
                    Toast.makeText(this@MainActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    mInterstitialAd!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null
                            Log.d("TAG", "The ad was dismissed.")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Log.e(TAG, "The ad failed to show.")
                            mInterstitialAd = null
                            show_interstitial_btn!!.isEnabled = false
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "The ad was shown.")
                        }
                    })
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                    val error = String.format(
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain, loadAdError.code, loadAdError.message
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "onAdFailedToLoad() with error: $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    private fun showInterstitialAd() {
        show_interstitial_btn!!.isEnabled = false
        mInterstitialAd!!.show(this)
    }

    private fun loadAdmobNativeAd() {
        native_btn!!.isEnabled = false
        val adLoader = AdLoader.Builder(this, Config().ADMOB_NATIVE_ID)
            .forNativeAd(OnNativeAdLoadedListener { nativeAd ->
                var isDestroyed = false
                native_btn!!.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isDestroyed = isDestroyed()
                }
                if (isDestroyed || isFinishing || isChangingConfigurations) {
                    nativeAd.destroy()
                    return@OnNativeAdLoadedListener
                }
                if (this@MainActivity.nativeAd != null) {
                    this@MainActivity.nativeAd!!.destroy()
                }
                this@MainActivity.nativeAd = nativeAd
                val frameLayout = findViewById<FrameLayout>(R.id.fl_adplaceholder)
                val adView =
                    layoutInflater.inflate(R.layout.ad_unified, frameLayout, false) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            })
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    native_btn!!.isEnabled = true
                    val error = String.format(
                        Locale.getDefault(),
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain,
                        loadAdError.code,
                        loadAdError.message
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to load native ad with error $error",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun loadRewardedAd() {
        if (mRewardedAd == null) {
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(this, Config().ADMOB_REWARDED_ID, adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error.
                        show_rewarded_btn!!.isEnabled = false
                        Log.d(TAG, loadAdError.message)
                        mRewardedAd = null
                        Toast.makeText(this@MainActivity, "onAdFailedToLoad", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        show_rewarded_btn!!.isEnabled = true
                        mRewardedAd = rewardedAd
                        Log.d(TAG, "onAdLoaded")
                        Toast.makeText(this@MainActivity, "onAdLoaded", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    private fun showRewardedAd() {
        if (mRewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
            return
        }
        show_rewarded_btn!!.isEnabled = false
        mRewardedAd!!.setFullScreenContentCallback(
            object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "onAdShowedFullScreenContent")
                    Toast.makeText(
                        this@MainActivity,
                        "onAdShowedFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.d(TAG, "onAdFailedToShowFullScreenContent")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    Toast.makeText(
                        this@MainActivity,
                        "onAdFailedToShowFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    Log.d(TAG, "onAdDismissedFullScreenContent")
                    Toast.makeText(
                        this@MainActivity,
                        "onAdDismissedFullScreenContent",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    // Preload the next rewarded ad.
                }
            })
        val activityContext: Activity = this@MainActivity
        mRewardedAd!!.show(
            activityContext,
            OnUserEarnedRewardListener { rewardItem -> // Handle the reward.
                Log.d("TAG", "The user earned the reward.")
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
            }
        )
    }


    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView

        // Set other ad assets.
        adView.headlineView = adView.findViewById<View>(R.id.ad_headline)
        adView.bodyView = adView.findViewById<View>(R.id.ad_body)
        adView.callToActionView = adView.findViewById<View>(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById<View>(R.id.ad_app_icon)
        adView.priceView = adView.findViewById<View>(R.id.ad_price)
        adView.starRatingView = adView.findViewById<View>(R.id.ad_stars)
        adView.storeView = adView.findViewById<View>(R.id.ad_store)
        adView.advertiserView = adView.findViewById<View>(R.id.ad_advertiser)

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView?)!!.text = nativeAd.headline
        adView.mediaView!!.mediaContent = nativeAd.mediaContent

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button?)!!.text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView?)!!.setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }
        //        Log.e("테스트","getIcon: "+ nativeAd.getIcon().getDrawable());
        if (nativeAd.price == null) {
            adView.priceView!!.visibility = View.INVISIBLE
        } else {
            adView.priceView!!.visibility = View.VISIBLE
            (adView.priceView as TextView?)!!.text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView!!.visibility = View.INVISIBLE
        } else {
            adView.storeView!!.visibility = View.VISIBLE
            (adView.storeView as TextView?)!!.text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar?)
                ?.setRating(nativeAd.starRating!!.toFloat())
            adView.starRatingView!!.visibility = View.VISIBLE
        }
        //        Log.e("테스트","StarRating(): "+ nativeAd.getStarRating().floatValue());
        if (nativeAd.advertiser == null) {
            adView.advertiserView!!.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView?)!!.text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }
        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
        native_btn!!.isEnabled = true
    }
}