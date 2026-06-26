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
