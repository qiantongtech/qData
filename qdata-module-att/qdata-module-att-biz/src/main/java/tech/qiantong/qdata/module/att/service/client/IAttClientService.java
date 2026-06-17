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

package tech.qiantong.qdata.module.att.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 应用管理Service接口
 *
 * @author qdata
 * @date 2025-02-18
 */
public interface IAttClientService extends IService<AttClientDO> {

    /**
     * 获得应用管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 应用管理分页列表
     */
    PageResult<AttClientDO> getAttClientPage(AttClientPageReqVO pageReqVO);

    /**
     * 创建应用管理
     *
     * @param createReqVO 应用管理信息
     * @return 应用管理编号
     */
    Long createAttClient(AttClientSaveReqVO createReqVO);

    /**
     * 更新应用管理
     *
     * @param updateReqVO 应用管理信息
     */
    int updateAttClient(AttClientSaveReqVO updateReqVO);

    /**
     * 删除应用管理
     *
     * @param idList 应用管理编号
     */
    int removeAttClient(Collection<Long> idList);

    /**
     * 获得应用管理详情
     *
     * @param id 应用管理编号
     * @return 应用管理
     */
    AttClientDO getAttClientById(Long id);

    /**
     * 获得全部应用管理列表
     *
     * @return 应用管理列表
     */
    List<AttClientDO> getAttClientList();

    /**
     * 获得全部应用管理 Map
     *
     * @return 应用管理 Map
     */
    Map<Long, AttClientDO> getAttClientMap();


    /**
     * 导入应用管理数据
     *
     * @param importExcelList 应用管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttClient(List<AttClientRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
