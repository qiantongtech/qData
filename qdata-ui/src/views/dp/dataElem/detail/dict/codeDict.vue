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
  For brand customization, please contact the official team for authorization.
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
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
    <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                </el-button>
            </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </div>
    </div>
    <el-table stripe height="360" v-loading="loading" :data="dpDataElemCodeList"
        @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
        <el-table-column label="编号" align="left" prop="id" width="50" />
        <el-table-column label="代码值" align="left" prop="codeValue" width="160">
            <template #default="scope">
                {{ scope.row.codeValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="代码名称" align="left" prop="codeName" width="220">
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
        <el-table-column label="备注" align="left" prop="remark" :show-overflow-tooltip="{ effect: 'light' }">
            <template #default="scope">
                {{ scope.row.remark || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="300">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
            </div>
        </template>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 新增或修改数据元代码对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
        <el-form ref="dpDataElemCodeRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="代码值" prop="codeValue">
                        <el-input v-model="form.codeValue" placeholder="请输入代码值" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="代码名称" :show-overflow-tooltip="{ effect: 'light' }" prop="codeName">
                        <el-input v-model="form.codeName" placeholder="请输入代码名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注" :show-overflow-tooltip="{ effect: 'light' }" prop="remark">
                        <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button size="mini" @click="cancel">取 消</el-button>
                <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="ComponentOne">
import {
    listDpDataElemCode,
    getDpDataElemCode,
    delDpDataElemCode,
    addDpDataElemCode,
    updateDpDataElemCode,
    validateCodeValue
} from '@/api/dp/dataElem/dataElem';
const route = useRoute();
const { proxy } = getCurrentInstance();

const dpDataElemCodeList = ref([]);

const open = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const defaultSort = ref({ prop: 'createTime', order: 'desc' });

const data = reactive({
    dpDataElemCodeDetail: {},
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        dataElemId: null,
        codeValue: null,
        codeName: null,
        createTime: null
    },
    rules: {
        codeValue: [
            { required: true, message: '代码值不能为空', trigger: 'blur' },
            { validator: validatorCodeValue, trigger: 'blur' }
        ],
        codeName: [{ required: true, message: '代码名称不能为空', trigger: 'blur' }]
    }
});

let id = route.query.id;

const { queryParams, form, dpDataElemCodeDetail, rules } = toRefs(data);
// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
        id = newId || -1; // 如果 id 为空，使用默认值 1
        getList();
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
function validatorCodeValue(rule, value, callback) {
    if (value !== null && value !== undefined) {
        //调用接口判断是否存在重复的值
        var params = {
            id: form.value.id || null,
            dataElemId: id,
            codeValue: value
        };
        validateCodeValue(params).then((res) => {
            if (res.data == 0) {
                callback(new Error('代码值已存在'));
            } else {
                callback();
            }
        });
    } else {
        callback();
    }
}

/** 查询数据元代码列表 */
function getList() {
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

/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}

/** 重置按钮操作 */
function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
    ids.value = selection.map((item) => item.id);
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
    title.value = '新增数据元代码';
}

/** 修改按钮操作 */
function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value;
    getDpDataElemCode(_id).then((response) => {
        form.value = response.data;
        open.value = true;
        title.value = '修改数据元代码';
    });
}

/** 提交按钮 */
function submitForm() {
    proxy.$refs['dpDataElemCodeRef'].validate((valid) => {
        console.log(dpDataElemCodeDetail.value);
        form.value.dataElemId = id;
        if (valid) {
            if (form.value.id != null) {
                updateDpDataElemCode(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('修改成功');
                        open.value = false;
                        getList();
                        //事件推送
                        proxy.$bus.emit('data_elem_code_change');
                    })
                    .catch((error) => { });
            } else {
                addDpDataElemCode(form.value)
                    .then((response) => {
                        proxy.$modal.msgSuccess('新增成功');
                        open.value = false;
                        getList();
                        //事件推送
                        proxy.$bus.emit('data_elem_code_change');
                    })
                    .catch((error) => { });
            }
        }
    });
}

/** 删除按钮操作 */
function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal
        .confirm('是否确认删除数据元代码编号为"' + _ids + '"的数据项？')
        .then(function () {
            return delDpDataElemCode(_ids);
        })
        .then(() => {
            getList();
            //事件推送
            proxy.$bus.emit('data_elem_code_change');
            proxy.$modal.msgSuccess('删除成功');
        })
        .catch(() => { });
}

// getList();
</script>
