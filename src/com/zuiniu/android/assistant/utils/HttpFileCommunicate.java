/**
 * 
 */
package com.zuiniu.android.assistant.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

/**
 * @author Administrator
 *
 */
public class HttpFileCommunicate {

    /** 返回码-成功 */
    public static final int RET_CODE_SUCCESS = 0;

    /** 返回码-网络错误 */
    public static final int RET_CODE_NET_ERROR = 1;

    /** 返回码-文件错误 */
    public static final int RET_CODE_FILE_ERROR = 2;

    /** 返回码-url错误 */
    public static final int RET_CODE_URL_ERROR = 3;

    /** 返回码-链接错误 */
    public static final int RET_CODE_CONNECTION_ERROR = 4;

    /** 返回码-协议错误 */
    public static final int RET_CODE_PROTOCOL_ERROR = 5;

    /** 返回码-传输数据错误 */
    public static final int RET_CODE_POSTDATA_ERROR = 6;

    /** 返回码-创建临时文件错误 */
    public static final int RET_CODE_CREATETEMPFILE_ERROR = 7;

    /** 返回码-未找到sdcard错误 */
    public static final int RET_CODE_NOTFINDSDCARD_ERROR = 8;

    /** 返回码-下载部分文件成功（分段下载） */
    public static final int RET_CODE_DOWNLODPART_SUCCESS = 9;

    /** 返回码-创建目标文件失败 */
    public static final int RET_CODE_CREATEDESTFILE_ERROR = 10;

    /** 返回码-下载文件不完整 */
    public static final int RET_CODE_DOWNLOADFILE_NOT_COMPLETE_ERROR = 11;

    /** 返回码-IO异常 */
    public static final int RET_CODE_IOEXCETPTION_ERROR = 12;

    /** 返回码-json解析异常 */
    public static final int RET_CODE_JSON_ERROR = 13;

    /** 返回码-内存异常 */
    public static final int OUT_OF_MEMORY_ERROR = 14;

    /** http */
    public static final String HTTP_PROTOCOL = "http://";

    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /** -- */
    private static final String FIX = "--";

    /** \r\n */
    private static final String enterNewline = "\r\n";

    /** 1K */
    private static final int read_to_bytes = 64 * 1024;

    /** 1K */
    private static final int read_to_bytes_from_server = 64 * 1024;

    /** 256K */
    private static final int bytes_to_write = 256 * 1024;

    /** ----------HV2ymHFg03ehbqgZCaKO6jyH */
    private static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";

    /** context */
    private Context context = null;

    /** uploadFild */
    private UploadField uploadFild = null;

    /**
     * Creates a new HttpFileCommunicate object.
     * 
     * @param context
     *            TODO
     */
    public HttpFileCommunicate(Context context) {
        this.context = context.getApplicationContext();
        uploadFild = new UploadField();
    }

    /**
     * 文件下载，如果需要同步接口，则callback传null即可。callback传非空值时，自动创建线程完成下载
     * 
     * @param fileSource
     *            文件url地址
     * @param fileDest
     *            文件本地目标路径
     * @param tempFile
     *            临时文件绝对路径
     * @param callback
     *            回调对象
     * 
     * @return TODO
     */
    public int download(String fileSource, String fileDest, String tempFile, DownloadCallback callback) {

        if (callback == null) {

            return downloadFile(fileSource, fileDest, tempFile, callback);
        } else {
            new Thread(new DownloadRunable(fileSource, fileDest, tempFile, callback)).start();

            return RET_CODE_SUCCESS;
        }
    }

    /**
     * 异步上传
     * 
     * @param fileName
     *            文件名
     * @param needSkeleton
     *            是否需要缩略图
     * @param callback
     *            回调对象
     */
    public void upload(String fileName, boolean needSkeleton, UploadCallback callback) {
        new Thread(new UploadRunable(fileName, needSkeleton, callback)).start();
    }

    /**
     * 同步上传
     * 
     * @param fileName
     *            文件名
     * @param needSkeleton
     *            是否需要缩略图
     * 
     * @return 上传结果
     */
    public int upload(String fileName, boolean needSkeleton) {

        if (uploadFild == null) {
            uploadFild = new UploadField();
        }

        return uploadFile(fileName, uploadFild, needSkeleton);
    }

