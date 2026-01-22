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
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <el-form ref="form2" :model="form2" label-width="100px" label="字段列表：">
        <template v-if="form2.apiServiceType != 3">
            <div class="clearfix header-text">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    请求参数
                </div>
            </div>

            <el-table :data="form2.reqParams" max-height="250" stripe>
                <el-table-column label="序号" width="80" align="center">
                    <template #default="scope">
                        <span>{{ scope.$index + 1 }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="paramName" label="参数名称" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }" />
                <el-table-column prop="nullable" label="是否允许为空" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        <el-checkbox disabled v-model="scope.row.nullable" true-label="1" false-label="0" />
                    </template>
                </el-table-column>
                <el-table-column prop="paramComment" label="描述" align="center">
                    <template #default="scope">
                        {{ scope.row.paramComment || '-' }}
                    </template>
                </el-table-column>
                <el-table-column prop="paramType" label="参数类型" align="center">
                    <template #default="scope">
                        <dict-tag :options="ds_api_param_type" :value="scope.row.paramType" />
                    </template>
                </el-table-column>
                <el-table-column
                  prop="whereType"
                  label="操作符"
                  align="center"
                  v-if="splReult !== true"
                >
                  <template #default="scope">
                    <dict-tag :options="da_api_param_operator" :value="scope.row.whereType" />
                  </template>
                </el-table-column>
                <el-table-column prop="exampleValue" label="示例值" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.exampleValue || '-' }}
                    </template>
                </el-table-column>
                <el-table-column prop="defaultValue" label="默认值" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.defaultValue || '-' }}
                    </template>
                </el-table-column>
            </el-table>
            <div class="clearfix header-text">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    返回字段
                </div>
            </div>
            <el-table :data="form2.resParams" stripe>
                <el-table-column label="序号" width="80" align="center">
                    <template #default="scope">
                        <span>{{ scope.$index + 1 }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="fieldName" label="中文名称" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }" />
                <el-table-column prop="fieldComment" label="描述" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.fieldComment || '-' }}
                    </template>
                </el-table-column>
                <el-table-column prop="dataType" label="数据类型" align="center" :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.dataType || '-' }}
                    </template>
                </el-table-column>
                <el-table-column prop="exampleValue" label="示例值" align="center"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.exampleValue || '-' }}
                    </template>
                </el-table-column>
            </el-table>
        </template>
        <template v-if="form2.apiServiceType == 3">
            <div class="clearfix header-text">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    请求数据
                </div>
            </div>
            <el-form :model="form2.reqParams" :rules="rules" ref="inputForm" label-width="0">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="form2.reqParams" class="tableStyle" row-key="id" stripe default-expand-all
                            :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
                            <el-table-column label="序号" width="80" align="center" fixed="left">
                                <template #default="{ $index }">
                                    {{ $index + 1 }}
                                </template>
                            </el-table-column>

                            <el-table-column label="参数名称" fixed="left" align="center" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.name || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="描述" fixed="left" align="center" prop="remark"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.remark || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="是否为空" width="100" fixed="left" align="center" prop="requestFlag"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    <el-form-item>
                                        <el-checkbox v-model="row.requestFlag" disabled :true-label="'1'"
                                            :false-label="'0'"> </el-checkbox>
                                    </el-form-item>
                                </template>
                            </el-table-column>

                            <el-table-column label="参数类型" fixed="left" align="center" prop="columnType"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.columnType || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="示例值" fixed="left" align="center" prop="exampleValue"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    <!-- <el-form-item
                                        :prop="`form2.reqParams[${findPosi(form2.reqParams, row.id)}].exampleValue`"
                                        :rules="hasChildren(row) ? rules.fieldDefault : []">
                                        <el-input v-model="row.fieldDefault" placeholder="请输入示例值"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item> -->
                                    {{ row.reqParams }}
                                </template>
                            </el-table-column>

                            <el-table-column label="默认值" fixed="left" align="center" prop="defaultValue"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    <!-- <el-form-item
                                        :prop="`form2.reqParams[${findPosi(form2.reqParams, row.id)}].defaultValue`"
                                        :rules="hasChildren(row) ? rules.defaultValue : []">
                                        <el-input v-model="row.defaultValue" placeholder="请输入默认值"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item> -->
                                    {{ row.defaultValue }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
            </el-form>
            <div class="clearfix header-text">
                <div class="header-left">
                    <div class="blue-bar"></div>
                    返回参数
                </div>
            </div>
            <el-form :model="form2.resParams" :rules="rules" ref="inputForm" label-width="0">
                <el-row>
                    <el-col :span="24">
                        <el-table :data="form2.resParams" class="tableStyle" row-key="id" stripe default-expand-all
                            :tree-props="{ children: 'daAssetApiParamList', hasChildren: 'hasChildren' }">
                            <el-table-column label="序号" width="80" align="center" fixed="left">
                                <template #default="{ $index }">
                                    {{ $index + 1 }}
                                </template>
                            </el-table-column>

                            <el-table-column label="参数名称" fixed="left" align="center" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.name || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="描述" fixed="left" align="center" prop="remark"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.remark || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="参数类型" fixed="left" align="center" prop="columnType"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    {{ row?.columnType || '' }}
                                </template>
                            </el-table-column>

                            <el-table-column label="示例值" fixed="left" align="center" prop="exampleValue"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="{ row }">
                                    <!-- <el-form-item
                                        :prop="`form2.reqParams[${findPosi(form2.reqParams, row.id)}].exampleValue`"
                                        :rules="hasChildren(row) ? rules.fieldDefault : []">
                                        <el-input v-model="row.fieldDefault" placeholder="请输入示例值"
                                            :disabled="hasChildren(row)" />
                                    </el-form-item> -->
                                    {{ row.reqParams }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
            </el-form>
        </template>
    </el-form>
</template>

<script setup name="ComponentOne">
import { listDsApi, getDsApi, delDsApi, addDsApi, updateDsApi } from '@/api/ds/api/api.js';

const { proxy } = getCurrentInstance();
const {
    ds_api_log_status,
    ds_api_bas_info_api_service_type,
    ds_api_bas_info_api_method_type,
    ds_api_bas_info_res_data_type,
    da_api_param_operator,
    ds_api_param_type,
} = proxy.useDict(
    'ds_api_log_status',
    'ds_api_bas_info_api_service_type',
    'ds_api_bas_info_api_method_type',
    'ds_api_bas_info_res_data_type',
    "da_api_param_operator",
    "ds_api_param_type",
);

const dsApiList = ref([]);
const props = defineProps({
    form2: {
        type: Object,
        default: {}
    }
});

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const daterangeCreateTime = ref([]);
const defaultSort = ref({ prop: 'createTime', order: 'desc' });

const data = reactive({
    dsApiDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        NAME: null,
        STATUS: null,
        createTime: null
    },
    rules: {}
});

const { queryParams, form, dsApiDetail, rules } = toRefs(data);

/** 查询API服务列表 */
function getList() {
    loading.value = true;
    queryParams.value.params = {};
    if (null != daterangeCreateTime && '' != daterangeCreateTime) {
        queryParams.value.params['beginCreateTime'] = daterangeCreateTime.value[0];
        queryParams.value.params['endCreateTime'] = daterangeCreateTime.value[1];
    }
    listDsApi(queryParams.value).then((response) => {
        dsApiList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}

// 取消按钮
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

// 表单重置
function reset() {
    form.value = {
        ID: null,
        NAME: null,
        apiVersion: null,
        apiUrl: null,
        reqMethod: null,
        apiServiceType: null,
        resDataType: null,
        denyIp: null,
        configJson: null,
        limitJson: null,
        reqParams: null,
        resParams: null,
        DESCRIPTION: null,
        STATUS: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        REMARK: null
    };
    proxy.resetForm('dsApiRef');
}

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    daterangeCreateTime.value = [];
    proxy.resetForm('queryRef');
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map((item) => item.ID);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = '新增API服务';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _ID = row.ID || ids.value;
    getDsApi(_ID).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改API服务';
    });
}

