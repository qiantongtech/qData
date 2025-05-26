<template>
  <el-dialog
      title="API服务-单选"
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
      <el-form-item label="API服务名称" prop="NAME">
        <el-input
            style="width:240px"
            v-model="queryParams.NAME"
            placeholder="请输入API服务名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="STATUS">
        <el-select style="width:240px" v-model="queryParams.STATUS" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in ds_api_log_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
            style="width:240px"
            v-model="daterangeCreateTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
      <el-table-column label="API服务名称" align="center" prop="NAME">
        <template #default="scope">
          {{ scope.row.NAME || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="API版本" align="center" prop="apiVersion">
        <template #default="scope">
          {{ scope.row.apiVersion || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="API路径" align="center" prop="apiUrl">
        <template #default="scope">
          {{ scope.row.apiUrl || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="请求方式" align="center" prop="reqMethod">
        <template #default="scope">
              <dict-tag :options="ds_api_bas_info_api_method_type" :value="scope.row.reqMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="返回结果类型" align="center" prop="resDataType">
        <template #default="scope">
              <dict-tag :options="ds_api_bas_info_res_data_type" :value="scope.row.resDataType"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="REMARK">
        <template #default="scope">
          {{ scope.row.REMARK || '-' }}
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

<script setup name="DsApiSingle">
  import { listDsApi } from "@/api/ds/api/dsApi";
  import { ref } from "vue";
  const daterangeCreateTime = ref([]);
  const { proxy } = getCurrentInstance();

  const { ds_api_log_status, ds_api_bas_info_api_service_type, ds_api_bas_info_api_method_type, ds_api_bas_info_res_data_type } = proxy.useDict('ds_api_log_status', 'ds_api_bas_info_api_service_type', 'ds_api_bas_info_api_method_type', 'ds_api_bas_info_res_data_type');

  const dataList = ref([]);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      NAME: null,
      STATUS: null,
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
    listDsApi(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
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
    daterangeCreateTime.value = null;
    handleQuery();
  }

  defineExpose({ open });
</script>
