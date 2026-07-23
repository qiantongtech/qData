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
        // Set up the entire project object
        setProject(newProject) {
            this.project = newProject
        },
        // Set a property in the project
        setProjectField(field, value) {
            this.project[field] = value
        },
        // Get asset category tree
        async getAssetDeptTree(refresh = false) {
            if (this.assetDeptTree.length > 0 && !refresh) {
                return this.assetDeptTree
            }
            try {
                const response = await getAssetTree({ validFlag: true })
                this.assetDeptTree = response.data || []
                return this.assetDeptTree
            } catch (error) {
                console.error("Failed to fetch asset category tree:", error)
                return []
            }
        },
        // Get model category tree
        async getModelDeptTree(refresh = false) {
            if (this.modelDeptTree.length > 0 && !refresh) {
                return this.modelDeptTree
            }
            try {
                const response = await getModelTree({ validFlag: true })
                this.modelDeptTree = response.data || []
                return this.modelDeptTree
            } catch (error) {
                console.error("Failed to fetch model category tree:", error)
                return []
            }
        }
    }
})
