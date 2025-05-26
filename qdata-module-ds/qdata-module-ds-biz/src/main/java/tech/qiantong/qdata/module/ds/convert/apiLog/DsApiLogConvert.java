package tech.qiantong.qdata.module.ds.convert.apiLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;

import java.util.List;

/**
 * API服务调用日志 Convert
 *
 * @author lhs
 * @date 2025-02-12
 */
@Mapper
public interface DsApiLogConvert {
    DsApiLogConvert INSTANCE = Mappers.getMapper(DsApiLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dsApiLogPageReqVO 请求参数
     * @return DsApiLogDO
     */
     DsApiLogDO convertToDO(DsApiLogPageReqVO dsApiLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dsApiLogSaveReqVO 保存请求参数
     * @return DsApiLogDO
     */
     DsApiLogDO convertToDO(DsApiLogSaveReqVO dsApiLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dsApiLogDO 实体对象
     * @return DsApiLogRespVO
     */
     DsApiLogRespVO convertToRespVO(DsApiLogDO dsApiLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dsApiLogDOList 实体对象列表
     * @return List<DsApiLogRespVO>
     */
     List<DsApiLogRespVO> convertToRespVOList(List<DsApiLogDO> dsApiLogDOList);
}