    //
    /**
     * 获取 field
     * 
     * @return uploadFild
     */
    public UploadField getField() {

        return uploadFild;
    }

    /**
     * 实际写数据方法
     * 
     * @param url
     *            文件url
     * @param pkt
     *            包数据
     * @param fileOut
     *            写文件流
     * @param tempFile
     *            临时文件名
     * @param callback
     *            回调对象
     * 
     * @return TODO
     */
    private boolean readData(String url, readPacket pkt, FileOutputStream fileOut, String tempFile,
            DownloadCallback callback) {

        HttpClient httpClient = createHttpClient();
        org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet(url.toString());
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Charset", "UTF-8");
        request.setHeader("Accept-Encoding", "gzip, deflate");

        if (pkt.hasdownSize != 0) {
            request.setHeader("Range", "bytes=" + pkt.hasdownSize + "-");
        }

        String strEncode = new String(Base64.encode("master:123456".getBytes(), Base64.DEFAULT));
        strEncode = strEncode.substring(0, strEncode.length() - 1);
        request.setHeader("Authorization", "Basic " + strEncode);

        DataOutputStream ds = null;

        try {

            HttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();

            if ((HttpURLConnection.HTTP_OK == responseCode) || (HttpURLConnection.HTTP_PARTIAL == responseCode)) {

                Header header = response.getFirstHeader("Content-Length");

                String value = header.getValue();
                int len = Integer.parseInt(value);
                pkt.toalFileSize = pkt.hasdownSize + len;

                InputStream inputstream = response.getEntity().getContent();
                byte[] readbytes = new byte[read_to_bytes_from_server];
                int bytestoread = 0;

                while ((bytestoread = inputstream.read(readbytes)) != -1) {
                    len = len - bytestoread;
                    pkt.hasdownSize = pkt.hasdownSize + bytestoread;
                    fileOut.write(readbytes, 0, bytestoread);

                    if (callback != null) {
                        callback.downloadCallback(RET_CODE_DOWNLODPART_SUCCESS, tempFile, pkt.hasdownSize,
                                pkt.toalFileSize);
                    }
                }

                inputstream.close();
                inputstream = null;

                if (len != 0) {

                    return false;
                }

                return true;
            } else {

                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

            return false;
        } finally {

            try {

                if (ds != null) {
                    ds.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            ds = null;
        }
    }

    /**
     * 实际下载方法，支持文件服务器和普通文件的文件下载
     * 
     * @param fileSource
     *            文件url地址
     * @param fileDest
     *            文件绝对路径
     * @param tempFile
     *            临时文件绝对路径
     * @param callback
     *            回调对象
     * 
     * @return 下载结果
     * @since 2012-9-20 增加对url的检查，如果包含字段fileid=，则认定为服务器新地址，对其进行简单的处理在进行下载， 例如
     *        /mnt/sdcard/Android/data/CoolyouHui/coolyou/orgi/
     *        dbdd997b0c967e93c901be0b44a427b2.jpg, 需要删除最后的后缀名方可进行下载
     */
    public int downloadFile(String fileSource, String fileDest, String tempFile, DownloadCallback callback) {

        if (TextUtils.isEmpty(fileDest) || TextUtils.isEmpty(fileSource)) {

            return RET_CODE_NET_ERROR;
        }

        if (fileSource.contains("fileid=") || fileSource.contains("fileId=")) {

            // 如果包含缩略图等，根据传入的路径确定下载大图还是缩略图
            if (!TextUtils.isEmpty(fileDest) && fileSource.contains(",")) {

                String saveName = fileDest.substring(fileDest.lastIndexOf("/") + 1);

                // 缩略图，原图等
                if (fileSource.contains(saveName)) {
                    fileSource = fileSource.substring(0, fileSource.lastIndexOf("=")) + saveName;
                }
            }

            // 删除.png等类似的文件类型
            fileSource = fileSource.substring(0, fileSource.lastIndexOf("."));

            // 语音文件多加了时间长度
            if (fileSource.contains("_")) {
                fileSource = fileSource.substring(0, fileSource.lastIndexOf("_"));
            }

        } else {

            int fileNameindex = fileSource.lastIndexOf("/");

            if (fileNameindex != -1) {

                String fileNameStr = fileSource.substring(fileNameindex + 1);
                String filePath = fileSource.substring(0, fileNameindex + 1);

                try {
                    fileSource = filePath + (URLEncoder.encode(fileNameStr, "utf-8")).replaceAll("\\+", "%20");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (TextUtils.isEmpty(tempFile)) {

            int temp = FileUtils.canTempFile();

            if (temp == 0) {
                tempFile = Environment.getExternalStorageDirectory().getPath() + System.getProperty("file.separator")
                        + "Android/data/whitespot/profile" + System.getProperty("file.separator")
                        + new Date().getTime();
            } else if (temp == 1) {
                tempFile = "udisk" + System.getProperty("file.separator") + "UserMgrCache"
                        + System.getProperty("file.separator") + new Date().getTime();
            } else if (temp == -1) {

                if (fileDest.contains("/")) {

                    int iEnd = fileDest.lastIndexOf("/");

                    if (iEnd > 0) {
                        tempFile = fileDest.substring(0, iEnd) + System.getProperty("file.separator")
                                + new Date().getTime();
                        ;
                    } else {

                        return RET_CODE_NET_ERROR;
                    }
                } else {

                    return RET_CODE_NET_ERROR;
                }
            }
        }

        int startPos = 0;
        FileInputStream fileInput = null;
        FileOutputStream fileOutput = null;
        FileOutputStream destFileOutputStream = null;

        if (!isConnection()) {

            if (callback != null) {
                callback.downloadCallback(RET_CODE_CREATEDESTFILE_ERROR, "", 0, 0);
            }


            return RET_CODE_NET_ERROR;
        }

        try {

            File file = new File(tempFile);

            if (!file.isFile()) {

                String folderPath = tempFile.substring(0, tempFile.lastIndexOf("/"));
                File folder = new File(folderPath);

                if (!folder.exists()) {

                    if (!folder.mkdirs()) {

                        if (callback != null) {
                            callback.downloadCallback(RET_CODE_CREATETEMPFILE_ERROR, "", 0, 0);
                        }


                        return RET_CODE_CREATETEMPFILE_ERROR;
                    }
                }

                if (!file.createNewFile()) {

                    if (callback != null) {
                        callback.downloadCallback(RET_CODE_CREATETEMPFILE_ERROR, "", 0, 0);
                    }


                    return RET_CODE_CREATETEMPFILE_ERROR;
                }
            }

            File des = new File(fileDest);

            if (des.exists()) {
                des.delete();
            }

            des.createNewFile();
            fileInput = new FileInputStream(file);
            fileOutput = new FileOutputStream(file);
            startPos = fileInput.available();

            readPacket rpk = new readPacket();
            rpk.hasdownSize = startPos;

            if (readData(fileSource, rpk, fileOutput, tempFile, callback)) {

                // File filedest = new File(fileDest+url.getFile());
                File filedest = new File(fileDest);

                if (filedest.isFile()) {
                    filedest.delete();
                }

                if (!filedest.createNewFile()) {

                    if (callback != null) {
                        callback.downloadCallback(RET_CODE_CREATEDESTFILE_ERROR, tempFile, rpk.hasdownSize,
                                rpk.toalFileSize);
                    }

                    return RET_CODE_CREATEDESTFILE_ERROR;
                }

                destFileOutputStream = new FileOutputStream(filedest);

                byte[] writebytes = new byte[bytes_to_write];
                int bytestoread = 0;

                while ((bytestoread = fileInput.read(writebytes)) != -1) {
                    destFileOutputStream.write(writebytes, 0, bytestoread);
                }

                if (callback != null) {
                    callback.downloadCallback(RET_CODE_SUCCESS, tempFile, rpk.hasdownSize, rpk.toalFileSize);
                }

                if (file.exists()) {
                    file.delete();
                }

                return RET_CODE_SUCCESS;
            }

            if (callback != null) {
                callback.downloadCallback(RET_CODE_POSTDATA_ERROR, tempFile, rpk.hasdownSize, rpk.toalFileSize);
            }


            return RET_CODE_POSTDATA_ERROR;
        } catch (Exception e) {

            // TODO: handle exception
            if (callback != null) {
                callback.downloadCallback(RET_CODE_IOEXCETPTION_ERROR, tempFile, 0, 0);
            }


            return RET_CODE_IOEXCETPTION_ERROR;
        } catch (OutOfMemoryError e) {

            // TODO: handle exception
            if (callback != null) {
                callback.downloadCallback(RET_CODE_IOEXCETPTION_ERROR, tempFile, 0, 0);
            }


            return RET_CODE_IOEXCETPTION_ERROR;
        } finally {

            if (fileOutput != null) {

                try {
                    fileOutput.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }

            if (fileInput != null) {

                try {
                    fileInput.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }

            if (destFileOutputStream != null) {

                try {
                    destFileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
    }
    
    private int uploadFile(String fileName, UploadField uField, boolean needSkeleton) {
    	return uploadFile("", fileName, uField, needSkeleton);
    }

    /**
     * 实际上传方法，实现了文件服务器的简单文件上传
     * 
     * @param fileName
     *            文件名称
     * @param uField
     *            上传field
     * @param needSkeleton
     *            是否需要缩略图
     * 
     * @return 上传结果
     */
    private int uploadFile(String urlString, String fileName, UploadField uField, boolean needSkeleton) {

        if (!isConnection()) {

            return RET_CODE_NET_ERROR;
        }

        File file = new File(fileName);

        if (!file.isFile()) {

            return RET_CODE_FILE_ERROR;
        }

        ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();

        if (needSkeleton) {
            ffkvp.add(new FormFieldKeyValuePair("username", "master"));
            ffkvp.add(new FormFieldKeyValuePair("password", "123456"));
            ffkvp.add(new FormFieldKeyValuePair("flag", "1"));
            ffkvp.add(new FormFieldKeyValuePair("type", "0"));
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            int width = (int) (options.outWidth*120/options.outHeight);
            int height = (int) (options.outHeight*width/options.outWidth);
            ffkvp.add(new FormFieldKeyValuePair("rate", width + "," + height));
        } else {
            ffkvp.add(new FormFieldKeyValuePair("flag", "0"));
        }

        String boundary = generatesRandom();
        URL url;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block

            return RET_CODE_URL_ERROR;
        }

        HttpURLConnection con;

        try {
            con = getURLConnection(url);
        } catch (IOException e) {
            // TODO Auto-generated catch block

            return RET_CODE_CONNECTION_ERROR;
        }

        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block

            return RET_CODE_PROTOCOL_ERROR;
        }

        con.setRequestProperty("Connection", "keep-alive");
        // con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Charset", "UTF-8");
        setAuthority(con);
        con.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + boundary);

        DataOutputStream dos = null;
        FileInputStream fileInput = null;

        try {
            dos = new DataOutputStream(con.getOutputStream());

            StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);

            for (FormFieldKeyValuePair temp : ffkvp) {
                contentBody.append("\r\n").append("Content-Disposition: form-data; name=\"").append(
                        temp.getKey() + "\"").append("\r\n").append("\r\n").append(temp.getValue()).append("\r\n")
                        .append("--").append(boundary);
            }

            String boundaryMessage = contentBody.toString();
            dos.write(boundaryMessage.getBytes("utf-8"));

            contentBody = new StringBuffer();
            contentBody.append("\r\n");
            contentBody.append("Content-Disposition: form-data; " + "name=\"file\"" + ";filename" + "=\""
                    + fileName.replace(" ", "") + "\"" + enterNewline);
            contentBody.append("Content-Type: application/octet-stream" + enterNewline);

            contentBody.append(enterNewline);

            dos.write(contentBody.toString().getBytes("utf-8"));

            byte[] readfile = new byte[read_to_bytes];
            fileInput = new FileInputStream(file);

            int byteCount = 0;

            try {

                while ((byteCount = fileInput.read(readfile)) != -1) {
                    dos.write(readfile, 0, byteCount);
                }
            } catch (OutOfMemoryError e) {

                return OUT_OF_MEMORY_ERROR;
            }

            dos.writeBytes(enterNewline + FIX + boundary + FIX + enterNewline);

            int responseCode = con.getResponseCode();

            if (HttpURLConnection.HTTP_OK == responseCode) {

                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;

                responseReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("");
                }

                responseReader.close();

                JSONArray jarray = new JSONArray(sb.toString());
                JSONObject jobject = null;

                // UploadField field = new UploadField();
                if (jarray.length() > 0) {
                    jobject = jarray.getJSONObject(0);
                    uField.filedid = jobject.getString("id");
                    uField.fileUrl = jobject.getString("url");
                    uField.fileMd5 = jobject.getString("md5");
                    uField.skeletonid = jobject.getString("thumbId");
                    uField.skeletonUrl = jobject.getString("thumbUrl");
                    uField.skeletonMd5 = jobject.getString("thumbMd5");
                }


                return RET_CODE_SUCCESS;
            } else {

                return RET_CODE_POSTDATA_ERROR;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

            return RET_CODE_POSTDATA_ERROR;
        } catch (JSONException e) {
            // TODO Auto-generated catch block

            return RET_CODE_JSON_ERROR;
        } finally {

            try {

                if (fileInput != null) {
                    fileInput.close();
                }

                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            fileInput = null;
            dos = null;
            con.disconnect();
            con = null;
        }
    }

    /**
     * 是否网络连接正常
     * 
     * @return 网络状况
     */
    private boolean isConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null) {

            return netInfo.getState() == NetworkInfo.State.CONNECTED;
        } else {

            return false;
        }
    }

    /**
     * 生成随机数
     * 
     * @return 随机数
     */
    private String generatesRandom() {

        Random random = new Random();
        byte[] byteRandom = new byte[64];
        random.nextBytes(byteRandom);

        return random.toString();
    }

    /**
     * 获取url connection对象
     * 
     * @param url
     *            url地址
     * 
     * @return 连接对象
     * 
     * @throws IOException
     *             io异常
     */
    private HttpURLConnection getURLConnection(URL url) throws IOException {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String type = info.getTypeName();
        HttpHost host = null;

        if (type.toUpperCase().contains("MOBILE")) {

            String proxyHost = android.net.Proxy.getDefaultHost();

            if (proxyHost != null) {
                host = new HttpHost(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort());
            }
        }

        URL posturl = url;

        if (host != null) {
            posturl = new URL(host.toURI() + url.getFile());
        }

        HttpURLConnection conn = (HttpURLConnection) posturl.openConnection();

        if (host != null) {
            conn.setRequestProperty("X-Online-Host", url.getHost());
        }

        return conn;
    }

    /**
     * 设置鉴权
     * 
     * @param conn
     *            链接对象
     */
    private void setAuthority(HttpURLConnection conn) {

        String strEncode = new String(Base64.encode("master:123456".getBytes(), Base64.DEFAULT));
        strEncode = strEncode.substring(0, strEncode.length() - 1);
        conn.setRequestProperty("Authorization", "Basic " + strEncode);
    }

    /**
     * 创建httpClient对象
     * 
     * @return HttpClient
     */
    protected HttpClient createHttpClient() {

        HttpParams httpParameters = new BasicHttpParams();
        HttpClient httpClient = null;

        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

        HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
        HttpProtocolParams.setUserAgent(httpParameters, "HttpComponents/1.1");
        HttpProtocolParams.setUseExpectContinue(httpParameters, true);

        HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
        HttpConnectionParams.setSoTimeout(httpParameters, 20000);
        httpClient = new DefaultHttpClient(httpParameters);

        return httpClient;
    }

    /**
     * 下载回调定义
     * 
     * @author $zhaohao1$
     * @version $1.0$
     */
    public interface DownloadCallback {

        /**
         * 下载结果通知
         * 
         * @param rcode
         *            TODO
         * @param fileUri
         *            TODO
         * @param downloadSize
         *            TODO
         * @param totalSize
         *            TODO
         */
        public void downloadCallback(int rcode, String fileUri, long downloadSize, long totalSize);
    }

    /**
     * 上传回调定义
     * 
     * @author $zhaohao1$
     * @version $1.0$
     */
    public interface UploadCallback {

        /**
         * 上传回调通知
         * 
         * @param rcode
         *            TODO
         * @param object
         *            TODO
         */
        public void uploadCallback(int rcode, UploadField object);
    }

    /**
     * TODO
     * 
     * @author 
     * @version $Revision: 1.0$
     */
    private class DownloadRunable implements Runnable {

        /** TODO */
        private String fileSource = "";

        /** TODO */
        private String tempfile = "";

        /** TODO */
        private String fileSavePath = "";

        /** TODO */
        private DownloadCallback callback = null;

        /**
         * Creates a new DownloadRunable object.
         * 
         * @param fileSource
         *            TODO
         * @param fileDestPath
         *            TODO
         * @param tempFile
         *            TODO
         * @param callback
         *            TODO
         */
        public DownloadRunable(String fileSource, String fileDestPath, String tempFile, DownloadCallback callback) {
            this.fileSource = fileSource;
            this.fileSavePath = fileDestPath;
            this.callback = callback;
            this.tempfile = tempFile;
        }

        /**
         * TODO
         */
        public void run() {
            // TODO Auto-generated method stub
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            downloadFile(fileSource, this.fileSavePath, tempfile, callback);
        }
    }

    /**
     * TODO
     * 
     * @author 
     * @version $Revision: 1.0$
     */
    private class UploadRunable implements Runnable {

        /** TODO */
        private String filename = "";

        /** TODO */
        private UploadCallback callback = null;

        /** TODO */
        private boolean needSkeleton;

        /**
         * Creates a new UploadRunable object.
         * 
         * @param fileName
         *            TODO
         * @param needSkeleton
         *            TODO
         * @param callback
         *            TODO
         */
        public UploadRunable(String fileName, boolean needSkeleton, UploadCallback callback) {
            this.filename = fileName;
            this.callback = callback;
            this.needSkeleton = needSkeleton;
        }

        /**
         * TODO
         */
        public void run() {
            // TODO Auto-generated method stub
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            UploadField field = new UploadField();
            int rCode = uploadFile(filename, field, needSkeleton);
            callback.uploadCallback(rCode, field);
        }
    }

    /**
     * TODO
     * 
     * @author  
     * @version $Revision: 1.0$
     */
    private class readPacket {

        /** TODO */
        public long hasdownSize = 0;

        /** TODO */
        public long toalFileSize = 0;
    }

    /**
     * TODO
     * 
     * @author $zhaohao1$
     * @version $Revision: 1.0$
     */
    public class UploadField {

        /** TODO */
        public String filedid = "";

        /** TODO */
        public String fileUrl = "";

        /** TODO */
        public String fileMd5 = "";

        /** TODO */
        public String skeletonid = "";

        /** TODO */
        public String skeletonUrl = "";

        /** TODO */
        public String skeletonMd5 = "";
    }

    /**
     * FormFieldKeyValuePair
     * 
     * @author lijie
     */
    class FormFieldKeyValuePair implements Serializable {

        /** TODO */
        private static final long serialVersionUID = 1L;

        /** TODO */
        private String key;

        /** TODO */
        private String value;

        /**
         * Creates a new FormFieldKeyValuePair object.
         * 
         * @param key
         *            TODO
         * @param value
         *            TODO
         */
        public FormFieldKeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * TODO
         * 
         * @return TODO
         */
        public String getKey() {

            return key;
        }

        /**
         * TODO
         * 
         * @param key
         *            TODO
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * TODO
         * 
         * @return TODO
         */
        public String getValue() {

            return value;
        }

        /**
         * TODO
         * 
         * @param value
         *            TODO
         */
        public void setValue(String value) {
            this.value = value;
        }
    }
}
