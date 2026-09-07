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

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<!-- Complex detail route template
    {
        path: '/dp/dataElem',
        component: Layout,
        redirect: 'dataElem',
        hidden: true,
        children: [
            {
                path: 'dpDataElemDetail',
                component: () => import('@/views/dp/dataElem/detail/user.vue'),
                name: 'tree',
                meta: { title: 'Data Element Details', activeMenu: '/dp/dpDataElem'  }
            }
        ]
    }
 -->

<template>
    44
    <div class="app-container" ref="app-container">
        <DetailInfo
            :show="showSearch"
            :data="dpDataElemDetail"
            :header="{
                nameKey: 'name',
                statusKey: 'status',
                statusOptions: sys_disable,
            }"
            :items="detailItems"
            mode="free"
        />

        <div class="pagecont-bottom">
            <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
                <el-tab-pane :label="td('dp.dataElem.detail.codeDict')" name="1">
                    <codeDict />
                </el-tab-pane>
                <el-tab-pane :label="td('dp.dataElem.detail.codeMap')" name="2">
                    <codeMap />
                </el-tab-pane>
                <el-tab-pane :label="td('dp.dataElem.detail.relationInfo')" name="3">
                    <asset />
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script setup name="DpDataElem">
import useDefaultLang from "@/composables/useDefaultLang"
import { getDpDataElem } from '@/api/dp/dataElem/dataElem';
import { useRoute } from 'vue-router';
import codeDict from '@/views/dp/dataElem/detail/dict/codeDict.vue';
import codeMap from '@/views/dp/dataElem/detail/dict/codeMap.vue';
import asset from "@/views/dp/dataElem/detail/components/asset.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
    'column_type',
    'sys_disable',
    'dp_data_elem_code_type'
);

const detailItems = computed(() => [
    { label: td('dp.dataElem.nameEn'), key: 'engName' },
    { label: td('dp.dataElem.catCode'), key: 'catName' },
    { label: td('dp.dataElem.type'), key: 'type', dictOptions: dp_data_elem_code_type.value },
    { label: td('dp.dataElem.personCharge'), key: 'personCharge' },
    { label: td('dp.dataElem.contactNumber'), key: 'contactNumber' },
    { label: td('dp.dataElem.columnType'), key: 'columnType', dictOptions: column_type.value },
    { label: td('common.texts.status'), key: 'status', dictOptions: sys_disable.value },
    { label: td('common.texts.createdTime'), key: 'createTime', type: 'time' },
    { label: td('common.texts.createdBy'), key: 'createBy' },
    { label: td('common.texts.description'), key: 'description', span: 24, className: 'mt2 mb2' }
]);

const activeName = ref('1');

const handleClick = (tab, event) => {
    console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// Monitor id changes
watch(
    () => route.query.id,
    (newId) => {
        id = newId || -1; // If id is empty, the default value 1 is used
        getDpDataElemDetailById();
    },
    { immediate: true } // `immediate` is true, which means that a watch will be executed immediately when the page is loaded.
);
const data = reactive({
    dpDataElemDetail: {},
    form: {}
});

const { dpDataElemDetail, form } = toRefs(data);

/** Form query at the top of the complex details page */
function getDpDataElemDetailById() {
    const _id = id;
    if (!id || id == -1) return;
    getDpDataElem(_id).then((response) => {
        if (!response.data) return;
        dpDataElemDetail.value = response.data;
        console.log(dpDataElemDetail.value);
    });
}
// Get data when page loads
onMounted(() => {
    getDpDataElemDetailById();
});
</script>

<style scoped lang="scss">
.app-container {
    margin: 15px 15px 0px 15px;

    .pagecont-bottom {
        min-height: calc(100vh - 380px) !important;
    }
}
</style>
