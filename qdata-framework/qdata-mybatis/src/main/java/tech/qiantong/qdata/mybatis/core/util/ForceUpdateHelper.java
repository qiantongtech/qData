package tech.qiantong.qdata.mybatis.core.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.domain.model.LoginUser;
import tech.qiantong.qdata.common.utils.SecurityUtils;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class ForceUpdateHelper {

    public static <T> int updateById(T entity, BaseMapper<T> mapper, Collection<SFunction<T, ?>> excludedField) {
        Class<T> clazz = (Class<T>) entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);

        // 获取主键值
        Object id = null;
        try {
            Field idField = clazz.getDeclaredField(tableInfo.getKeyProperty());
            idField.setAccessible(true);
            id = idField.get(entity);
        } catch (Exception e) {
            throw new RuntimeException("获取主键失败", e);
        }

        if (id == null) {
            throw new IllegalArgumentException("主键不能为空");
        }

        // 创建 UpdateWrapper
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        wrapper.eq(tableInfo.getKeyColumn(), id);

        // 使用反射获取所有字段值
        Field[] fields = clazz.getDeclaredFields();
        Set<String> exclude = excludedField == null ? Collections.emptySet() : excludedField.stream().map(ForceUpdateHelper::getPropertyName).collect(Collectors.toSet());
        for (Field field : fields) {
            if (exclude.contains(field.getName())) {
                continue;
            }
            if (excludeUpdate(field, tableInfo)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(entity);

                // 获取字段对应的数据库列名
                TableField tableField = field.getAnnotation(TableField.class);
                String columnName;
                if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
                    columnName = tableField.value();
                } else {
                    columnName = StrUtil.toUnderlineCase(field.getName());
                }
                wrapper.set(columnName, value);
            } catch (IllegalAccessException e) {
                // 忽略无法访问的字段
            }
        }

        wrapper.set("update_time", new Date());
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.info("获取用户信息异常:{}", e);
        }
        if (loginUser != null) {
            wrapper.set("updater_id", loginUser.getUserId());
            wrapper.set("update_by", loginUser.getUser().getNickName());
        }

        return mapper.update(null, wrapper);
    }

    private static boolean excludeUpdate(Field field, TableInfo tableInfo) {
        // 排除静态字段和 transient 字段
        if (Modifier.isStatic(field.getModifiers()) ||
                Modifier.isTransient(field.getModifiers())) {
            return true;
        }
        // 排除主键
        if (field.getName().equals(tableInfo.getKeyProperty())) {
            return true;
        }
        if (field.getAnnotation(TableLogic.class) != null) {
            return true;
        }
        TableField tableField = field.getAnnotation(TableField.class);
        return tableField != null && !tableField.exist();
    }

    public static <T, R> String getPropertyName(SFunction<T, R> getter) {
        LambdaMeta lambdaMeta = LambdaUtils.extract(getter);
        String methodName = lambdaMeta.getImplMethodName();

        // 转换 getter 方法名为属性名
        return methodName.startsWith("get") ?
                Introspector.decapitalize(methodName.substring(3)) :
                Introspector.decapitalize(methodName.substring(2));
    }

}
