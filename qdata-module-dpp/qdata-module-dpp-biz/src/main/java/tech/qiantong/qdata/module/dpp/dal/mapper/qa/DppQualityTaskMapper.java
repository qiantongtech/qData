package tech.qiantong.qdata.module.dpp.dal.mapper.qa;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;

/**
 * 数据质量任务Mapper接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface DppQualityTaskMapper extends BaseMapperX<DppQualityTaskDO> {

    default PageResult<DppQualityTaskDO> selectPage(DppQualityTaskPageReqVO reqVO) {
        String leftJoin = "ATT_QUALITY_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'";

        MPJLambdaWrapperX<DppQualityTaskDO> lambdaWrapperX = new MPJLambdaWrapperX<>();
        lambdaWrapperX.selectAll(DppQualityTaskDO.class)
                .select("t2.NAME AS catName")
                .select("(SELECT COUNT(*) FROM DPP_QUALITY_TASK_OBJ o WHERE o.TASK_ID = t.ID ) taskObjNum")
                .select("(SELECT COUNT(*) FROM DPP_QUALITY_TASK_EVALUATE e WHERE e.TASK_ID = t.ID ) taskEvaluateNum");
        lambdaWrapperX.leftJoin(leftJoin);
        lambdaWrapperX.likeIfPresent(DppQualityTaskDO::getTaskName, reqVO.getTaskName())
                .eqIfPresent(DppQualityTaskDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(DppQualityTaskDO::getContact, reqVO.getContact())
                .eqIfPresent(DppQualityTaskDO::getAssetFlag, "0")
                .eqIfPresent(DppQualityTaskDO::getContactId, reqVO.getContactId())
                .eqIfPresent(DppQualityTaskDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DppQualityTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppQualityTaskDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppQualityTaskDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DppQualityTaskDO::getWorkerGroup, reqVO.getWorkerGroup())
                .eqIfPresent(DppQualityTaskDO::getRetryTimes, reqVO.getRetryTimes())
                .eqIfPresent(DppQualityTaskDO::getRetryInterval, reqVO.getRetryInterval())
                .eqIfPresent(DppQualityTaskDO::getDelayTime, reqVO.getDelayTime())
                .eqIfPresent(DppQualityTaskDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppQualityTaskDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        // 构造动态查询条件
        return selectPage(reqVO, lambdaWrapperX);
    }
}
