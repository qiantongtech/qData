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
   * project id
   */
  id: string
  /**
   * Project name
   */
  name: string
  /**
   * Project status:
   *
   * 0 - Published
   * 1 - Not published
   */
  status: number
  /**
   * creation time
   */
  createTime: number
  /**
   * Preview image URL
   */
  picUrl: string
  /**
   * Creator
   */
  creator: string
  /**
   * Project notes
   */
  remark: string
}

export interface ProjectDetail extends ProjectItem {
  /**
   * Project parameters
   */
   content: string
}
