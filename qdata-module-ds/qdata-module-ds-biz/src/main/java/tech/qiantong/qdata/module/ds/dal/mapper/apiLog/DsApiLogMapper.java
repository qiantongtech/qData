package tech.qiantong.qdata.module.ds.dal.mapper.apiLog;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * API服务调用日志Mapper接口
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface DsApiLogMapper extends BaseMapperX<DsApiLogDO> {

    default PageResult<DsApiLogDO> selectPage(DsApiLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        MPJLambdaWrapper<DsApiLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DsApiLogDO.class)
                .select("t2.NAME AS apiName,t2.REQ_METHOD as reqMethod,t3.NAME as catName")
                .leftJoin("DS_API t2 on t.API_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .leftJoin("ATT_API_CAT t3 on t.CAT_CODE = t3.CODE AND t3.DEL_FLAG = '0'")
                .like(StringUtils.isNotEmpty(reqVO.getApiName()), "t2.NAME", reqVO.getApiName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DsApiLogDO::getCatCode, reqVO.getCatCode())
                .eq(reqVO.getApiId() != null, DsApiLogDO::getApiId, reqVO.getApiId())
                .eq(reqVO.getCallerId() != null, DsApiLogDO::getCallerId, reqVO.getCallerId())
                .eq(reqVO.getStatus() != null, DsApiLogDO::getStatus, reqVO.getStatus())
                .between(tech.qiantong.qdata.common.utils.StringUtils.isNotNull(reqVO.getParamByKey("beginCreateTime"))
                        &&tech.qiantong.qdata.common.utils.StringUtils.isNotNull(reqVO.getParamByKey("endCreateTime")),
                        DsApiDO::getCreateTime, reqVO.getParamByKey("beginCreateTime"), reqVO.getParamByKey("endCreateTime"))
                .eq(reqVO.getCreateTime() != null, DsApiLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        // 构造动态查询条件
        return selectJoinPage(reqVO, DsApiLogDO.class, wrapper);
    }

    public DsApiLogDO selectDsApiLogByID(Long id);
}
