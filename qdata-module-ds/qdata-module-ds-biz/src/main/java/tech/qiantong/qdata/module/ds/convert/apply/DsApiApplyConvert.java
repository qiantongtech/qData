package tech.qiantong.qdata.module.ds.convert.apply;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplySaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apply.DsApiApplyDO;

import java.util.List;

/**
 * API服务-申请 Convert
 *
 * @author qdata
 * @date 2025-04-22
 */
@Mapper
public interface DsApiApplyConvert {
    DsApiApplyConvert INSTANCE = Mappers.getMapper(DsApiApplyConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dsApiApplyPageReqVO 请求参数
     * @return DsApiApplyDO
     */
     DsApiApplyDO convertToDO(DsApiApplyPageReqVO dsApiApplyPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dsApiApplySaveReqVO 保存请求参数
     * @return DsApiApplyDO
     */
     DsApiApplyDO convertToDO(DsApiApplySaveReqVO dsApiApplySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dsApiApplyDO 实体对象
     * @return DsApiApplyRespVO
     */
     DsApiApplyRespVO convertToRespVO(DsApiApplyDO dsApiApplyDO);

    /**
     * DOList 转换为 RespVOList
     * @param dsApiApplyDOList 实体对象列表
     * @return List<DsApiApplyRespVO>
     */
     List<DsApiApplyRespVO> convertToRespVOList(List<DsApiApplyDO> dsApiApplyDOList);
}
