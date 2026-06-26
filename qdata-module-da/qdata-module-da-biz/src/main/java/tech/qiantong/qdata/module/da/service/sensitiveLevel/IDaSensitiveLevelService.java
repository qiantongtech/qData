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

package tech.qiantong.qdata.module.da.service.sensitiveLevel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 敏感等级Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDaSensitiveLevelService extends IService<DaSensitiveLevelDO> {

    /**
     * 获得敏感等级分页列表
     *
     * @param pageReqVO 分页请求
     * @return 敏感等级分页列表
     */
    PageResult<DaSensitiveLevelDO> getDaSensitiveLevelPage(DaSensitiveLevelPageReqVO pageReqVO);

    /**
     * 创建敏感等级
     *
     * @param createReqVO 敏感等级信息
     * @return 敏感等级编号
     */
    Long createDaSensitiveLevel(DaSensitiveLevelSaveReqVO createReqVO);

    /**
     * 更新敏感等级
     *
     * @param updateReqVO 敏感等级信息
     */
    int updateDaSensitiveLevel(DaSensitiveLevelSaveReqVO updateReqVO);

    /**
     * 删除敏感等级
     *
     * @param idList 敏感等级编号
     */
    int removeDaSensitiveLevel(Collection<Long> idList);

    /**
     * 获得敏感等级详情
     *
     * @param id 敏感等级编号
     * @return 敏感等级
     */
    DaSensitiveLevelDO getDaSensitiveLevelById(Long id);

    /**
     * 获得全部敏感等级列表
     *
     * @return 敏感等级列表
     */
    List<DaSensitiveLevelDO> getDaSensitiveLevelList();

    /**
     * 获得全部敏感等级 Map
     *
     * @return 敏感等级 Map
     */
    Map<Long, DaSensitiveLevelDO> getDaSensitiveLevelMap();


    /**
     * 导入敏感等级数据
     *
     * @param importExcelList 敏感等级数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaSensitiveLevel(List<DaSensitiveLevelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 修改状态
     * @param id 主键
     * @param status 状态值
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
