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
        path: '/dp/dataCodeTable',
        component: Layout,
        redirect: 'dataElem',
        hidden: true,
        children: [
            {
                path: 'dpDataElemDetail',
                component: () => import('@/views/dp/dataCodeTable/detail/user.vue'),
                name: 'tree',
                meta: { title: 'Data Element Details', activeMenu: '/dp/dpDataElem'  }
            }
        ]
    }
 -->

<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="infotop-title mb15">
                    {{ dpDataElemDetail.name || '-' }}
                </div>
                <el-row :gutter="20">
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('dp.dataElem.nameEn') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.engName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('dp.dataElem.catCode') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.catName || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('dp.dataElem.personCharge') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.personCharge || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('dp.dataElem.contactNumber') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.contactNumber || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('dp.dataElem.columnType') }}</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="column_type" :value="dpDataElemDetail.columnType" />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('common.texts.status') }}</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="sys_disable" :value="dpDataElemDetail.status" />
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('common.texts.createdTime') }}</div>
                            <div class="infotop-row-value">
                                {{ parseTime(dpDataElemDetail.createTime, '{y}-{m}-{d}') }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('common.texts.createdBy') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.createBy || '-' }}
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="24">
                        <div class="infotop-row border-top">
                            <div class="infotop-row-lable">{{ td('common.texts.description') }}</div>
                            <div class="infotop-row-value">
                                {{ dpDataElemDetail.description || '-' }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>

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
import codeDict from '@/views/dp/dataCodeTable/detail/dict/codeDict.vue';
import codeMap from '@/views/dp/dataCodeTable/detail/dict/codeMap.vue';
import asset from "@/views/dp/dataCodeTable/detail/components/asset.vue";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const { column_type, sys_disable } = proxy.useDict(
    'column_type',
    'sys_disable'
);

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
