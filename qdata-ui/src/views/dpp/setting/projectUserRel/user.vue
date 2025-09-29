<template>
  <div class="pagecont-top" v-show="showSearch">
    <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="68" v-show="showSearch"
      @submit.prevent>
      <el-form-item label="用户姓名" prop="nickName">
        <el-input class="el-form-input-width" v-model="queryParams.nickName" placeholder="请输入用户姓名" clearable
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号码" prop="phoneNumber">
        <el-input v-model="queryParams.phoneNumber" placeholder="请输入手机号码" clearable class="el-form-input-width"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker @change="handleDateChange" class="el-form-input-width" v-model="createTime"
          value-format="YYYY-MM-DD" type="daterange" range-separator="-" start-placeholder="开始日期"
          end-placeholder="结束日期"></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="pagecont-bottom">
    <div class="justify-between mb15">
      <el-row :gutter="15" class="btn-style">
        <el-col :span="1.5">
          <el-button type="primary" plain @click="handleAdd" v-hasPermi="['att:projectUserRel:add']"
            @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain :disabled="multiple" @click="handleDelete"
            v-hasPermi="['att:projectUserRel:remove']" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-shanchu-huise mr5"></i>移除
          </el-button>
        </el-col>
      </el-row>
      <div class="justify-end top-right-btn">
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>
    </div>
    <el-table stripe v-loading="loading" :data="AttProjectUserRelList" @selection-change="handleSelectionChange"
      :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--       <el-table-column v-if="getColumnVisibility(0)" label="ID" align="center" prop="id" />-->
      <el-table-column label="编号" width="80" align="center" prop="userId">
        <template #default="scope">
          {{ scope.row.userId || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="用户姓名" align="center" prop="nickName">
        <template #default="scope">
          {{ scope.row.nickName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="角色" align="center" prop="roleStr" :show-overflow-tooltip="{ effect: 'light' }">
        <template #default="scope">
          {{ scope.row.roleStr || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="部门" align="center" prop="deptName">
        <template #default="scope">
          {{ scope.row.deptName || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="phoneNumber">
        <template #default="scope">
          {{ scope.row.phoneNumber || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column v-if="getColumnVisibility(14)" label="创建时间" align="center" prop="create_time" width="150"
        sortable="custom" column-key="create_time" :sort-orders="['descending', 'ascending']"> <template
          #default="scope"> <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['att:projectUserRel:edit']">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
            v-hasPermi="['att:projectUserRel:remove']">移除</el-button>
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
  </div>

  <!-- 新增或修改项目与用户关联关系对话框 -->
  <el-dialog :title="title" v-model="open" :append-to-body="false" class="warn-dialog-23012" width="700px"
    :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="AttProjectUserRelRef" :model="form" :rules="rules" label-width="80px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="24" v-if="form.id == null">
          <div class="hint-div">
            <el-icon color="#2A7BFD" size="16px">
              <InfoFilled />
            </el-icon>
            <span>
              如需添加新用户，请先点击‘‘<a href="/system/user" style="color: #2a7bfd">用户管理</a>’’进行添加。
            </span>
          </div>
        </el-col>
        <el-col :span="24" v-if="form.id == null">
          <el-form-item label="系统用户" prop="userNameList">
            <el-input style="width: 76%" v-model="form.userNameList" placeholder="请选择用户" disabled>
            </el-input>
            <el-button style="margin-left: 12px" type="primary" @click="getListUser">选择用户</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.id != null">
          <el-form-item label="系统用户" prop="nickName">
            <el-input v-model="form.nickName" placeholder="请选择用户" disabled>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="用户角色" prop="roleIdList">
            <el-checkbox-group v-model="form.roleIdList" class="checkbox-vertical">
              <div v-for="item in roleList" :key="item.roleId" style="margin-bottom: 15px;height: 40px;">
                <el-checkbox :label="item.roleId">
                  {{ item.roleName }}
                </el-checkbox>
                <p
                  style="display: flex;align-items: center;line-height:1;font-size: 12px;color: #888;margin-left: 23px;margin-top: 10px;">
                  {{ item.remark }}</p>
              </div>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog title="用户选择" v-model="openTwo" width="1000px" class="user-select-tatble" draggable>
    <template>
      <span role="heading" aria-level="2" class="el-dialog__title"> 用户选择 </span>
    </template>
    <!--用户数据-->
    <el-form class="btn-style" :model="queryParamsUser" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="登录账号" prop="userName">
        <el-input v-model="queryParamsUser.userName" placeholder="请输入登录账号" clearable class="el-form-input-width"
          @keyup.enter="handleQueryUser" />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input v-model="queryParamsUser.phonenumber" placeholder="请输入手机号码" clearable class="el-form-input-width"
          @keyup.enter="handleQueryUser" />
      </el-form-item>
      <el-form-item>
        <el-button plain type="primary" @click="handleQueryUser" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
        </el-button>
        <el-button @click="resetQueryUser" @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table ref="userTableRef" stripe v-loading="loadingUser" :data="userList"
      @selection-change="handleSelectionChangeUser">
      <el-table-column type="selection" width="70" align="center" />
      <el-table-column label="编号" width="80" align="center" key="userId" prop="userId" />
      <el-table-column label="登录账号" align="center" key="userName" prop="userName"
        :show-overflow-tooltip="{ effect: 'light' }" />
      <el-table-column label="用户姓名" align="center" key="nickName" prop="nickName"
        :show-overflow-tooltip="{ effect: 'light' }" />
      <el-table-column label="部门" width="180" align="center" key="deptName" prop="dept.deptName"
        :show-overflow-tooltip="{ effect: 'light' }" />
      <el-table-column label="手机号码" width="180" align="center" key="phonenumber" prop="phonenumber" />
      <el-table-column label="创建人" :show-overflow-tooltip="true" align="left" prop="createBy">
        <template #default="scope">
          {{ scope.row.createBy || "-" }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="150"> <template #default="scope"> <span>{{
        parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="totalUser > 0" :total="totalUser" v-model:page="queryParamsUser.pageNum"
      v-model:limit="queryParamsUser.pageSize" @pagination="getListUser" />
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="openTwo = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitFormUser">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="AttProjectUserRel">
import { listUser } from '@/api/system/system/user.js';
import {
  listAttProjectUserRel,
  getAttProjectUserRel,
  delAttProjectUserRel,
  addAttProjectUserRel,
  updateAttProjectUserRel,
  editUserListAndRoleList,
  listRole,
  getRoleUser,
  addUserListAndRoleList
} from '@/api/att/projectUserRel/attProjectUserRel';
import { getToken } from '@/utils/auth.js';
import useUserStore from '@/store/system/user';
import { addUserAndProject, noProjectUser } from '@/api/att/project/project.js';
import { ref } from 'vue';
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  'sys_normal_disable',
  'sys_user_sex'
);
const AttProjectUserRelList = ref([]);
const size = (ref < 'default') | 'large' | ('small' > 'default');
const value1 = ref('');
const value2 = ref('');
const activeName = ref('first');
// 列显隐信息
const columns = ref([
  { key: 0, label: 'ID', visible: true },
  { key: 2, label: '用户ID', visible: true },
  { key: 7, label: '创建时间', visible: true }
]);
const userList = ref([]);
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};
const userStore = useUserStore();
const open = ref(false);
const openTwo = ref(false);
const openDetail = ref(false);
const loading = ref(true);
const loadingUser = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const idsUser = ref([]);
const userName = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const totalUser = ref(0);
const title = ref('');
const defaultSort = ref({ prop: 'createTime', order: 'desc' });
const router = useRouter();
const roleList = ref([]);
/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: 'Bearer ' + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/att/AttProjectUserRel/importData'
});
const createTime = ref(null);
const data = reactive({
  form: {
    userIdList: [],
    userNameList: [],
    roleIdList: []
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    userId: null,
    createTime: null,
    endTime: null,
    startTime: null
  },
  queryParamsUser: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    userName: undefined,
    phoneNumber: undefined,
    status: undefined,
    deptId: undefined
  },
  rules: {
    userNameList: [{ required: true, message: '请选择用户', trigger: 'change' }],
    roleIdList: [{ required: true, message: '请选择用户角色', trigger: 'change' }]
  }
});

const { queryParams, queryParamsUser, form, rules } = toRefs(data);
let addUserAdnProject = ref(false);
// 监听 userStore 中的 projectId 变化
watch(
  () => userStore.projectId,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      console.log(userStore.projectCode, 'userStore.projectCode');

      queryParams.value.projectId = newValue;
      queryParamsUser.value.projectId = newValue;
      getList();
    }
  },
  { immediate: true }
);
function handleDateChange(value) {
  queryParams.value.startTime = value[0];
  queryParams.value.endTime = value[1];
}
/** 查询项目与用户关联关系列表 */
function getList() {
  loading.value = true;
  if (queryParams.value.projectId) {
    listAttProjectUserRel(queryParams.value).then((response) => {
      AttProjectUserRelList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
    addUserAndProject(queryParams.value.projectId).then((response) => {
      addUserAdnProject.value = response.data;
    });
  }
}

function getListUser() {
  loadingUser.value = true;
  noProjectUser(queryParamsUser.value).then((response) => {
    userList.value = response.rows;
    openTwo.value = true;
    totalUser.value = response.total;
    loadingUser.value = false;
    console.log(userList.value, 'userList');

    // 在表格加载完成后，设置之前选中的用户
    nextTick(() => {
      userList.value.forEach((user) => {
        if (form.value.userIdList.includes(user.userId)) {
          proxy.$refs.userTableRef.toggleRowSelection(user, true);
        }
      });
    });
  });
}
/** 搜索按钮操作 */
function handleQueryUser() {
  queryParamsUser.value.pageNum = 1;
  getListUser();
}
/** 重置按钮操作 */
function resetQueryUser() {
  queryParamsUser.value = {
    pageNum: 1,
    pageSize: 10,
    projectId: userStore.projectId,
    userName: undefined,
    phoneNumber: undefined,
    status: undefined,
    deptId: undefined
  };
  handleQueryUser();
}
/** 提交按钮操作 */
function submitFormUser() {
  form.value.userIdList = idsUser.value;
  form.value.userNameList = userName.value;
  openTwo.value = false;
}
// 多选框选中数据
function handleSelectionChangeUser(selection) {
  idsUser.value = selection.map((item) => item.userId);
  userName.value = selection.map((item) => item.nickName);
}
function getRoleList() {
  if (queryParams.value.projectId) {
    listRole(queryParams.value).then((response) => {
      roleList.value = response.rows;
      console.log(roleList.value, 'roleList');
    });
  }
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
    projectId: null,
    userId: null,
    userIdList: [],
    userName: null,
    userNameList: [],
    roleIdList: [],
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
  proxy.resetForm('AttProjectUserRelRef');
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  createTime.value = null;
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    projectId: userStore.projectId,
    userId: null,
    createTime: null,
    endTime: null,
    startTime: null
  };
  // proxy.resetForm('queryRef');
  handleQuery();
}

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

/** 新增按钮操作 */
function handleAdd() {
  getRoleList();
  reset();
  open.value = true;
  title.value = '新增项目成员';
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value;
  getRoleList();
  getRoleUser(_id).then((response) => {
    form.value = response.data;
    console.log(form.value, 'form');
    open.value = true;
    title.value = '修改项目成员';
  });
}

/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getAttProjectUserRel(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = '项目与用户关联关系详情';
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['AttProjectUserRelRef'].validate((valid) => {
    if (valid) {
      if (form.value.id != null) {
        editUserListAndRoleList(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess('修改成功');
            open.value = false;
            getList();
          })
          .catch((error) => { });
      } else {
        // 新增时增加额外验证
        if (!form.value.userIdList || form.value.userIdList.length === 0) {
          proxy.$modal.msgWarning('未选择用户，请选择用户后重试');
          return;
        }
        form.value.projectId = userStore.projectId;
        addUserListAndRoleList(form.value)
          .then((response) => {
            proxy.$modal.msgSuccess('新增成功');
            open.value = false;
            getList();
          })
          .catch((error) => { });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const _userId =
    row.userId ||
    AttProjectUserRelList.value
      .filter((item) => ids.value.includes(item.id))
      .map((item) => item.userId);
  proxy.$modal
    .confirm('是否确认移除编号为"' + _userId + '"的数据项？')
    .then(function () {
      return delAttProjectUserRel(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('移除成功');
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'att/AttProjectUserRel/export',
    {
      ...queryParams.value
    },
    `AttProjectUserRel_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = '项目与用户关联关系导入';
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    'system/user/importTemplate',
    {},
    `AttProjectUserRel_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs['uploadRef'].submit();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs['uploadRef'].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
    response.msg +
    '</div>',
    '导入结果',
    { dangerouslyUseHTMLString: true }
  );
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== '' && link.indexOf('http') !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== '') {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id
        }
      });
    }
  }
}
</script>
<style lang="scss" scoped>
.hint-div {
  margin: 0px 0px 20px 12px;
  /* border-top: 1px solid rgba(204, 204, 204, 0.5); */
  border-right: 1px solid rgba(204, 204, 204, 0.5);
  border-bottom: 1px solid #e5f1f8;
  border-left: 1px solid #e5f1f8;
  border-radius: 8px;
  background-color: #ecf5ff;
  padding: 10px;
  box-shadow: -1px 1px 2px #e5f1f8;
  display: flex;
  align-items: center;

  span {
    margin-left: 5px;
  }
}
</style>
<style scoped lang="scss">
.app-container {
  .pagecont-bottom {
    min-height: calc(100vh - 240px);
  }
}
</style>
<style lang="scss">
.warn-dialog-23012 {
  .el-dialog__body {
    overflow: auto;
    height: 500px !important;
    padding: 20px 40px !important;
  }
}

.user-select-tatble {
  .el-dialog__body {
    height: 600px !important;
  }
}

.checkbox-vertical {
  display: flex;
  flex-direction: column;
  /* 竖排 */
  margin-top: 8px;
}

.checkbox-vertical .el-checkbox {
  display: block;
  margin-bottom: 0px;
  height: 15px !important;
}
</style>
