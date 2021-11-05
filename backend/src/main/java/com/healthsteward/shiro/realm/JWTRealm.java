package com.healthsteward.shiro.realm;

import com.healthsteward.service.interfaces.auth.AuthService;
import com.healthsteward.shiro.CustomToken;
import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class JWTRealm extends AuthorizingRealm {
    @Autowired
    private AuthService authService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomToken token = (CustomToken) authenticationToken;
        String jwtToken = (String) token.getCredentials();

        String userName = (String) JWTUtil.getUserName(jwtToken);

        if (!authService.checkUserExistence(userName)) {
            // token里的用户是非法的！
            throw new AuthenticationException("用户名不存在");
        }

        return new SimpleAuthenticationInfo(userName, jwtToken, getName());
    }
}
