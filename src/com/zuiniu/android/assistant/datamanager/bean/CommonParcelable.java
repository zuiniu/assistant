/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class CommonParcelable implements Parcelable, Serializable {

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeSerializable(this);
	}

}
