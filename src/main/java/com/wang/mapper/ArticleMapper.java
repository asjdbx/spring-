package com.wang.mapper;

import com.wang.popj.Article;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
    //新增文章
    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time,artiState,reading,today_reads)"
            +" values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime},0,0,0)")
    void add(Article article);


    //获取文章列表
    @Select("select * from article where create_user = #{userid}")
    List<Article> getArticleList(int userid);


    //更新文章
    @Update("update article set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state},category_id=#{categoryId},update_time=#{updateTime} where id=#{id}")
    void update(Article article);

    //删除文章
    @Update("delete from article where id=#{id}")
    void delete(Integer id);

    //根据id查询文章
    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    //分页查询文章
    List<Article> list(Integer userid, Integer categoryId, String state);

    List<Article> listAll( Integer categoryId, String state);

    //全部文章
    @Select("select * from article where state=#{state}")
    List<Article> listAlls(String state);

    //修改文章状态
    @Update("update article set artiState=#{state} where id=#{id}")
    void updateState(Integer id,String state);

    //更新文章阅读量
    @Update("UPDATE article SET reading = reading + 1 WHERE id = #{id}")
    void increaseReading(Integer id);


    //全部阅读量
    @Select("select SUM(reading) from article")
    Integer getAllReading();

    //今日阅读量 DATE_FORMAT(create_time, '%Y-%m-%d')：将 create_time（文章创建时间）格式化为 年-月-日（忽略时分秒）。
    //DATE_FORMAT(NOW(), '%Y-%m-%d')：获取当前日期（年-月-日）。
    @Select(" SELECT COALESCE(SUM(s.daily_reads), 0) " +
            "        FROM statice s " +
            "        JOIN article a ON s.article_id = a.id "+
            "        WHERE DATE(s.read_date) = CURDATE() ")
    Integer getTodayReading();

    //查询一周阅读量
    @Select("SELECT COALESCE(SUM(s.week), 0) \n" +
            "        FROM statice s \n" +
            "        JOIN article a ON s.article_id = a.id \n" +
            "        WHERE s.update_time >= CURDATE() - INTERVAL 6 DAY")
    Integer getWeekReading();

    //平均阅读量
    @Select("SELECT AVG(reading) FROM article")
    Double getAvgReading();  // 返回类型改为 Double，因为平均值可能是小数

    //通过时间查询文章
    @Select("""
    SELECT DATE(s.read_date) as date,
           COALESCE(SUM(s.daily_reads), 0) as sumReading 
    FROM statice s
    JOIN article a ON s.article_id = a.id  
    WHERE s.read_date BETWEEN #{startTime} AND #{endTime}
    GROUP BY DATE(s.read_date)
    ORDER BY date
""")
    List<Map<String, Object>> getDateAndReadingByTime(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    //图片路径
//    @Update("UPDATE article " +
//            "SET cover_img = REPLACE(cover_img, 'blong1.sypo3c5e8.hn-bkt.clouddn.com', 't42b10v5v.hn-bkt.clouddn.com') " +
//            "WHERE cover_img LIKE '%blong1.sypo3c5e8.hn-bkt.clouddn.com%'")
//    int batchUpdateCoverImg();
}
