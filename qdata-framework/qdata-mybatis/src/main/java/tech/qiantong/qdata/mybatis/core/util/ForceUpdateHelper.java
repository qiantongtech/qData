package tech.qiantong.qdata.mybatis.core.util;
import tech.qiantong.qdata.common.exception.ServiceException;

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

        // Get primary key value
        Object id = null;
        try {
            Field idField = clazz.getDeclaredField(tableInfo.getKeyProperty());
            idField.setAccessible(true);
            id = idField.get(entity);
        } catch (Exception e) {
            throw new ServiceException("sys.error.pk.fail", "获取主键失败");
        }

        if (id == null) {
            throw new IllegalArgumentException("主键不能为空");
        }

        // Create UpdateWrapper
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        wrapper.eq(tableInfo.getKeyColumn(), id);

        // Get all field values using reflection
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

                // Get the database column name corresponding to the field
                TableField tableField = field.getAnnotation(TableField.class);
                String columnName;
                if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
                    columnName = tableField.value();
                } else {
                    columnName = StrUtil.toUnderlineCase(field.getName());
                }
                wrapper.set(columnName, value);
            } catch (IllegalAccessException e) {
                // Ignore inaccessible fields
            }
        }

        wrapper.set("update_time", new Date());
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        } catch (Exception e) {
// logger.info("Exception in obtaining user information: {}", e);
        }
        if (loginUser != null) {
            wrapper.set("updater_id", loginUser.getUserId());
            wrapper.set("update_by", loginUser.getUser().getNickName());
        }

        return mapper.update(null, wrapper);
    }

    private static boolean excludeUpdate(Field field, TableInfo tableInfo) {
        // Exclude static and transient fields
        if (Modifier.isStatic(field.getModifiers()) ||
                Modifier.isTransient(field.getModifiers())) {
            return true;
        }
        // Exclude primary key
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

        // Convert getter method name to property name
        return methodName.startsWith("get") ?
                Introspector.decapitalize(methodName.substring(3)) :
                Introspector.decapitalize(methodName.substring(2));
    }

}
