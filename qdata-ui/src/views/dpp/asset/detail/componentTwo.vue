<template>
    <div style="padding: 5px">
        <div class="justify-between mb15">
            <el-row :gutter="15" class="btn-style">
                <el-col :span="1.5" v-if="form1.type != '6'">
                    <el-button type="primary" plain @click="handleAdd" v-hasPermi="['da:assetColumn:assetcolumn:add']"
                        :loading="loading" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                    </el-button>
                </el-col>
                <el-button style="margin-left: 7px" plain type="primary" :loading="loading" @click="handleQuery"
                    @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
            </el-row>
        </div>
        <el-row :gutter="24" v-if="!formVisible && form1.type != '6'">
            <el-col :span="1">
                <el-button style="" @click="toggleForm(true)" type="primary" size="small">+</el-button>
            </el-col>
            <el-col :span="7">
                <el-alert style="height: 24px" title="点击“+”以添加筛选准则" type="info" :closable="false" />
            </el-col>
        </el-row>
        <div class="custom-form">
            <el-form v-show="formVisible" :model="formData" ref="formRef" label-width="auto">
                <div v-for="(item, index) in formData.rows" :key="index" class="form-row">
                    <el-form-item :prop="'rows.' + index + '.checked'">
                        <el-checkbox v-model="item.checked"> </el-checkbox>
                    </el-form-item>
                    <el-form-item :prop="'rows.' + index + '.field'">
                        <div>
                            <el-select :disabled="!item.checked" :class="item.checked ? 'select' : ''"
                                style="margin: 0; width: 100px; border: none; color: red" v-model="item.field"
                                placeholder="选择字段">
                                <el-option v-for="field in tableColumns" :key="field.en" :label="field.en"
                                    :value="field.en"></el-option>
                            </el-select>
                        </div>
                    </el-form-item>
                    <el-form-item :prop="'rows.' + index + '.operator'">
                        <el-select :disabled="!item.checked" style="margin: 0; width: 20px" v-model="item.operator">
                            <el-option style="text-align: center" v-for="operator in operators" :key="operator"
                                :label="operator" :value="operator"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item :prop="'rows.' + index + '.value'">
                        <div :class="item.checked ? 'inner' : ''">
                            <el-input v-if="item.operator === '='" :disabled="!item.checked" v-model="item.value"
                                placeholder="请输入值"></el-input>

                            <el-input v-else-if="item.operator === '>'" :disabled="!item.checked" v-model="item.value"
                                type="number" placeholder="请输入值"></el-input>
                        </div>
                    </el-form-item>
                    <el-form-item :prop="'rows.' + index + '.logic'" style="display: block">
                        <el-select :disabled="!item.checked" v-if="index < formData.rows.length - 1"
                            style="margin: 0; width: 80px; display: block" v-model="item.logic" placeholder="选择逻辑">
                            <el-option value="AND" style="text-align: center">AND</el-option>
                        </el-select>
                        <div v-else style="width: 80px"></div>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="removeRow(index)" type="danger" size="small"
                            style="margin-left: 10px">-</el-button>
                        <el-button v-if="index == formData.rows.length - 1" @click="addRow(index)" type="primary"
                            size="small">+</el-button>
                    </el-form-item>
                </div>
            </el-form>
        </div>
    </div>
    <el-table :data="tableData" height="60vh" border v-loading="loading">
        <el-table-column v-for="col in tableColumns" :key="col.field" :prop="col.field" :min-width="'150px'"
            show-overflow-tooltip>
            <template #header>
                <div class="column-header">
                    <div class="column-item">
                        {{ col.en || '-' }}
                    </div>
                    <div class="column-item">
                        {{ col.cn || '-' }}
                    </div>
                </div>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
            v-if="tableColumns && form1.type != '6'" width="150px">
            <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                    v-hasPermi="['da:asset:asset:edit']">修改</el-button>
                <el-button link type="primary" icon="view" @click="openHistory(scope.row)"
                    v-hasPermi="['da:asset:asset:edit']">修改记录</el-button>
                <!-- <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                    v-hasPermi="['da:asset:asset:remove']">删除</el-button> -->
            </template>
        </el-table-column>

        <template #empty>
            <div class="emptyBg">
                <img src="@/assets/system/images/no_data/noData.png" alt="" />
                <p>暂无记录</p>
            </div>
        </template>
    </el-table>
    <pagination v-show="total > 0" :total="total" v-model:page="pageNum" v-model:limit="pageSize"
        @pagination="handleQuery" />
    <UpdateDataDaiol ref="updateDialogRef" :columns="tableColumns" @ok="handleQuery" />
    <UpdateHistory ref="updateHistoryRef" :columns="tableColumns" @success="handleQuery" />
</template>

