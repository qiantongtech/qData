<template>
  <div class="app-container">
    <guide-tip tip-id="meta/unreleased/structured/database" />
    <qt-wrap :columns="tableStroe.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStroe.params"
          :tableRef="tableRef"
          :config="{ permi: ['md:unreleased:structured:db:query'] }"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAddClick"
          v-hasPermi="['md:unreleased:structured:db:add']"
        >
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDeleteColumnClick"
          v-hasPermi="['md:unreleased:structured:db:remove']"
        >
          删除
        </el-button>
      </template>
      <qt-table v-bind="tableStroe" ref="tableRef">
        <template #domain-name="scope">
          {{ getDomainPath(scope.row.domainId) }}
        </template>

        <template #status="scope">
          <el-switch
            v-if="scope.row.status != undefined"
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row, $event)"
          />
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="view"
            @click="handleDetailClick(row)"
            v-hasPermi="['md:unreleased:structured:db:detail']"
          >
            详情
          </el-button>
          <el-button
            link
            type="primary"
            icon="Edit"
            :disabled="row.status == 1"
            @click="handleEditClick(row)"
            v-hasPermi="['md:unreleased:structured:db:edit']"
          >
            修改
          </el-button>
          <el-popover
            placement="bottom"
            :width="107"
            popper-class="handle-popover"
            trigger="click"
          >
            <template #reference>
              <el-button
                link
                type="primary"
                icon="ArrowDown"
                v-hasPermi="[
                  'md:unreleased:structured:db:remove',
                  'md:unreleased:structured:db:edit',
                ]"
              >
                更多
              </el-button>
            </template>
            <el-button
              link
              type="danger"
              icon="Delete"
              :disabled="row.status == 1"
              @click="handleDeleteClick(row)"
              v-hasPermi="['md:unreleased:structured:db:remove']"
            >
              删除
            </el-button>
          </el-popover>
        </template>
      </qt-table>
    </qt-wrap>

    <!-- 新增/修改弹窗 -->
    <el-dialog
      v-model="dialog.open"
      :title="dialog.title"
      width="1200"
      draggable
    >
      <el-form
        :model="dialog.form"
        :rules="rules"
        ref="formRef"
        class="column-form"
        label-width="110px"
      >
        <el-form-item label="业务域" prop="domainId">
          <el-tree-select
            filterable
            v-model="dialog.form.domainId"
            :data="store.treeDomains"
            :props="{ value: 'id', label: 'name', children: 'children' }"
            value-key="id"
            placeholder="请选择业务域"
            check-strictly
            @change="handleDomainChange"
            default-expand-all
            clearable
          />
        </el-form-item>
        <el-form-item label="数据连接名称" prop="datasourceId">
          <el-select
            clearable
            v-model="dialog.form.datasourceId"
            placeholder="请选择数据连接名称"
            @change="handleDatasourceChange"
          >
            <el-option
              v-for="item in store.datasources"
              :key="item.id"
              :label="item.datasourceName"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="数据库类型" prop="dbType">
          <el-input
            v-model="dialog.form.dbType"
            disabled
            placeholder="请输入数据库类型"
          />
        </el-form-item>

        <el-form-item label="库名" prop="dbName">
          <el-select
            clearable
            v-model="dialog.form.dbName"
            placeholder="请选择库名"
          >
            <el-option
              v-for="(item, index) in store.databases"
              :key="item.dbName + '_' + index"
              :label="item.dbName"
              :value="item.dbName"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="IP" prop="ip">
          <el-input v-model="dialog.form.ip" disabled placeholder="请输入ip" />
        </el-form-item>

        <el-form-item label="端口号" prop="port">
          <el-input
            v-model="dialog.form.port"
            disabled
            placeholder="请输入端口号"
          />
        </el-form-item>

        <el-form-item label="账号" prop="username">
          <el-input
            v-model="dialog.form.username"
            disabled
            placeholder="请输入账号"
          />
        </el-form-item>

        <!-- <el-form-item label="安全等级">
          <el-select
            clearable
            v-model="dialog.form.safetyLevelId"
            placeholder="请选择安全等级"
          >
            <el-option
              v-for="item in store.sensitiveLevels"
              :key="item.id"
              :label="item.sensitiveLevel"
              :value="item.id"
            />
          </el-select>
        </el-form-item> -->

        <el-form-item label="所属分层">
          <el-select
            clearable
            v-model="dialog.form.belongingLayer"
            placeholder="请选择所属分层"
          >
            <el-option
              v-for="dict in toValue(dicts.meta_dw_layers)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="所属系统">
          <el-input
            clearable
            v-model="dialog.form.belongingSystem"
            placeholder="请输入所属系统"
            maxlength="50"
          />
        </el-form-item>

        <el-form-item label="技术负责人">
          <el-tree-select
            clearable
            filterable
            v-model="dialog.form.techLeader"
            :data="store.userList"
            :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
            value-key="userId"
            placeholder="请选择技术负责人"
            check-strictly
            @change="handleUserChange($event, 'techLeaderPhone')"
          />
        </el-form-item>

        <el-form-item label="技术负责人电话">
          <el-input
            clearable
            v-model="dialog.form.techLeaderPhone"
            placeholder="请输入技术负责人电话"
          />
        </el-form-item>

        <el-form-item label="业务负责人">
          <el-tree-select
            clearable
            filterable
            v-model="dialog.form.businessLeader"
            :data="store.userList"
            :props="{
              value: 'userId',
              label: 'nickName',
              children: 'children',
            }"
            value-key="userId"
            placeholder="请选择业务负责人"
            check-strictly
            @change="handleUserChange($event, 'businessLeaderPhone')"
          />
        </el-form-item>

        <el-form-item label="业务负责人电话">
          <el-input
            clearable
            v-model="dialog.form.businessLeaderPhone"
            placeholder="请输入业务负责人电话"
          />
        </el-form-item>

        <el-form-item label="状态" class="row-full">
          <el-radio-group v-model="dialog.form.status">
            <el-radio
              v-for="dict in toValue(dicts.meta_task_status)"
              :key="dict.value"
              :label="dict.value"
            >
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="描述" class="row-full">
          <el-input
            v-model="dialog.form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>

        <el-form-item label="备注" class="row-full">
          <el-input
            v-model="dialog.form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>

        <el-form-item
          label="变更说明"
          class="row-full"
          prop="updateMsg"
          v-if="dialog.form.id"
        >
          <el-input
            v-model="dialog.form.updateMsg"
            type="textarea"
            placeholder="请输入变更说明"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancelClick">取消</el-button>
          <el-button type="primary" @click="handleConfirmClick">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="UnreleasedStructuredDatabase">
