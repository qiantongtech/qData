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
