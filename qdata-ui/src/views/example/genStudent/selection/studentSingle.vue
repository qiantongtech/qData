<template>
  <el-dialog
      title="学生-单选"
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
      <el-form-item label="姓名" prop="name">
        <el-input
            style="width:240px"
            v-model="queryParams.name"
            placeholder="请输入姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select style="width:240px" v-model="queryParams.sex" placeholder="请选择性别" clearable>
          <el-option
              v-for="dict in sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input
            style="width:240px"
            v-model="queryParams.age"
            placeholder="请输入年龄"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学号" prop="studentNumber">
        <el-input
            style="width:240px"
            v-model="queryParams.studentNumber"
            placeholder="请输入学号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="班级" prop="grade">
        <el-input
            style="width:240px"
            v-model="queryParams.grade"
            placeholder="请输入班级"
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
      <el-table-column label="姓名" align="center" prop="name">
        <template #default="scope">
          {{ scope.row.name || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="学生照" align="center" prop="pictureUrl" width="100">
        <template #default="scope">
          <image-preview :src="scope.row.pictureUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="教育经历" align="center" prop="experience">
        <template #default="scope">
          {{ scope.row.experience || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="sex">
        <template #default="scope">
              <dict-tag :options="sys_user_sex" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age">
        <template #default="scope">
          {{ scope.row.age || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="学号" align="center" prop="studentNumber">
        <template #default="scope">
          {{ scope.row.studentNumber || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="班级" align="center" prop="grade">
        <template #default="scope">
          {{ scope.row.grade || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="爱好" align="center" prop="hobby">
        <template #default="scope">
              <dict-tag :options="message_level" :value="scope.row.hobby ? scope.row.hobby.split(',') : []"/>
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

<script setup name="StudentSingle">
  import { listStudent } from "@/api/example/genStudent/student";
  import { ref } from "vue";
  const { proxy } = getCurrentInstance();

  const { sys_user_sex, message_level } = proxy.useDict('sys_user_sex', 'message_level');

  const dataList = ref([]);
  const loading = ref(true);
  const showSearch = ref(true);
  const total = ref(0);
  const data = reactive({
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      name: null,
      pictureUrl: null,
      experience: null,
      sex: null,
      age: null,
      studentNumber: null,
      grade: null,
      hobby: null,
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
    listStudent(proxy.addDateRange(queryParams.value, daterangeCreateTime.value)).then(
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
