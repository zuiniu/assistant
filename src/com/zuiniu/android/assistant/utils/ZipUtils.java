/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Administrator
 * 
 */
public class ZipUtils {

	/**
	 * 解压
	 * @param srcFile 源文件
	 * @param targetFile 目标文件
	 */
	@SuppressWarnings("resource")
	public static void unZip(File srcFile, File targetFile) {
		if (!srcFile.exists())
			return;
		if (!targetFile.getParentFile().exists())
			targetFile.getParentFile().mkdir();
		while (true) {
			FileOutputStream localFileOutputStream;
			byte[] arrayOfByte;
			int i;
			try {
				FileInputStream localFileInputStream = new FileInputStream(
						srcFile);
				localFileOutputStream = new FileOutputStream(targetFile);
				ZipInputStream localZipInputStream = new ZipInputStream(
						localFileInputStream);
				arrayOfByte = new byte[1024];
				i = localZipInputStream.read(arrayOfByte);
				if (i <= 0) {
					localFileInputStream.close();
					localFileOutputStream.close();
					return;
				}
			} catch (Exception localException) {
				localException.printStackTrace();
				return;
			}
			try {
				localFileOutputStream.write(arrayOfByte, 0, i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 压缩
	 * @param srcFile 源文件
	 * @param targetFile 目标文件
	 * @return
	 */
	public static boolean zip(File srcFile, File targetFile) {
		if (!srcFile.exists())
			return false;
		if (!targetFile.getParentFile().exists())
			targetFile.getParentFile().mkdir();
		try {
			FileInputStream localFileInputStream = new FileInputStream(
					targetFile);
			ZipOutputStream localZipOutputStream = new ZipOutputStream(
					new FileOutputStream(targetFile));
			byte[] arrayOfByte = new byte[1024];
			localZipOutputStream
					.putNextEntry(new ZipEntry(srcFile.getName()));
			while (true) {
				int i = localFileInputStream.read(arrayOfByte);
				if (i <= 0) {
					localZipOutputStream.closeEntry();
					localFileInputStream.close();
					localZipOutputStream.close();
					return true;
				}
				localZipOutputStream.write(arrayOfByte, 0, i);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}
}
