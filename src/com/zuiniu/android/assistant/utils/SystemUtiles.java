/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

/**
 * @author Administrator
 * 
 */
public class SystemUtiles {
	/**
	 * 判断邮箱的合法性
	 * 
	 * @param 邮箱地址字符串
	 * @return 是否为合法邮箱
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断手机号的合法性
	 * 
	 * @param 手机号码字符串
	 * @return 是否为合法手机
	 */
	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从链接中(url)中读取流获取图片(不带session模式)
	 * 
	 * @param 图片链接
	 * @return 图片(异常等情况为null)
	 */
	public final static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;

		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;

			conn = (HttpURLConnection) myFileUrl.openConnection();

			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapUtils.decodeStream(is);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 将图片写入对应的路径中
	 * @param 图片
	 * @param 路径
	 * @param 文件名称
	 * @throws 写入异常
	 */
	public static void saveFile(Bitmap bm, String path, String fileName)
			throws IOException {
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		BufferedOutputStream bos = null;
		try {
			File myCaptureFile = new File(path + fileName);
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
		} catch (Exception e) {
			e.printStackTrace();
			if (bos != null) {
				bos.flush();
				bos.close();
			}
		} finally {
			if (bos != null) {
				bos.flush();
				bos.close();
			}
		}
	}

	/**
	 * 制作32色圆角图片(从网络图片中制作缩略图)
	 * 
	 * @param url
	 *            图片链接
	 * @return 返回图片
	 * */
	public static Bitmap getIcon(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		Options options = new Options();
		// 设置解码
		options.inJustDecodeBounds = true;
		Bitmap bitmap = null;

		int be = (int) (options.outHeight / (float) 140);
		options.inSampleSize = be;
		if (be <= 0) {
			options.inSampleSize = 1;
		}

		options.inJustDecodeBounds = false;
		options.inScaled = true;
		try {
			bitmap = returnBitMap(url);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bitmap == null) {
			return null;
		}
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.RGB_565);
		} catch (OutOfMemoryError e) {
			return bitmap;
		}
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// 设置圆角
		final float roundPx = 5;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return output;
	}

	/**
	 * 获取256色圆角图片(圆角半径80)
	 * 
	 * @param bitmap
	 *            原图片
	 * @return 圆角图片
	 * */
	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 80;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		try {
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}

		bitmap.recycle();
		return output;
	}

	/**
	 * 获取256色圆角图片(圆角半径5)
	 * 
	 * @param bitmap
	 *            原图片
	 * @return 圆角图片
	 * */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 5;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			// bitmap.recycle();
			return output;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			return null;
		}

	}

	/**
	 * 根据图片链接和给出的新大小缩放制作新图片
	 * 
	 * @param URL
	 *            图片链接
	 * @param newWidth
	 *            新图片的宽度
	 * @param newHeight
	 *            新图片的高度
	 * @return 新图片
	 */
	public static Bitmap zoomImage(String url, int newWidth, int newHeight) {
		Bitmap bgimage = returnBitMap(url);
		if (bgimage == null || newWidth <= 0 || newHeight <= 0) {
			return null;
		}
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		return getRoundedBitmap(bitmap);
	}

	/**
	 * 根据图片链接和给出的新大小缩放制作新图片
	 * 
	 * @param bgimage
	 *            原图片
	 * @param newWidth
	 *            新图片的宽度
	 * @param newHeight
	 *            新图片的高度
	 * @return 新图片
	 */
	public static Bitmap zoomImageCache(Bitmap bgimage, int newWidth,
			int newHeight) {
		if (bgimage == null || newWidth <= 0 || newHeight <= 0) {
			return null;
		}
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		return getRoundedBitmap(bitmap);
	}
}
