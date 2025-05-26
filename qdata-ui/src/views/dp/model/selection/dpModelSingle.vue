<template>
  <el-dialog
      title="逻辑模型-单选"
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
      <el-form-item label="模型编码" prop="modelName">
        <el-input
            style="width:240px"
            v-model="queryParams.modelName"
            placeholder="请输入模型编码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型名称" prop="modelComment">
        <el-input
            style="width:240px"
            v-model="queryParams.modelComment"
            placeholder="请输入模型名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select style="width:240px" v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in dp_model_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
        row-key="ID"
        @current-change="handleCurrentChange"
    >
      <el-table-column label="ID" align="center" prop="ID" />
      <el-table-column label="模型编码" align="center" prop="modelName">
        <template #default="scope">
          {{ scope.row.modelName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="模型名称" align="center" prop="modelComment">
        <template #default="scope">
          {{ scope.row.modelComment || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="类目编码" align="center" prop="catCode">
        <template #default="scope">
          {{ scope.row.catCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
              <dict-tag :options="dp_model_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建方式" align="center" prop="createType">
        <template #default="scope">
              <dict-tag :options="dp_model_create_type" :value="scope.row.createType"/>
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

<script setup name="DpModelSingle">
  import { listDpModel } from "@/api/dp/model/dpModel";
  import { ref } from "vue";
  const { proxy } = getCurrentInstance();

  const { dp_model_status, dp_model_create_type } = proxy.useDict('dp_model_status', 'dp_model_create_type');

  const dataList = ref([]);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      modelName: null,
      modelComment: null,
      status: null,
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
      let data = dataList.value.filter((item) => item.ID == row.ID);
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
    listDpModel(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
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
