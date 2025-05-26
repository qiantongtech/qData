<template>
  <el-dialog
      title="数据资产申请-单选"
      v-model="visible"
      width="1200px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
      @close="cancel"
  >
    <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
    >
      <el-form-item label="资产id" prop="assetId">
        <el-input
            style="width:240px"
            v-model="queryParams.assetId"
            placeholder="请输入资产id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目id" prop="projectId">
        <el-input
            style="width:240px"
            v-model="queryParams.projectId"
            placeholder="请输入项目id"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目编码" prop="projectCode">
        <el-input
            style="width:240px"
            v-model="queryParams.projectCode"
            placeholder="请输入项目编码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请理由" prop="applyReason">
        <el-input
            style="width:240px"
            v-model="queryParams.applyReason"
            placeholder="请输入申请理由"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批理由" prop="approvalReason">
        <el-input
            style="width:240px"
            v-model="queryParams.approvalReason"
            placeholder="请输入审批理由"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker style="width:240px"
                        clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
            plain
            type="primary"
            @click="handleQuery"
            @mousedown="(e) => e.preventDefault()"
        >
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
        ref="tableRef"
        stripe
        height="300px"
        v-loading="loading"
        :data="dataList"
        highlight-current-row
        row-key="id"
        @current-change="handleCurrentChange"
    >
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="资产id" align="center" prop="assetId">
        <template #default="scope">
          {{ scope.row.assetId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="项目id" align="center" prop="projectId">
        <template #default="scope">
          {{ scope.row.projectId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="项目编码" align="center" prop="projectCode">
        <template #default="scope">
          {{ scope.row.projectCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="申请理由" align="center" prop="applyReason">
        <template #default="scope">
          {{ scope.row.applyReason || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="审批理由" align="center" prop="approvalReason">
        <template #default="scope">
          {{ scope.row.approvalReason || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          {{ scope.row.status || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
          {{ scope.row.remark || '-' }}
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="confirm">
          确 定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="DaAssetApplySingle">
  import { listDaAssetApply } from "@/api/da/daAssetApply/daAssetApply";
  import { ref } from "vue";
  const { proxy } = getCurrentInstance();


  const dataList = ref([]);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      assetId: null,
      projectId: null,
      projectCode: null,
      applyReason: null,
      approvalReason: null,
      status: null,
      createTime: null,
    }
  });
  const { queryParams, form} = toRefs(data);

  // -------------------------------------------
  const visible = ref(false);
  // 定义单选数据
  const single = ref();
  // 当前界面table
  const tableRef = ref();

  const emit = defineEmits(["open", "confirm", "cancel"]);

  /** 单选选中事件 */
  function handleCurrentChange(selection) {
    if (selection) {
      single.value = selection;
    }
  }

  /**
   * 设置当前行
   * @param {Object} row 行对象
   * @returns 更改选中对象
   */
  function setCurrentRow(row) {
    if (row) {
      let data = dataList.value.filter((item) => item.id == row.id);
      tableRef.value?.setCurrentRow(data[0]);
    }
  }

  /**
   * 打开选择框
   * @param {Array} val 选中的对象数组
   */
  function open(val) {
    visible.value = true;
    single.value = val;
    resetQuery();
    getList();
  }

  /**
   * 取消按钮
   * @description 取消按钮时，重置所有状态
   */
  function cancel() {
    queryParams.value.pageNum = 1;
    proxy.resetForm("queryRef");
    visible.value = false;
  }

  /**
   * 确定按钮
   * @description 确定按钮时，emit confirm 事件，以便父组件接收到选中的数据
   */
  function confirm() {
    if (!single.value) {
      proxy.$modal.msgWarning("请选择数据！");
      return;
    }
    emit("confirm", single.value);
    visible.value = false;
  }

  /** 查询字典类型列表 */
  function getList() {
    loading.value = true;
    listDaAssetApply(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
        async (response) => {
          dataList.value = response.data.rows;
          total.value = response.data.total;
          loading.value = false;
          // 初始化及分页切换选中逻辑
          await nextTick();
          setCurrentRow(single.value);
        }
    );
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    queryParams.value.pageNum = 1;
    handleQuery();
  }

  defineExpose({ open });
</script>
