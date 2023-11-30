package kr.co.cauly.sdk.android.mediation.sample.natives;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

/** Interface to send callbacks when {@link kr.co.cauly.sdk.android.mediation.sample.natives.DownloadDrawablesAsync} finishes downloading images. */
public interface DrawableDownloadListener {

  // A success callback.
  void onDownloadSuccess(HashMap<String, Drawable> drawableMap);

  // A failure callback.
  void onDownloadFailure();
}
