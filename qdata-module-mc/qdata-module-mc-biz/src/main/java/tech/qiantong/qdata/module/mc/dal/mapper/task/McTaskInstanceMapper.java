package tech.qiantong.qdata.module.mc.dal.mapper.task;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * Collection task instance Mapper interface
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface McTaskInstanceMapper extends BaseMapperX<McTaskInstanceDO> {

//    default PageResult<McTaskInstanceDO> selectPage(McTaskInstancePageReqVO reqVO) {
// // Define the sorting field (prevent SQL injection, consistent with the database field name)
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
// //Construct dynamic query conditions
//        return selectPage(reqVO, new LambdaQueryWrapperX<McTaskInstanceDO>()
//                .eqIfPresent(McTaskInstanceDO::getDomainId, reqVO.getDomainId())
//                .eqIfPresent(McTaskInstanceDO::getDomainCode, reqVO.getDomainCode())
//                .eqIfPresent(McTaskInstanceDO::getTaskId, reqVO.getTaskId())
//                .eqIfPresent(McTaskInstanceDO::getCollectionMode, reqVO.getCollectionMode())
//                .eqIfPresent(McTaskInstanceDO::getCollectionScope, reqVO.getCollectionScope())
//                .eqIfPresent(McTaskInstanceDO::getTotalCount, reqVO.getTotalCount())
//                .eqIfPresent(McTaskInstanceDO::getSuccessCount, reqVO.getSuccessCount())
//                .eqIfPresent(McTaskInstanceDO::getFailCount, reqVO.getFailCount())
//                .eqIfPresent(McTaskInstanceDO::getFailCause, reqVO.getFailCause())
//                .eqIfPresent(McTaskInstanceDO::getAddCount, reqVO.getAddCount())
//                .eqIfPresent(McTaskInstanceDO::getDelCount, reqVO.getDelCount())
//                .eqIfPresent(McTaskInstanceDO::getUpdateCount, reqVO.getUpdateCount())
//                .eqIfPresent(McTaskInstanceDO::getStartTime, reqVO.getStartTime())
//                .eqIfPresent(McTaskInstanceDO::getEndTime, reqVO.getEndTime())
//                .eqIfPresent(McTaskInstanceDO::getDuration, reqVO.getDuration())
//                .eqIfPresent(McTaskInstanceDO::getStatus, reqVO.getStatus())
//                .eqIfPresent(McTaskInstanceDO::getCreateTime, reqVO.getCreateTime())
//                .eqIfPresent(McTaskInstanceDO::getDescription, reqVO.getDescription())
// // If reqVO.getName() is not empty, add the exact matching condition of name (name = '<name>')
//                // .likeIfPresent(McTaskInstanceDO::getName, reqVO.getName())
// // Sort by createTime field in descending order
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
//    }

    default PageResult<McTaskInstanceDO> selectPage(McTaskInstancePageReqVO reqVO) {

        MPJLambdaWrapper<McTaskInstanceDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(McTaskInstanceDO.class)
                .select("t2.NAME AS sourceSystemName"
                        , "t3.NAME AS name"
                        , "t3.STATUS AS taskStatus"
                        ,"t4.DATASOURCE_NAME AS datasourceName"
                        ,"t4.DATASOURCE_TYPE AS datasourceType"
                )
                .leftJoin("ATT_SOURCE_SYSTEM t2 on t.SOURCE_SYSTEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .leftJoin("MC_TASK t3 ON t3.id = t.task_id")
                .leftJoin("DA_DATASOURCE t4 ON t3.datasource_id = t4.id AND t4.DEL_FLAG = '0'")
                .eq(reqVO.getDatasourceId() != null,"t3.DATASOURCE_ID", reqVO.getDatasourceId())
                .eq(reqVO.getSourceSystemId() != null,McTaskInstanceDO::getSourceSystemId, reqVO.getSourceSystemId())
                .eq(reqVO.getTaskId() != null,McTaskInstanceDO::getTaskId, reqVO.getTaskId())
                .likeRight(StringUtils.isNotBlank(reqVO.getSourceSystemName()), McTaskDO::getSourceSystemName, reqVO.getSourceSystemName())
                .like(StringUtils.isNotEmpty( reqVO.getName()), "t3.NAME", reqVO.getName())
                .eq(StringUtils.isNotEmpty( reqVO.getCollectionMode()),McTaskInstanceDO::getCollectionMode, reqVO.getCollectionMode())
                .eq(StringUtils.isNotEmpty( reqVO.getCollectionScope()),McTaskInstanceDO::getCollectionScope, reqVO.getCollectionScope())
                .eq(StringUtils.isNotEmpty( reqVO.getStatus()),McTaskInstanceDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotEmpty( reqVO.getValidFlag()),McTaskInstanceDO::getValidFlag, reqVO.getValidFlag())
                .ge(reqVO.getCreateTimeStart() != null,
                        McTaskInstanceDO::getCreateTime, reqVO.getCreateTimeStart())
                .le(reqVO.getCreateTimeEnd() != null,
                        McTaskInstanceDO::getCreateTime, reqVO.getCreateTimeEnd())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, McTaskInstanceDO.class, lambdaWrapper);


    }
}
