package com.jerry.up.lala.framework.core.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jerry.up.lala.framework.core.satoken.SaTokenUtil;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * <p>Description: 实体
 *
 * @author FMJ
 * @date 2023/8/16 15:26
 */
@Data
@FieldNameConstants
public class Entity {

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private String updateUser;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;

    public void loadCreate() {
        setCreateUser(SaTokenUtil.currentUser().getUserId());
        setCreateTime(new Date());
    }

    public void loadUpdate() {
        setUpdateUser(SaTokenUtil.currentUser().getUserId());
        setUpdateTime(new Date());
    }
}
