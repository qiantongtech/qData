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
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" content="用于配置字段的排序规则，包括字段顺序、排序方式、是否区分大小写等选项" placement="top">
                    <el-icon class="tip-icon">
                        <InfoFilled />
                    </el-icon>
                </el-tooltip>
            </div>
        </template>
        <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent v-loading="loading"
            :disabled="info">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="节点名称" prop="name"
                        :rules="[{ required: true, message: '请输入节点名称', trigger: 'change' }]">
                        <el-input v-model="form.name" placeholder="请输入节点名称" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="类型" prop="typeName">
                        <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
                            <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="t('common.texts.description')" prop="description">
                        <el-input v-model="form.description" type="textarea" :placeholder="t('common.form.descriptionPlaceholder')" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-divider content-position="center">
                <span class="blue-text">字段</span>
            </el-divider>
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ t('common.button.add') }}
                        </el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table stripe height="310px" :data="tableFields" v-loading="loadingList" ref="dragTable"
                row-key="columnName">
                <el-table-column label="序号" width="80" align="left">
                    <template #header>
                        <div class="justify-center">
                            <span>序号</span>
                            <el-tooltip effect="light" content="序号越小，字段排序优先级越高" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="{ $index }">
                        <div class="allowDrag"
                            style="cursor: move; display: flex; justify-content: center; align-items: center;">
                            <el-icon>
                                <Operation />
                            </el-icon>
                            <span style="margin-left: 4px;">{{ $index + 1 }}</span>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="字段名称" align="left" prop="columnName">
                    <template #default="scope">
                        <el-select v-model="scope.row.columnName" placeholder="请选择字段" style="flex: 1">
                            <el-option v-for="item in inputFields" :key="item.value" :label="item.label"
                                :value="item.columnName" :disabled="isOptionDisabled(item.columnName, scope.row)" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="排序规则" align="left" prop="order">
                    <template #default="scope">
                        <el-select v-model="scope.row.order" placeholder="请选择">
                            <el-option label="降序" value="desc" />
                            <el-option label="升序" value="asc" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column :label="t('common.texts.operation')" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="150">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">
                            {{ t('common.button.delete') }}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

        </el-form>

        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">{{ t('common.button.close') }}</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">{{ t('common.button.save') }}</el-button>
                <el-button type="warning" @click="handleFetchFields" v-if="!info">获取字段</el-button>
            </div>
        </template>
    </el-dialog>

    <FieldConflictDialog v-model="showConflictDialog" :existingFields="tableFields" :newFields="inputFields"
        @resolve="onResolveFields" />
    <CreateEditModal :visibleDialogs="opens" @update:visibleDialogs="opens = $event" @confirm="submitForm" :row="row"
        :tableFields="tableFields" :inputFields="inputFields" />
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import CreateEditModal from "../fieldMergeModal.vue";
import FieldConflictDialog from "../fieldDetection.vue";
import { defineProps, defineEmits, ref, computed, watchEffect, getCurrentInstance } from "vue";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import { createNodeSelect, getParentNode } from "@/views/dpp/utils/opBase.js";
import draggable from "vuedraggable";
import Sortable from "sortablejs";

const { t } = useI18n();
const { proxy } = getCurrentInstance();
const userStore = useUserStore();

const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: "表单标题" },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    graph: { type: Object, default: () => ({}) },
});

let dragTable = ref(null);
let sortableInstance = null;
function setSort() {
    nextTick(() => {
        const tbody = dragTable.value?.$el.querySelector(
            ".el-table__body-wrapper tbody"
        );
        if (!tbody) {
            console.warn("tbody 找不到，拖拽初始化失败");
            return;
        }

        if (sortableInstance) {
            sortableInstance.destroy();
        }

        sortableInstance = Sortable.create(tbody, {
            handle: ".allowDrag",
            animation: 150,
            onEnd: (evt) => {

                const movedItem = tableFields.value.splice(evt.oldIndex, 1)[0];
                tableFields.value.splice(evt.newIndex, 0, movedItem);
                console.log("拖拽后顺序:", tableFields.value.map((f) => f.columnName));
            },
        });
    });
}

function handleAddField() {
    if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
        proxy.$message.warning("输入字段为空，无法添加字段");
        return;
    }
    // 已添加的字段名
    const usedNames = tableFields.value.map((item) => item.columnName);

    // 找到未使用的字段
    const nextField = inputFields.value.find(
        (item) => !usedNames.includes(item.columnName)
    );

    if (!nextField) {
        proxy.$message.warning("新增失败，已无可添加的字段");
        return;
    }

    tableFields.value.push({
        columnName: nextField.columnName,
        order: 'asc',
    });
    setSort()

}
const showConflictDialog = ref(false);

