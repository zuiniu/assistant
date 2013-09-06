/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

/**
 * @author Administrator
 * 
 */
public class FileUtils {
	private static final int CONTENT_SIZE = 1024;

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * 文件是否存在
	 * 
	 * @param path
	 *            路径
	 * 
	 * @return 是否存在
	 */
	public static boolean exist(String path) {
		if (path == null) {
			return false;
		}

		File file = new File(path);

		if ((file == null) || !file.exists()) {
			return false;
		}

		return true;
	}

	/**
	 * 查看文件
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            路径
	 */
	public static void onView(Context context, String path, int dataType) {
		if (dataType == InvariantUtils.DATA_TYPE_VIDEO
				|| dataType == InvariantUtils.DATA_TYPE_APK) {
			MimeTypeMap mtm = MimeTypeMap.getSingleton();
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String sufix = null;
				String mimeType = "*/*";
				int index = path.lastIndexOf(".");
				if (index > 0) {
					sufix = path.substring(index + 1);
					mimeType = mtm.getMimeTypeFromExtension(sufix);
				}
				intent.setDataAndType(Uri.fromFile(new File(path)), mimeType);
				context.startActivity(intent);
			} catch (ActivityNotFoundException anfe) {
				anfe.printStackTrace();
			}

		} else {
			try {
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(path);
				player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					public void onPrepared(MediaPlayer player) {
						player.start();
					}
				});
				player.setOnCompletionListener(new OnCompletionListener() {

					public void onCompletion(MediaPlayer mp) {
						if (mp != null) {
							mp.release();
							mp = null;
						}
					}
				});
				player.setOnErrorListener(new OnErrorListener() {

					public boolean onError(MediaPlayer mp, int what, int extra) {
						if (mp != null) {
							mp.release();
							mp = null;
						}
						return false;
					}
				});
				player.prepareAsync();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 格式化大小
	 * 
	 * @param size
	 *            大小
	 * 
	 * @return 字符串
	 */
	public static String formatSize(float size) {
		long kb = CONTENT_SIZE;
		long mb = (kb * CONTENT_SIZE);
		long gb = (mb * CONTENT_SIZE);

		if (size < kb) {
			return "1.0KB";
		} else if (size < mb) {
			return String.format("%.1f KB", size / kb);
		} else if (size < gb) {
			return String.format("%.1f MB", size / mb);
		} else {
			return String.format("%.1f GB", size / gb);
		}
	}

	/**
	 * 根据时间获取另存为路径(含文件名)
	 * 
	 * @param path
	 *            路径
	 * 
	 * @return 路径
	 */
	public static String genPathByTime(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}

		int dotIndex = path.lastIndexOf(".");
		String postfix = "";
		String preFileName = "";

		if (dotIndex > 0) {
			postfix = path.substring(dotIndex);
			preFileName = path.substring(0, dotIndex);
		}

		return preFileName + String.valueOf(new Date().getTime()) + postfix;
	}

	/**
	 * 复制文件
	 * 
	 * @param src
	 *            源文件
	 * @param des
	 *            目标文件
	 * @param isOverride
	 *            是否覆盖
	 * 
	 * @return boolean 操作是否成功
	 */
	public static boolean copy(File src, File des, boolean isOverride) {
		boolean bResult = true;
		final int bufferSize = 1024 * 4;

		// 当目标文件已经存在并且不覆盖时返回true
		if (des.exists() && (isOverride != true)) {
			return true;
		}

		File fParent = new File(des.getParent());

		if (!fParent.exists()) {
			fParent.mkdirs();
		}

		InputStream in = null;
		OutputStream out = null;

		try {
			// 获取源文件输入流
			in = new BufferedInputStream(new FileInputStream(src));

			if (!des.exists()) {
				try {
					des.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// 获取目标文件输出流
			out = new BufferedOutputStream(new FileOutputStream(des));

			int bytes;
			byte[] buffer = new byte[bufferSize];

			while ((bytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes);
			}

			out.flush();
			bResult = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bResult = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bResult = false;
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				bResult = false;
				e.printStackTrace(System.out);
			}
		}

		return bResult;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param path
	 *            路径
	 * 
	 * @return 大小
	 */
	public static long getSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		} else {
			File file = new File(path);

			if ((file != null) && file.exists()) {
				return file.length();
			}
		}

		return 0;
	}

	/**
	 * 
	 * @return 是否存在SD卡
	 */
	public static boolean isSDExist() {
		long size = getSDCardAvailableSpace();
		if (size < 1024 * 10) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return SD卡是否可用
	 */
	public static long getSDCardAvailableSpace() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			if (path != null) {
				return getAvailableSpace(path.getPath());
			}
		}
		return 0;
	}

	/**
	 * 获取对应路径的可用空间大小
	 * @param path
	 *            路径
	 * @return 文件夹的大小
	 */
	private static long getAvailableSpace(String path) {
		long result = 0;
		try {
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			result = blockSize * availableBlocks / 1024;
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 判断是否可以接收文件 接收的条件：sdcard或者udisk存在
	 * 
	 * @return 是否成功
	 */
	public static int canTempFile() {
		if (isSDExist()) {
			return 0;
		} else {
			File file = new File("udisk");
			if (file.exists() && file.canWrite()) {
				return 1;
			}
		}
		return -1;
	}
}
