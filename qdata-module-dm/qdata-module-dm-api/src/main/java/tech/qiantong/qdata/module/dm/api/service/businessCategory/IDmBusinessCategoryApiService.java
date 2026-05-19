package tech.qiantong.qdata.module.dm.api.service.businessCategory;

import tech.qiantong.qdata.common.core.domain.TreeData;

import java.util.List;

/**
 * <P>
 * 用途:业务分类ApiService接口
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 11:01
 **/
public interface IDmBusinessCategoryApiService {
    /**
     * 获取树形数据
     * @param type 统计类型 1：统计资产数量
     * @return
     */
    List<TreeData> getTreeData(String type);
}
