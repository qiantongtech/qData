<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://docs.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <el-select v-bind="$attrs" @change="handleDatasourceChange">
        <el-option
            v-for="source in store.datasources"
            :key="source.id"
            :label="source.datasourceName"
            :value="source.id"
            :disabled="source.disabled"
        />
    </el-select>
</template>

<script setup name="DatasourceList">
    import { reactive } from 'vue';
    import { getDatasourceData, getAvailableDatasource } from './utils';
    import useUserStore from '@/store/system/user';

    const props = defineProps({
        flag: {
            type: String
        },
        data: {
            type: Array
        },
        project: {
            type: Boolean
        }
    });

    const emits = defineEmits(['change']);

    const userStore = useUserStore();

    const store = reactive({
        types: [],
        datasources: []
    });

    function initDatasourceData() {
        if (props.data) {
            store.datasources = data;
            return;
        }
        const params = {};
        params.pageNum = 1;
        params.pageSize = 9999;
        if (props.project) {
            params.projectId = userStore.projectId;
            params.projectCode = userStore.projectCode;
        }
        getDatasourceData(params).then((data) => {
            store.datasources = getAvailableDatasource(data, props.flag);
        });
    }

    function handleDatasourceChange(id) {
        const source = store.datasources.find((item) => item.id === id);
        emits('change', id, source);
    }

    initDatasourceData();
</script>
