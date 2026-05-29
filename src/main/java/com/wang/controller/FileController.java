package com.wang.controller;

import com.wang.popj.Result;
import com.wang.utils.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController {

    /**
     * MultipartFile 是 Spring 框架提供的一个接口，
     * 用于处理 HTTP 多部分请求（multipart/form-data）
     * 中的文件上传。当客户端通过表单上传文件时，Spring
     * 会将文件内容封装为 MultipartFile 对象，方便在控制器中接收和处理
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件上传到本地服务器
        String fileName =file.getOriginalFilename();
        String name= UUID.randomUUID().toString()+fileName.substring(fileName.lastIndexOf("."));
        //getOriginalFilename:获取上传文件的原始文件名（包含扩展名）。
//        file.transferTo(new File("D:\\blong地址\\File\\"+fileName));
//        System.out.println(file.getInputStream());
        String url=FileUtils.uploadFile(name,file.getInputStream());
        //transferTo:将上传的文件写到服务器上指定的文件。

        System.out.println(url);
        return Result.success(url);
    }
}
