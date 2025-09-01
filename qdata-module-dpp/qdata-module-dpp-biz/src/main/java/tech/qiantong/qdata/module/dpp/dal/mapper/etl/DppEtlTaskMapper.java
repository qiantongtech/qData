package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.TaskCatEnum;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据集成任务Mapper接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskMapper extends BaseMapperX<DppEtlTaskDO> {

    IPage<DppEtlTaskRespVO> getDppEtlTaskPage(Page page, @Param("params") DppEtlTaskPageReqVO reqVO);

    default PageResult<DppEtlTaskDO> selectPage(DppEtlTaskPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DppEtlTaskDO> lambdaWrapper = new MPJLambdaWrapper();

        String leftJoin = TaskCatEnum.findEnumByType(reqVO.getType()) + " t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'";

        lambdaWrapper.selectAll(DppEtlTaskDO.class)
                .select("t2.NAME AS catName",
                        "t3.CRON_EXPRESSION AS cronExpression"
                        ,"t3.STATUS AS schedulerState"
                        , "(SELECT MAX(ti.CREATE_TIME) FROM DPP_ETL_TASK_INSTANCE ti WHERE ti.TASK_CODE = t.CODE AND ti.DEL_FLAG = '0') AS lastExecuteTime")
                .leftJoin(leftJoin)
                .leftJoin("DPP_ETL_SCHEDULER t3 ON t.id = t3.task_id AND t3.DEL_FLAG = '0'")
                .eq(StringUtils.isNotBlank(reqVO.getType()), DppEtlTaskDO::getType, reqVO.getType())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DppEtlTaskDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DppEtlTaskDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getCode()), DppEtlTaskDO::getCode, reqVO.getCode())
                .ne(DppEtlTaskDO::getStatus,"-2")
                .ne(DppEtlTaskDO::getStatus,"-3")
                .eq(reqVO.getProjectId() != null, DppEtlTaskDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()), DppEtlTaskDO::getProjectCode, reqVO.getProjectCode())
                .eq(StringUtils.isNotBlank(reqVO.getPersonCharge()), DppEtlTaskDO::getPersonCharge, reqVO.getPersonCharge())
                .eq(StringUtils.isNotBlank(reqVO.getLocations()), DppEtlTaskDO::getLocations, reqVO.getLocations())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DppEtlTaskDO::getDescription, reqVO.getDescription())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DppEtlTaskDO::getStatus, reqVO.getStatus())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        // 构造动态查询条件
        return selectJoinPage(reqVO, DppEtlTaskDO.class, lambdaWrapper);
    }


    int checkTaskIdInSubTasks(Long id);

    int checkTaskIdInDatasource(@Param("datasourceIdList") List<Long> datasourceIdList,@Param("projectIdList") List<Long> projectIdList);

    int checkTaskIdInAsset(@Param("assetIdList") List<Long> assetIdList);
}
