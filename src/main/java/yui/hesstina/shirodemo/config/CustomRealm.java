package yui.hesstina.shirodemo.config;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ObjectUtils;

import yui.hesstina.shirodemo.bean.StatelessToken;
import yui.hesstina.shirodemo.constant.UserInfo;
import yui.hesstina.shirodemo.util.JwtUtils;

/**
 * 自定义 realm
 *
 * @package yui.hesstina.shirodemo.config
 * @class CustomRealm
 * @author YuI
 * @create 2021/1/4 23:01 
 * @since
 **/
public class CustomRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();

        Set<String> roles = UserInfo.ROLES_MAP.get(userName);
        if (ObjectUtils.isEmpty(roles)) {
            return null;
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);

        Set<String> permissions = UserInfo.PERMISSION_MAP.get(userName);
        if (ObjectUtils.isEmpty(permissions)) {
            return null;
        }
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        StatelessToken token = (StatelessToken) authenticationToken;
        if (token == null || token.getToken() == null) {
            return null;
        }

        String userName = JwtUtils.getIssuer(token.getToken());

        token.setUserName(userName);

        return new SimpleAuthenticationInfo(userName, token.getToken(), this.getName());
    }
}
