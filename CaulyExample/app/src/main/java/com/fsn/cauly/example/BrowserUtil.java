package com.fsn.cauly.example;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;

public class BrowserUtil {
	
	// 브라?��?�??�러�???경우  ?�정 브라?��?�??�택?�여 ?�출?�는 기능
	  public static void openBrowser(Context context, String url) {
			
			Intent intent;
			try {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent = selectBrowserIntent(context, intent, url);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getApplicationContext().getPackageName());
				context.startActivity(intent);
			} catch (Throwable e) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getApplicationContext().getPackageName());
				context.startActivity(intent);
			}
		}
		
	  // ?�드로이??기본 브라?��????��?�??�선 ?�인?�고  ?�롬 ?�키�?�� ?�치 ?��?�??�인?�다.
	  static Intent selectBrowserIntent(Context context, Intent intent, String url)
	  {
			PackageManager pm =context. getPackageManager();
			if (pm != null && url.startsWith("http")) {
				String className =  getMainActivity( context,"com.android.browser");
				if (!TextUtils.isEmpty(className)) 
				{
					intent.setClassName("com.android.browser",  className);					
				}
				else
				{
					String className2 =  getMainActivity( context,"com.android.chrome");
					if (!TextUtils.isEmpty(className2)) 
					{
						intent.setClassName("com.android.chrome",className2);
					}
				}
			}	
		return intent;
	  }
		
	  static String getMainActivity(Context context,String packageName)
		{
			Intent i = new Intent("android.intent.action.MAIN");
		     i.setPackage(packageName);
		     List<ResolveInfo> groupApps= context.getPackageManager().queryIntentActivities(i, PackageManager.GET_INTENT_FILTERS);
		      for(ResolveInfo info: groupApps)
		      {
		    	return  info.activityInfo.name;
		      }
		      return "";
		}
}