/** 详情按钮操作 */
function handleDetail(row) {
    reset();
    const _ID = row.ID || ids.value;
    getDsApi(_ID).then((response) => {
        form.value = response.data;
        openDetail.value = true;
        title.value = 'API服务详情';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['dsApiRef'].validate((valid) => {
        if (valid) {
            if (form.value.ID != null) {
                updateDsApi(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            } else {
                addDsApi(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('新增成功');
                        open.value = false;
                        getList();
                    })
                    .catch((error) => { });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _IDs = row.ID || ids.value;
    proxy.$modal
        .confirm('是否确认删除API服务编号为"' + _IDs + '"的数据项？')
        .then(function () {
            return delDsApi(_IDs);
        })
        .then(() => {
            getList();
            proxy.$modal.msgSuccess('删除成功');
        })
        .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
    proxy.download(
        'ds/dsApi/export',
        {
            ...queryParams.value
        },
        `dsApi_${new Date().getTime()}.xlsx`
    );
}

getList();
</script>

<style scoped lang="scss">
.blue-bar {
    background-color: #2666fb;
    width: 5px;
    height: 20px;
    margin-right: 10px;
    border-radius: 2px;
}

.header-text {
    margin: 12px 0
}

.header-left {
    display: flex;
    align-items: center;
    font-size: 16px;
    line-height: 24px;
    font-style: normal;
}
</style>
