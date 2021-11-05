package com.healthsteward.shiro;

import com.healthsteward.filter.JWTFilter;
import com.healthsteward.shiro.jwt.JWTMatcher;
import com.healthsteward.shiro.realm.JWTRealm;
import com.healthsteward.shiro.realm.UsernamePasswordRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Bean(name = "SecurityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        // 设置Authenticator
        securityManager.setAuthenticator(getMyModularRealmAuthenticator());

        // 设置多个Realm
        List<Realm> realms = new ArrayList<>();
        realms.add(getUsernamePasswordRealm());
        realms.add(getJWTRealm());
        securityManager.setRealms(realms);

        return securityManager;
    }

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     *
     * @return ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager){
        // 0. autowire securityManager
        ShiroFilterFactoryBean filterFactoryBean  = new ShiroFilterFactoryBean();

        // 1. basic configuration
        filterFactoryBean.setSecurityManager(securityManager);
//        System.out.println("get securityManager... "+ (securityManager!=null) );
//        filterFactoryBean.setLoginUrl("/login");
//        filterFactoryBean.setSuccessUrl("/userInfo"); // !! 临时设置

//
        // 2 custom interceptor
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwtFilter", new JWTFilter());
        filterFactoryBean.setFilters(filterMap);
//
//        // 3 interceptor
//        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
////        filterChainDefinitionMap.put("/login", "anon");
////        filterChainDefinitionMap.put("/index", "anon");
//        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/config/**", "anon");
//        filterChainDefinitionMap.put("/unauthorized/**", "anon");
////        filterChainDefinitionMap.put("/logout/**", "logout");;
////        filterChainDefinitionMap.put("/**", "urlFilter");
//        filterChainDefinitionMap.put("/**", "jwtFilter");
//        filterFactoryBean.setUnauthorizedUrl("/unauthorized");
//        // summarize
//        filterFactoryBean.setFilters(customisedFilter);
//        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, String> filterRuleMap = new LinkedHashMap<String, String>();
        filterRuleMap.put("/filterEx/jwtAuthFail", "anon");

//        filterRuleMap.put("/**/dietManage/clients/**/detail/features/get", "urlFilter");

        filterRuleMap.put("/**/HealthSteward/**", "jwtFilter");
        filterRuleMap.put("/**/HistoryServices/**", "jwtFilter");
        filterRuleMap.put("/**/StarServices/**", "jwtFilter");
        filterRuleMap.put("/**/ShoppingMall/**/ServiceBuy/**", "jwtFilter");

        filterRuleMap.put("/**", "anon");
        filterRuleMap.put("/unauthorized/**", "anon");
        filterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);




        return filterFactoryBean;
    }


    /**
     * 多realm认证器，决定当前token会匹配到哪些relam
     */
    @Bean
    public ModularRealmAuthenticator getMyModularRealmAuthenticator(){
        // 自己重写的ModularRealmAuthenticator
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 每个realm要设置自己的CredentialsMatcher
     */
    @Bean
    public UsernamePasswordRealm getUsernamePasswordRealm(){
        UsernamePasswordRealm loginRealm = new UsernamePasswordRealm();
        loginRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return loginRealm;
    }

    @Bean
    public JWTRealm getJWTRealm(){
        JWTRealm jwtRealm = new JWTRealm();
        jwtRealm.setCredentialsMatcher(jwtMatcher());
        return jwtRealm;
    }

    /**
     * login matcher, 密码验证的规则设置
     *
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }

    @Bean
    public JWTMatcher jwtMatcher(){
        return new JWTMatcher();
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }



}
