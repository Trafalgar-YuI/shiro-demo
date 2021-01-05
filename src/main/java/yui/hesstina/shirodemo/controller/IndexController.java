package yui.hesstina.shirodemo.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yui.hesstina.shirodemo.constant.UserInfo;
import yui.hesstina.shirodemo.pojo.User;
import yui.hesstina.shirodemo.util.JwtUtils;

import java.util.Collections;
import java.util.HashSet;

/**
 * @package yui.hesstina.shirodemo.controller
 * @class IndexController
 * @description
 * @author YuI
 * @create 2021/1/5 1:52 
 * @since
 **/
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/login")
    public String login(User user) {
        // 替换成 JWT
        String token = JwtUtils.generateToken();
        UserInfo.USER_MAP.put(token, user);
        UserInfo.ROLES_MAP.put(user.getName(), new HashSet<>(Collections.singletonList("admin")));

        return token;
    }

    @RequiresRoles(value = {"admin"})
    @GetMapping("/testAdmin")
    public String testAdmin() {
        return "test";
    }

    @RequiresRoles(value = {"user"})
    @GetMapping("/testUser")
    public String testUser() {
        return "test";
    }

}
