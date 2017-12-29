package com.fsn.cauly.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyCustomAd;
import com.fsn.cauly.CaulyCustomAdListener;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.ImageCacheManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NativeDataActivity extends Activity {
	static final String APP_CODE = "CAULY"; // AppCode 
	List<NativeItem> myList ;
	CaulyCustomAd mCaulyAdView;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		View requestJsontype = findViewById(R.id.btn1);
		View addView = findViewById(R.id.btn2);
		requestJsontype.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestNativJsonData();
			}
		});
		
		addView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addView();
			}
		});
	}
	
	
	private void addView() {

		if(myList!=null && myList.size()>0)
		{
			LinearLayout container = (LinearLayout)findViewById(R.id.ad_container);
			for(final NativeItem item:myList)
			{
				View v = View.inflate(this, R.layout.adview, null);
				ImageView img = (ImageView) v.findViewById(R.id.image);
				TextView title = (TextView) v.findViewById(R.id.title);
				TextView subtitle = (TextView) v.findViewById(R.id.subtitle);
				View admarker = v.findViewById(R.id.ad_marker);
				admarker.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						BrowserUtil.openBrowser(getApplicationContext(), mCaulyAdView.getOptoutUrl(APP_CODE, item.id));
					}
				});
				title.setText(""+item.title);
				subtitle.setText(""+item.subtitle);
				ImageCacheManager.getInstance(NativeDataActivity.this).setImageBitmap(""+item.image, img);
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
				         BrowserUtil.openBrowser(NativeDataActivity.this, item.linkUrl);
					}
				});
				container.addView(v);
				
				//광고노출 시, 호출한다.
				mCaulyAdView.sendImpressInform(item.id);
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mCaulyAdView!=null)
			mCaulyAdView.cancel();
	}

	void requestNativJsonData() {
		LinearLayout container = (LinearLayout)findViewById(R.id.ad_container);
		container.removeAllViews();
		
		CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE).build();
		mCaulyAdView = new CaulyCustomAd(this);
		mCaulyAdView.setAdInfo(adInfo);
		mCaulyAdView.setCustomAdListener(new CaulyCustomAdListener() {
			@Override
			public void onShowedAd() {
			}

			
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
			@Override
			public void onLoadedAd(boolean isChargeableAd) {
			//	String dataString = mCaulyAdView.getJsonString(); 
				
				 myList = new ArrayList<NativeDataActivity.NativeItem>();
				List<HashMap<String,Object>> adlist = mCaulyAdView.getAdsList();
				if(adlist!=null && adlist.size()>0)
				{
					for(HashMap<String,Object> map: adlist)
					{
						NativeItem data = new  NativeItem();
						data.id = (String) map.get("id");
						data.title =(String) map.get("title");
						data.subtitle =(String) map.get("subtitle");
						data.icon =(String) map.get("icon");
						data.image =(String) map.get("image");
						data.linkUrl =(String) map.get("linkUrl");
						String ad_charge_type = (String) map.get("ad_charge_type");
						if(!"100".equalsIgnoreCase(ad_charge_type))  //?�료광고�??�출??경우
						{
							myList.add(data);
						}
					}
				}
				
				Toast.makeText(NativeDataActivity.this, " Data "+adlist.size()+"  loaded  ", Toast.LENGTH_SHORT).show();
			}
			// 광고 호출이 실패할 경우, 호출된다.
			@Override
			public void onFailedAd(int errCode, String errMsg) {
				Toast.makeText(NativeDataActivity.this, "onFailedAd "+errCode+"  "+errMsg, Toast.LENGTH_SHORT).show();
			}

			// 광고가 클릭된 경우, 호출된다.
			@Override
			public void onClikedAd() {
			}

		});

		// CaulyCustomAd.INTERSTITIAL_PORTRAIT,CaulyCustomAd.NATIVE_PORTRAIT,CaulyCustomAd.NATIVE_LANDSCAPE
		mCaulyAdView.requestAdData(CaulyCustomAd.NATIVE_LANDSCAPE, 5/*ad_count 광고개수*/);

	}
	
	
	class NativeItem {
		public String title;
		public String subtitle;
		public String description;
		public String icon;
		public String image;
		public String id;
		public String linkUrl;
	}

}
