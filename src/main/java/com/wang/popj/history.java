package com.wang.popj;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class history {

    private  Integer id;//主键ID
    @NotNull
    private  Integer userid;//用户ID
    @NotNull
    private  Integer articleId;//文章ID
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;//阅读时间
    private  Integer readDuration;//阅读时长
    private  Integer readTtype;//阅读类型

}
