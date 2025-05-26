<template>
  <el-dialog
      title="敏感等级-单选"
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
      <el-form-item label="敏感级别" prop="sensitiveLevel">
        <el-input
            style="width:240px"
            v-model="queryParams.sensitiveLevel"
            placeholder="请输入敏感级别"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="敏感规则" prop="sensitiveRule">
        <el-input
            style="width:240px"
            v-model="queryParams.sensitiveRule"
            placeholder="请输入敏感规则"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="起始字符位置" prop="startCharLoc">
        <el-input
            style="width:240px"
            v-model="queryParams.startCharLoc"
            placeholder="请输入起始字符位置"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="截止字符位置" prop="endCharLoc">
        <el-input
            style="width:240px"
            v-model="queryParams.endCharLoc"
            placeholder="请输入截止字符位置"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="遮盖字符" prop="maskCharacter">
        <el-input
            style="width:240px"
            v-model="queryParams.maskCharacter"
            placeholder="请输入遮盖字符"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上下线标识" prop="onlineFlag">
        <el-input
            style="width:240px"
            v-model="queryParams.onlineFlag"
            placeholder="请输入上下线标识"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
            style="width:240px"
            v-model="queryParams.description"
            placeholder="请输入描述"
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
      <el-table-column label="敏感级别" align="center" prop="sensitiveLevel">
        <template #default="scope">
          {{ scope.row.sensitiveLevel || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="敏感规则" align="center" prop="sensitiveRule">
        <template #default="scope">
          {{ scope.row.sensitiveRule || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="起始字符位置" align="center" prop="startCharLoc">
        <template #default="scope">
          {{ scope.row.startCharLoc || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="截止字符位置" align="center" prop="endCharLoc">
        <template #default="scope">
          {{ scope.row.endCharLoc || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="遮盖字符" align="center" prop="maskCharacter">
        <template #default="scope">
          {{ scope.row.maskCharacter || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="上下线标识" align="center" prop="onlineFlag">
        <template #default="scope">
          {{ scope.row.onlineFlag || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description">
        <template #default="scope">
          {{ scope.row.description || '-' }}
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

<script setup name="DaSensitiveLevelSingle">
  import { listDaSensitiveLevel } from "@/api/da/sensitiveLevel/daSensitiveLevel";
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
      sensitiveLevel: null,
      sensitiveRule: null,
      startCharLoc: null,
      endCharLoc: null,
      maskCharacter: null,
      onlineFlag: null,
      description: null,
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
    listDaSensitiveLevel(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
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
