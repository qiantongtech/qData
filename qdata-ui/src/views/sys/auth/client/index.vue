<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
        <el-form-item label="应用ID" prop="id">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.id"
              placeholder="请输入应用ID"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="应用名称" prop="name">
          <el-input
              class="el-form-input-width"
              v-model="queryParams.name"
              placeholder="请输入应用名称"
              clearable
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
          </el-button>
          <el-button @click="resetQuery" @mousedown="e => e.preventDefault()">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div  class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="10" class="btn-style">
          <el-col :span="1.5">
            <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['auth:client:add']"
            >新增</el-button>
          </el-col>
        </el-row>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </div>

      <el-table stripe height="60vh"  v-loading="loading" :data="clientList" @selection-change="handleSelectionChange">
        <el-table-column label="应用ID" align="center" prop="id" />
        <el-table-column label="应用秘钥" align="center" prop="secretKey"  width="300"/>
        <el-table-column label="应用名称" align="center" prop="name" width="120"/>
        <el-table-column label="应用类型" align="center" prop="type">
          <template #default="scope">
            <dict-tag :options="auth_app_type" :value="scope.row.type"/>
          </template>
        </el-table-column>
        <el-table-column label="应用图标" align="center" prop="icon" width="100">
          <template #default="scope">
            <image-preview :src="scope.row.icon" :width="50" :height="50"/>
          </template>
        </el-table-column>
        <el-table-column label="应用首页" align="center" prop="homeUrl" >
        <template #default="scope">
            <span>{{ scope.row.homeUrl || "-" }}</span>
          </template>
        </el-table-column>
  <!--      <el-table-column label="同步地址" align="center" prop="syncUrl" />-->
        <el-table-column label="允许授权的url" align="center" prop="redirectUrl" width="150"/>
        <el-table-column label="是否公开" align="center" prop="publicFlag">
          <template #default="scope">
            <dict-tag :options="auth_public" :value="scope.row.publicFlag"/>
          </template>
        </el-table-column>
        <el-table-column label="是否有效" align="center" prop="validFlag">
          <template #default="scope">
            <dict-tag :options="sys_valid" :value="scope.row.validFlag"/>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark">
        <template #default="scope">
            <span>{{ scope.row.remark || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['auth:client:edit']">修改</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['auth:client:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>

    <!-- 添加或修改应用管理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']"  draggable destroy-on-close>
      <el-form ref="clientRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用首页" prop="homeUrl">
              <el-input v-model="form.homeUrl" placeholder="请输入应用首页" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同步地址" prop="syncUrl">
              <el-input v-model="form.syncUrl" placeholder="请输入同步地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入应用名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择应用类型">
                <el-option
                    v-for="dict in auth_app_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否公开" prop="publicFlag">
              <el-radio-group v-model="form.publicFlag">
                <el-radio
                    v-for="dict in auth_public"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否有效" prop="validFlag">
              <el-radio-group v-model="form.validFlag">
                <el-radio
                    v-for="dict in sys_valid"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="应用图标" prop="icon">
              <div class="xgtpcont">
                  <ImageUpload class="sctplist" v-model="form.icon">
                  </ImageUpload>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="允许授权的url" prop="redirectUrl">
              <el-input v-model="form.redirectUrl" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Client">
import { listClient, getClient, delClient, addClient, updateClient } from "@/api/system/auth/client.js";

const { proxy } = getCurrentInstance();
const { auth_app_type } = proxy.useDict('auth_app_type');
const { sys_valid } = proxy.useDict('sys_valid');
const { auth_public } = proxy.useDict('auth_public');

const clientList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {
  },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    id: null,
    secretKey: null,
    name: null,
    type: null,
    icon: null,
    homeUrl: null,
    syncUrl: null,
    redirectUrl: null,
    publicFlag: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  },
  rules: {
    secretKey: [
      { required: true, message: "应用秘钥不能为空", trigger: "blur" }
    ],
    name: [
      { required: true, message: "应用名称不能为空", trigger: "blur" }
    ],
    type: [
      { required: true, message: "应用类型", trigger: "change" }
    ],
    redirectUrl: [
      { required: true, message: "允许授权的url不能为空", trigger: "blur" }
    ],
    publicFlag: [
      { required: true, message: "是否公开不能为空", trigger: "blur" }
    ],
    validFlag: [
      { required: true, message: "是否有效", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询应用管理列表 */
function getList() {
  loading.value = true;
  listClient(queryParams.value).then(response => {
    clientList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    secretKey: null,
    name: null,
    type: null,
    icon: null,
    homeUrl: null,
    syncUrl: null,
    redirectUrl: null,
    publicFlag: '1',
    validFlag: '1',
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null
  };
  proxy.resetForm("clientRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增应用管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getClient(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改应用管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["clientRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateClient(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addClient(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除应用管理编号为"' + _ids + '"的数据项？').then(function() {
    return delClient(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('auth/client/export', {
    ...queryParams.value
  }, `client_${new Date().getTime()}.xlsx`)
}

getList();
</script>

<style scoped lang="scss">

</style>
