package com.wang.service;

import com.wang.popj.Article;
import com.wang.popj.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ArticleSerivce {
    void add(Article article);

    List<Article> getArticleList();

    void updateArticle(Article article);

    void deleteArticle(Integer id);

    Article findById(Integer id);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    PageBean<Article> listAll(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    List<Article> listAlls(String state);

    void updateState(Integer id,String state);

    Boolean increaseReading(Integer id,Integer userid);

    Integer getAllReading();

    Integer getTodayReading();

    Integer getWeekReading();

    Double getAvgReading();

    List<Integer> getTotalReadingByTime(
            @Param("startTime") LocalDateTime startTime,//不带时区的日期和时间
            @Param("endTime") LocalDateTime endTime
    );

//    int batchUpdateCoverImg();
//    int batchUpdateCoverImg() ;
}