import { reactive, ref, getCurrentInstance, toValue } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import {
  listDb,
  addDb,
  updateDb,
  getDb,
  updateDbStatus,
  delDb,
  batchDeleteCheck,
} from "@/api/mc/unreleased/db.js";
import { getParentLabelPath } from "@/utils/anivia.js";
import { listDaDatasource } from "@/api/mc/dataSource/dataSource";
import { deptUserTree } from "@/api/system/system/user.js";
import { useRoute, useRouter } from "vue-router";
import { listDgSensitiveLevel } from "@/api/dg/compliance/sensitiveLevel";
import { getRealtimeMcTaskScopeList } from "@/api/mc/task/task.js";

// 表单验证规则
const rules = {
  domainId: [{ required: true, message: "请选择业务域", trigger: "change" }],
  datasourceId: [
    { required: true, message: "请选择数据连接名称", trigger: "change" },
  ],
  dbName: [
    { required: true, message: "请选择库名", trigger: ["blur", "change"] },
  ],
  dbType: [
    {
      required: true,
      message: "请输入数据库类型",
      trigger: ["blur", "change"],
    },
  ],
  ip: [
    { required: true, message: "请输入数据库ip", trigger: ["blur", "change"] },
  ],
  port: [
    {
      required: true,
      message: "请输入数据库端口",
      trigger: ["blur", "change"],
    },
  ],
  username: [
    {
      required: true,
      message: "请输入数据库用户名",
      trigger: ["blur", "change"],
    },
  ],
  updateMsg: [
    { required: true, message: "请输入变更说明", trigger: ["change", "blur"] },
  ],
};

