/**
 * 
 */
package com.zuiniu.android.assistant.datamanager.bean;

import java.io.Serializable;
import java.lang.reflect.Array;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Administrator
 *
 */
public class CommonParcelableCreator<T extends Serializable> implements Parcelable.Creator<T> {
	private Class<T> clazz;
	
	public CommonParcelableCreator(Class<T> clazz) {
        if (clazz == null) {
            throw new java.lang.IllegalArgumentException();
        }

        this.clazz = clazz;
    }

	@SuppressWarnings("unchecked")
	@Override
	public T createFromParcel(Parcel source) {
		// TODO Auto-generated method stub
		Serializable obj = source.readSerializable();

        if (obj == null) {
            return null;
        }

        return (T) obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] newArray(int size) {
		// TODO Auto-generated method stub
		return (T[]) Array.newInstance(clazz, size);
	}

}
