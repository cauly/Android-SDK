package kr.co.cauly.sdk.android.mediation.sample.natives;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.fsn.cauly.CaulyBrowserUtil;
import com.fsn.cauly.CaulyCustomAd;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper;

import java.util.ArrayList;
import java.util.List;


public class SampleUnifiedNativeAdMapper extends UnifiedNativeAdMapper{
    private final kr.co.cauly.sdk.android.mediation.sample.natives.AdItem data;
    private final CaulyCustomAd caulyAdView;
    private final Context context;
//    private final CustomEventNativeListener nativeListener;

    public SampleUnifiedNativeAdMapper(final Context context, Drawable icon, Drawable image, final kr.co.cauly.sdk.android.mediation.sample.natives.AdItem data, CaulyCustomAd mCaulyAdView) {

        this.data = data;
        this.caulyAdView = mCaulyAdView;
        this.context = context;

        if (context==null) {
            Log.e("CaulyNative", "Failed to load ad. Request must be for unified native ads.");
            return;
        }
        List<NativeAd.Image> imagesList = new ArrayList<NativeAd.Image>();
        imagesList.add(new SampleNativeMappedImage(image, Uri.parse(data.image), 1.0));
        setImages(imagesList);
        setHeadline(data.subtitle);

        setIcon(new SampleNativeMappedImage(icon, Uri.parse(data.icon), 1.0));
        setBody(data.description);
        setOverrideImpressionRecording(false);
        setOverrideClickHandling(false);
    }


    @Override
    public void recordImpression() {
        super.recordImpression();
        caulyAdView.sendImpressInform(data.id);
    }

    @Override
    public void handleClick(View view) {
        super.handleClick(view);
        CaulyBrowserUtil.openBrowser(context, data.linkUrl);
    }


}
