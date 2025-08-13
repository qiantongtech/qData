<template>
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="8">
                <el-form-item label="关联代码表" prop="useCodeTable">
                    <el-radio-group v-model="form.useCodeTable" @change="handleUseCodeTableChange">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>

            <el-col :span="8">
                <el-form-item label="忽略空值" prop="ignoreNullValue">
                    <el-radio-group v-model="form.ignoreNullValue">
                        <el-radio :value="'1'">否</el-radio>
                        <el-radio :value="'0'">是</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>

            <el-col :span="8">
                <el-form-item label="是否区分大小写" prop="ignoreCase">
                    <el-radio-group v-model="form.ignoreCase">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>

        <!-- 👇 当选择了“关联代码表”为“是”时展示 -->
        <el-row v-if="form.useCodeTable == '1'">
            <el-col :span="8">
                <el-form-item label="选择代码表" prop="codeTableId"
                    :rules="[{ required: true, message: '选择代码表', trigger: 'change' }]">
                    <el-select v-model="form.codeTableId" placeholder="请选择代码表" filterable clearable
                        @change="handleCodeTableChange">
                        <el-option v-for="item in dpDataElemList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>


        <div v-loading="loadingList">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" icon="Plus" v-if="form.useCodeTable == 0"
                            @click="opencodeDialog(undefined)">新增</el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table stripe height="200px" :data="form.codeList">
                <el-table-column label="代码值" align="center" prop="codeValue">
                    <template #default="scope">
                        <template v-if="form.useCodeTable == 0">
                            <el-input v-model="scope.row.codeValue" style="width: 100%" placeholder="请输入代码值" />
                        </template>
                        <template v-else>
                            {{ scope.row.codeValue }}
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="代码名称" align="center" prop="codeName">
                    <template #default="scope">
                        <template v-if="form.useCodeTable == 0">
                            <el-input v-model="scope.row.codeName" style="width: 100%" placeholder="请输入代码名称" />
                        </template>
                        <template v-else>
                            {{ scope.row.codeName }}
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="150">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete"
                            @click="handleDelete(scope.row, scope.$index + 1)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-row>
        </el-row>

    </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import {
    listDpDataElem,
} from "@/api/dp/dataElem/dpDataElem";
import {
    listDpDataElemCode,
} from '@/api/dp/dataElem/dpDataElemCode';
const props = defineProps({
    form: Object,
    dppQualityTaskObjSaveReqVO: Array,
    falg: Boolean,
});
let loadingList = ref(false);
const emit = defineEmits(["update:form"]);
let loading = ref(false);
const formRef = ref(null);

const form = reactive({ ...props.form, });
let dpDataElemCodeList = ref([])
let dpDataElemList = ref([])
function handleCodeTableChange(id) {
    if (!id || id == -1) return;
    loading.value = true;
    form.dataElemId = id;
    loadCodeItemsByTableId(id)
}
function loadCodeItemsByTableId(id) {
    if (!id || id == -1) return;
    loading.value = true;
    listDpDataElemCode({
        pageNum: 1,
        pageSize: 999,
        dataElemId: id,
        ruleType: 2
    }).then((res) => {
        dpDataElemCodeList.value = res.data.rows;
        form.codeList = res.data.rows;
        loading.value = false;
    });
}
function handleDelete(row, index) {
    form.codeList.splice(Number(index) - 1, 1);
}
function opencodeDialog() {
    const hasIncomplete = form.codeList.some(item =>
        !item.codeValue || !item.codeName
    );
    if (hasIncomplete) {
        ElMessage.warning('请先填写完整所有项');
        return;
    }

    // 新增一行空数据
    form.codeList.push({
        codeValue: '',
        codeName: '',
    });
}
function loadCodeTableList() {

    listDpDataElem({
        pageNum: 1,
        pageSize: 999,
        type: '2'

    }).then((res) => {
        dpDataElemList.value = res.data.rows;
        loading.value = false;
    }).catch(() => {
        loading.value = false;
    });
}
function handleUseCodeTableChange(val) {
    if (val == '1') {
        loadCodeTableList();
    } else {
        form.codeTableId = '';
        form.codeList = [];
        dpDataElemList.value = [];
    }
}

onMounted(() => {
    if (form.useCodeTable === '1' && form.codeTableId) {
        handleUseCodeTableChange('1', true);
    }
});
function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (!valid) {
                resolve({ valid: false });
                return;
            }

            if (form.useCodeTable === '0') {
                if (!form.codeList || form.codeList.length === 0) {
                    ElMessage.warning("请至少添加一条代码项！");
                    resolve({ valid: false });
                    return;
                }

                const values = form.codeList.map(item => item.codeValue?.trim()).filter(v => v !== '');
                const hasEmpty = values.length !== form.codeList.length;
                if (hasEmpty) {
                    ElMessage.warning("代码值不能为空！");
                    resolve({ valid: false });
                    return;
                }

                const duplicates = values.filter((val, idx) => values.indexOf(val) !== idx);
                if (duplicates.length > 0) {
                    ElMessage.warning("代码值不能重复！");
                    resolve({ valid: false });
                    return;
                }
            }

            const result = {
                useCodeTable: form.useCodeTable,
                ignoreNullValue: form.ignoreNullValue,
                ignoreCase: form.ignoreCase,
                codeTableId: form.useCodeTable === '1' ? form.codeTableId : '',
                codeList: form.codeList,
                validValues: form.codeList
                    .map(item => item.codeValue)
                    .filter(v => v !== undefined && v !== null && v !== ''),
            };

            resolve({ valid: true, data: result });
        });
    });
}





defineExpose({ validate });
</script>
<style scoped></style>