const DEFAULT_FORM = {
  status: "0",
};

const { proxy } = getCurrentInstance();
const dicts = proxy.useDict(
  "datasource_type",
  "meta_task_status",
  "meta_dw_layers"
);

const router = useRouter();
const route = useRoute();

const formRef = ref();
const store = reactive({
  rows: [],
  treeDomains: [],
});

// 列表
const tableRef = ref(null);
const tableStroe = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "createTime", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetailClick,
    },
  },
  columns: [
    {
      type: "selection",
      width: 55,
      // selectable: function (row) {
      //     return row.status === '0' ? true : false;
      // }
    },
    {
      label: "编号",
      prop: "id",
      sortable: true,
      width: 60,
    },
    {
      label: "库名",
      prop: "dbName",
      showOverflowTooltip: {
        effect: "light",
      },
      minWidth: 240,
      align: "left",
      link: {
        external: handleDetailClick,
      },
    },
    {
      label: "描述",
      prop: "description",
      align: "left",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "业务域",
      prop: "domainId",
      slot: "domain-name",
      width: 240,
      showOverflowTooltip: {
        effect: "light",
      },
    },
    {
      label: "数据库类型",
      prop: "dbType",
      dict: "datasource_type",
      width: 90,
    },
    {
      label: "数据质量",
      prop: "dataQuality",
      width: 90,
      sortable: true,
    },
    {
      label: "表数量",
      prop: "tableCount",
      sortable: true,
      width: 90,
    },
    {
      label: "版本号",
      prop: "version",
      width: 90,
    },
    {
      label: "状态",
      prop: "status",
      width: 90,
      slot: "status",
    },

    {
      label: "更新人",
      prop: "updateBy",
      width: 120,
    },
    {
      label: "更新时间",
      prop: "updateTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "创建人",
      prop: "createBy",
      width: 120,
    },
    {
      label: "创建时间",
      prop: "createTime",
      sortable: true,
      width: 160,
      date: true,
    },
    {
      label: "操作",
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDb,
  params: {
    dataType: 1,
  },
  events: {
    formatData: function (data) {
      data.forEach((item) => {
        item.version = proxy.formatVersion(item.version);
      });
      return data;
    },
  },
});

// 搜索项
const searchStore = reactive({
  items: [
    {
      label: "库名",
      prop: "dbName",
      component: {
        is: "input",
      },
    },
    {
      label: "数据库类型",
      prop: "dbType",
      component: {
        is: "select",
        options: dicts.datasource_type,
      },
    },
    {
      label: "业务域",
      prop: "domainCode",
      component: {
        is: "tree-select",
        filterable: true,
        data: store.treeDomains,
        props: { value: "code", label: "name", children: "children" },
        valueKey: "id",
        checkStrictly: true,
        defaultExpandAll: true,
      },
    },
  ],
});

// 新增/修改弹窗
const dialog = reactive({
  open: false,
  title: "",
  form: { ...DEFAULT_FORM },
});

// 获取业务域路径
const getDomainPath = computed(() => {
  return function (id) {
    let domainName = getParentLabelPath(store.treeDomains, id, {
      idKey: "id",
      labelKey: "name",
      childrenKey: "children",
    });
    const idx = domainName.indexOf("/");
    return idx == -1 ? domainName : domainName.slice(idx + 1);
  };
});

// 获取用户列表
function getUserList() {
  deptUserTree().then((res) => {
    store.userList = res.data;
  });
}

