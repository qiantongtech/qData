package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.Flag;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

/**
 * 数据集成节点实例Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlNodeInstanceMapper extends BaseMapperX<DppEtlNodeInstanceDO> {

    default PageResult<DppEtlNodeInstanceDO> selectPage(DppEtlNodeInstancePageReqVO reqVO) {

        MPJLambdaWrapper<DppEtlNodeInstanceDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(DppEtlNodeInstanceDO.class)
                .select(DppEtlTaskInstanceDO::getCommandType)
                .select("t3.NICK_NAME AS personChargeName")
//                .leftJoin("DPP_ETL_TASK_INSTANCE t2 ON t.TASK_INSTANCE_ID = t2.id AND t2.DEL_FLAG = '0'")
                .innerJoin(DppEtlTaskInstanceDO.class, DppEtlTaskInstanceDO::getId, DppEtlNodeInstanceDO::getTaskInstanceId)
                .leftJoin("SYSTEM_USER t3 ON t1.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DppEtlTaskInstanceDO::getSubTaskFlag, Flag.NO.getCode())
                .eq(DppEtlNodeInstanceDO::getTaskType, reqVO.getTaskType())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DppEtlTaskInstanceDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DppEtlNodeInstanceDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getNodeType()), DppEtlNodeInstanceDO::getNodeType, reqVO.getNodeType())
                .eq(reqVO.getNodeId() != null, DppEtlNodeInstanceDO::getNodeId, reqVO.getNodeId())
                .eq(StringUtils.isNotBlank(reqVO.getNodeCode()), DppEtlNodeInstanceDO::getNodeCode, reqVO.getNodeCode())
                .eq(reqVO.getTaskInstanceId() != null, DppEtlNodeInstanceDO::getTaskInstanceId, reqVO.getTaskInstanceId())
                .like(StringUtils.isNotBlank(reqVO.getTaskInstanceName()), DppEtlNodeInstanceDO::getTaskInstanceName, reqVO.getTaskInstanceName())
                .like(StringUtils.isNotBlank(reqVO.getJobName()), DppEtlNodeInstanceDO::getTaskInstanceName, reqVO.getJobName())
                .eq(reqVO.getProjectId() != null, DppEtlNodeInstanceDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()), DppEtlNodeInstanceDO::getProjectCode, reqVO.getProjectCode())
                .gt(reqVO.getStartTime() != null, DppEtlNodeInstanceDO::getStartTime, reqVO.getStartTime())
                .le(reqVO.getEndTime() != null, DppEtlNodeInstanceDO::getEndTime, reqVO.getEndTime())
                .eq(StringUtils.isNotBlank(reqVO.getPriority()), DppEtlNodeInstanceDO::getPriority, reqVO.getPriority())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DppEtlNodeInstanceDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDsId() != null, DppEtlNodeInstanceDO::getDsId, reqVO.getDsId())
                .eq(reqVO.getDsTaskInstanceId() != null, DppEtlNodeInstanceDO::getDsTaskInstanceId, reqVO.getDsTaskInstanceId())
                .eq(reqVO.getCreateTime() != null, DppEtlNodeInstanceDO::getCreateTime, reqVO.getCreateTime())
                .in(DppEtlNodeInstanceDO::getStatus, "1", "6", "7")
                .orderByDesc(DppEtlNodeInstanceDO::getStartTime);

        // 构造动态查询条件
        return selectJoinPage(reqVO, DppEtlNodeInstanceDO.class, lambdaWrapper);
    }
}
