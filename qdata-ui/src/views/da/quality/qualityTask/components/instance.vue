<template>
  <!-- 列表的 执行记录 -->
  <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" style="width: 1200px" destroy-on-close>
    <el-table stripe height="380px" v-loading="loading" :data="jobLogList" :default-sort="defaultSort"
      @sort-change="handleSortChange">
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="任务名称" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="质量评分" align="center" prop="score" width="80">
        <template #default="scope">
          {{ scope.row.score || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="执行状态" align="center" prop="successFlag">
        <template #default="scope">
          <dict-tag :options="quality_log_success_flag" :value="scope.row.successFlag" />

        </template>
      </el-table-column>
      <el-table-column label="问题数据" align="center" prop="problemData">
        <template #default="scope">
          {{ scope.row.problemData || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="160" sortable="custom"
        column-key="start_time" :sort-orders="['descending', 'ascending']">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160" sortable="custom" column-key="end_time"
        :sort-orders="['descending', 'ascending']">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="200">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="logDetailCatList(scope.row)"
            v-hasPermi="['monitor:job:query']">查看</el-button>
          <!-- <el-button link type="warning" @click="handleExport(scope.row)" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-download-line mr5"></i>下载
          </el-button> -->
          <el-button link type="primary" icon="view" @click="
            routeTo('/da/quality/qualityTaskLog/detail', {
              ...scope.row,
              info: true,
            })
            ">详情</el-button>
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
    <!-- <template #footer>
            <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData">保存</el-button>
        </div>
</template> -->
  </el-dialog>
  <!-- 调度日志详细 -->
  <el-dialog title="查看日志" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
    <div v-html="formattedText"></div>
    <!-- <template #footer>
            <div class="dialog-footer">
                <el-button @click="open = false">关 闭</el-button>
            </div>
        </template> -->
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue';

const { proxy } = getCurrentInstance();
const defaultSort = ref({ columnKey: 'start_time', order: 'desc' });
import { useRoute, useRouter } from "vue-router"
const { sys_common_status, sys_job_group, quality_log_success_flag } = proxy.useDict(
  'sys_common_status',
  'sys_job_group',
  'quality_log_success_flag'
);
import { listDppQualityLog, getDppQualityLog, delDppQualityLog, addDppQualityLog, updateDppQualityLog } from "@/api/da/quality/qualityTaskLog";
import {
  qualityLogLogDetailCat
} from "@/api/da/quality/qualityTask";;
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: '表单标题' },
  data: { type: Object, default: () => ({}) }
});
const open = ref(false);
let form = ref();
let queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  nodeId: undefined,
  taskId: undefined
});
const formattedText = computed(() => {
  return form.value.logContent.replace(/\n/g, '<br>');
});
const router = useRouter();

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
  queryParams.value.orderByColumn = column?.columnKey || prop;
  queryParams.value.isAsc = column.order;
  getList();
}

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row?.id,
          info: row?.info,
          score: row.score
        },
      });
    }
  }
}

async function logDetailCatList(row) {
  try {
    if (!row.path) {
      proxy.$message.warning('操作失败，未查询到日志');
      return;
    }
    form.value = {};
    const response = await qualityLogLogDetailCat({ handleMsg: row.path });
    if (response && response.content) {
      form.value = response.content;
      open.value = true;
    }
  } catch (error) { }
}

const total = ref(0);
const dateRange = ref([]);
let jobLogList = ref([]);
let loading = ref(false);
/** 查询调度日志列表 */
function getList() {
  loading.value = true;
  queryParams.value.qualityId = props.data.id;
  listDppQualityLog({
    ...queryParams.value
  }).then((response) => {
    jobLogList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
}
const emit = defineEmits(['update:visible', 'confirm']);

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      getList();
    } else {
      jobLogList.value = [];
    }
  }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit('update:visible', newValue);
  }
});

// 关闭对话框的方法
const closeDialog = () => {
  emit('update:visible', false);
};

// 保存数据的方法
const saveData = () => {
  emit('confirm', localNode.value); // 向父组件提交本地数据
  emit('update:visible', false);
};
</script>
