package com.healthsteward.shiro.realm;

import com.healthsteward.pojo.DO.auth.Authentication;
import com.healthsteward.service.interfaces.auth.AuthService;
import com.healthsteward.shiro.CustomToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernamePasswordRealm extends AuthorizingRealm {

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
        String userName = token.getPrincipal().toString();

        // 获取数据库中的密码
        if (!authService.checkUserExistence(userName)) {
            throw new AuthenticationException("用户名不存在");
        }


        Authentication auth = authService.getAuthByUserName(userName);
        String passwordInDB = auth.getPasswordInDB();
        String salt = auth.getSalt();

        // 这样通过 HashedCredentialsMatcher 进行自动校验
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userName, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return info;
    }
}
