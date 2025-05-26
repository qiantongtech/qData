package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;


import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据元代码Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemCodeMapper extends BaseMapperX<DpDataElemCodeDO> {

    default PageResult<DpDataElemCodeDO> selectPage(DpDataElemCodePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DpDataElemCodeDO>()
                .eqIfPresent(DpDataElemCodeDO::getDataElemId, reqVO.getDataElemId())
                .eqIfPresent(DpDataElemCodeDO::getCodeValue, reqVO.getCodeValue())
                .likeIfPresent(DpDataElemCodeDO::getCodeName, reqVO.getCodeName())
                .eqIfPresent(DpDataElemCodeDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DpDataElemCodeDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
