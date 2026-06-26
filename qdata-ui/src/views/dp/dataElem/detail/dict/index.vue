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

<!-- 复杂详情路由模板
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
                meta: { title: '数据元详情', activeMenu: '/dp/dpDataElem'  }
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
                            <div class="infotop-row-lable">{{ td('dp.dataElem.type') }}</div>
                            <div class="infotop-row-value">
                                <dict-tag :options="dp_data_elem_code_type" :value="dpDataElemDetail.type" />
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

const activeName = ref('1');

const handleClick = (tab, event) => {
    console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
        id = newId || -1; // 如果 id 为空，使用默认值 1
        getDpDataElemDetailById();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
    dpDataElemDetail: {},
    form: {}
});

const { dpDataElemDetail, form } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDpDataElemDetailById() {
    const _id = id;
    if (!id || id == -1) return;
    getDpDataElem(_id).then((response) => {
        if (!response.data) return;
        dpDataElemDetail.value = response.data;
        console.log(dpDataElemDetail.value);
    });
}
// 页面加载时获取数据
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
