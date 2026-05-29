package com.wang.service;

import com.wang.popj.User;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public interface UserService  {
    User findByUsername(String username);

    void register(String username, String password,String role);

    void updateAvatar(@URL String avatar);

    void update(User user);

    void updatePassword(String password);

    void delete(Integer id);

    void updates(String role,Integer id);

    List<User> findAll();

    void updateStatus(String status,Integer id);

    void deleteUserWithArticles(Integer id);

}
