package tech.qiantong.qdata.module.att.dal.mapper.cat;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 逻辑模型类目管理Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttModelCatMapper extends BaseMapperX<AttModelCatDO> {

    default PageResult<AttModelCatDO> selectPage(AttModelCatPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttModelCatDO>()
                .likeIfPresent(AttModelCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttModelCatDO::getCode, reqVO.getCode())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttModelCatDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
                .orderByAsc(AttModelCatDO::getSortOrder));
    }
}
