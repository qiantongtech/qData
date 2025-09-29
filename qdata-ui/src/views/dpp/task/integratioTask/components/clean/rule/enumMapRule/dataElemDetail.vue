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
