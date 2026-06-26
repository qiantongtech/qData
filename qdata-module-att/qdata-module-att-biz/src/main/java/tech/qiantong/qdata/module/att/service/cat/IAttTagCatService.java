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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;

import java.util.List;
import java.util.Map;

/**
 * 标签类目管理Service接口
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagCatService extends IService<AttTagCatDO> {

    /**
     * 获得标签类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标签类目管理分页列表
     */
    PageResult<AttTagCatDO> getAttTagCatPage(AttTagCatPageReqVO pageReqVO);

    /**
     * 创建标签类目管理
     *
     * @param createReqVO 标签类目管理信息
     * @return 标签类目管理编号
     */
    Long createAttTagCat(AttTagCatSaveReqVO createReqVO);

    /**
     * 更新标签类目管理
     *
     * @param updateReqVO 标签类目管理信息
     */
    int updateAttTagCat(AttTagCatSaveReqVO updateReqVO);

    /**
     * 删除标签类目管理
     *
     * @param idList 标签类目管理编号
     */
//    int removeAttTagCat(Collection<Long> idList);

    /**
     * 获得标签类目管理详情
     *
     * @param id 标签类目管理编号
     * @return 标签类目管理
     */
    AttTagCatDO getAttTagCatById(Long id);

    /**
     * 获得全部标签类目管理列表
     *
     * @return 标签类目管理列表
     */
    List<AttTagCatDO> getAttTagCatList();

    /**
     * 获得全部标签类目管理 Map
     *
     * @return 标签类目管理 Map
     */
    Map<Long, AttTagCatDO> getAttTagCatMap();


    /**
     * 导入标签类目管理数据
     *
     * @param importExcelList 标签类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTagCat(List<AttTagCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获得全部数据标签类目管理列表
     *
     * @return 数据标签类目管理列表
     */
    List<AttTagCatDO> getAttTagCatLIst(AttTagCatPageReqVO attTagCat);

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

    Integer removeAttTagCat(Long id);

}
