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

export type ProjectItem = {
  /**
   * 项目 id
   */
  id: string
  /**
   * 项目名称
   */
  name: string
  /**
   * 项目状态:
   *
   * 0 - 已发布
   * 1 - 未发布
   */
  status: number
  /**
   * 创建时间
   */
  createTime: number
  /**
   * 预览图片 URL
   */
  picUrl: string
  /**
   * 创建者
   */
  creator: string
  /**
   * 项目备注
   */
  remark: string
}

export interface ProjectDetail extends ProjectItem {
  /**
   * 项目参数
   */
   content: string
}
