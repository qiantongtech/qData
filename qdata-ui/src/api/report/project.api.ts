import { http } from '@/api/http'
import { httpErrorHandle } from '@/utils'
import { ContentTypeEnum, RequestHttpEnum, ModuleTypeEnum } from '@/enums/httpEnum'
import { ProjectItem, ProjectDetail } from './project' // TODO 分页返回，优化使用 ProjectItem

// * 项目列表
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

// * 新增项目
export const createProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.POST)<number>(`${ModuleTypeEnum.PROJECT}/create`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * 获取项目
export const fetchProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.GET)<ProjectDetail>(`${ModuleTypeEnum.PROJECT}/get`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * 保存项目
export const saveProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * 修改项目基础信息
export const updateProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * 删除项目
export const deleteProjectApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.DELETE)(`${ModuleTypeEnum.PROJECT}/delete`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}

// * 修改发布状态 [0 已发布, 1 未发布]
export const changeProjectReleaseApi = async (data: object) => {
  try {
    const res = await http(RequestHttpEnum.PUT)(`${ModuleTypeEnum.PROJECT}/update`, data)
    return res
  } catch {
    httpErrorHandle()
  }
}
