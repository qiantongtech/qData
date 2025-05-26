package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListRespVO;
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
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlTaskInstanceDO>()
                .likeIfPresent(DppEtlTaskInstanceDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlTaskInstanceDO::getTaskType, reqVO.getTaskType())
                .eqIfPresent(DppEtlTaskInstanceDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DppEtlTaskInstanceDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DppEtlTaskInstanceDO::getTaskVersion, reqVO.getTaskVersion())
                .eqIfPresent(DppEtlTaskInstanceDO::getStatusHistory, reqVO.getStatusHistory())
                .eqIfPresent(DppEtlTaskInstanceDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DppEtlTaskInstanceDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlTaskInstanceDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlTaskInstanceDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(DppEtlTaskInstanceDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(DppEtlTaskInstanceDO::getCommandType, reqVO.getCommandType())
                .eqIfPresent(DppEtlTaskInstanceDO::getMaxTryTimes, reqVO.getMaxTryTimes())
                .eqIfPresent(DppEtlTaskInstanceDO::getFailureStrategy, reqVO.getFailureStrategy())
                .eqIfPresent(DppEtlTaskInstanceDO::getSubTaskFlag, reqVO.getSubTaskFlag())
                .eqIfPresent(DppEtlTaskInstanceDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppEtlTaskInstanceDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlTaskInstanceDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppEtlTaskInstanceDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsDesc(), allowedColumns));
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
