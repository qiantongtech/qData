<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="queryParams.userName" placeholder="请输入用户名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openSelectUser"
              v-hasPermi="['system:role:add']">新增用户</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="CircleClose" :disabled="multiple" @click="cancelAuthUserAll"
              v-hasPermi="['system:role:remove']">批量取消授权</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Close" @click="handleClose">关闭</el-button>
          </el-col>
        </el-row>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh" v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
        <el-table-column label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button link type="primary" icon="CircleClose" @click="cancelAuthUser(scope.row)"
              v-hasPermi="['system:role:remove']">取消授权</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize"
        @pagination="getList" />
    </div>
    <select-user ref="selectRef" :roleId="queryParams.roleId" @ok="handleQuery" />
  </div>
</template>

<script setup name="AuthUser">
import selectUser from "./selectUser.vue";
import { allocatedUserList, authUserCancel, authUserCancelAll } from "@/api/system/system/role.js";

const route = useRoute();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const userList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const multiple = ref(true);
const total = ref(0);
const userIds = ref([]);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleId: route.params.roleId,
  userName: undefined,
  phonenumber: undefined,
});

/** 查询授权用户列表 */
function getList() {
  loading.value = true;
  allocatedUserList(queryParams).then((response) => {
    userList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 返回按钮 */
function handleClose() {
  const obj = { path: "/system/role" };
  proxy.$tab.closeOpenPage(obj);
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  userIds.value = selection.map((item) => item.userId);
  multiple.value = !selection.length;
}

/** 打开授权用户表弹窗 */
function openSelectUser() {
  proxy.$refs["selectRef"].show();
}

/** 取消授权按钮操作 */
function cancelAuthUser(row) {
  proxy.$modal
    .confirm('确认要取消该用户"' + row.userName + '"角色吗？')
    .then(function () {
      return authUserCancel({ userId: row.userId, roleId: queryParams.roleId });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("取消授权成功");
    })
    .catch(() => { });
}

/** 批量取消授权按钮操作 */
function cancelAuthUserAll(row) {
  const roleId = queryParams.roleId;
  const uIds = userIds.value.join(",");
  proxy.$modal
    .confirm("是否取消选中用户授权数据项?")
    .then(function () {
      return authUserCancelAll({ roleId: roleId, userIds: uIds });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("取消授权成功");
    })
    .catch(() => { });
}

getList();
</script>
