/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.TypedValue;

/**
 * @author Administrator
 * 
 */
public class BitmapUtils {
	/**
	 * Tag
	 */
	private final static String tag = "BitmapUtils";

	/**
	 * 以最省内存的方式读取本地资源的图片(所谓最省，就是想像素值降低就是了)
	 * 
	 * @param context
	 *        上下文对象
	 * @param resId
	 *        图片资源id
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		TypedValue value = new TypedValue();
		InputStream is = context.getResources().openRawResource(resId);
		opt.inTargetDensity = value.density;

		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 以最省内存的方式读取本地路径的图片
	 * 
	 * @param context
	 *        上下文对象
	 * @param filePath
	 *        图片路径
	 * @param density
	 *        图片密度
	 * @return 图片对象
	 */
	public static Bitmap readBitMapfromPath(Context context, String filePath,
			int density) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inTargetDensity = density;
		// 获取资源图片
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			recycle(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bitmap == null) {
			File file = new File(filePath);
			file.delete();
		}
		return bitmap;
	}

	/**
	 * 以最省内存的方式读取本地路径的图片
	 * 
	 * @param context
	 *        上下文对象
	 * @param filePath
	 *        图片路径
	 * @param height
	 *        高度
	 * @param width
	 *        宽度
	 * @return 图片
	 */
	public static Bitmap getBitMapfromPath(Context context, String filePath,
			int height, int width) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.outHeight = height;
		opt.outWidth = width;
		// 获取资源图片
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			recycle(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bitmap == null) {
			File file = new File(filePath);
			file.delete();
		}
		return bitmap;
	}

	/**
	 * 以最省内存的方式读取assert的图片
	 * @param context
	 *        上下文对象
	 * @param fileName
	 *        图片名称
	 * @param height
	 *        图片高度
	 * @param width
	 *        图片宽度
	 * @return  图片
	 */
	public static Bitmap readBitMapfromAssert(Context context, String fileName,
			int height, int width) {
		InputStream is = null;

		try {
			is = context.getAssets().open(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.outHeight = height;
		opt.outWidth = width;
		// 获取资源图片
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			recycle(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 获取分享的缩略图
	 * 
	 * @param context
	 *        上下文对象
	 * @param path
	 *        图片路径
	 * @return
	 *        图片
	 */
	public static Bitmap getShareThumb(Context context, String path) {
		Bitmap newBitmap = null;
		Bitmap oldBitmap = null;

		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			oldBitmap = decodeFile(path, opts);
			opts.inJustDecodeBounds = false;

			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;

			BitmapFactory.Options newOpts = new BitmapFactory.Options();

			newOpts.outHeight = srcHeight;
			newOpts.outWidth = srcWidth;
			newOpts.inJustDecodeBounds = false;
			// 非常重要
			newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
			newBitmap = decodeFile(path, newOpts);
			if (newBitmap == null) {
				newOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
				newBitmap = decodeFile(path, newOpts);
			}
			if (newBitmap == null) {
				newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				newBitmap = decodeFile(path, newOpts);
			}

			if (newBitmap == null) {
				newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
				for (int i = 2; i <= 5; i++) {
					newOpts.inSampleSize = i;
					newBitmap = decodeFile(path, newOpts);
					if (newBitmap != null) {
						break;
					}
				}

			}
		} catch (OutOfMemoryError oe) {
			// oe.printStackTrace();
			recycle(newBitmap);
		} catch (Exception ex) {
			ex.printStackTrace();
			recycle(newBitmap);
		} finally {
			recycle(oldBitmap);
		}

		return newBitmap;
	}

	/**
	 * 获得72*72的图片
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 返回图片
	 * */
	public static Bitmap getIcon(Context context, String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			recycle(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int be = (int) (options.outHeight / (float) 142);
		options.inSampleSize = be;
		if (be <= 0) {
			options.inSampleSize = 1;
		}

		options.inJustDecodeBounds = false;
		options.inScaled = true;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			recycle(bitmap);
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
		final float roundPx = 5;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return SystemUtiles.getRoundedCornerBitmap(output);
	}

	/**
	 * 通过路径获取记事本缩略图
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 * @return
	 */
	public static byte[] getMemoShareThumbByte(Context context, String path) {
		byte[] str = null;
		Bitmap destBm = null;
		Bitmap srcBm = null;

		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			int srcWidth = 255;
			int srcHeight = 255;
			srcBm = BitmapUtils.decodeFile(path, opts);
			if (srcBm != null) {
				srcWidth = srcBm.getWidth();
				srcHeight = srcBm.getHeight();
			}
			opts.inJustDecodeBounds = false;

			// 缩放的比例
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.outHeight = srcHeight;
			newOpts.outWidth = srcWidth;
			newOpts.inJustDecodeBounds = false;

			destBm = BitmapUtils.decodeFile(path, newOpts);
			if (destBm != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				str = bitmap2BytesForMemo(destBm, baos);
				baos.flush();
				baos.close();
			}
		} catch (OutOfMemoryError oe) {
			// oe.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (srcBm != null && !srcBm.isRecycled()) {
				srcBm.recycle();
				srcBm = null;
			}

			if (destBm != null && !destBm.isRecycled()) {
				destBm.recycle();
				destBm = null;
			}
		}
		return str;
	}

	/**
	 * TODO
	 * 
	 * @param bm
	 *            TODO
	 * @param bis
	 *            TODO
	 * 
	 * @return TODO
	 */
	private static byte[] bitmap2BytesForMemo(Bitmap bm,
			ByteArrayOutputStream bis) {
		bm.compress(Bitmap.CompressFormat.JPEG, 50, bis);

		return bis.toByteArray();
	}

	/**
	 * 获取图片
	 * 
	 * @param path
	 *            路径
	 * @return 图片
	 */
	public static Bitmap decodeFile(String path) {

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			bitmap = BitmapFactory.decodeFile(path, options);
			if (bitmap != null) {
				android.util.Log.i(tag, "decode: " + bitmap);
			}
		} catch (OutOfMemoryError oe) {
			// oe.printStackTrace();
			recycle(bitmap);
		}
		return bitmap;
	}

	public static void recycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	/**
	 * 从Asset文件获取指定路径的文件
	 */
	public static Bitmap decodeBitmapFromAssert(Context context, String path) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			is = context.getAssets().open(path);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap != null) {
				android.util.Log.i(tag, "decode: " + bitmap);
			}
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError oomr) {
			// oomr.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;

	}

	public static Bitmap decodeByteArray(byte[] bytes) {
		Bitmap bitmap = null;
		if (bytes != null && bytes.length > 0) {
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}

		return bitmap;
	}

	public static Bitmap decodeFile(String path, BitmapFactory.Options option) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(path, option);
		} catch (OutOfMemoryError oe) {
			recycle(bitmap);
		}

		return bitmap;
	}

	public static Bitmap decodeStream(InputStream inputStream) {
		Bitmap bitmap = null;
		if (inputStream != null) {
			try {
				bitmap = BitmapFactory.decodeStream(inputStream);
			} catch (OutOfMemoryError ofme) {
				// ofme.printStackTrace();
			} finally {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * 获取视频缩略图
	 * 
	 * @param path
	 *            路径
	 * @return 图片
	 */
	public static Bitmap getThumbnailForVideo(String path) {
		return ThumbnailUtils.createVideoThumbnail(path,
				Images.Thumbnails.MICRO_KIND);
	}
}
