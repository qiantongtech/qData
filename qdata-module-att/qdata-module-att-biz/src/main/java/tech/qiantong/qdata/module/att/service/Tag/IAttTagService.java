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

package tech.qiantong.qdata.module.att.service.Tag;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 标签管理Service接口
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagService extends IService<AttTagDO> {

    /**
     * 获得标签管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标签管理分页列表
     */
    PageResult<AttTagDO> getAttTagPage(AttTagPageReqVO pageReqVO);

    /**
     * 创建标签管理
     *
     * @param createReqVO 标签管理信息
     * @return 标签管理编号
     */
    Long createAttTag(AttTagSaveReqVO createReqVO);

    /**
     * 更新标签管理
     *
     * @param updateReqVO 标签管理信息
     */
    int updateAttTag(AttTagSaveReqVO updateReqVO);

    /**
     * 删除标签管理
     *
     * @param idList 标签管理编号
     */
    int removeAttTag(Collection<Long> idList);

    /**
     * 获得标签管理详情
     *
     * @param id 标签管理编号
     * @return 标签管理
     */
    AttTagRespVO getAttTagById(Long id);

    /**
     * 获得全部标签管理列表
     *
     * @return 标签管理列表
     */
    List<AttTagDO> getAttTagList();

    /**
     * 获得全部标签管理 Map
     *
     * @return 标签管理 Map
     */
    Map<Long, AttTagDO> getAttTagMap();


    /**
     * 导入标签管理数据
     *
     * @param importExcelList 标签管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTag(List<AttTagRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Long getCountByCatCode(String code);


    /**
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
     */
    int updateCatCode(String oldCatCode, String newCatCode);
}
