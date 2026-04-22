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
