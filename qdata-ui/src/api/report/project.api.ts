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

import { http } from '@/api/http'
import { httpErrorHandle } from '@/utils'
import { ContentTypeEnum, RequestHttpEnum, ModuleTypeEnum } from '@/enums/httpEnum'
import { ProjectItem, ProjectDetail } from './project' // TODO paging return, optimized use of ProjectItem

// * Project list
export const projectListApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.GET)<{
      list: ProjectItem[],
      count: number
    }>(`${ModuleTypeEnum.PROJECT}/my-page`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Add new items
export const createProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.POST)<number>(`${ModuleTypeEnum.PROJECT}/create`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Get items
export const fetchProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.GET)<ProjectDetail>(`${ModuleTypeEnum.PROJECT}/get`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Save project
export const saveProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Modify basic project information
export const updateProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Delete project
export const deleteProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.DELETE)(`${ModuleTypeEnum.PROJECT}/delete`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * Modify publishing status [0 published, 1 unpublished]
export const changeProjectReleaseApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}
