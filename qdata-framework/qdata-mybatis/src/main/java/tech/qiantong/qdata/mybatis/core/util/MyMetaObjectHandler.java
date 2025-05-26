package tech.qiantong.qdata.mybatis.core.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.core.domain.model.LoginUser;
import tech.qiantong.qdata.common.utils.SecurityUtils;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void insertFill(MetaObject metaObject) {
        // 检查是否有名为createTime的字段，如果有则自动填充当前时间
        boolean hasCreateTime = metaObject.hasSetter("createTime");
        if (hasCreateTime) {
            metaObject.setValue("createTime", new Date());
        }
        // 检查是否有名为updateTime的字段，如果有则自动填充当前时间 部分表设置的updateTime不能为空
        boolean hasUpdateTime = metaObject.hasSetter("updateTime");
        if (hasUpdateTime) {
            metaObject.setValue("updateTime", new Date());
        }
        boolean hasCreatorId = metaObject.hasSetter("creatorId");
        boolean hasCreateBy = metaObject.hasSetter("createBy");
        boolean hasUpdatorId = metaObject.hasSetter("updaterId");
        boolean HasUpdateBy = metaObject.hasSetter("updateBy");
        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.info("获取用户信息异常:{}", e);
        }
        if (loginUser != null) {
            if (hasCreatorId) {
                metaObject.setValue("creatorId", loginUser.getUserId());
            }
            if (hasCreateBy) {
                metaObject.setValue("createBy", loginUser.getUser().getNickName());
            }
            if (hasUpdatorId) {
                metaObject.setValue("updaterId", loginUser.getUserId());
            }
            if (HasUpdateBy) {
                metaObject.setValue("updateBy", loginUser.getUser().getNickName());
            }
        }

    }


    @Override
    public void updateFill(MetaObject metaObject) {
        // 检查是否有名为updateTime的字段，如果有则自动填充当前时间
        boolean hasUpdateTime = metaObject.hasSetter("updateTime");
        if (hasUpdateTime) {
            metaObject.setValue("updateTime", new Date());
        }

        boolean hasUpdatorId = metaObject.hasSetter("updaterId");
        boolean HasUpdateBy = metaObject.hasSetter("updateBy");

        LoginUser loginUser = null;
        try {
            loginUser = (LoginUser) SecurityUtils.getAuthentication().getPrincipal();
        } catch (Exception e) {
//            logger.info("获取用户信息异常:{}", e);
        }
        if (loginUser != null) {
            if (hasUpdatorId) {
                metaObject.setValue("updaterId", loginUser.getUserId());
            }
            if (HasUpdateBy) {
                metaObject.setValue("updateBy", loginUser.getUser().getNickName());
            }
        }
    }
}
