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
    <div class="app-container">
        <!-- 头部区域 -->
        <div slot="header" class="header-container">
            <div class="header-left">
                <div class="blue-bar"></div>
                API调用
            </div>
            <el-button  size="mini" style="border-radius: 30px !important" round
                @click="handleCall">
                接口调用
            </el-button>
        </div>

        <!-- 正文区域 -->
        <div class="body-wrapper">
            <el-form v-if="isChange" ref="data" :model="data" label-width="100px" :disabled="true">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="API名称">
                            <el-input v-model="data.name" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="API版本">
                            <el-input v-model="data.apiVersion" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="请求类型">
                            <!--                            <el-input v-model="form.reqMethod"/>-->
                            <dict-tag :options="ds_api_bas_info_api_method_type" :value="data.reqMethod" />
                        </el-form-item>

                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="返回格式" prop="resDataType">
                            <dict-tag :options="ds_api_bas_info_res_data_type" :value="data.resDataType" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="调用地址">
                            <el-input v-model="data.apiUrl" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 请求数据部分 -->
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    请求数据
                </div>
            </div>
            <el-form :model="data.reqParams" :rules="rules" ref="inputForm" label-width="0">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="data.reqParams" class="tableStyle" row-key="id" stripe default-expand-all
                            :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
                            <el-table-column label="序号" width="80" align="center" fixed="left">
                                <template #default="{ $index }">
                                    {{ $index + 1 }}
                                </template>
                            </el-table-column>

                            <el-table-column label="参数名称" fixed="left" align="center" prop="name"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.name || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column :label="t('common.texts.description')" fixed="left" align="center" prop="remark"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.remark || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="是否为空" width="70" fixed="left" align="center" prop="requestFlag"
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

                            <el-table-column label="参数类型" fixed="left" align="center" prop="columnType"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    {{ row?.columnType || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="示例值" fixed="left" align="center" prop="exampleValue"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    <el-form-item
                                        :prop="`data.reqParams[${findPosi(data.reqParams, row.id)}].exampleValue`"
                                        :rules="hasChildren(row) ? rules.fieldDefault : []">
                                        <el-input v-model="row.fieldDefault" placeholder="请输入示例值"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item>
                                </template>
                            </el-table-column>

                            <el-table-column label="默认值" fixed="left" align="center" prop="defaultValue"
                                :show-overflow-tooltip="{effect: 'light'}">
                                <template #default="{ row }">
                                    <el-form-item
                                        :prop="`data.reqParams[${findPosi(data.reqParams, row.id)}].defaultValue`"
                                        :rules="hasChildren(row) ? rules.defaultValue : []">
                                        <el-input v-model="row.defaultValue" placeholder="请输入默认值"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 返回结果部分 -->
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    返回数据
                </div>
            </div>
            <el-row>
                <el-col :span="24">
                    <div v-if="apiExecuting">
                        <el-table :data="callData.dataList" stripe border :max-height="600"
                            style="width: 100%; margin: 15px 0">
                            <el-table-column label="序号" align="center">
                                <template #default="{ scope }">
                                    <span>{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <template v-for="(column, index) in callData.columnList" :key="index">
                                <el-table-column :prop="column" :label="column" align="center" :show-overflow-tooltip="{effect: 'light'}" />
                            </template>
                        </el-table>
                        <!-- 分页 -->
                        <el-pagination :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
                            :current-page.sync="callData.pageNum" :page-size.sync="callData.pageSize"
                            :total="callData.dataTotal" @size-change="handleSizeChange"
                            @current-change="handleCurrentChange" />
                    </div>
                    <!-- 返回数据为 HTML 格式 -->
                    <div v-if="apiExecuting2" class="html-container">
                        <VAceEditor v-if="jsonFlag" ref="aceRef" v-model:value="htmlData" class="editor-content"
                            placeholder="" :options="aceOptions" lang="json" readonly theme="github" />
                        <iframe class="html-iframe" v-if="!jsonFlag" :srcdoc="htmlData"></iframe>
                    </div>
                    <!-- 没有返回数据时显示提示 -->
                    <div v-else>{{ t('common.message.noData') }}</div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
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

const { t } = useI18n();
const hasChildren = (row) => {
    return Array.isArray(row.daAssetApiParamList) && row.daAssetApiParamList.length > 0;
};
const aceOptions = ref({
    printMarginColumn: 30,
    displayIndentGuides: false,
    useWorker: true,
    showPrintMargin: false,
    useSoftTabs: true, // 使用空格替代 Tab
    highlightActiveLine: true, // 高亮当前行
    enableMultiselect: true,
    readOnly: true,
    wrap: true,
    showLineNumbers: false,
    showGutter: false,
});
// 定义响应式数据
const title = ref('数据API调用');  // 标题
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


const rules = {
    name: [{ required: true, message: "请输入参数名称", trigger: "blur" }],
    columnType: [{ required: true, message: "请选择参数类型", trigger: "change" }],
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
                fieldDescribes: `通过分配的账号密码作为参数调用${request.defaults.baseURL}/system/auth/getToken接口得到accessToken`,
            });
        }
    });
};
const baseUrl = import.meta.env.VITE_APP_BASE_API;
// 获取数据API配置
const getDataApi = async () => {
    getYApiConfig(props.data?.daAssetApi.id);
};
// console.log("🚀 ~ getDataApi ~ props.data ?.daAssetApi.id:", props.data ?.daAssetApi.id)

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
        title: t('common.message.prompt'),
        message: '接口调用成功',
        type: 'success',
        duration: 2000,
    });
};

const showErrorNotify = (msg) => {
    ElNotification({
        title: t('common.message.prompt'),
        message: msg || '接口调用失败',
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
        if (typeof obj === 'string' && obj.trim().startsWith('<?xml')) {
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(obj, 'application/xml');

            if (xmlDoc.documentElement.nodeName === 'Capabilities') {
                return true;
            }
        }

        // 判断是否为标准 JSON 对象
        return Object.prototype.toString.call(obj) === '[object Object]';
    };

    // 参数校验
    const isNull = props.data.reqParams.some(param => {
        if (isParamInvalid(param)) {
            proxy.$message.warning(`参数校验未通过，输入参数‘${param.name}’不能为空`);
            return true;
        }
        return false;
    });
    if (isNull) return;
    // 构造请求体
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

    // 请求接口
    queryServiceForwarding(data).then((response) => {
        if (response.code == 200) {
            // proxy.$message.msgSuccess('接口调用成功');
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
