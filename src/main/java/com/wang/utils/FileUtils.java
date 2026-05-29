package com.wang.utils;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.InputStream;

public class FileUtils {
    // 修改域名格式为正确的区域节点
    //private static final String DOMAIN = "http://t42b10v5v.hn-bkt.clouddn.com"; // 华南区域节点
    private static final String DOMAIN = "http://tds79gjsg.hn-bkt.clouddn.com";//在七牛云空间域名里

    private static final String ACCESS_KEY = "coMVA_0oX86cqaQAoOMRI8989Zo6Mwnh1ArNIAJ8";
    private static final String SECRET_KEY = "OAHDwZ6YUpC7IX2FIOr6oOImB13ikqTckJuA-PlW";
    private static final String BUCKET = "blong3";

    public static String uploadFile(String filePath, InputStream in) {
//        Configuration cfg = new Configuration(Region.autoRegion());
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UploadManager uploadManager = new UploadManager(cfg);

        // 使用传入的文件路径作为key
        String key = filePath;
        String url = "";
        try {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(BUCKET);

            Response response = uploadManager.put(in, key, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            // 正确构造URL格式：http://<域名>/<文件key>
            url = DOMAIN + "/" + putRet.key;

            System.out.println("Key: " + putRet.key);
            System.out.println("Hash: " + putRet.hash);
            System.out.println("File URL: " + url);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 上传失败返回null
        }
        return url;
    }
}