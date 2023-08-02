package com.fsn.cauly.example

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fsn.cauly.CaulyCustomAd
import com.fsn.cauly.CaulyCustomAdListener
import com.fsn.cauly.CaulyNativeAdInfoBuilder
import com.fsn.cauly.ImageCacheManager
import java.util.ArrayList

class KotlinNativeDataActivity : AppCompatActivity() {
    var myList: MutableList<NativeItem>? = null
    var mCaulyAdView: CaulyCustomAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_native_data)
        val requestJsontype = findViewById<View>(R.id.btn1)
        val addView = findViewById<View>(R.id.btn2)
        requestJsontype.setOnClickListener { requestNativJsonData() }
        addView.setOnClickListener { addView() }
    }

    private fun addView() {
        if (myList != null && myList!!.size > 0) {
            val container = findViewById<View>(R.id.ad_container) as LinearLayout
            for (item in myList!!) {
                val v = View.inflate(this, R.layout.adview, null)
                val img = v.findViewById<View>(R.id.image) as ImageView
                val title = v.findViewById<View>(R.id.title) as TextView
                val subtitle = v.findViewById<View>(R.id.subtitle) as TextView
                val admarker = v.findViewById<View>(R.id.ad_marker)
                admarker.setOnClickListener {
                    BrowserUtil.openBrowser(
                        applicationContext, mCaulyAdView!!.getOptoutUrl(
                            APP_CODE, item.id
                        )
                    )
                }
                title.text = "" + item.title
                subtitle.text = "" + item.subtitle
                ImageCacheManager.getInstance(this@KotlinNativeDataActivity)
                    .setImageBitmap("" + item.image, img)
                v.setOnClickListener {
                    BrowserUtil.openBrowser(
                        this@KotlinNativeDataActivity,
                        item.linkUrl
                    )
                }
                container.addView(v)

                //광고노출 시, 호출한다.
                mCaulyAdView!!.sendImpressInform(item.id)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCaulyAdView != null) mCaulyAdView!!.cancel()
    }

    fun requestNativJsonData() {
        val container = findViewById<View>(R.id.ad_container) as LinearLayout
        container.removeAllViews()
        val adInfo = CaulyNativeAdInfoBuilder(APP_CODE).build()
        mCaulyAdView = CaulyCustomAd(this)
        mCaulyAdView!!.setAdInfo(adInfo)
        mCaulyAdView!!.setCustomAdListener(object : CaulyCustomAdListener {
            override fun onShowedAd() {}

            /*
             * JSON Data Format.
            {"ads":[
                   {"id":"광고ID",
                    "ad_charge_type":"0 : 유료광고, 100: 하우스 광고",
                      "icon":"아이콘 이미지",
                    "image":"메인 이미지",
                    "title":"제목",
                    "subtitle":"부제목",
                    "description":"설명"
                    "linkUrl":랜딩 페이지 URL
                   }
                  ]
              }
             */
            //광고 호출이 성공할 경우, 호출된다.
            override fun onLoadedAd(isChargeableAd: Boolean) {
                //	String dataString = mCaulyAdView.getJsonString();
                myList = ArrayList()
                val adlist = mCaulyAdView!!.adsList
                if (adlist != null && adlist.size > 0) {
                    for (map in adlist) {
                        val data: NativeItem = NativeItem()
                        data.id = map["id"] as String?
                        data.title = map["title"] as String?
                        data.subtitle = map["subtitle"] as String?
                        data.icon = map["icon"] as String?
                        data.image = map["image"] as String?
                        data.linkUrl = map["linkUrl"] as String?
                        val ad_charge_type = map["ad_charge_type"] as String?
                        if (!"100".equals(ad_charge_type, ignoreCase = true)) //?�료광고�??�출??경우
                        {
                            (myList as ArrayList<NativeItem>).add(data)
                        }
                    }
                }
                Toast.makeText(
                    this@KotlinNativeDataActivity,
                    " Data " + adlist!!.size + "  loaded  ",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // 광고 호출이 실패할 경우, 호출된다.
            override fun onFailedAd(errCode: Int, errMsg: String) {
                Toast.makeText(
                    this@KotlinNativeDataActivity,
                    "onFailedAd $errCode  $errMsg",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // 광고가 클릭된 경우, 호출된다.
            override fun onClikedAd() {}
        })

        // CaulyCustomAd.INTERSTITIAL_PORTRAIT,CaulyCustomAd.NATIVE_PORTRAIT,CaulyCustomAd.NATIVE_LANDSCAPE
        mCaulyAdView!!.requestAdData(CaulyCustomAd.NATIVE_LANDSCAPE, 5 /*ad_count 광고개수*/)
    }

    inner class NativeItem {
        var title: String? = null
        var subtitle: String? = null
        var description: String? = null
        var icon: String? = null
        var image: String? = null
        var id: String? = null
        var linkUrl: String? = null
    }

    companion object {
        const val APP_CODE = "CAULY" // AppCode
    }
}