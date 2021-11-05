package com.healthsteward.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 如果有多个Realm, 默认RealmAuthenticator使用所有配置的Realm
 *
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(CustomModularRealmAuthenticator.class);

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        assertRealmsConfigured();

        // 从参数authenticationToken来判断需要由那个realm处理
        CustomToken token = (CustomToken)authenticationToken;

        Collection<Realm> realms = getRealms();
        Realm resRealm = null;
        for (Realm realm : realms) {
            if (realm.getName().contains(token.getAuthType().toString())){
                resRealm = realm;
                break;
            }
        }

        return doSingleRealmAuthentication(resRealm, token);
    }
}
