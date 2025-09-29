<template>
    <div class="app-container">
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
        <div class="body-wrapper">
            <el-form v-if="isChange" ref="form" :model="form" label-width="100px" :disabled="true">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="API名称">
                            <el-input v-model="form.name" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="API版本">
                            <el-input v-model="form.apiVersion" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="请求类型">
                            <!--                            <el-input v-model="form.reqMethod"/>-->
                            <dict-tag :options="ds_api_bas_info_api_method_type" :value="form.reqMethod" />
                        </el-form-item>
                        <!--                        <el-table-column v-if="getColumnVisibility(4)" label="请求方式" align="center" prop="reqMethod">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <dict-tag :options="ds_api_bas_info_api_method_type" :value="scope.row.reqMethod"/>-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="返回格式" prop="resDataType">
                            <dict-tag :options="ds_api_bas_info_res_data_type" :value="form.resDataType" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="调用地址">
                            <el-input v-model="form.apiUrl" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    请求数据
                </div>
            </div>
            <el-row>
                <el-col :span="24">
                    <el-table class="tableStyle" :data="form.reqParams" stripe :max-height="250"
                        style="width: 100%; margin: 15px 0">
                        <el-table-column label="序号" width="80" align="center">
                            <template #default="scope">
                                <span>{{ scope.$index + 1 }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="paramName" label="参数名称" align="center" :show-overflow-tooltip="{effect: 'light'}" />
                        <el-table-column prop="nullable" label="是否允许为空" align="center" :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                <el-checkbox v-model="scope.row.nullable" true-label="1" false-label="0" disabled />
                            </template>
                        </el-table-column>
                        <el-table-column prop="paramComment" label="描述" align="center" :show-overflow-tooltip="{effect: 'light'}" />
                        <el-table-column prop="paramType" label="参数类型" align="center" :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                <dict-tag :options="ds_api_param_type" :value="scope.row.paramType" />
                                <!--                                <el-select v-model="scope.row.paramType" placeholder="请选择参数类型" disabled>-->
                                <!--                                    <el-option v-for="dict in paramTypeOptions" :key="dict.id" :label="dict.itemValue"-->
                                <!--                                               :value="dict.itemText"/>-->
                                <!--                                </el-select>-->
                            </template>
                        </el-table-column>
                        <!--                        <el-table-column prop="whereType" label="操作符" align="center" :show-overflow-tooltip="{effect: 'light'}">-->
                        <!--                            <template #default="scope">-->
                        <!--                                <el-select v-model="scope.row.whereType" placeholder="请选择操作符" disabled>-->
                        <!--                                    <el-option v-for="dict in whereTypeOptions" :key="dict.id" :label="dict.itemValue"-->
                        <!--                                               :value="dict.itemText"/>-->
                        <!--                                </el-select>-->
                        <!--                            </template>-->
                        <!--                        </el-table-column>-->
                        <el-table-column prop="paramValue" label="参数值" align="center" :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                <el-input v-if="scope.row.paramType != '2'" v-model="scope.row.paramValue"
                                    placeholder="请输入参数值" />
                                <el-input v-else-if="scope.row.paramType === '2'" v-model="scope.row.paramValue"
                                    placeholder="请输入参数值" type="number" />
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
            <div class="header-container">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    返回数据
                </div>
            </div>
            <el-row>
                <el-col :span="24">
                    <div v-if="apiExecuting">
                        <el-table :data="callData.dataList" stripe border :max-height="250"
                            style="width: 100%; margin: 15px 0">
                            <el-table-column label="序号" width="80" align="center">
                                <template #default="scope">
                                    <span>{{ scope.$index + 1 }}</span>
                                </template>
                            </el-table-column>
                            <template v-for="(column, index) in callData.columnList" :key="index">
                                <el-table-column :prop="column" :label="column" align="center" :min-width="180"
                                    :show-overflow-tooltip="{effect: 'light'}" />
                            </template>
                        </el-table>
                        <div style="display: flex; justify-content: flex-end; margin-top: 20px;"
                            v-if="callData.dataTotal">
                            <el-pagination v-if="form.resDataType == '1' || form.resDataType == '3'"
                                :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper"
                                v-model:current-page="callData.pageNum" v-model:page-size="callData.pageSize"
                                :total="callData.dataTotal" @size-change="handleSizeChange"
                                @current-change="handleCurrentChange" />
                        </div>

                    </div>
                    <div v-else class="no-data">暂无数据</div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>
import { serviceTesting } from '@/api/ds/api/api.js';
const { proxy } = getCurrentInstance();
const { ds_api_bas_info_api_method_type, ds_api_param_type, ds_api_bas_info_res_data_type } =
    proxy.useDict(
        'ds_api_bas_info_api_method_type',
        'ds_api_param_type',
        'ds_api_bas_info_res_data_type'
    );

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
const data = reactive({
    title: '数据API调用',
    // 展示切换
    showOptions: {
        data: {},
        showList: true,
        showAdd: false,
        showEdit: false,
        showDetail: false,
        showExample: false
    },
    activeTabName: 'table0',
    apiHeader: {},
    apiHeaderList: [],
    // 操作符数据字典
    whereTypeOptions: [],
    // 参数类型数据字典
    paramTypeOptions: [],
    apiExecuting: false,
    callData: {
        dataList: [],
        columnList: [],
        pageNum: 1,
        pageSize: 20,
        dataTotal: 0
    },
    bashUrl: null
});

const {
    apiHeader,
    apiHeaderList,
    whereTypeOptions,
    activeTabName,
    showOptions,
    title,
    paramTypeOptions,
    apiExecuting,
    callData,
    bashUrl
} = toRefs(data);

function showCard() {
    this.$emit('showCard', this.showOptions);
}

function handleSizeChange(val) {
    callData.pageNum = 1;
    callData.pageSize = val;
    handleCall();
}

function handleCurrentChange(val) {
    callData.pageNum = val;
    handleCall();
}

function handleCall() {
    let isNull = false;
    props.form.reqParams.forEach((param) => {
        if (
            param.nullable == '0' &&
            (param.paramValue == null ||
                param.paramValue == undefined ||
                param.paramValue == '')
        ) {
            proxy.$message.warning('输入参数‘' + param.paramName + '’不能为空');
            isNull = true;
            return;
        }
    });
    if (isNull) {
        return;
    }
    const data = {};
    data.pageNum = callData.pageNum;
    data.pageSize = callData.pageSize;
    props.form.reqParams.forEach((param) => {
        if (param.paramType == 2) {
            if (
                param.paramValue != null &&
                param.paramValue != '' &&
                param.paramValue != undefined
            ) {
                param.paramValue = parseInt(param.paramValue);
            }
        }
        data[param.paramName] = param.paramValue;
    });
    props.form.params = data;
    let params = {};
    //将props.form的值全部给params
    Object.assign(params, props.form);
    params.reqParamsList = params.reqParams;
    params.resParamsList = params.resParams;
    //删除reqParams和resParams
    delete params.reqParams;
    delete params.resParams;
    delete params.createTime;
    delete params.updateTime;
    // 根据请求方法 (GET / POST) 进行处理
    if (props.form.reqMethod === '1') {
        // 使用 serviceTesting 来模拟 GET 请求
        serviceTesting(params).then((response) => {
            if (response.code === 200) {
                proxy.$message.success('接口调用成功');
                const { data } = response;
                const dataList = [];

                // 根据 resDataType 处理返回的数据
                if (props.form.resDataType == 3) {
                    dataList.push(...data.data);
                } else if (props.form.resDataType == '2') {
                    dataList.push(...data);
                } else {
                    dataList.push(data);
                }

                // 获取列名
                let columnList = [];
                if (dataList.length > 0) {
                    columnList = Object.keys(dataList[0]);
                }

                // 更新数据和列名
                callData.value.dataList = dataList;
                callData.value.columnList = columnList;

                // 如果 resDataType 为 '1'，更新 total
                if (props.form.resDataType == '1' || props.form.resDataType == '3') {
                    callData.value.dataTotal = data.total;
                }
                // 更新 API 执行状态
                apiExecuting.value = true;
            } else {
                // 请求失败的处理
                proxy.$message.warning("操作失败，请联系管理员");
            }
        });
    } else if (props.form.reqMethod === '2') {
        serviceTesting(params).then((response) => {
            if (response.code === 200) {
                proxy.$message.success('接口调用成功');

                const { data } = response;
                const dataList = [];

                // 根据 resDataType 处理返回的数据
                if (props.form.resDataType == 3) {
                    dataList.push(...data.data);
                } else if (props.form.resDataType === '2') {
                    dataList.push(...data);
                } else {
                    dataList.push(data);
                }

                // 获取列名
                let columnList = [];
                if (dataList.length > 0) {
                    columnList = Object.keys(dataList[0]);
                }

                // 更新数据和列名
                callData.value.dataList = dataList;
                callData.value.columnList = columnList;

                // 如果 resDataType 为 '1'，更新 total
                if (props.form.resDataType === '1' || props.form.resDataType == '3') {
                    callData.value.dataTotal = data.total;
                }
                // 更新 API 执行状态
                apiExecuting.value = true;
            } else {
                proxy.$message.warning("操作失败，请联系管理员");
            }
        });
    }
}
</script>

<style lang="scss" scoped>
.app-container {
    margin-top: -5px;
    min-height: 65vh;
    margin-left: 0px;
    // padding: 20px;
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
