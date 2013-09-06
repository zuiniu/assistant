/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public class DensityUtil {
	/**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文对象
     * @param dpValue dp值
     *
     * @return 对应的像素值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) ((dpValue * scale) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文对象
     * @param pxValue 像素值
     *
     * @return 对应的dip值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) ((pxValue / scale) + 0.5f);
    }
    
    /**
     * 获取当前手机的屏幕密度
     * @param context 上下文对象
     * @return 屏幕密度
     */
    public static float getDensity(Context context) {
    	return context.getResources().getDisplayMetrics().density;
    }
}
