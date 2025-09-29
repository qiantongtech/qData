<template>
  <el-dialog v-model="visibleDialog" draggable width="1000px" class="dialog" :title="title" destroy-on-close>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="表描述" name="1">
        <div class="infotop">
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据库连接</div>
                <div class="infotop-row-value">
                  {{ daDiscoveryTaskDetail?.datasourceName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据库类型</div>
                <div class="infotop-row-value">
                  {{ daDiscoveryTaskDetail?.datasourceType || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据库地址</div>
                <div class="infotop-row-value">
                  {{ daDiscoveryTaskDetail?.ip || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表中文名称</div>
                <div class="infotop-row-value">
                  {{ form.tableComment || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表英文名称</div>
                <div class="infotop-row-value">
                  {{ form.tableName || '-' }}
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">表描述</div>
                <div class="infotop-row-value">
                  {{ form.tableComment || '-' }}
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane label="表字段" name="2">
        <el-table :data="daDiscoveryTaskList" height="455" style="width: 100%" v-loading="loading">
          <el-table-column label="英文名称" align="left" prop="tableName" :show-overflow-tooltip="{effect: 'light'}" width="280">
            <template #default="scope">
                            <span @click="handltableNamee(scope.row)">
                                {{ scope.row.columnName || '-' }}
                            </span>
            </template>
          </el-table-column>
          <el-table-column label="中文名称" align="left" prop="tableComment" :show-overflow-tooltip="{effect: 'light'}" width="310">
            <template #default="scope">
              {{ scope.row.columnComment || '-' }}
            </template>
          </el-table-column>

          <el-table-column label="数据类型" align="left" prop="columnType" :show-overflow-tooltip="{effect: 'light'}" width="110">
            <template #default="scope">
              {{ scope.row.columnType || '-' }}
            </template>
          </el-table-column>

          <el-table-column label="属性长度" align="left" prop="columnLength" :show-overflow-tooltip="{effect: 'light'}" width="80">
            <template #default="scope">
              {{ scope.row.columnLength || '-' }}
            </template>
          </el-table-column>

          <el-table-column label="是否主键" align="center" prop="pkFlag" :show-overflow-tooltip="{effect: 'light'}" width="100">
            <template #default="scope">
              <!-- <dict-tag :options="da_discovery_table_change_flag" :value="scope.row.pkFlag" /> -->
              <el-switch disabled v-model="scope.row.pkFlag" active-value="1"
                         inactive-value="0"></el-switch>
            </template>
          </el-table-column>
          <template #empty>
            <div class="emptyBg">
              <img src="../../../../assets/system/images/no_data/noData.png" alt="" />
              <p>暂无记录</p>
            </div>
          </template>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="表数据" name="3">
        <el-table :data="tableData" height="400px" border v-loading="loading">
          <el-table-column v-for="col in tableColumns" :key="col.field" :prop="col.field" :min-width="'150px'"
                           :show-overflow-tooltip="{effect: 'light'}">
            <template #header>
              <div class="column-header">
                <div class="column-item">
                  {{ col.en }}
                </div>
                <div class="column-item">
                  <el-tooltip :content="col.cn" placement="top" show-after="200"
                              v-if="col.cn.length > 10">
                                        <span class="text-ellipsis" :title="col.cn">{{
                                            col.cn
                                          }}</span>
                  </el-tooltip>
                  <span v-else class="text-ellipsis">{{ col.cn }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                    v-model:limit="queryParams.pageSize" @pagination="getListss" />
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { getDaDiscoveryColumnList, preview } from '@/api/da/discovery/discoveryTable.js';
const { proxy } = getCurrentInstance();
import { defineProps, defineEmits, ref, computed, watch } from 'vue';
const activeName = ref('1');
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '表单标题' },
  data: { type: Object, default: () => ({}) },
  daDiscoveryTaskDetail: { type: Object, default: () => ({}) }
});
const columns = ref([
  { key: 1, label: '英文名称', visible: true },
  { key: 2, label: '中文名称', visible: true },
  { key: 3, label: '行数', visible: true },
  { key: 4, label: '字段数', prop: 'tableComment', visible: true },
  { key: 5, label: '表结构变更标识', prop: 'tableComment', visible: true },
  { key: 6, label: '状态', prop: 'tableComment', visible: true }
]);
const tableColumns = ref();
const tableData = ref();
const total = ref(0);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    taskId: null,
    tableName: null
  }
});

const { queryParams, form } = toRefs(data);

const handleClick = (tab, event) => {
  console.log(tab.props.name, event, '3333');
  if (tab.props.name == 2) {
    getDaDiscoveryColumnListID();
  } else if (tab.props.name == 3) {
    getListss();
  }
};
let loading = ref(false);
function getListss() {
  loading.value = true;
  preview(queryParams.value)
      .then((response) => {
        tableColumns.value = response.data.columns;
        tableData.value = response.data.tableData;
        total.value = response.data.total;
        loading.value = false;
      })
      .catch(() => {
        loading.value = false;
      });
}
let daDiscoveryTaskList = ref([]);
const emit = defineEmits(['update:visible', 'confirm']);
function getDaDiscoveryColumnListID() {
  loading.value = true;
  getDaDiscoveryColumnList({
    taskId: props.daDiscoveryTaskDetail.id,
    tableId: props.data.id
  }).then((response) => {
    daDiscoveryTaskList.value = response.data || [];
    // total.value = response.data.total;
    loading.value = false;
  });
}


watch(
    () => props.visible,
    (newVal) => {
      if (newVal) {
        console.log('🚀 ~ newVal:', newVal);
        var data =  JSON.parse(JSON.stringify(props.data))
        form.value = data;
        queryParams.value.taskId = props.daDiscoveryTaskDetail.id;
        queryParams.value.tableName = data.tableName;
        console.log('🚀 ~ props.data:', props.data);
        props.daDiscoveryTaskDetail.contactNumber =
            Number(props.daDiscoveryTaskDetail.contactNumber) || '';
        daDiscoveryTaskList.value = [];
        activeName.value = '1';
      } else {
        queryParams.value.pageNum = 1;
        queryParams.value.pageSize = 10;
        queryParams.value.taskId = null;
        queryParams.value.tableName = null;
        activeName.value = '1';
        daDiscoveryTaskList.value = [];
        tableColumns.value = [];
        tableData.value = [];
      }
    }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit('update:visible', newValue); // 使用 emit 更新父组件的 visible
  }
});

// 关闭对话框的方法
const closeDialog = () => {
  emit('update:visible', false); // 更新父组件的 visible 为 false，关闭对话框
};

// 保存数据的方法
const saveData = () => {
  emit('confirm', form.value);
  emit('update:visible', false);
};
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
</script>
<style lang="scss" scoped>
.column-header {
  display: flex;
  flex-direction: column;
}

.column-item {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
