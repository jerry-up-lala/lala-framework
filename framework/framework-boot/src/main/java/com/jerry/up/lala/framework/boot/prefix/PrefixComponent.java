package com.jerry.up.lala.framework.boot.prefix;

import com.jerry.up.lala.framework.boot.properties.CommonProperties;
import com.jerry.up.lala.framework.common.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 前缀组件, 防止多套系统 共用一个环境，加入项目名称，服务名称 进行区分
 *
 * @author FMJ
 * @date 2024/8/2 10:37
 */
@Component
public class PrefixComponent {

    @Autowired
    private CommonProperties commonProperties;

    public String projectName(String key) {
        return projectName(key, CommonConstant.REDIS_KEY_SEPARATOR);
    }

    public String projectName(String key, String separator) {
        return commonProperties.getProjectName() + separator + key;
    }

    public String serverName(String key) {
        return serverName(key, CommonConstant.REDIS_KEY_SEPARATOR);
    }

    public String serverName(String key, String separator) {
        return commonProperties.getProjectName() +  separator + commonProperties.getServerName() + separator + key;
    }
}
