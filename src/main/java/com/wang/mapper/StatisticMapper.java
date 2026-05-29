package com.wang.mapper;

import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface StatisticMapper {
    // 插入新的阅读统计记录
    @Insert("""
        INSERT INTO statice (article_id, read_date, daily_reads, create_time, update_time,week,readata)
        VALUES (#{articleId}, #{readDate}, 1, NOW(), NOW(),1,1)
    """)
    void insertStatistic( Integer articleId, LocalDate readDate);

    // 更新已有统计记录
    @Update("""
        UPDATE statice 
        SET daily_reads = daily_reads + 1, 
            week = week + 1
        WHERE article_id = #{articleId}
    """)
    void updateWeek(Integer articleId);
    // 更新已有统计记录的阅读量
    @Update("""
        UPDATE statice 
        SET daily_reads = daily_reads + 1, 
            week = week + 1,
            readata=readata+1,
            read_date = NOW()
        WHERE article_id = #{articleId}
    """)
    void updateStatistic(Integer articleId);

    // 删除过期的统计记录
    @Update("update statice set daily_reads=0 where DATE(read_date) < CURDATE() ")
    void readta();

    //一个星期
    @Update("update statice set week=0, update_time = NOW() WHERE update_time <CURDATE() - INTERVAL 6 DAY")
    void week();

    // 查询某天是否已有统计记录
    @Select("SELECT COUNT(*) FROM statice WHERE article_id = #{articleId}")
    Integer countByArticleIdAndDate(Integer articleId);

}
