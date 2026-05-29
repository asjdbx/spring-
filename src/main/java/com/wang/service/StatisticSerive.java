package com.wang.service;

import java.time.LocalDate;

public interface StatisticSerive {
    Boolean updateStatistic ( Integer articleId,Integer userid);
    void insertStatistic(Integer articleId,  LocalDate readDate);
    Integer countByArticleIdAndDate( Integer articleId);
    void readta();
    void week();
//    void updateWeek(Integer articleId);
}
