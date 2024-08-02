package com.jerry.up.lala.framework.boot.satoken;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: sa-token jwt 配置
 *
 * @author FMJ
 * @date 2023/9/4 14:47
 */
@Configuration
public class SaTokenJwtConfig {

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

}
