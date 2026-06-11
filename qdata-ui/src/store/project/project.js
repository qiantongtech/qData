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

import { defineStore } from 'pinia'
import { getTreeData as getAssetTree } from "@/api/da/asset/asset"
import { getTreeData as getModelTree } from "@/api/dp/model/model"

export const useProjectStore = defineStore('project', {
    state: () => ({
        project: {},
        assetDeptTree: [],
        modelDeptTree: []
    }),
    actions: {
        // 设置整个项目对象
        setProject(newProject) {
            this.project = newProject
        },
        // 设置项目中的某个属性
        setProjectField(field, value) {
            this.project[field] = value
        },
        // 获取资产类目树
        async getAssetDeptTree(refresh = false) {
            if (this.assetDeptTree.length > 0 && !refresh) {
                return this.assetDeptTree
            }
            try {
                const response = await getAssetTree({ validFlag: true })
                this.assetDeptTree = response.data || []
                return this.assetDeptTree
            } catch (error) {
                console.error('获取资产类目树失败:', error)
                return []
            }
        },
        // 获取模型类目树
        async getModelDeptTree(refresh = false) {
            if (this.modelDeptTree.length > 0 && !refresh) {
                return this.modelDeptTree
            }
            try {
                const response = await getModelTree({ validFlag: true })
                this.modelDeptTree = response.data || []
                return this.modelDeptTree
            } catch (error) {
                console.error('获取模型类目树失败:', error)
                return []
            }
        }
    }
})
