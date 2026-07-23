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
    <div class="app-container">
        <!-- head area -->
        <div slot="header" class="header-container">
            <div class="header-left">
                <div class="blue-bar"></div>
              {{ td('ds.apiEdit.testApi.apiCall') }}
            </div>
            <el-button  size="mini" style="border-radius: 30px !important" round
                @click="handleCall">
                {{ td('ds.apiEdit.testApi.interfaceCall') }}
            </el-button>
        </div>

        <!-- text area -->
        <div class="body-wrapper">
            <el-form v-if="isChange" ref="data" :model="data" label-width="100px" :disabled="true">
                <el-row>
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiEdit.testApi.apiName')">
                            <el-input v-model="data.name" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiEdit.testApi.apiVersion')">
                            <el-input v-model="data.apiVersion" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiEdit.testApi.requestType')">
                            <!--                            <el-input v-model="form.reqMethod"/>-->
                            <dict-tag :options="ds_api_bas_info_api_method_type" :value="data.reqMethod" />
                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item :label="td('ds.apiEdit.testApi.returnFormat')" prop="resDataType">
                            <dict-tag :options="ds_api_bas_info_res_data_type" :value="data.resDataType" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item :label="td('ds.apiEdit.testApi.callAddress')">
                            <el-input v-model="data.apiUrl" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- Request data part -->
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    {{td('ds.apiEdit.testApi.requestData')}}
                </div>
            </div>
            <el-form :model="data.reqParams" :rules="rules" ref="inputForm" label-width="0">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="data.reqParams" class="tableStyle" row-key="id" stripe default-expand-all
                            :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
                            <el-table-column :label="td('common.display.index')" width="80" align="center" fixed="left">
                                <template #default="{ $index }">
                                    {{ $index + 1 }}
                                </template>
                            </el-table-column>

                            <el-table-column :label="td('ds.apiEdit.testApi.paramName')" fixed="left" align="center" prop="name"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.name || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column :label="td('common.texts.description')" fixed="left" align="center" prop="remark"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.remark || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column :label="td('ds.apiEdit.testApi.nullable')" width="70" fixed="left" align="center" prop="requestFlag"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    <el-form-item
                                        :prop="`data.reqParams[${findPosi(data.reqParams, row.id)}].requestFlag`"
                                        :rules="rules.requestFlag">
                                        <el-checkbox v-model="row.requestFlag" disabled :true-label="'1'"
                                            :false-label="'0'"> </el-checkbox>
                                    </el-form-item>
                                </template>
                            </el-table-column>

                            <el-table-column :label="td('ds.apiEdit.testApi.columnType')" fixed="left" align="center" prop="columnType"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.columnType || '' }}
                                </template>
                            </el-table-column>


                            <el-table-column :label="td('ds.apiEdit.testApi.exampleValue')" fixed="left" align="center" prop="exampleValue"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    <el-form-item
                                        :prop="`data.reqParams[${findPosi(data.reqParams, row.id)}].exampleValue`"
                                        :rules="hasChildren(row) ? rules.fieldDefault : []">
                                        <el-input v-model="row.fieldDefault" :placeholder="td('ds.apiEdit.testApi.exampleValuePlaceholder')"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item>
                                </template>
                            </el-table-column>

                            <el-table-column :label="td('ds.apiEdit.testApi.defaultValue')" fixed="left" align="center" prop="defaultValue"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    <el-form-item
                                        :prop="`data.reqParams[${findPosi(data.reqParams, row.id)}].defaultValue`"
                                        :rules="hasChildren(row) ? rules.defaultValue : []">
                                        <el-input v-model="row.defaultValue" :placeholder="td('ds.apiEdit.testApi.defaultValuePlaceholder')"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
            </el-form>
            <!-- Return result part -->
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    {{td('ds.apiEdit.testApi.returnData')}}
                </div>
            </div>
            <el-row>
                <el-col :span="24">
                    <div v-if="apiExecuting">
                        <el-table :data="callData.dataList" stripe border :max-height="600"
                            style="width: 100%; margin: 15px 0">
                            <el-table-column :label="td('common.display.index')" align="center">
                                <template #default="{ scope }">
                                    <span>{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <template v-for="(column, index) in callData.columnList" :key="index">
                                <el-table-column :prop="column" :label="column" align="center" :show-overflow-tooltip="{effect: 'light'}" />
                            </template>
                        </el-table>
                        <!-- Pagination -->
                        <el-pagination :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
                            :current-page.sync="callData.pageNum" :page-size.sync="callData.pageSize"
                            :total="callData.dataTotal" @size-change="handleSizeChange"
                            @current-change="handleCurrentChange" />
                    </div>
                    <!-- Return data in HTML format -->
                    <div v-if="apiExecuting2" class="html-container">
                        <VAceEditor v-if="jsonFlag" ref="aceRef" v-model:value="htmlData" class="editor-content"
                            placeholder="" :options="aceOptions" lang="json" readonly theme="github" />
                        <iframe class="html-iframe" v-if="!jsonFlag" :srcdoc="htmlData"></iframe>
                    </div>
                    <!-- Display prompt when no data is returned -->
                    <div v-else>{{td('common.noData')}}</div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import { queryServiceForwarding } from '@/api/ds/api/api.js';
import { VAceEditor } from "vue3-ace-editor";
const props = defineProps({
    data: {
        type: Object,
        default: function () {
            return {};
        }
    },
    form: {
        type: Object,
        default: {}
    },
    reqMethodOptions: {
        type: Array,
        required: true
    },
    resTypeOptions: {
        type: Array,
        required: true
    },
    whetherOptions: {
        type: Array,
        required: true
    },
    statusOptions: {
        type: Array,
        required: true
    },
    isChange: {
        type: Boolean,
        default: true
    }
});
const { proxy } = getCurrentInstance();
const { ds_api_bas_info_api_method_type, ds_api_param_type, ds_api_bas_info_res_data_type } =
    proxy.useDict(
        'ds_api_bas_info_api_method_type',
        'ds_api_param_type',
        'ds_api_bas_info_res_data_type'
    );
import request from '@/utils/request';
const hasChildren = (row) => {
    return Array.isArray(row.daAssetApiParamList) && row.daAssetApiParamList.length > 0;
};
const aceOptions = ref({
    printMarginColumn: 30,
    displayIndentGuides: false,
    useWorker: true,
    showPrintMargin: false,
    useSoftTabs: true, // Use spaces instead of tabs
    highlightActiveLine: true, // Highlight current line
    enableMultiselect: true,
    readOnly: true,
    wrap: true,
    showLineNumbers: false,
    showGutter: false,
});
// Define responsive data
const title = ref(td('ds.apiEdit.testApi.dataApiCall'));  // Title
const form = reactive({});  // form data
const treeData1 = ref([]);  // Request header data
const treeData2 = ref([]);  // Request parameter data
const activeTabName = ref('table0');  // Currently active Tab
const bashUrl = ref(null);  // Call path
const apiExecuting = ref(false);  // Whether the API is being called
const apiExecuting2 = ref(false);  // Whether to return HTML data
const htmlData = ref(null);  // Returned HTML data
const jsonFlag = ref(false);  // Is it in JSON format?
const callData = reactive({
    dataList: [],  // Data list returned by the interface
    columnList: [],  // List of column names of data
    pageNum: 1,  // Current page number
    pageSize: 20,  // Amount of data per page
    dataTotal: 0  // Total data
});



const rules = {
    name: [{ required: true, message: td('ds.apiEdit.testApi.paramNameRequired'), trigger: "blur" }],
    columnType: [{ required: true, message: td('ds.apiEdit.testApi.paramTypeRequired'), trigger: "change" }],
};
const findPosi = (array, targetId, path = '') => {
    for (let i = 0; i < array.length; i++) {
        const item = array[i];
        if (item.id === targetId) {
            return path + i; // Returns the index of the current node as a path
        }
        if (item.daAssetApiParamList && item.daAssetApiParamList.length > 0) {
            // Find child nodes recursively
            const childPath = `${path}${i}.daAssetApiParamList.`;
            const result = findPosi(item.daAssetApiParamList, targetId, childPath);
            if (result !== null) {
                return result; // If found, return the path
            }
        }
    }
    return null; // Not found returns null
}
// Get YAPI configuration
const getYApiConfig = (id) => {
    queryServiceForwarding(id).then((response) => {
        console.log("🚀 ~ queryServiceForwarding ~ response:", response)
        if (response.code == 200) {
            treeData1.value = response.content.fieldHerderList || [];
            treeData1.value.unshift({
                fieldName: "Authorization",
                fieldDescribes: td('ds.apiEdit.testApi.authHelpText') + request.defaults.baseURL + td('ds.apiEdit.testApi.authHelpSuffix'),
            });
        }
    });
};
const baseUrl = import.meta.env.VITE_APP_BASE_API;
// Get data API configuration
const getDataApi = async () => {
    getYApiConfig(props.data?.daAssetApi.id);
};
// console.log("🚀 ~ getDataApi ~ props.data ?.daAssetApi.id:", props.data ?.daAssetApi.id)

// Get data when component is mounted
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

        // There are child nodes and are constructed recursively.
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
        title: td('ds.apiEdit.testApi.prompt'),
        message: td('ds.apiEdit.testApi.callSuccess'),
        type: 'success',
        duration: 2000,
    });
};

