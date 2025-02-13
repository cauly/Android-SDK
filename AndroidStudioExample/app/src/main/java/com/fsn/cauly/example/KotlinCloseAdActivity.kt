package com.fsn.cauly.example

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fsn.cauly.CaulyAdInfoBuilder
import com.fsn.cauly.CaulyCloseAd
import com.fsn.cauly.CaulyCloseAdListener

class KotlinCloseAdActivity : AppCompatActivity(), CaulyCloseAdListener {
    var mCloseAd: CaulyCloseAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_close_ad)

        // CloseAd 초기화
        val closeAdInfo = CaulyAdInfoBuilder(APP_CODE).build()
        mCloseAd = CaulyCloseAd()

        /*
         * Optional //원하는 버튼의 문구를 설젇 할 수 있다. mCloseAd.setButtonText("취소", "종료");
         * //원하는 텍스트의 문구를 설젇 할 수 있다. mCloseAd.setDescriptionText("종료하시겠습니까?");
         */mCloseAd!!.setAdInfo(closeAdInfo)
        mCloseAd!!.setCloseAdListener(this)

        // 종료광고 노출 후 back 버튼 사용을  막기 원할 경우 disableBackKey();을 추가한다
        // mCloseAd.disableBackKey();
    }

    override fun onResume() {
        if (mCloseAd != null) mCloseAd!!.resume(this)
        super.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCloseAd(null)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun showCloseAd(button: View?) {
        // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
        if (mCloseAd != null && mCloseAd!!.isModuleLoaded) {
            mCloseAd!!.show(this)
            //광고의 수신여부를 체크한 후 노출시키고 싶은 경우, show(this) 대신 request(this)를 호출.
            //onReceiveCloseAd에서 광고를 정상적으로 수신한 경우 , show(this)를 통해 광고 노출
        } else {
            // 광고에 필요한 리소스를 한번만 다운받는데 실패했을 때 앱의 종료팝업 구현
            showDefaultClosePopup()
        }
    }

    private fun showDefaultClosePopup() {
        AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
            .setPositiveButton("예") { dialog, which -> finish() }.setNegativeButton("아니요", null)
            .show()
    }

    // CaulyCloseAdListener
    override fun onFailedToReceiveCloseAd(
        ad: CaulyCloseAd, errCode: Int,
        errMsg: String
    ) {
    }

    // CloseAd의 광고를 클릭하여 앱을 벗어났을 경우 호출되는 함수이다.
    override fun onLeaveCloseAd(ad: CaulyCloseAd) {}

    // CloseAd의 request()를 호출했을 때, 광고의 여부를 알려주는 함수이다.
    override fun onReceiveCloseAd(ad: CaulyCloseAd, isChargable: Boolean) {}

    // 왼쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
    override fun onLeftClicked(ad: CaulyCloseAd) {}

    // 오른쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
    // Default로는 오른쪽 버튼이 종료로 설정되어있다.
    override fun onRightClicked(ad: CaulyCloseAd) {
        finish()
    }

    override fun onShowedCloseAd(ad: CaulyCloseAd, isChargable: Boolean) {}

    // CloseAd의 광고를 클릭할 경우 호출되는 함수이다.
    override fun onClickCloseAd(ad: caulyCloseAd) {}

    companion object {
        private const val APP_CODE = "CAULY"
    }
}