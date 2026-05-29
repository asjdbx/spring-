package com.wang.mapper;

import com.wang.popj.history;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface historyMapper {

    @Insert("insert into reading_history (user_id,article_id,read_Time,read_Duration,read_type) values ( #{userid},#{articleId},#{readTime},#{readDuration},#{readTtype})")
    void inserts(history history);

    @Select("select *from reading_history where user_id = #{userid}")
    List<history> selectAll(int userid);

    @Select("select *from reading_history where user_id = #{userid} and article_id = #{articleId}")
    Integer selectHistory(int userid,int articleId);

    @Delete("delete from reading_history where user_id = #{userid}")
    void deleteByUserId(int userid);

    @Update("update reading_history set read_Time = NOW() where user_id = #{userid} and article_id = #{articleId}")
    void upadteReadType(int userid,int articleId);
    @Select("select count(*) from reading_history where user_id = #{userid} and article_id = #{articleId}")
    Integer selectHistoryCount(int userid, int articleId);
}
