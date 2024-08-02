package com.jerry.up.lala.framework.boot.tenant;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jerry.up.lala.framework.boot.mapper.SysDataSourceMapper;
import com.jerry.up.lala.framework.common.exception.ServiceException;
import com.jerry.up.lala.framework.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: 集团处理器 字段隔离
 *
 * @author FMJ
 * @date 2023/9/5 09:17
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "mode", havingValue = "DATA_SOURCE")
public class TenantDataSourceHandler implements CommandLineRunner, AsyncHandlerInterceptor {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SysDataSourceMapper sysDataSourceMapper;

    @Autowired
    private DruidDataSourceCreator druidDataSourceCreator;

    @Override
    public void run(String... args) {
        try {
            refresh();
        } catch (Exception e) {
            log.error("初始化数据源异常", e);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = TenantContext.getTenantId();
        DynamicDataSourceContextHolder.push(StringUtil.isNull(tenantId) ? "master" : tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DynamicDataSourceContextHolder.clear();
    }

    public void refresh() {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
            Set<String> dsSet = ds.getDataSources().keySet();
            List<SysDataSource> dataSourceList = sysDataSourceMapper.selectList(Wrappers.emptyWrapper());
            if (CollectionUtils.isNotEmpty(dataSourceList)) {
                dataSourceList.forEach(item -> {
                    String tenantId = item.getTenantId();
                    // 判断是否存在于动态数据源
                    if (dsSet.contains(tenantId)) {
                        // 先删除 原有配置
                        ds.removeDataSource(tenantId);
                    }
                    ds.addDataSource(tenantId, druidDataSourceCreator.createDataSource(loadDataSource(item)));
                });
            }
        } catch (Exception e) {
            throw ServiceException.error(e);
        }
    }

    private DataSourceProperty loadDataSource(SysDataSource sysDataSource) {
        DataSourceProperty result = new DataSourceProperty();
        result.setLazy(true);
        result.setPoolName(sysDataSource.getTenantId());
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");
        result.setUrl("jdbc:mysql://" + sysDataSource.getIp() + ":" + sysDataSource.getPort()
                + "/lala_boot?autoReconnect=true&useAffectedRows=true&useSSL=false&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2b8&allowMultiQueries=true");
        result.setUsername(sysDataSource.getUserName());
        result.setPassword(sysDataSource.getPassWord());
        return result;
    }

}
