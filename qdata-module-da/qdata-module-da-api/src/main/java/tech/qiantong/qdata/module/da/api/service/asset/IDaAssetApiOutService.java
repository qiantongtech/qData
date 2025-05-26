package tech.qiantong.qdata.module.da.api.service.asset;

import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;

/**
 * 数据资产Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetApiOutService  {

    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO);

    /**
     * 根据类目编码查询数量
     *
     * @return
     */
    Long getCountByCatCode(String catCode);


}
