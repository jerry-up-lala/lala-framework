package com.jerry.up.lala.framework.boot.mybatis;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jerry.up.lala.framework.boot.entity.Entity;
import com.jerry.up.lala.framework.boot.satoken.SaTokenUtil;
import com.jerry.up.lala.framework.common.util.StringUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * <p>Description: 自动填充基础字段
 *
 * @author FMJ
 * @date 2024/2/27 13:38
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        String userId = SaTokenUtil.currentUser().getUserId();
        fillFieldValue(metaObject, Entity.Fields.createTime, now, false);
        fillFieldValue(metaObject, Entity.Fields.createUser, userId, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = new Date();
        String userId = SaTokenUtil.currentUser().getUserId();
        fillFieldValue(metaObject, Entity.Fields.updateTime, now, true);
        fillFieldValue(metaObject, Entity.Fields.updateUser, userId, true);
    }

    private static void fillFieldValue(MetaObject metaObject, String fieldName, Object fieldVal, boolean overwrite) {
        // 设置值为空 或者 字段不存在 或者 字段类型不匹配
        if (fieldVal == null || !metaObject.hasSetter(fieldName) || !ClassUtil.isAssignable(metaObject.getGetterType(fieldName), fieldVal.getClass())) {
            return;
        }
        if (overwrite || StringUtil.isNull(StrUtil.str(metaObject.getValue(fieldName), Charset.defaultCharset()))) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

}