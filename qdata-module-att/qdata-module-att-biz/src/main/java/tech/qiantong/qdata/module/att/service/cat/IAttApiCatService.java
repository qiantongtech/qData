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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据服务类目管理Service接口
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface IAttApiCatService extends IService<AttApiCatDO> {

    /**
     * 获得数据服务类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据服务类目管理分页列表
     */
    PageResult<AttApiCatDO> getAttApiCatPage(AttApiCatPageReqVO pageReqVO);

    /**
     * 创建数据服务类目管理
     *
     * @param createReqVO 数据服务类目管理信息
     * @return 数据服务类目管理编号
     */
    Long createAttApiCat(AttApiCatSaveReqVO createReqVO);

    /**
     * 更新数据服务类目管理
     *
     * @param updateReqVO 数据服务类目管理信息
     */
    int updateAttApiCat(AttApiCatSaveReqVO updateReqVO);

    /**
     * 删除数据服务类目管理
     *
     * @param idList 数据服务类目管理编号
     */
    int removeAttApiCat(Collection<Long> idList);

    /**
     * 获得数据服务类目管理详情
     *
     * @param id 数据服务类目管理编号
     * @return 数据服务类目管理
     */
    AttApiCatDO getAttApiCatById(Long id);

    /**
     * 获得全部数据服务类目管理列表
     *
     * @return 数据服务类目管理列表
     */
    List<AttApiCatDO> getAttApiCatList();

    /**
     * 获得全部数据服务类目管理列表
     *
     * @return 数据服务类目管理列表
     */
    List<AttApiCatDO> getAttApiCatList(AttApiCatPageReqVO pageReqVO);

    /**
     * 获得全部数据服务类目管理 Map
     *
     * @return 数据服务类目管理 Map
     */
    Map<Long, AttApiCatDO> getAttApiCatMap();


    /**
     * 导入数据服务类目管理数据
     *
     * @param importExcelList 数据服务类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttApiCat(List<AttApiCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
