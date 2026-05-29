package com.wang.mapper;

import com.wang.popj.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    // 注册用户
    @Insert("insert into user(username,password,create_time,update_time,role,status) "
            +"values (#{username},#{password},now(),now(),#{role},1)")
    void add(String username, String password,String role);
    //更新用户信息
    @Update("update user set  nickname = #{nickname},email = #{email},update_time = now() where id = #{id}")
    void update(User user);

    //更新用户信息
    @Update("update user set role= #{role} where id = #{id}")
    void updates(String role,Integer id);

    //更新用户状态
    @Update("update user set status= #{status} where id = #{id}")
    void updateStatus(String status,Integer id);

    //更新用户头像
    @Update("update user set  user_pic = #{avatar},update_time = now() where id = #{id}")
    void updateAvatar(String avatar, Integer id);


    //更新用户密码
    @Update("update user set  password = #{md5String},update_time = now() where id = #{id}")
    void updatePassword(String md5String, Integer id);

    //删除用户
    @Update("delete from user where id = #{id}")
    void delete(Integer id);

    //查询所有用户
    @Select("select * from user")
    List<User> findAll();

//    public interface UserRepository extends JpaRepository<User, Long> {
//        Optional<User> findByUsername(String username);
//    }
}
