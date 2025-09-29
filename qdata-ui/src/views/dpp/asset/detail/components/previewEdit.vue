<template>
    <!-- 数据预览的修改弹窗 -->
    <el-dialog v-model="visible" class="dialog" draggable destroy-on-close>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="queryFormRef" :model="dataForm" :rules="rules" label-width="200px" @submit.prevent
            v-loading="loading">
            <el-row v-for="item in columnsTwo" :key="item.en" :gutter="20">
                <el-col :span="24">
                    <el-form-item :prop="item.en" style="width: 100%;">
                        <!-- label -->
                        <template #label>
                            <overflow-tooltip :text="item.cn ? `${item.en} (${item.cn})` : item.en" max-width="200px" />
                        </template>
                        <el-input v-model="dataForm[item.en]" :type="item.dataLength > 200 ? 'textarea' : 'input'"
                            :placeholder="`请输入 ${formatLabel(item)}`" style="width: 100%;" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="submitForm" :disabled="loading">
                确定
            </el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { addDaAssetOperateLog } from "@/api/da/assetchild/operate/daAssetOperateLog.js";
import moment from "moment";
import OverflowTooltip from "@/components/OverflowTooltip";
const props = defineProps({
    columns: {
        type: Array,
        default: () => [],
    },
    maxWidth: { type: Number, default: 300 }, // 默认 300
});

const emit = defineEmits(["ok"]);

const visible = ref(false);
const loading = ref(false);
const dataForm = ref({});
const oldData = ref({});
const fillUniqueKey = ref(1);
const columnsTwo = ref([]);
const uniqueKeys = ref([]);
const rules = ref({});
const queryFormRef = ref();

watch(
    () => props.columns,
    (arr) => {
        if (arr && arr.length > 0) {
            const requiredFields = arr.filter((item) => item.columnNullable == true);
            columnsTwo.value = arr.filter((item) => item.columnKey == false);
            uniqueKeys.value = arr.filter((item) => item.columnKey != false);

            const rulesObj = {};
            requiredFields.forEach((item) => {
                rulesObj[item.en] = [
                    { required: true, message: `请输入${item.en}`, trigger: "blur" },
                    { validator: noSpecialCharacters, trigger: "blur" },
                ];
            });
            rules.value = rulesObj;
        }
    },
    { immediate: true }
);

// -------- label 拼接和 tooltip 判断 ----------
function formatLabel(item) {
    return item.cn ? `${item.en} (${item.cn})` : item.en;
}

function isLongLabel(item) {
    return formatLabel(item).length > 16; // 超过 16 个字符才显示 tooltip
}

// 特殊字符校验
function noSpecialCharacters(rule, value, callback) {
    const datePattern = /^(19|20)\d{2}[-/](0[1-9]|1[0-2])[-/](0[1-9]|[12]\d|3[01])$/;
    const isValidInput = /^[a-zA-Z0-9\s]+$/.test(value);

    if (datePattern.test(value)) {
        callback();
    } else if (!value) {
        callback(new Error("输入不能为空"));
    } else if (!isValidInput) {
        callback();
    } else {
        callback();
    }
}

let query = ref({});
let title = ref("");

function addRow(row, data) {
    query.value = {
        assetId: data.id,
        datasourceId: data.datasourceId,
        tableName: data.tableName,
        tableComment: data.tableComment,
        operateType: row ? "2" : "1",
    };

    title.value = row ? "修改" : "新增";
    visible.value = true;
    dataForm.value = { ...row };
    oldData.value = { ...row };
}

function submitForm() {
    queryFormRef.value.validate((valid) => {
        if (!valid) return;

        loading.value = true;
        const commentKeyList = uniqueKeys.value.map((item) => item.en).join(",");
        const getModifiedFields = (oldData, newData) => {
            return Object.keys(newData).filter((key) => newData[key] !== oldData[key]);
        };
        const modifiedFields = getModifiedFields(oldData.value, dataForm.value);
        const tableCommentList = modifiedFields.join(",");

        const fieldNamesObj = { tableCommentList, commentKeyList };
        function close() {
            visible.value = false;
        }

        const params = {
            ...query.value,
            operateTime: moment().format("YYYY-MM-DDTHH:mm:ss.SSSZ"),
            updateBefore: JSON.stringify(oldData.value),
            updateAfter: JSON.stringify(dataForm.value),
            fieldNames: JSON.stringify(fieldNamesObj),
        };

        addDaAssetOperateLog(params)
            .then((res) => {
                if (res.code == "200") {
                    close();
                    ElMessage.success("修改成功");
                    emit("ok");
                }
            })
            .finally(() => {
                loading.value = false;
            });
    });
}

defineExpose({ addRow });
</script>

<style scoped lang="scss">
::v-deep .el-form-item--small .el-form-item__content {
    line-height: 32px;
    width: 75%;
}

.label-ellipsis {
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
}
</style>
