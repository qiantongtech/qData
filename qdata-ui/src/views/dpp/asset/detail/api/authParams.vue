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

<template>
    <!-- 鉴权参数 -->
    <div class="app-container">
        <div class="body-wrapper">
            <!-- 请求数据部分 -->
            <div class="clearfix header-text" style="margin: 12px 0 0 0">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    {{ td('dpp.asset.detail.api.authParams') }}
                </div>
            </div>
            <el-form :model="{ headerList }" :rules="rules" ref="headerForm" label-width="0">
                <el-table :data="headerList" class="tableStyle" row-key="id" border default-expand-all
                    :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
                    <el-table-column :label="td('dpp.asset.detail.api.index')" width="100" align="left" fixed="left">
                        <template #default="{ $index }">
                            {{ $index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.asset.detail.api.key')" fixed="left" align="left" prop="name"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="{ row, $index }">
                            <!-- <el-form-item :prop="`headerList[${findPosi(headerList, row.id)}].name`"
                                                    :rules="rules.name">
                                                    <el-input v-model="row.name" placeholder="请输入键名" />
                                                </el-form-item> -->
                            {{ row.name || "-" }}

                        </template>
                    </el-table-column>
                    <el-table-column :label="td('common.texts.description')" fixed="left" align="left" prop="remark"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="{ row, $index }">
                            <!-- <el-form-item
                                                    :prop="`headerList[${findPosi(headerList, row.id)}].remark`"
                                                    :rules="rules.fieldDefault">
                                                    <el-input v-model="row.remark" :placeholder="t('common.form.descriptionPlaceholder')" />
                                                </el-form-item> -->
                            {{ row.remark || "-" }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="td('dpp.asset.detail.api.value')" fixed="left" align="left" prop="defaultValue"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="{ row, $index }">
                            <el-form-item :prop="`headerList[${findPosi(headerList, row.dataSculptor)}].defaultValue`"
                                :rules="rules.defaultValue">
                                <el-input v-model="row.defaultValue" :placeholder="td('dpp.asset.detail.api.defaultValuePlaceholder')" />
                            </el-form-item>
                            <!-- {{row.defaultValue||"-"}} -->
                        </template>
                    </el-table-column>
                </el-table>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"

// 引入 Vue 和必要的 API 方法
import { ref, reactive, onMounted } from 'vue';
// import { getApiCall, postApiCall } from "@/api/market/apimapping";
// import { getYApiConfig } from "@/api/market/yapiConfig";
// import { getDataApi } from "@/api/market/dataapi";

import { queryServiceForwarding } from "@/api/da/assetApply/assetApply";
const props = defineProps({
    form1: {
        type: Object,
        default: {}
    },
});
const { proxy } = getCurrentInstance();
import request from '@/utils/request';

const { td } = useDefaultLang();
const hasChildren = (row) => {
    return Array.isArray(row.daAssetApiParamList) && row.daAssetApiParamList.length > 0;
};
// 定义响应式数据
const title = ref('');  // 标题
const form = reactive({});  // 表单数据
const treeData1 = ref([]);  // 请求头数据
const treeData2 = ref([]);  // 请求参数数据
const activeTabName = ref('table0');  // 当前激活的 Tab
const bashUrl = ref(null);  // 调用路径
const apiExecuting = ref(false);  // 是否正在调用API
const apiExecuting2 = ref(false);  // 是否返回HTML数据
const htmlData = ref(null);  // 返回的HTML数据
const jsonFlag = ref(false);  // 是否为JSON格式
const callData = reactive({
    dataList: [],  // 接口返回的数据列表
    columnList: [],  // 数据的列名列表
    pageNum: 1,  // 当前页码
    pageSize: 20,  // 每页数据量
    dataTotal: 0  // 数据总数
});
const inputList = computed(() => {
    return props.form1?.daAssetApiParamList?.filter(item => Number(item.type) == 1) || [];
});

const headerList = computed(() => {
    return props.form1?.daAssetApiParamList?.filter(item => Number(item.type) == 3) || [];
});
const rules = {
    name: [{ required: true, message: td('dpp.asset.detail.api.paramNameRequired'), trigger: "blur" }],
    columnType: [{ required: true, message: td('dpp.asset.detail.api.paramTypeRequired'), trigger: "change" }],
};
const findPosi = (array, targetId, path = '') => {
    for (let i = 0; i < array.length; i++) {
        const item = array[i];
        if (item.id === targetId) {
            return path + i; // 返回当前节点的索引作为路径
        }
        if (item.daAssetApiParamList && item.daAssetApiParamList.length > 0) {
            // 递归查找子节点
            const childPath = `${path}${i}.daAssetApiParamList.`;
            const result = findPosi(item.daAssetApiParamList, targetId, childPath);
            if (result !== null) {
                return result; // 找到则返回路径
            }
        }
    }
    return null; // 没找到返回 null
}
// 获取YAPI配置
const getYApiConfig = (id) => {
    queryServiceForwarding(id).then((response) => {
        console.log("🚀 ~ queryServiceForwarding ~ response:", response)
        if (response.code == 200) {
            treeData1.value = response.content.fieldHerderList || [];
            treeData1.value.unshift({
                fieldName: "Authorization",
                fieldDescribes: `${td('dpp.asset.detail.api.authDesc')}${request.defaults.baseURL}/system/auth/getToken${td('dpp.asset.detail.api.authDescEnd')}`,
            });
        }
    });
};
const baseUrl = import.meta.env.VITE_APP_BASE_API;
// 获取数据API配置
const getDataApi = async () => {
    getYApiConfig(props.form1?.daAssetApi.id);
};
// console.log("🚀 ~ getDataApi ~ props.form1?.daAssetApi.id:", props.form1?.daAssetApi.id)

// 组件挂载时获取数据
onMounted(() => {
    // getDataApi();
});
let apiHeader = ref({})
function buildParamsTree(paramList) {
    const result = {};

    paramList.forEach(param => {
        let value = undefined;

        if (
            param.defaultValue !== null &&
            param.defaultValue !== '' &&
            param.defaultValue !== undefined
        ) {
            value = !isNaN(param.defaultValue)
                ? parseInt(param.defaultValue)
                : param.defaultValue;
        }

        // 有子节点，递归构建
        if (Array.isArray(param.daAssetApiParamList) && param.daAssetApiParamList.length > 0) {
            result[param.name] = buildParamsTree(param.daAssetApiParamList);
        } else {
            result[param.name] = value;
        }
    });

    return result;
}
const showSuccessNotify = () => {
    ElNotification({
        title: td('common.message.prompt'),
        message: td('dpp.asset.detail.api.callSuccess'),
        type: 'success',
        duration: 2000,
    });
};

const showErrorNotify = (msg) => {
    ElNotification({
        title: td('common.message.prompt'),
        message: msg || td('dpp.asset.detail.api.callFailed'),
        type: 'error',
        duration: 2000,
    });
};

// 调用接口
const handleCall = () => {
    const isParamInvalid = (param) => {
        const hasChildren = Array.isArray(param.daAssetApiParamList) && param.daAssetApiParamList.length > 0;
        return !hasChildren && param.requestFlag === '0' &&
            (param.defaultValue === null || param.defaultValue === undefined || param.defaultValue === '' || isNaN(param.defaultValue));
    };
    const isJSON = (obj) => {
        return Object.prototype.toString.call(obj) === '[object Object]';
    }
    // 参数校验
    const isNull = inputList.value.some(param => {
        if (isParamInvalid(param)) {
            proxy.$message.warning(`${td('dpp.asset.detail.api.paramRequired')}${param.name}${td('dpp.asset.detail.api.paramRequiredEnd')}`);
            return true;
        }
        return false;
    });
    if (isNull) return;

    // 构造请求体
    const data = {
        id: props.form1?.daAssetApi.id,
        queryParams: {
            params: buildParamsTree(inputList.value),
            fieldHerderList: headerList.value.map(item => ({
                name: item.name,
                defaultValue: item.defaultValue
            }))
        }
    };

    // 请求接口
    queryServiceForwarding(data).then((response) => {
        console.log("🚀 ~ queryServiceForwarding ~ response:", response);
        if (response.code == 200) {
            // proxy.$message.msgSuccess('接口调用成功');
            const dataList = response.data?.data || [];
            const columnList = dataList.length > 0 ? Object.keys(dataList[0]) : [];
            callData.dataList = dataList;
            callData.columnList = columnList;
            callData.dataTotal = response.data.total;
            apiExecuting.value = true;
        } else if (response && response.code == null) {
            // proxy.$message.msgSuccess('接口调用成功');
            htmlData.value = response;
            jsonFlag.value = isJSON(response);
            console.log("🚀 ~ queryServiceForwarding ~ response:", response)
            apiExecuting2.value = true;
        } else {
            // proxy.$message.error(response.msg || '接口调用失败');
        }
    });
};

// 处理API响应
const handleApiResponse = (response) => {
    apiExecuting.value = true;
    apiExecuting2.value = false;
    callData.dataList = response.data.dataList || [];
    callData.columnList = response.data.columnList || [];
    callData.pageNum = response.data.pageNum || 1;
    callData.pageSize = response.data.pageSize || 20;
    callData.dataTotal = response.data.dataTotal || 0;
};
</script>
<style lang="less" scoped>
.app-container {
    margin-top: -15px;
    min-height: 65vh;
    margin-left: 0px;
    background-color: #ffffff;
    border-radius: 8px;

    // box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    .header-text {
        display: flex;
        align-items: center;
        margin-bottom: 3px;
        margin: 10px 0;
    }

    .section-title {
        width: 100%;
        height: 36px;
        background-color: #f8f8f9;
        display: flex;
        align-items: center;
        padding-left: 10px;
        margin-bottom: 10px;
        font-size: 16px;
        font-weight: bold;
        color: #333;
    }

    .section-title span {
        display: flex;
        align-items: center;
    }

    .blue-bar {
        background-color: #2666FB; // 蓝条颜色
        width: 5px; // 宽度5px
        height: 20px; // 高度20px
        margin-right: 10px; // 图片与文字之间的间距
    }

    .header-container {
        height: 36px;
        background-color: #f8f8f9;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 5px -10px;
        margin: 10px 0;
        border-radius: 4px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .header-left {
        display: flex;
        align-items: center;
    }

    .el-form {
        margin-top: 20px;
    }

    .el-form-item {
        margin-bottom: 15px;
    }

    .el-input,
    .el-select {
        width: 100%;
    }

    .el-button {
        transition: background-color 0.3s;

        &:hover {
            background-color: #2666fb;
            color: #ffffff;
        }
    }

    .tableStyle {
        font-size: 14px;
        margin: 0px !important;

        ::v-deep {
            th.el-table__cell>.cell {
                padding: 0 5px !important;
                font-style: normal;
                text-transform: none;
                background-color: #f0f2f5;
                color: #333;
                white-space: nowrap;
            }

            .el-table__row {
                .el-table__cell {
                    padding: 4px 0 !important;
                    transition: background-color 0.3s;

                    &:hover {
                        background-color: #f5f7fa;
                    }
                }
            }

            .el-table__header-wrapper th {
                padding: 4px 0;
            }
        }
    }

    .no-data {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 250px;
        text-align: center;
        font-size: 14px;
        color: #909399;
        background-color: #f8f8f9;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
}

.el-card ::v-deep .el-card__body {
    height: calc(100vh - 230px);
    overflow-y: auto;
}

.html-iframe {
    width: 100%;
    height: 500px;
}
</style>
