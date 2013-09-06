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
public class Slot extends CommonParcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2927918418695759540L;

	public static final Parcelable.Creator<Slot> CREATOR = new CommonParcelableCreator<Slot>(
			Slot.class);

	private boolean empty;
	private String name;

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * @param empty
	 *            the empty to set
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
