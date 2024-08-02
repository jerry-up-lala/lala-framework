package com.jerry.up.lala.framework.boot.mybatis;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteBatchByIds;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.jerry.up.lala.framework.boot.tenant.TenantColumnHandler;
import com.jerry.up.lala.framework.boot.tenant.TenantModeEnum;
import com.jerry.up.lala.framework.boot.tenant.TenantProperties;
import com.jerry.up.lala.framework.boot.tenant.TenantTableNameHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>Description: mybatis配置
 *
 * @author FMJ
 * @date 2023/8/16 15:45
 */
@MapperScan(basePackages = {"com.jerry.up.lala.**.mapper"})
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private TenantProperties tenantProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 集团模式
        TenantModeEnum mode = tenantProperties.getMode();
        if (TenantModeEnum.COLUMN_NAME.equals(mode)) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantColumnHandler(tenantProperties)));
        }
        if (TenantModeEnum.TABLE_NAME.equals(mode)) {
            interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor(new TenantTableNameHandler(tenantProperties)));
        }
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(-1L);
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * Id 生成器
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }

    /**
     * sql 注入器
     */
    @Bean
    public DefaultSqlInjector easySqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
                methodList.add(new InsertBatchSomeColumn(t -> !t.isLogicDelete()));
                methodList.add(new AlwaysUpdateSomeColumnById());
                methodList.add(new LogicDeleteBatchByIds());
                return methodList;
            }

        };
    }

}
