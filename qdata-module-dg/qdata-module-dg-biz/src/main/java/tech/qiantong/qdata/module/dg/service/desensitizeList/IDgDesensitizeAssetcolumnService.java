/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dg.service.desensitizeList;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
/**
 * 脱敏清单关联关系Service接口
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface IDgDesensitizeAssetcolumnService extends IService<DgDesensitizeAssetcolumnDO> {

    /**
     * 获得脱敏清单关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏清单关联关系分页列表
     */
    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnPage(DgDesensitizeAssetcolumnPageReqVO pageReqVO);

    /**
     * 创建脱敏清单关联关系
     *
     * @param createReqVO 脱敏清单关联关系信息
     * @return 脱敏清单关联关系编号
     */
    Long createDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO createReqVO);

    /**
     * 更新脱敏清单关联关系
     *
     * @param updateReqVO 脱敏清单关联关系信息
     */
    int updateDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO updateReqVO);

    /**
     * 删除脱敏清单关联关系
     *
     * @param idList 脱敏清单关联关系编号
     */
    int removeDgDesensitizeAssetcolumn(Collection<Long> idList);

    /**
     * 获得脱敏清单关联关系详情
     *
     * @param id 脱敏清单关联关系编号
     * @return 脱敏清单关联关系
     */
    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnById(Long id);

    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnByAid(Long assetcolumnId);

    /**
     * 获得全部脱敏清单关联关系列表
     *
     * @return 脱敏清单关联关系列表
     */
    List<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnList();

    /**
     * 获得全部脱敏清单关联关系 Map
     *
     * @return 脱敏清单关联关系 Map
     */
    Map<Long, DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnMap();


    /**
     * 导入脱敏清单关联关系数据
     *
     * @param importExcelList 脱敏清单关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeAssetcolumn(List<DgDesensitizeAssetcolumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizePagebyRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn);

    DgDesensitizeAssetcolumnDO getByassetcolumnId(Long assetcolumnId);

    int deleteByassetcolumnId(Long assetcolumnId);
}
