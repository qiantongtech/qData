package tech.qiantong.qdata.module.dm.api.service.themeDomain;

import tech.qiantong.qdata.common.core.domain.TreeData;

import java.util.List;

/**
 * <P>
 * Purpose: Theme Domain ApiService Interface
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 11:01
 **/
public interface IDmThemeDomainApiService {
    /**
     * Get tree data
     *
     * @param type Statistics type 1: count assets
     * @return
     */
    List<TreeData> getTreeData(String type);
}
