<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
    <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus">{{ t('common.button.add') }}</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="info" plain icon="Sort" @click="toggleExpandAll"
                    >{{ t('common.button.un_fold') }}</el-button
                >
            </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </div>

    <el-table
        height="60vh"
        v-if="refreshTable"
        v-loading="loading"
        :data="detailsList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
        <el-table-column label="ID" align="center" prop="parentId">
            <template #default="scope">
                {{ scope.row.id || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="标题" prop="title" />
        <el-table-column label="内容" align="center" prop="content">
            <template #default="scope">
                {{ scope.row.content || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.createdBy')" align="center" prop="createBy">
            <template #default="scope">
                {{ scope.row.createBy || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.createdTime')" align="center" prop="createTime" width="180">
            <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.remark')" align="center" prop="remark">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
                <el-button link type="primary" icon="Edit">{{ t('common.button.update') }}</el-button>
                <el-button link type="primary" icon="Plus">{{ t('common.button.add') }}</el-button>
                <el-button link type="danger" icon="Delete">{{ t('common.button.delete') }}</el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 添加或修改详情对话框 -->
</template>

<script setup name="ComponentTwo">
import { useI18n } from 'vue-i18n'

const { t } = useI18n();
    
const { proxy } = getCurrentInstance();

    const detailsList = ref([]);
    const open = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const title = ref('');
    const isExpandAll = ref(true);
    const refreshTable = ref(true);

    const props = defineProps(['bidId']);

    const data = reactive({
        form: {},
        queryParams: {
            parentId: null,
            bidId: null,
            title: null,
            content: null,
            createTime: null
        },
        rules: {
            parentId: [{ required: true, message: '节点不能为空', trigger: 'blur' }],
            title: [{ required: true, message: '标题不能为空', trigger: 'blur' }],
            validFlag: [{ required: true, message: '是否有效不能为空', trigger: 'blur' }],
            delFlag: [{ required: true, message: '删除标志不能为空', trigger: 'blur' }],
            createTime: [{ required: true, message: '创建时间不能为空', trigger: 'blur' }],
            updateTime: [{ required: true, message: '更新时间不能为空', trigger: 'blur' }]
        }
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询详情列表 */
    function getList() {
        loading.value = true;
        let responseData = [
            {
                id: 1,
                parentId: 0,
                bidId: 13,
                title: '测试',
                content: '11',
                validFlag: true,
                delFlag: false,
                createBy: 'admin',
                creatorId: 1,
                createTime: '2024-12-16 12:08:41',
                updateBy: null,
                updaterId: null,
                updateTime: '2024-12-16 12:08:41',
                remark: '11'
            },
            {
                id: 2,
                parentId: 1,
                bidId: 13,
                title: '测试2',
                content: '2',
                validFlag: true,
                delFlag: false,
                createBy: 'admin',
                creatorId: 1,
                createTime: '2024-12-16 12:09:50',
                updateBy: null,
                updaterId: null,
                updateTime: '2024-12-16 12:09:50',
                remark: '2'
            }
        ];
        detailsList.value = proxy.handleTree(responseData, 'id', 'parentId');
        loading.value = false;
    }

    /** 查询详情下拉树结构 */
    function getTreeselect() {}

    // 取消按钮
    function cancel() {
        open.value = false;
        reset();
    }

    // 表单重置
    function reset() {
        form.value = {
            id: null,
            parentId: null,
            bidId: null,
            title: null,
            content: null,
            validFlag: null,
            delFlag: null,
            createBy: null,
            creatorId: null,
            createTime: null,
            updateBy: null,
            updaterId: null,
            updateTime: null,
            remark: null
        };
        proxy.resetForm('bidDetailsRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    /** 新增按钮操作 */
    function handleAdd(row) {
        reset();
        getTreeselect();
        if (row != null && row.id) {
            form.value.parentId = row.id;
        } else {
            form.value.parentId = 0;
        }
        open.value = true;
        title.value = '新增内容';
    }

    /** 展开/折叠操作 */
    function toggleExpandAll() {
        refreshTable.value = false;
        isExpandAll.value = !isExpandAll.value;
        nextTick(() => {
            refreshTable.value = true;
        });
    }

    /** 修改按钮操作 */
    async function handleUpdate(row) {
        reset();
        await getTreeselect();
        if (row != null) {
            form.value.parentId = row.parentId;
        }
        // (row.id).then(response => {
        //   form.value = response.data;
        //   open.value = true;
        //   title.value = "修改详情";
        // });
    }

    getList();
</script>
