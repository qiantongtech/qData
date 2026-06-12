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
    <Test ref="test" :isChange="false" :form="form1" :resTypeOptions="resTypeOptions" :whetherOptions="whetherOptions"
        :statusOptions="statusOptions" v-if="form1.apiServiceType != '3'" />
    <testapi ref="test" :isChange="false" :data="form1" :resTypeOptions="resTypeOptions"
        v-if="form1.apiServiceType == '3'" :whetherOptions="whetherOptions" :statusOptions="statusOptions" />
    <!-- 添加或修改详情对话框 -->

</template>

<script setup name="ComponentTwo">
import Test from '@/views/ds/api/edit/components/simulation.vue';
import testapi from '@/views/ds/api/edit/components/testApi.vue';
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const components = { Test };
const { proxy } = getCurrentInstance();
const detailsList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref('');
const isExpandAll = ref(true);
const refreshTable = ref(true);
const props = defineProps({
    form1: {
        type: Object,
        default: {}
    }
});

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
        parentId: [{ required: true, message: td('ds.api.apiDetail.simulation.nodeRequired'), trigger: 'blur' }],
        title: [{ required: true, message: td('ds.api.apiDetail.simulation.titleRequired'), trigger: 'blur' }],
        validFlag: [{ required: true, message: td('ds.api.apiDetail.simulation.validFlagRequired'), trigger: 'blur' }],
        delFlag: [{ required: true, message: td('ds.api.apiDetail.simulation.delFlagRequired'), trigger: 'blur' }],
        createTime: [{ required: true, message: td('ds.api.apiDetail.simulation.createTimeRequired'), trigger: 'blur' }],
        updateTime: [{ required: true, message: td('ds.api.apiDetail.simulation.updateTimeRequired'), trigger: 'blur' }]
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
function getTreeselect() { }

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
    title.value = td('ds.api.apiDetail.simulation.addContent');
}

/** 展开/折叠操作 */
function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
        refreshTable.value = true;
    });
}

getList();
</script>