<script setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { preview } from '@/api/da/assetColumn/daAssetColumn';
import UpdateDataDaiol from './components/UpdateDataDaiol.vue';
import UpdateHistory from './components/UpdateHistory.vue';
const props = defineProps({
    form1: {
        type: Object,
        default: {}
    }
});
const route = useRoute();
let assetId = route.query.id || 1;
const { proxy } = getCurrentInstance();
const tableColumns = ref([]);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(10);
const loading = ref(false);
const query = ref();
const operators = ref(['=', '>']);
const formData = ref({
    rows: []
});
const updateDialogRef = ref(null); // 组件的 ref 引用
const formVisible = ref(false); // 表单默认隐藏
watch(
    () => route.query.id,
    (newId) => {
        // 解决数据发现详情和当前界面同时打开报错问题
        if(route.path == '/da/asset/daAssetDetail' || route.path == '/dpp/asset/daAssetDetail') {
            assetId = newId || 1; // 如果 id 为空，使用默认值 1
            getListss();
        }
    },
    { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
// 显示或隐藏表单
const toggleForm = (falg) => {
    formVisible.value = !formVisible.value;
    if (falg) {
        addRow(-1);
    }
};

// 生成SQL查询条件
const generateSqlQuery = () => {
    const selectedRows = formData.value.rows.filter((row) => row.checked); // 筛选出 checked 为 true 的行
    let query = '';

    const conditions = selectedRows.map((row) => {
        return `${row.field} ${row.operator} '${row.value}'`;
    });

    query += conditions.join(' AND '); // 拼接条件，默认用 AND 连接
    console.log('生成的 SQL 查询语句：', query);
    return query;
};

// 查询按钮点击事件
const handleQuery = () => {
    let falg = validateFields();
    if (!falg) return false;
    query.value = generateSqlQuery();
    getListss();
};
// 表单验证
const validateFields = () => {
    for (let row of formData.value.rows) {
        if (!row.field || !row.operator) {
            ElMessage.warning('校验未通过，查询条件请输入完整');
            return false;
        }
    }
    return true;
};
const removeRow = (index) => {
    if (formData.value.rows.length > 1) {
        formData.value.rows.splice(index, 1);
    } else {
        formData.value.rows = [];
        toggleForm();
    }
};
const addRow = (index) => {
    let flag = validateFields();
    if (!flag) return false;
    if (index !== undefined && formData.value.rows[index]) {
        formData.value.rows[index].logic = 'AND';
    }
    const newRow = {
        checked: true,
        field:
            tableColumns.value[index + 1]?.en ||
            tableColumns.value[tableColumns.value.length - 1]?.en,
        operator: '=',
        value: '',
        logic: ''
    };

    if (index !== undefined) {
        formData.value.rows.splice(index + 1, 0, newRow);
    } else {
        formData.value.rows.push(newRow);
    }
};

const tableData = ref();

function handleUpdate(row) {
    console.log('🚀 ~ handleUpdate ~ row:', row);
    // proxy.$message.error('功能开发中....');
    updateDialogRef.value?.addRow(row, props.form1);
}
function handleAdd() {
    // proxy.$message.error('功能开发中....');
    updateDialogRef.value?.addRow(undefined, props.form1);
}

const updateHistoryRef = ref(null);
function openHistory(row) {
    // 调用子组件的 show 方法，传入你想要的参数，触发弹窗显示
    if (updateHistoryRef.value) {
        updateHistoryRef.value.show(row, props.form1);
    }
}
function handleDelete() {
    proxy.$message.warning('功能开发中....');
}

function getListss() {
    if (route.query.id == null || route.query.id == undefined) {
        return;
    }
    loading.value = true;
    preview({
        id: assetId,
        filter: query.value,
        pageNum: pageNum.value,
        pageSize: pageSize.value
    })
        .then((response) => {
            tableColumns.value = response.data.columns;
            tableData.value = response.data.tableData;
            loading.value = false;
            total.value = response.data.total;
        })
        .catch(() => {
            loading.value = false;
        });
}
</script>

<style scoped lang="scss">
.column-header {
    display: flex;
    flex-direction: column;
}

.column-item {
    white-space: nowrap;
}

.form-row {
    display: flex;
    align-items: center;
    margin-bottom: -17px;
}

.form-row .el-form-item {
    margin-right: 10px;
}

.custom-form {
    margin-bottom: 10px;
    max-height: 100px;
    overflow: auto;

    ::v-deep .el-input__wrapper,
    ::v-deep .el-select .el-input__wrapper {
        border: none !important;
        box-shadow: none !important;
    }

    ::v-deep .el-select__wrapper {
        box-shadow: none;
        padding: 0;
    }

    ::v-deep .is-hovering {
        box-shadow: none !important;
    }

    ::v-deep .el-icon {
        display: none;
    }

    .inner {
        ::v-deep .el-input__inner {
            color: #2666fb;
        }
    }

    .inner-text {
        ::v-deep .el-input__inner {
            color: #999093 !important;
        }
    }

    .select {
        ::v-deep .el-select__placeholder {
            color: #2666fb;
        }
    }

    :deep .el-select__wrapper.is-disabled {
        background-color: #fff;
    }

    .select-text {
        ::v-deep .el-select__placeholder {
            color: #999093 !important;
        }
    }

    :deep .el-input.is-disabled .el-input__wrapper {
        background-color: #fff;
    }
}
</style>