// 获取业务域列表
function getDomains() {
  listDomain().then((res) => {
    store.domains = [...res.data];
    store.treeDomains.splice(0, store.treeDomains.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.treeDomains.push(domains);
  });
}

// 获取数据源列表
function getDatasources() {
  listDaDatasource().then((res) => {
    res.data.rows.forEach((item) => {
      item.datasourceConfig = item.datasourceConfig
        ? JSON.parse(item.datasourceConfig)
        : {};
    });
    store.datasources = res.data.rows;
  });
}

// 获取安全等级
function getSensitiveLevel() {
  listDgSensitiveLevel({ pageSize: 1000 }).then((res) => {
    store.sensitiveLevels = res.data.rows;
  });
}

// 切换数据源
function handleDatasourceChange(id) {
  const data = store.datasources.find((item) => item.id === id);
  dialog.form.ip = data.ip;
  dialog.form.port = data.port;
  dialog.form.username = data.datasourceConfig?.username;
  dialog.form.dbType = data.datasourceType;
  getRealtimeMcTaskScopeList(id).then((res) => {
    store.databases = res.data;
    dialog.form.dbName = "";
  });
}

// 切换用户
function handleUserChange(id, key) {
  const data = store.userList.find((item) => item.userId === id);
  dialog.form[key] = data.phonenumber;
}

// 切换业务域
function handleDomainChange(id) {
  const data = store.domains.find((item) => item.id === id);
  dialog.form.domainCode = data.code;
}

// 取消新增/修改
function handleCancelClick() {
  formRef.value.resetFields();
  dialog.form = {
    ...DEFAULT_FORM,
  };
  dialog.loading = false;
  dialog.open = false;
}

// 确认新增/修改
async function handleConfirmClick() {
  dialog.loading = true;
  const valid = await formRef.value.validate();
  dialog.loading = false;
  if (!valid) return;
  dialog.loading = true;
  if (dialog.form.safetyLevelId == undefined) {
    dialog.form.safetyLevelId = null;
    dialog.form.safetyLevelName = null;
  }
  await dialog.func(dialog.form);
  dialog.loading = false;
  proxy.$modal.msgSuccess(`${dialog.form.id ? "修改" : "新增"}库元数据成功！`);
  handleCancelClick();
  tableRef.value.getList();
}

// 点击新增
function handleAddClick() {
  dialog.title = "新增库元数据";
  dialog.open = true;
  dialog.func = addDb;
}

// 打开修改弹窗
function handleEditClick(row) {
  dialog.open = true;
  dialog.func = updateDb;
  dialog.title = "修改任务";
  getDb(row.id).then((res) => {
    const {
      createBy,
      createTime,
      delFlag,
      updateBy,
      updateTime,
      updaterId,
      validFlag,
      auditTime,
      ...form
    } = res.data;
    dialog.form = form;
    handleDatasourceChange(res.data.datasourceId);
  });
}

// 切换状态
function handleStatusChange(row, status) {
  ElMessageBox.confirm(
    `是否确认${status == 1 ? "发布" : "取消发布"}数据编号为${
      row.id
    }的库元数据吗？`,
    "系统提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(() => {
      return updateDbStatus({
        id: row.id,
        status,
      });
    })
    .then(() => {
      ElMessage.success(
        `编号为${row.id}的库元数据${status == 1 ? "发布" : "取消发布"}成功!`
      );
      row.status = status;
    })
    .catch(() => {
      row.status = status == "1" ? "0" : "1";
    });
}

// 删除选中行
function handleDeleteColumnClick() {
  if (!store.rows.length) return;
  const ids = store.rows.map((item) => item.id);
  store.loading = true;
  batchDeleteCheck(ids).then((res) => {
    const { canDeleteCount, cannotDeleteCount, canDeleteIds } = res.data;
    store.loading = false;
    ElMessageBox.confirm(
      `可删除${canDeleteCount}个，不可删除${cannotDeleteCount}个，是否删除可删部分`,
      "系统提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    )
      .then(() => {
        if (!canDeleteIds.length) {
          ElMessage.success("删除成功");
          return;
        }
        return delDb(canDeleteIds.toString());
      })
      .then((res) => {
        if (!res) return;
        ElMessage.success("删除成功");
        tableRef.value.getList();
      });
  });
}

// 点击详情
function handleDetailClick(row) {
  router.push({
    path: route.path + "/detail",
    query: {
      id: row.id,
      table_status: 1,
    },
  });
}

// 删除
function handleDeleteClick(row) {
  ElMessageBox.confirm(`是否确认删除编号为${row.id}的数据项？`, "系统提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      return delDb(row.id);
    })
    .then(() => {
      ElMessage.success("删除成功");
      tableRef.value.getList();
    });
}

getUserList();
// getDomains();
// getSensitiveLevel();
getDatasources();
</script>
