package tech.qiantong.qdata.module.dp.api.service.dataElem;

import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-03 18:06
 **/
public interface IDataElemRuleRelService {
    /**
     * 通过数据元id列表查询数据元规则关联信息
     *
     * @param dataElemIdList
     * @return
     */
    List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> dataElemIdList, String type);
}
