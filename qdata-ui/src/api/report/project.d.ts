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
