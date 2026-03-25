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
