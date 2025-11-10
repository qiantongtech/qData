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
    </div>

    <!-- 表格部分 -->
    <el-table stripe height="300px" v-loading="loading" :data="dpCodeMapList">
        <el-table-column label="原始值" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="originalValue">
            <template #default="scope">
                {{ scope.row.originalValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="字典名" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="dictName">
            <template #default="scope">
                {{ scope.row.dictName || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="字典值" :show-overflow-tooltip="{ effect: 'light' }" align="left" prop="dictValue">
            <template #default="scope">
                {{ scope.row.dictValue || '-' }}
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
            <template #default="scope">
                <!-- 修改时传递行索引，用于后续的 local 编辑 -->
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row, scope.$index)">修改</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDelete(scope.$index)">删除</el-button>
            </template>
        </el-table-column>
        <template #empty>
            <div class="emptyBg">
                <p>无数据</p>
            </div>
        </template>
    </el-table>

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" v-model="open" :append-to="$refs['app-container']" draggable destroy-on-close>
        <el-form ref="dpCodeMapRef" :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="原始值" prop="originalValue">
                        <el-input v-model="form.originalValue" placeholder="请输入原始值" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="字典名" prop="dictName">
                        <el-input v-model="form.dictName" placeholder="请输入字典名" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="字典值" prop="dictValue">
                        <el-input v-model="form.dictValue" placeholder="代码值" />
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

<script setup>
const { proxy } = getCurrentInstance();
const props = defineProps({
    row: { type: Object, default: () => ({}) },
});
function cancel() {
    open.value = false;
    openDetail.value = false;
    reset();
}

const dpCodeMapList = ref(props.row || []);

watch(
    () => props.row,
    (newValue) => {
        dpCodeMapList.value = newValue || []; // 如果新值是 undefined 或 null，则赋为空数组
    },
    { deep: true }
);


// 其他状态变量
const loading = ref(false);
const total = ref(dpCodeMapList.value.length);
const open = ref(false);
const title = ref('');
// 表单和验证规则
const data = reactive({
    oldOriginalValue: null,
    form: {
        originalValue: null,
        dictName: null,
        dictValue: null,
    },
    rules: {
        originalValue: [{ required: true, message: '原始值不能为空', trigger: 'change' }],
        dictName: [{ required: true, message: '代码名不能为空', trigger: 'change' }],
        dictValue: [{ required: true, message: '字典值不能为空', trigger: 'change' }]
    }
});

const { oldOriginalValue, form, rules } = toRefs(data);
const emit = defineEmits(["dpCodeMapList",]);
/** 表单重置 */
function reset() {
    form.value = { index: null, id: null, originalValue: null, dictName: null, dictValue: null };
    oldOriginalValue.value = null;
    // proxy.resetForm('dpCodeMapRef');
}

/** 新增按钮操作 */
function handleAdd() {
    reset();
    open.value = true;
    title.value = '新增';
}

/** 修改按钮操作 */
function handleUpdate(row, index) {
    reset();
    form.value = { ...row, index };
    oldOriginalValue.value = row.originalValue;
    open.value = true;
    title.value = '修改';
}

/** 删除按钮操作 */
function handleDelete(index) {
    proxy.$modal.confirm('是否确认删除该数据项？')
        .then(() => {
            dpCodeMapList.value.splice(index, 1);
            total.value = dpCodeMapList.value.length;
            proxy.$modal.msgSuccess('删除成功');
            emit('dpCodeMapList', dpCodeMapList.value);
        })
        .catch(() => { });
}

/** 提交按钮：新增或修改 */
function submitForm() {
    proxy.$refs['dpCodeMapRef'].validate((valid) => {
        if (valid) {
            // 检查 originalValue 是否已经存在
            const isDuplicate = dpCodeMapList.value.some(item => item.originalValue === form.value.originalValue);
            if (!(oldOriginalValue.value !== null && oldOriginalValue.value === form.value.originalValue) && isDuplicate) {
                proxy.$modal.msgWarning('原始值已存在，不能新增');
                return; // 阻止继续执行
            }

            // 如果是修改操作
            if (form.value.index !== null && form.value.index !== undefined) {
                dpCodeMapList.value.splice(form.value.index, 1, { ...form.value });
                proxy.$modal.msgSuccess('修改成功');
            } else {
                dpCodeMapList.value.push({ ...form.value });
                proxy.$modal.msgSuccess('新增成功');
            }
            emit('dpCodeMapList', dpCodeMapList.value);

            open.value = false;
            total.value = dpCodeMapList.value.length;
        }
    });
}
</script>
