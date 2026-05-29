package com.wang;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;

import java.io.*;

//构造一个带指定 Region 对象的配置类
public class Demo {
    @Test
    public void test() {

            Configuration cfg = new Configuration(Region.autoRegion()); // 明确指定 Region
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
            UploadManager uploadManager = new UploadManager(cfg);

            String accessKey = "coMVA_0oX86cqaQAoOMRI8989Zo6Mwnh1ArNIAJ8";
            String secretKey = "OAHDwZ6YUpC7IX2FIOr6oOImB13ikqTckJuA-PlW";
            String bucket = "blong1";

            String key = null; // 指定存储路径和文件名（避免哈希命名）

            try (InputStream byteInputStream = new FileInputStream("D:\\blong地址\\File\\11.png")) {
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);

                Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                System.out.println("Key: " + putRet.key);   // 应该输出 "test/11.png"
                System.out.println("Hash: " + putRet.hash); // 文件的 etag

                // 构造访问 URL（需要你绑定域名）
                String domain = "http://your-bucket-domain.com";
                String fileUrl = domain + "/" + putRet.key;
                System.out.println("File URL: " + fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
