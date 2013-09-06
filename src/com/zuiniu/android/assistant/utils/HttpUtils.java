/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;

/**
 * @author Administrator
 * 
 */
public class HttpUtils {
	private static CookieStore cookieStore = null;
	private final static int timeoutConnection = 30000;
	private final static int timeoutSocket = 50000;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4; // 一次接收数据的大小
	private static final int EOF = -1; // 结束标识
	private static HttpClient httpClient;

	private static void init() {
		if (httpClient == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);// 连接超时设置
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket); // 交互常数数据超时设置
			httpClient = new DefaultHttpClient(httpParameters);
		}
	}

	public static HttpClient getInstance() {
		init();

		return httpClient;
	}

	private HttpUtils() {
	}

	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static String getString(final String url, List<NameValuePair> params) {
		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {

				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

				httpPost.setEntity(entity);
			}

			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=\"UTF-8\"");
			init();

			if (cookieStore != null) {
				((DefaultHttpClient) httpClient).setCookieStore(cookieStore);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				StringBuffer sb = new StringBuffer();

				/*
				 * 开源的好处在于给予参考，事实上我之前的实现也没什么问题，如果需要编码就加编码就好了
				 * 或者说因为行数较多或者教少的时候导致容易卡的话，借鉴自己读取等方式是没有问题的，但没必要整个包引用过来，因为很大。
				 * http://grepcode.com/file/repo1.maven.org/maven2/commons-io/commons-io/2.4/org/apache/commons/io/IOUtils.java#IOUtils.copyLarge%28 java.io.InputStream%2Cjava.io.OutputStream%29
				 */
				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

				InputStream input = httpResponse.getEntity().getContent();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				int n = 0;
				while (EOF != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
				}
				sb.append(output.toString("UTF-8"));
				output.close();

				cookieStore = ((AbstractHttpClient) httpClient)
						.getCookieStore();

				return sb.toString();
			} else {
				return null;
			}
		} catch (OutOfMemoryError e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean downlodFile(String url, String fullFilename,
			List<NameValuePair> params, List<NameValuePair> headerParams) {
		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

				httpPost.setEntity(entity);
			}

			if (headerParams != null && !headerParams.isEmpty()) {
				for (NameValuePair para : headerParams) {
					httpPost.addHeader(para.getName(), para.getValue());
				}
			}

			init();

			if (cookieStore != null) {
				((DefaultHttpClient) httpClient).setCookieStore(cookieStore);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				if (httpResponse.getEntity().isStreaming()) {
					InputStream input = httpResponse.getEntity()
							.getContent();
					FileOutputStream output = new FileOutputStream(
							new File(fullFilename));

					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int n = 0;
					while (EOF != (n = input.read(buffer))) {
						output.write(buffer, 0, n);
					}

					output.flush();
					output.close();
					
					return true;
				}
			}

			return false;
		} catch (OutOfMemoryError e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean downlodFile(String url, String fullFilename,
			List<NameValuePair> params) {
		return downlodFile(url, fullFilename, params, null);
	}
	
	public static boolean downlodFile(String url, String fullFilename) {
		return downlodFile(url, fullFilename, null);
	}
	
	public static Bitmap downlodImage(String url, List<NameValuePair> params, List<NameValuePair> headerParams) {
		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

				httpPost.setEntity(entity);
			}

			if (headerParams != null && !headerParams.isEmpty()) {
				for (NameValuePair para : headerParams) {
					httpPost.addHeader(para.getName(), para.getValue());
				}
			}

			init();

			if (cookieStore != null) {
				((DefaultHttpClient) httpClient).setCookieStore(cookieStore);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (httpResponse.getEntity().isStreaming()) {
					Bitmap bitmap = BitmapUtils.decodeStream(httpResponse.getEntity()
							.getContent());
					
					return bitmap;
				}
			}

			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Bitmap downlodImage(String url, List<NameValuePair> params) {
		return downlodImage(url, params, null);
	}
	
	public static Bitmap downlodImage(String url) {
		return downlodImage(url, null);
	}
}
