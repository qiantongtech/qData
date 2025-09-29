package tech.qiantong.qdata.module.att.dal.mapper.client;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 应用API服务关联Mapper接口
 *
 * @author FXB
 * @date 2025-08-21
 */
public interface AttClientApiRelMapper extends BaseMapperX<AttClientApiRelDO> {

    default PageResult<AttClientApiRelDO> selectPage(AttClientApiRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<AttClientApiRelDO> lambdaWrapper = new MPJLambdaWrapper();

        // CONCAT("/services/",CONCAT(t2.API_VERSION,t2.API_URL))
        lambdaWrapper.selectAll(AttClientApiRelDO.class)
                .select("t2.NAME AS apiName",
                        "CONCAT('/services/',CONCAT(t2.API_VERSION,t2.API_URL)) AS apiUrl",
                        "t2.REQ_METHOD AS reqMethod")
                .leftJoin("DS_API t2 ON t.API_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(reqVO.getClientId() != null, AttClientApiRelDO::getClientId, reqVO.getClientId())
                .eq(reqVO.getApiId() != null, AttClientApiRelDO::getApiId, reqVO.getApiId())
                .eq(StringUtils.isNotBlank(reqVO.getPvFlag()), AttClientApiRelDO::getPvFlag, reqVO.getPvFlag())
//                .eq(AttClientApiRelDO::getStartTime, reqVO.getStartTime())
//                .eq(AttClientApiRelDO::getEndTime, reqVO.getEndTime())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), AttClientApiRelDO::getStatus, reqVO.getStatus())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        // 构造动态查询条件
        return selectJoinPage(reqVO, AttClientApiRelDO.class, lambdaWrapper);
    }
}
