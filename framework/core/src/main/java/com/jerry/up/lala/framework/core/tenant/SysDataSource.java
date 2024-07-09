package com.jerry.up.lala.framework.core.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jerry.up.lala.framework.core.common.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>Description: 多数据源配置
 *
 * @author FMJ
 * @date 2023/9/5 17:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_data_source")
public class SysDataSource extends Entity {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID(连接池名称)
     */
    private String tenantId;

    /**
     * 数据库连接地址
     */
    private String ip;

    /**
     * 数据库链接端口
     */
    private String port;

    /**
     * 数据库用户名
     */
    private String userName;

    /**
     * 数据库密码
     */
    private String passWord;

}