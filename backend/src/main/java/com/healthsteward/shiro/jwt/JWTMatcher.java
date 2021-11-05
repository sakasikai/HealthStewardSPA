package com.healthsteward.shiro.jwt;

import com.healthsteward.util.ZQTemp.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTMatcher extends SimpleCredentialsMatcher {
    private static final Logger logger = LoggerFactory.getLogger(JWTMatcher.class);
    @Override
    public boolean doCredentialsMatch(AuthenticationToken tokenCredentials,
                                      AuthenticationInfo accountCredentials){
        String token = (String)getCredentials(tokenCredentials);

//        logger.info("token validating...");
        // jwt token 验证 userName 是否可信
        return JWTUtil.verify(token, (String) JWTUtil.getUserName(token));
    }
}