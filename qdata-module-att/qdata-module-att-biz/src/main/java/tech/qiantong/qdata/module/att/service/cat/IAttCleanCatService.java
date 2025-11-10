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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;

import java.util.List;
import java.util.Map;

/**
 * 清洗规则类目Service接口
 *
 * @author qdata
 * @date 2025-08-11
 */
public interface IAttCleanCatService extends IService<AttCleanCatDO> {

    /**
     * 获得清洗规则类目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 清洗规则类目分页列表
     */
    PageResult<AttCleanCatDO> getAttCleanCatPage(AttCleanCatPageReqVO pageReqVO);

    /**
     * 创建清洗规则类目
     *
     * @param createReqVO 清洗规则类目信息
     * @return 清洗规则类目编号
     */
    Long createAttCleanCat(AttCleanCatSaveReqVO createReqVO);

    /**
     * 更新清洗规则类目
     *
     * @param updateReqVO 清洗规则类目信息
     */
    int updateAttCleanCat(AttCleanCatSaveReqVO updateReqVO);

    /**
     * 删除清洗规则类目
     *
     * @param idList 清洗规则类目编号
     */
    int removeAttCleanCat(Long idList);

    /**
     * 获得清洗规则类目详情
     *
     * @param id 清洗规则类目编号
     * @return 清洗规则类目
     */
    AttCleanCatDO getAttCleanCatById(Long id);

    /**
     * 获得全部清洗规则类目列表
     *
     * @return 清洗规则类目列表
     */
    List<AttCleanCatDO> getAttCleanCatList(AttCleanCatPageReqVO attCleanCat);
    List<AttCleanCatDO> getAttCleanCatList();

    /**
     * 获得全部清洗规则类目 Map
     *
     * @return 清洗规则类目 Map
     */
    Map<Long, AttCleanCatDO> getAttCleanCatMap();


    /**
     * 导入清洗规则类目数据
     *
     * @param importExcelList 清洗规则类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttCleanCat(List<AttCleanCatRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

}
