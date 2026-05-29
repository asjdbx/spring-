package com.wang.service.impl;
import com.wang.mapper.ArticleMapper;
import com.wang.mapper.UserMapper;
import com.wang.popj.User;
import com.wang.service.UserService;
import com.wang.utils.Md5Util;
import com.wang.utils.UserID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
            private ArticleMapper articleMapper;
    UserID id=new UserID();

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        return user;
    }

    @Override
    public void register(String username, String password,String role) {
       //加密
//        System.out.println(password);
        String md5Password=Md5Util.getMD5String(password);
        userMapper.add(username,md5Password,role);
    }

    @Override
    public void updateAvatar(String avatar) {
        userMapper.updateAvatar(avatar,id.userid());
    }

    @Override
    public void update(User user) {
        //LocalDateTime不带时区的日期和时间
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updatePassword( String newPassword) {
        userMapper.updatePassword(Md5Util.getMD5String(newPassword),id.userid());
        System.out.println(id.userid()+"                 " +newPassword);
    }

    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public void updates(String  role, Integer id) {
        userMapper.updates(role,id);
    }

    @Override
    public List<User> findAll() {
       return userMapper.findAll();
    }

    @Override
    public void updateStatus(String status, Integer id) {
        userMapper.updateStatus(status,id);
    }

    @Transactional
    public void deleteUserWithArticles(Integer userId) {
        // 方式1：先删除关联文章，再删除用户
        articleMapper.delete(userId);
        userMapper.delete(userId);
    }

}
