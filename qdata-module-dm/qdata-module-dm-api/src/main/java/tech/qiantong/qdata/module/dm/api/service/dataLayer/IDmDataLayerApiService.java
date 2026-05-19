package tech.qiantong.qdata.module.dm.api.service.dataLayer;

import tech.qiantong.qdata.common.core.domain.TreeData;

import java.util.List;

/**
 * <P>
 * 用途:数仓分层ApiService接口
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-27 16:53
 **/
public interface IDmDataLayerApiService {
    /**
     * 获取树形数据
     * @param type 统计类型 1：统计资产数量
     * @return
     */
    List<TreeData> getTreeData(String type);
}
