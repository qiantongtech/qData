package tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;

@Data
@AllArgsConstructor
public class DaAssetColumnRelRuleVO {

    private DaAssetColumnDO assetColumn;
    private DpDataElemRuleRelRespDTO elemRuleRel;

}
