package tech.qiantong.qdata.module.att.dal.mapper.theme;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 主题Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttThemeMapper extends BaseMapperX<AttThemeDO> {

    default PageResult<AttThemeDO> selectPage(AttThemePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id","sort_order", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttThemeDO>()
                .likeIfPresent(AttThemeDO::getName, reqVO.getName())
                .eqIfPresent(AttThemeDO::getIcon, reqVO.getIcon())
                .eqIfPresent(AttThemeDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttThemeDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttThemeDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttThemeDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
                //按照 createTime 字段降序排序 sort_order升序
                .orderByAsc(AttThemeDO::getSortOrder)
                .orderByDesc(AttThemeDO::getCreateTime));


    }
}
