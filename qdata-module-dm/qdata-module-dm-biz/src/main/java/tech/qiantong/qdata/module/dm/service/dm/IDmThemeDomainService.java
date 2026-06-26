/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dm.service.dm;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;

/**
 * 主题域管理Service接口
 *
 * @author FXB
 * @date 2026-03-24
 */
public interface IDmThemeDomainService extends IService<DmThemeDomainDO> {

    /**
     * 获得主题域管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 主题域管理分页列表
     */
    PageResult<DmThemeDomainDO> getDmThemeDomainPage(DmThemeDomainPageReqVO pageReqVO);

    /**
     * 创建主题域管理
     *
     * @param createReqVO 主题域管理信息
     * @return 主题域管理编号
     */
    Long createDmThemeDomain(DmThemeDomainSaveReqVO createReqVO);

    /**
     * 更新主题域管理
     *
     * @param updateReqVO 主题域管理信息
     */
    int updateDmThemeDomain(DmThemeDomainSaveReqVO updateReqVO);

    /**
     * 删除主题域管理
     *
     * @param idList 主题域管理编号
     */
    int removeDmThemeDomain(Collection<Long> idList);

    /**
     * 获得主题域管理详情
     *
     * @param id 主题域管理编号
     * @return 主题域管理
     */
    DmThemeDomainDO getDmThemeDomainById(Long id);

    /**
     * 获得全部主题域管理列表
     *
     * @return 主题域管理列表
     */
    List<DmThemeDomainDO> getDmThemeDomainList();

    /**
     * 获得全部主题域管理 Map
     *
     * @return 主题域管理 Map
     */
    Map<Long, DmThemeDomainDO> getDmThemeDomainMap();


    /**
     * 导入主题域管理数据
     *
     * @param importExcelList 主题域管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDmThemeDomain(List<DmThemeDomainRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取主题域管理列表
     *
     * @param reqVO
     * @return
     */
    List<DmThemeDomainDO> getDmThemeDomainList(DmThemeDomainPageReqVO reqVO);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * 更改指定pid下的所有code
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);
}
