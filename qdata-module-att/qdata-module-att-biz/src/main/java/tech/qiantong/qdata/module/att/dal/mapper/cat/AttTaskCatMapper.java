package tech.qiantong.qdata.module.att.dal.mapper.cat;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据集成任务类目管理Mapper接口
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface AttTaskCatMapper extends BaseMapperX<AttTaskCatDO> {

    default PageResult<AttTaskCatDO> selectPage(AttTaskCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttTaskCatDO>()
                .likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTaskCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTaskCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTaskCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttTaskCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTaskCatDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AttTaskCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttTaskCatDO::getProjectCode,reqVO.getProjectCode())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
