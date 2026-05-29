package com.wang.controller;

import com.wang.popj.Result;
import com.wang.popj.User;
import com.wang.service.UserService;
import com.wang.utils.JwtUtil;
import com.wang.utils.Md5Util;
import com.wang.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 注册接口
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password,
                         String role) {
        //查询用户是否存在
        User user = userService.findByUsername(username);
        System.out.println(role);
        if ("null".equals(role)) {
            return Result.error("不能为null");
        }
        if (user == null) {
            //保存用户
            userService.register(username, password, role);
            return Result.success();
        } else {
            //占用用户名
            return Result.error("用户名已存在");
        }
    }

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户是否存在
        User user = userService.findByUsername(username);
        if (user == null) {
            //用户名不存在
            return Result.error("用户名不存在");
        }
        if (user.getStatus().equals("0")) {
            return Result.error("用户被禁用");
        }
        //判断密码是否正确
        if (Md5Util.getMD5String(password).equals(user.getPassword())) {
            Map<String, Object> jw = new HashMap<>();
            jw.put("id", user.getId());
            jw.put("username", user.getUsername());
            //生成token
            String token = JwtUtil.genToken(jw);
//            System.out.println(jw.toString());
            //token存储到redis中
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(token, token, 1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    /**
     * 获取用户信息接口
     */
    @RequestMapping(value = "/info")
    public Result<User> info() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    /**
     * 跟新用户信息
     *
     * @param user
     * @return
     */
    @PutMapping(value = "/update")
    public Result update(@RequestBody @Validated User user) {
        System.out.println(user);
        userService.update(user);
        return Result.success();
    }

    /**
     * 更新头像
     *
     * @param avatar
     * @return
     */

    @PatchMapping(value = "/updateAvatar")
    public Result updateAvatars(@RequestParam @URL String avatar) {
        userService.updateAvatar(avatar);
        return Result.success();
    }

    /**
     * 更新密码
     *
     * @param params
     * @return
     */

    @PatchMapping(value = "/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
//        System.out.println(params.toString()+"                 "+token);
        //校正密码
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String newPassword2 = params.get("newPassword2");

        if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword) || !StringUtils.hasLength(newPassword2)) {
            return Result.error("参数不能为空");
        }

        //service层拿到用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        System.out.println("用户名"+username);
        User user = userService.findByUsername(username);
        System.out.println("用户名2"+username);
        if (!Md5Util.checkPassword(oldPassword, user.getPassword())) {
            return Result.error("原密码错误");
        }

        if (!newPassword.equals(newPassword2)) {
            return Result.error("两次密码不一致");
        }
        if (oldPassword.length() > 12 || newPassword.length() > 12 || newPassword2.length() > 12) {
            return Result.error("密码长度不能大于12位");
        }
        if (oldPassword.length() < 4 || newPassword.length() < 4 || newPassword2.length() < 4) {
            return Result.error("密码长度不能小于4位");
        }
        //更新密码

        userService.updatePassword(newPassword);
        //删除redis中的token
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result delete(Integer id) {
        userService.deleteUserWithArticles(id);
        return Result.success();
    }

    /**
     * 更新用户角色
     *
     * @param role
     * @return
     */
    @PatchMapping(value = "/updateRole")
    public Result updates(@RequestParam String role, @RequestParam Integer id) {
        userService.updates(role,id);
        return Result.success();
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping(value = "/findAll")
    public Result <List<User>> findAll() {
        List<User> users = userService.findAll();
//        System.out.println(users);
        return Result.success(users);
    }

    /**
     * 更新用户状态
     *
     * @param status
     * @param id
     * @return
     */
    @PatchMapping("/updateStatus")
    public Result updateStatus(@RequestParam String status, @RequestParam Integer id) {
        userService.updateStatus(status, id);
        return Result.success();
    }


}