const handleFetchFields = () => {
    const tableNames = tableFields.value.map(f => f.columnName).sort();
    const inputNames = inputFields.value.map(f => f.columnName).sort();

    if (
        tableNames.length === inputNames.length &&
        tableNames.every((name, idx) => name === inputNames[idx])
    ) {
        return proxy.$message.warning("新增失败，当前已是最新字段");
    }
    showConflictDialog.value = true;
};
function onResolveFields(payload) {
    if (!payload || !payload.action) return;

    switch (payload.action) {
        case "addNewOnly": {
            console.log("父组件：只增加新字段");

            // 计算已有字段名称
            const existingNames = tableFields.value.map(f => f.columnName);
            // 找到新字段中不在已有字段中的字段
            const newUniqueFields = inputFields.value.filter(
                f => !existingNames.includes(f.columnName)
            );
            // 加入到 tableFields 中
            tableFields.value = tableFields.value.concat(deepCopy(newUniqueFields));
            break;
        }

        case "addAll": {
            console.log("🚀 ~ onResolveFields ~  tableFields.value =:", tableFields.value)

            console.log("父组件：增加所有字段");
            tableFields.value = []
            // 这里先清空，再加全部字段，避免重复
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "clearAndAddAll": {
            console.log("父组件：清空并增加所有字段");

            // 恢复原始备份字段
            tableFields.value = deepCopy(inputFields.value);

            break;
        }

        case "cancel": {
            console.log("父组件：取消操作");
            break;
        }

    }
}

const isOptionDisabled = (optionValue, currentRow) => {
    return tableFields.value.some(
        (row) => row !== currentRow && row.columnName === optionValue
    );
};

const emit = defineEmits(["update", "confirm"]);

const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit("update", newValue);
    },
});

let tableFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});

function handleRule(data) {
    row.value = { ...data };
    opens.value = true;
}

function handleDelete(row) {
    // 1. 从 tableFields 中删除对应项
    const idxTable = tableFields.value.findIndex(
        (item) => item.columnName === row.columnName
    );
    if (idxTable !== -1) {
        tableFields.value.splice(idxTable, 1);
    }
    const originalField = originalTableFieldsBackup.value.find(
        (item) => item.columnName === row.columnName
    );
    if (originalField) {
        const idxField = inputFields.value.findIndex(
            (item) => item.columnName === row.columnName
        );
        if (idxField !== -1) {
            inputFields.value[idxField] = JSON.parse(JSON.stringify(originalField));
        } else {
            inputFields.value.push(JSON.parse(JSON.stringify(originalField)));
        }
    }
    setSort()
}

// 提交弹窗规则数据
const submitForm = (value) => {
    if (!value || !Array.isArray(value)) return;

    value.forEach((ruleItem) => {
        if (!ruleItem?.ruleConfig) return;

        let parsedConfig;
        try {
            parsedConfig = JSON.parse(ruleItem.ruleConfig);
        } catch (e) {
            console.warn("无法解析 ruleConfig:", ruleItem.ruleConfig);
            return;
        }
        const sourceField = parsedConfig?.fieldMerge?.sourceField;
        if (!sourceField) return;

        const tableIndex = tableFields.value.findIndex(
            (item) => item.columnName == sourceField
        );
        if (tableIndex !== -1) {
            const updatedItem = {
                ...tableFields.value[tableIndex],
                cleanRuleList: [ruleItem],
                elementId: [ruleItem.ruleId],
            };
            tableFields.value[tableIndex] = updatedItem;

            const fieldIndex = inputFields.value.findIndex(
                (item) => item.columnName == sourceField
            );
            if (fieldIndex !== -1) {
                inputFields.value[fieldIndex] = updatedItem;
            } else {
                inputFields.value.push(updatedItem);
            }
        }
    });
    opens.value = false;
};

const off = () => {
    proxy.resetForm("dpModelRefs");
    tableFields.value = [];
    inputFields.value = [];
    originalTableFieldsBackup.value = [];
};

const saveData = async () => {
    try {
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;
        // 判断表格是否为空
        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning("校验未通过，请至少添加一个字段");
            return;
        }
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || "133545087166112",
                projectId: userStore.projectId,
            });
            loading.value = false;
            form.value.code = response.data;
        }

        // 标准化 tableFields
        tableFields.value = tableFields.value.map(item => ({
            ...item,
            order: item.order ?? 'asc',
        }));

        const taskParams = form.value?.taskParams || {};
        taskParams.tableFields = tableFields.value;
        // 构造 outputFields = inputFields + tableFields 的增强值
        taskParams.outputFields = inputFields.value.map(input => {
            const matched = tableFields.value.find(item => item.columnName === input.columnName);
            return matched ? { ...input, ...matched } : { ...input };
        });
        emit("confirm", form.value);

    } catch (error) {
        console.error("保存数据失败:", error);
        loading.value = false;
    }
};

const closeDialog = () => {
    off();
    emit("update", false);
};

function deepCopy(data) {
    if (data === undefined || data === null) {
        return {};
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {};
    }
}

let nodeOptions = ref([]);
watchEffect(() => {
    if (!props.visible) {
        off();
        return;
    }
    form.value = deepCopy(props.currentNode?.data || {});
    nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
    originalTableFieldsBackup.value = deepCopy(
        props.currentNode?.data?.taskParams?.inputFields || []
    );
    inputFields.value = props.currentNode?.data?.taskParams?.inputFields;
    tableFields.value = Array.isArray(props.currentNode?.data?.taskParams?.tableFields)
        ? props.currentNode?.data?.taskParams?.tableFields
            .filter(item => item && typeof item === 'object')
            .map(item => ({
                ...item,
                order: item.order ?? 'asc',
            }))
        : [];
    setSort()

});
</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
