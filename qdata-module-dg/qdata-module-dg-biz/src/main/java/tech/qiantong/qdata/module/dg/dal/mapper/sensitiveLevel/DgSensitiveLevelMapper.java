package tech.qiantong.qdata.module.dg.dal.mapper.sensitiveLevel;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 敏感等级Mapper接口
 *
 * @author Chaos
 * @date 2025-01-21
 */
public interface DgSensitiveLevelMapper extends BaseMapperX<DgSensitiveLevelDO> {

    default PageResult<DgSensitiveLevelDO> selectPage(DgSensitiveLevelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DgSensitiveLevelDO>()
                .likeIfPresent(DgSensitiveLevelDO::getSensitiveLevel, reqVO.getSensitiveLevel())
                .eqIfPresent(DgSensitiveLevelDO::getSensitiveRule, reqVO.getSensitiveRule())
                .eqIfPresent(DgSensitiveLevelDO::getStartCharLoc, reqVO.getStartCharLoc())
                .eqIfPresent(DgSensitiveLevelDO::getEndCharLoc, reqVO.getEndCharLoc())
                .eqIfPresent(DgSensitiveLevelDO::getMaskCharacter, reqVO.getMaskCharacter())
                .eqIfPresent(DgSensitiveLevelDO::getOnlineFlag, reqVO.getOnlineFlag())
                .eqIfPresent(DgSensitiveLevelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DgSensitiveLevelDO::getCreateTime, reqVO.getCreateTime())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

}