const showErrorNotify = (msg) => {
    ElNotification({
        title: td('ds.apiEdit.testApi.prompt'),
        message: msg || td('ds.apiEdit.testApi.callFailed'),
        type: 'error',
        duration: 2000,
    });
};

// Call interface
const handleCall = () => {
    const isParamInvalid = (param) => {
        const hasChildren = Array.isArray(param.daAssetApiParamList) && param.daAssetApiParamList.length > 0;
        return !hasChildren && param.requestFlag === '0' &&
            (param.defaultValue === null || param.defaultValue === undefined || param.defaultValue === '' || isNaN(param.defaultValue));
    };
    const isJSON = (obj) => {
        if (typeof obj === 'string' && obj.trim().startsWith('<?xml')) {
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(obj, 'application/xml');

            if (xmlDoc.documentElement.nodeName === 'Capabilities') {
                return true;
            }
        }

        // Determine whether it is a standard JSON object
        return Object.prototype.toString.call(obj) === '[object Object]';
    };

    // Parameter verification
    const isNull = props.data.reqParams.some(param => {
        if (isParamInvalid(param)) {
            proxy.$message.warning(td('ds.apiEdit.testApi.nullableWarning') + `‘${param.name}’` + td('ds.apiEdit.testApi.cannotBeEmpty'));
            return true;
        }
        return false;
    });
    if (isNull) return;
    // Construct request body
    const data = {
        ...props.data,
        id: props.data.apiId,
        headerJson: JSON.stringify(props.data.headerJson),
        transmitType: props.data.transmitType,
        daAssetApi: props.data.daAssetApi,
        apiServiceType: props.data.apiServiceType,
        queryParams: {
            params: buildParamsTree(props.data.reqParams),
        }
    };
    console.log("🚀 ~ queryServiceForwarding ~ data:", data)

    // Request interface
    queryServiceForwarding(data).then((response) => {
        if (response.code == 200) {
            // proxy.$message.msgSuccess('Interface call successful');
            const dataList = response.data?.data || [];
            const columnList = dataList.length > 0 ? Object.keys(dataList[0]) : [];
            callData.dataList = dataList;
            callData.columnList = columnList;
            callData.dataTotal = response.data.total;
            apiExecuting.value = true;
        } else if (response && response.code == null) {
            htmlData.value = response;
            jsonFlag.value = isJSON(response);
            if (jsonFlag.value) {
                htmlData.value = JSON.stringify(response, null, 2);
            }
            apiExecuting2.value = true;
        } else {
            // proxy.$message.error(response.msg || 'Interface call failed');
        }
    });
};


// Handling API responses
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
<style lang="scss" scoped>
.app-container {
    margin-top: -5px;
    min-height: 65vh;
    margin-left: 0px;
    // padding: 20px;
    background-color: #ffffff;
    border-radius: 8px;

    .header-text {
        display: flex;
        align-items: center;
        margin-bottom: 3px;
        margin: 10px 0;
    }

    // box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    .html-container {
        width: 100%;
        height: 300px;

        .editor-content {
            width: 100%;
            height: 300px;
        }
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
        background-color: #2666fb;
        width: 5px;
        height: 20px;
        margin-right: 10px;
        border-radius: 2px;
    }

    .header-container {
        height: 36px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 5px 0;
        margin: 10px 0;
        border-radius: 4px;
    }

    .header-left {
        display: flex;
        align-items: center;
        font-size: 16px;
        line-height: 24px;
        font-style: normal;
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
</style>
<style lang="less" scoped>
.el-card ::v-deep .el-card__body {
    height: calc(100vh - 230px);
    overflow-y: auto;
}

.html-iframe {
    width: 100%;
    height: 500px;
}
</style>