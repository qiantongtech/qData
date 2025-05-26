package tech.qiantong.qdata.module.att.convert.client;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;

import java.util.List;

/**
 * 应用管理 Convert
 *
 * @author qdata
 * @date 2025-02-18
 */
@Mapper
public interface AttClientConvert {
    AttClientConvert INSTANCE = Mappers.getMapper(AttClientConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attClientPageReqVO 请求参数
     * @return AttClientDO
     */
     AttClientDO convertToDO(AttClientPageReqVO attClientPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attClientSaveReqVO 保存请求参数
     * @return AttClientDO
     */
     AttClientDO convertToDO(AttClientSaveReqVO attClientSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attClientDO 实体对象
     * @return AttClientRespVO
     */
     AttClientRespVO convertToRespVO(AttClientDO attClientDO);

    /**
     * DOList 转换为 RespVOList
     * @param attClientDOList 实体对象列表
     * @return List<AttClientRespVO>
     */
     List<AttClientRespVO> convertToRespVOList(List<AttClientDO> attClientDOList);
}
