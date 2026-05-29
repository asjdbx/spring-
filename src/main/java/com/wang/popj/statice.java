package com.wang.popj;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class statice {
    private  Integer id;
    @NotNull
    private  Integer articleId;
    @NotNull
    private LocalDateTime readDate;
    private  Integer dailyReads;
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;
    private  Integer week;
    private  Integer readata;

}
