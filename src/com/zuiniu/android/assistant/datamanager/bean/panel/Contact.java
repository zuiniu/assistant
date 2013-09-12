/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.bean.panel;

import android.os.Parcelable;

import com.zuiniu.android.assistant.datamanager.bean.CommonParcelable;
import com.zuiniu.android.assistant.datamanager.bean.CommonParcelableCreator;

/**
 * @author Administrator
 *
 */
public class Contact extends CommonParcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5942698115542602160L;
	
	public static final Parcelable.Creator<Contact> CREATOR = new CommonParcelableCreator<Contact>(
			Contact.class);

	
	private String id;
	private String name;
	private String phoneNumber;

}
