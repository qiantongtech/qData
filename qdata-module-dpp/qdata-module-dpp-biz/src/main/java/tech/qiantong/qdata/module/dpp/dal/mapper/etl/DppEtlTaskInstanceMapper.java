package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据集成任务实例Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskInstanceMapper extends BaseMapperX<DppEtlTaskInstanceDO> {

    default PageResult<DppEtlTaskInstanceDO> selectPage(DppEtlTaskInstancePageReqVO reqVO) {

        MPJLambdaWrapper<DppEtlTaskInstanceDO> lambdaWrapper = new MPJLambdaWrapper();


        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        lambdaWrapper.selectAll(DppEtlTaskInstanceDO.class)
                .select("t3.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t3 ON t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()),DppEtlTaskInstanceDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getTaskType()),DppEtlTaskInstanceDO::getTaskType, reqVO.getTaskType())
                .eq(reqVO.getTaskId() !=null,DppEtlTaskInstanceDO::getTaskId, reqVO.getTaskId())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()),DppEtlTaskInstanceDO::getTaskCode, reqVO.getTaskCode())
                .eq(reqVO.getTaskVersion() !=null,DppEtlTaskInstanceDO::getTaskVersion, reqVO.getTaskVersion())
                .eq(StringUtils.isNotBlank(reqVO.getPersonCharge()),DppEtlTaskInstanceDO::getPersonCharge, reqVO.getPersonCharge())
                .eq(reqVO.getProjectId() !=null,DppEtlTaskInstanceDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()),DppEtlTaskInstanceDO::getProjectCode, reqVO.getProjectCode())
                .eq(reqVO.getStartTime() !=null,DppEtlTaskInstanceDO::getStartTime, reqVO.getStartTime())
                .eq(reqVO.getEndTime() !=null,DppEtlTaskInstanceDO::getEndTime, reqVO.getEndTime())
                .eq(StringUtils.isNotBlank(reqVO.getCommandType()),DppEtlTaskInstanceDO::getCommandType, reqVO.getCommandType())
                .eq(reqVO.getMaxTryTimes() !=null,DppEtlTaskInstanceDO::getMaxTryTimes, reqVO.getMaxTryTimes())
                .eq(StringUtils.isNotBlank(reqVO.getFailureStrategy()),DppEtlTaskInstanceDO::getFailureStrategy, reqVO.getFailureStrategy())
                .eq(StringUtils.isNotBlank(reqVO.getSubTaskFlag()),DppEtlTaskInstanceDO::getSubTaskFlag, reqVO.getSubTaskFlag())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()),DppEtlTaskInstanceDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDsId() !=null,DppEtlTaskInstanceDO::getDsId, reqVO.getDsId())
                .eq(reqVO.getCreateTime() !=null,DppEtlTaskInstanceDO::getCreateTime, reqVO.getCreateTime())

                .in(DppEtlNodeInstanceDO::getStatus, "1","5", "6", "7")
                .orderByDesc(DppEtlNodeInstanceDO::getStartTime);


        // 构造动态查询条件
        return selectJoinPage(reqVO, DppEtlTaskInstanceDO.class, lambdaWrapper);
    }

    DppEtlTaskInstanceDO selectOneNew(@Param("reqVO") DppEtlTaskInstanceDO reqVO);

    /**
     * 根据任务实例ID查询节点实例列表
     *
     * @param taskInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> nodeListByTaskInstanceId(@Param("taskInstanceId") Long taskInstanceId);

    IPage<DppEtlTaskInstanceTreeListRespVO> treeList(Page page, @Param("params") DppEtlTaskInstanceTreeListReqVO params);

    /**
     * 获取子任务下所以节点实例
     *
     * @param taskInstanceId
     * @param nodeInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> listSubNodeInstance(@Param("taskInstanceId") Long taskInstanceId, @Param("nodeInstanceId") Long nodeInstanceId);
}
