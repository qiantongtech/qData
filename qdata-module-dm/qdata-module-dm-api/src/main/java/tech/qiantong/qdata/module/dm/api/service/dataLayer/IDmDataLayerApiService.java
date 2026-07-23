package tech.qiantong.qdata.module.dm.api.service.dataLayer;

import tech.qiantong.qdata.common.core.domain.TreeData;

import java.util.List;

/**
 * <P>
 * Purpose: Data Warehouse Layer ApiService Interface
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-27 16:53
 **/
public interface IDmDataLayerApiService {
    /**
     * Get tree data
     * @param type Statistics type 1: count assets
     * @return
     */
    List<TreeData> getTreeData(String type);
}
