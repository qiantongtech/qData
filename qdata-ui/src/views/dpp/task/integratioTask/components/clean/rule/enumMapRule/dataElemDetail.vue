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
    <el-dialog title="详请" v-model="visible" width="800px" draggable>
        <el-table stripe height="65vh" v-loading="loading" :data="dpDataElemCodeList" :default-sort="defaultSort">
            <el-table-column label="编号" align="left" prop="id" width="80" />
            <el-table-column label="代码值" align="left" prop="codeValue" width="160">
                <template #default="scope">
                    {{ scope.row.codeValue || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="代码名称" align="left" prop="codeName" width="350">
                <template #default="scope">
                    {{ scope.row.codeName || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="创建人" align="left" prop="createBy" width="160">
                <template #default="scope">
                    {{ scope.row.createBy || '-' }}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="left" prop="createTime" width="220">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                </template>
            </el-table-column>
            <el-table-column label="备注" align="left" prop="remark" width="360"
                :show-overflow-tooltip="{ effect: 'light' }">
                <template #default="scope">
                    {{ scope.row.remark || '-' }}
                </template>
            </el-table-column>
            <template #empty>
                <div class="emptyBg">
                    <img src="../../../../../../../../assets/system/images/no_data/noData.png" alt="" />
                    <p>无数据</p>
                </div>
            </template>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="handleClose">关 闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="ComponentOne">
import {
    listDpDataElemCode,
    validateCodeValue
} from '@/api/dp/dataElem/dataElem.js';
const route = useRoute();
const { proxy } = getCurrentInstance();

const dpDataElemCodeList = ref([]);

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const total = ref(0);
const defaultSort = ref({ prop: 'createTime', order: 'desc' });

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        codeValue: null,
        codeName: null,
        createTime: null
    },

});

let id = route.query.id;

const { queryParams, form } = toRefs(data);



/** 查询数据元代码列表 */
function getList(id) {
    if (id == -1) {
        return;
    }
    loading.value = true;
    queryParams.value.dataElemId = id;
    listDpDataElemCode(queryParams.value).then((response) => {
        dpDataElemCodeList.value = response.data.rows;
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
        id: null,
        dataElemId: null,
        codeValue: null,
        codeName: null,
        validFlag: null,
        delFlag: null,
        createBy: null,
        creatorId: null,
        createTime: null,
        updateBy: null,
        updaterId: null,
        updateTime: null,
        remark: null
    };
    proxy.resetForm('dpDataElemCodeRef');
}

const visible = ref(false);

function openDialog(row) {
    visible.value = true;
    getList(row.id);

}
function handleClose() {
    visible.value = false;
    reset();
}
defineExpose({ openDialog });
</script>
