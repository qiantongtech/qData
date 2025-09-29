<template>
  <div class="app-container onlDesform" ref="app-container">

    <GuideTip tip-id="dpp/onlDesform.list" />

    <div class="pagecont-top" v-show="showSearch">
      <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
        v-show="showSearch" @submit.prevent>
        <el-form-item label="表单名称" prop="desformName">
          <el-input class="el-form-input-width" v-model="queryParams.desformName" placeholder="请输入表单名称" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="表单编码" prop="desformCode">
          <el-input class="el-form-input-width" v-model="queryParams.desformCode" placeholder="请输入表单编码" clearable
            @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()"> <i
              class="iconfont-mini icon-a-zu22377 mr5"></i>查询 </el-button>
          <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()"> <i
              class="iconfont-mini icon-a-zu22378 mr5"></i>重置 </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom">
      <div class="justify-between mb15">
        <el-row :gutter="15" class="btn-style">
          <el-col :span="1.5">
            <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dpp:OnlDesform:desform:add']"
              @mousedown="(e) => e.preventDefault()">
              <i class="iconfont-mini icon-xinzeng mr5"></i>新增
            </el-button>
          </el-col>
        </el-row>
        <div class="justify-end top-right-btn">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </div>
      </div>
      <el-table stripe v-loading="loading" :data="desformList" :default-sort="defaultSort"
        @sort-change="handleSortChange">
        <el-table-column type="index" v-if="getColumnVisibility(0)" label="编号" width="75" align="left" />
        <el-table-column v-if="getColumnVisibility(1)" label="表单名称" align="left" prop="desformName">
          <template #default="scope">
            {{ scope.row.desformName || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(2)" label="表单编码" align="left" prop="desformCode">
          <template #default="scope">
            {{ scope.row.desformCode || "-" }}
          </template>
        </el-table-column>
        <!-- <el-table-column v-if="getColumnVisibility(2)" label="描述" :show-overflow-tooltip="true" align="left"
          prop="description" width="240">
          <template #default="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column> -->
        <el-table-column v-if="getColumnVisibility(9)" label="更新时间" align="left" prop="updateTime">
          <template #default="scope">
            <span>{{
              parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(10)" label="创建人" align="left" prop="createBy">
          <template #default="scope">
            {{ scope.row.createBy || "-" }}
          </template>
        </el-table-column>
        <el-table-column v-if="getColumnVisibility(11)" label="创建时间" align="left" prop="createTime" sortable="custom"
          :sort-orders="['descending', 'ascending']">
          <template #default="scope">
            <span>{{
              parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
          <template #default="scope">
            <!-- <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['dpp:OnlDesform:desform:edit']">修改 </el-button> -->
            <el-button link type="primary" icon="Setting" @click="handleDesignForm(scope.row)"
              v-hasPermi="['dpp:OnlDesform:desform:sjbd']">设计表单 </el-button>
            <!-- <el-popover placement="bottom" :width="150" trigger="click">
              <template #reference>
                <el-button link type="primary" icon="ArrowDown"
                >更多</el-button
                >
              </template>
  <div style="width: 100px" class="butgdlist">
    <el-button link style="padding-left: 14px" type="primary" icon="View" @click="formDataList(scope.row)"
      v-hasPermi="['dpp:OnlDesform:desform:bdsj']">表单数据 </el-button>
    <el-button link type="primary" icon="Connection" @click="handlePathConfig(scope.row)"
      v-hasPermi="['dpp:OnlDesform:desform:pzdz']">配置地址 </el-button>
    <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
      v-hasPermi="['dpp:OnlDesform:desform:remove']">删除 </el-button>
  </div>
  </el-popover> -->
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

    <!-- 新增，修改，设计表单，表单数据，配置地址 -->
    <Add ref="addRef" @ok="ok" />
    <Edit ref="editRef" @ok="ok" />
    <PathConfig ref="pathConfigRef" @addDataId="addDataId" />
    <AddDataId ref="addDataIdRef" @ok="handleAddDataId" />
  </div>
</template>

<script setup name="OnlDesform">
import Add from "./components/add";
import Edit from "./components/edit";
import PathConfig from "./components/pathConfig";
import AddDataId from "./components/addDataId";
import DesignForm from "./components/designForm";
import { listdesform, deldesform } from "@/api/dpp/onlDesform/desForm.js";

const addRef = ref();
const editRef = ref();
const pathConfigRef = ref();
const addDataIdRef = ref();
const designFormRef = ref();
const { proxy } = getCurrentInstance();
const desformList = ref([]);

// 列显隐信息
const columns = ref([
  { key: 0, label: "序号", visible: true },
  { key: 1, label: "表单名称", visible: true },
  { key: 2, label: "表单编码", visible: true },
  { key: 9, label: "更新时间", visible: true },
  { key: 10, label: "创建人", visible: true },
  { key: 11, label: "创建时间", visible: true },
]);
/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const router = useRouter();

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    desformName: null,
    desformCode: null,
    desformJson: null,
    saveTableFlag: null,
    datasourceId: null,
    databaseName: null,
    tableName: null,
    columnName: null,
    pkColumnName: null,
    createPkDataFlag: null,
    createTime: null,
  },
});

const { queryParams } = toRefs(data);

/** 查询在线单设计器列表 */
function getList() {
  loading.value = true;
  listdesform(queryParams.value).then((response) => {
    desformList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  });
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

/** 新增按钮操作 */
function handleAdd() {
  addRef.value.add();
}

/** 修改按钮操作 */
function handleUpdate(row) {
  editRef.value.edit(row);
}
//新增/修改ok
function ok() {
  queryParams.value.pageNum = 1;
  queryParams.value.pageSize = 20;
  handleQuery();
}
// 设计表单
function handleDesignForm(row) {
  router.push({
    path: `/dpp/onlDesform/edit`,
    query: row,
  });
  // designFormRef.value.design(row);
}
//表单数据列表跳转
function formDataList(row) {
  router.push(`/dpp/onlDesform/data/list/${row.desformCode}`);
}
// 配置地址
function handlePathConfig(row) {
  pathConfigRef.value.show(row);
}
// 确认弹框
function addDataId() {
  addDataIdRef.value.show();
}
function handleAddDataId(dataId) {
  pathConfigRef.value.addDataId(dataId);
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认删除数据填报编号为"' + row.id + '"的数据项？')
    .then(function () {
      return deldesform(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

getList();
</script>
