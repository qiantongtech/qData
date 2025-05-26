package tech.qiantong.qdata.module.da.dal.mapper.sensitiveLevel;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 敏感等级Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DaSensitiveLevelMapper extends BaseMapperX<DaSensitiveLevelDO> {

    default PageResult<DaSensitiveLevelDO> selectPage(DaSensitiveLevelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaSensitiveLevelDO>()
                .eqIfPresent(DaSensitiveLevelDO::getSensitiveLevel, reqVO.getSensitiveLevel())
                .eqIfPresent(DaSensitiveLevelDO::getSensitiveRule, reqVO.getSensitiveRule())
                .eqIfPresent(DaSensitiveLevelDO::getStartCharLoc, reqVO.getStartCharLoc())
                .eqIfPresent(DaSensitiveLevelDO::getEndCharLoc, reqVO.getEndCharLoc())
                .eqIfPresent(DaSensitiveLevelDO::getMaskCharacter, reqVO.getMaskCharacter())
                .eqIfPresent(DaSensitiveLevelDO::getOnlineFlag, reqVO.getOnlineFlag())
                .eqIfPresent(DaSensitiveLevelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DaSensitiveLevelDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaSensitiveLevelDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
