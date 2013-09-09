/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.bean.panel;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import com.zuiniu.android.assistant.datamanager.bean.CommonParcelable;
import com.zuiniu.android.assistant.datamanager.bean.CommonParcelableCreator;

/**
 * @author Administrator
 * 
 */
public class AppInfo extends CommonParcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7272498116453950759L;

	public static final Parcelable.Creator<AppInfo> CREATOR = new CommonParcelableCreator<AppInfo>(
			AppInfo.class);

	private String activityName;
	private String packageName;
	private String appName;
	private Drawable icon;

	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName
	 *            the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the icon
	 */
	public Drawable getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

}
