<template>
  <div class="app-container" ref="app-container" v-loading="loading">
    <template v-if="hasForm">
      <div class="pagecont-bottom desformData">
        <div class="justify-between mb15">
          <el-row :gutter="15" class="btn-style">
            <el-col :span="1.5">
              <el-button type="primary" plain @click="handleAdd" v-hasPermi="['dpp:OnlDesformData:data:add']"
                @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-xinzeng mr5"></i>新增
              </el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="warning" plain @click="handleExport" v-hasPermi="['dpp:OnlDesformData:data:export']"
                @mousedown="(e) => e.preventDefault()">
                <i class="iconfont-mini icon-download-line mr5"></i>导出
              </el-button>
            </el-col>
          </el-row>
          <div class="justify-end top-right-btn">
            <right-toolbar :search="false" @queryTable="getList" :columns="columns"></right-toolbar>
          </div>
        </div>
        <el-table stripe :data="dataList">
          <el-table-column type="index" v-if="getColumnVisibility(-1)" label="序号" width="75" align="left" />
          <template v-for="(item, index) in columns">
            <el-table-column v-if="item.visible" :key="index" :prop="item.prop" :label="item.label"
              :formatter="item.formatter" :width="item.width != null ? item.width : null" :align="'center'"
              :show-overflow-tooltip="noOverflow.indexOf(item.config.type) == -1 ? { effect: 'light' } : false">
              <template #default="scope">
                <!--多选、下拉选、单选等-->
                <span v-if="types.optionsTypes.indexOf(item.config.type) > -1">
                  {{ computedOptions(scope.row[item.prop], item.config.options) }}
                </span>

                <!--级联-->
                <span v-else-if="types.cascaderTypes.indexOf(item.config.type) > -1">
                  {{ computedCascaderOptions(scope.row[item.prop], item.config.options) }}
                </span>

                <!--颜色选择器-->
                <span v-else-if="item.config.type === 'color'">
                  <el-tooltip class="item" effect="dark" :content="scope.row[item.prop]" placement="top">
                    <div class="j-table-slot-view-color" :style="{ 'background-color': scope.row[item.prop] }"></div>
                  </el-tooltip>
                </span>

                <!--滑块-->
                <span v-else-if="item.config.type === 'slider'">
                  <el-slider v-model="scope.row[item.prop]" disabled></el-slider>
                </span>

                <!--评分-->
                <span v-else-if="item.config.type === 'rate'">
                  <el-rate v-model="scope.row[item.prop]" disabled></el-rate>
                </span>
                <span v-else-if="item.config.type === 'datePicker'">
                  {{ parseTime(scope.row[item.prop], "{y}-{m}-{d}") }}
                </span>
                <span v-else>
                  {{ scope.row[item.prop] }}
                </span>
              </template>
            </el-table-column>
          </template>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
            <template #default="scope">
              <el-button link type="primary" icon="view" @click="handleDetail(scope.row, columns)"
                v-hasPermi="['dpp:OnlDesformData:data:view']">查看</el-button>
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row, columns)"
                v-hasPermi="['dpp:OnlDesformData:data:edit']">修改</el-button>
              <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['dpp:OnlDesformData:data:remove']">删除</el-button>
            </template>
          </el-table-column>
          <template #empty>
            <div class="emptyBg">
              <img src="../../../../../assets/system/images/no_data/noData.png" alt="" />
              <p>暂无记录</p>
            </div>
          </template>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize" @pagination="getList" />
      </div>
    </template>
    <div style="width: 100%; text-align: center; background: #fff" class="emptyBg" v-else>
      <img src="../../../../../assets/system/images/no_data/noData.png" alt="" />
      <div>你还没有设计任何表单项，不能查看表单数据！</div>
    </div>
    <add v-if="formConfig" ref="addRef" @ok="ok" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
    <edit v-if="formConfig" ref="editRef" @ok="ok" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
    <dialog-details v-if="formConfig" ref="detailsRef" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
  </div>
</template>

<script setup name="desformData">
import Add from "./components/add.vue";
import Edit from "./components/edit.vue";
import DialogDetails from "./components/details.vue";
// 接口
import { listdata, deldata } from "@/api/dpp/onlDesformData/desformData.js";
import { getByDesformCode } from "@/api/dpp/onlDesform/desForm.js";

const { proxy } = getCurrentInstance();
const router = useRouter();

const dataList = ref([]);

// 列显隐信息
const columns = ref([]);
const noOverflow = ref(["rate", "slider", "color"]);
const types = ref({
  //transfer {key: 1, label: "选项1"},
  // cascader-panel 级联面板
  optionsTypes: ["select", "checkbox", "radio", "radio-button", "checkbox-button", "transfer"],
  cascaderTypes: ["cascader", "cascader-panel"],
});

const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};

const loading = ref(true);
const hasForm = ref(false);
const ids = ref([]);
const total = ref(0);
// 表单名称
const desformName = ref();
const formConfig = ref();

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    desformCode: null,
    desformName: null,
    desformId: null,
    desformData: null,
    createTime: null,
  },
});

const { queryParams } = toRefs(data);

function ok() {
  queryParams.value.pageNum = 1;
  queryParams.value.pageSize = 20;
  getList();
}

/** 查询在线单数据列表 */
function getList() {
  loading.value = true;
  listdata(queryParams.value)
    .then((response) => {
      let list = [];
      response.data.rows.forEach((e) => {
        let obj = { id: e.id };
        let json = JSON.parse(e.desformData);
        for (let key in json) {
          obj[key] = json[key];
        }
        list.push(obj);
      });
      dataList.value = list;
      total.value = response.data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

//级联
function computedCascaderOptions(arrVal, options) {
  if (!arrVal) {
    return "";
  }
  let optionArr = JSON.parse(JSON.stringify(options));
  let result = "";
  arrVal.forEach((val) => {
    for (let i = 0; i < optionArr.length; i++) {
      let e = optionArr[i];
      if (val == e.value) {
        result += e.label + ",";
        if (e.children && e.children.length > 0) {
          optionArr = e.children;
          break;
        }
      }
    }
  });
  if (result.length > 0) {
    result = result.substr(0, result.length - 1);
  }
  return result;
}
//单选多选
function computedOptions(val, options) {
  if (!val) {
    return "";
  }
  let result = "";
  if (val instanceof Array) {
    val.forEach((e) => {
      result += getText(e, options) + ",";
    });
    if (result.length > 0) {
      result = result.substr(0, result.length - 1);
    }
  } else {
    result = getText(val, options);
  }
  return result;
}
function getText(val, options) {
  let result = "";
  options.forEach((e) => {
    if ((e.value != undefined && e.value == val) || (e.key != undefined && e.key == val)) {
      result = e.text || e.label;
    }
  });
  return result;
}
// 获取表格列
async function getColumns() {
  let flag = true;
  loading.value = true;
  await getByDesformCode(queryParams.value.desformCode)
    .then((res) => {
      if (res.code == 200) {
        let desformJson = JSON.parse(res.data.desformJson);
        if (!desformJson || desformJson.rule.length == 0) {
          flag = false;
        } else {
          let rule = JSON.parse(desformJson.rule);
          let options = JSON.parse(desformJson.options);
          console.log("desformJson", rule, options);
          rule.forEach((item) => {
            let col = {
              prop: item.field,
              label: item.title,
              visible: true,
              config: {
                type: item.type,
                options: item.options,
              },
            };
            if (item.type == "rate") {
              col["width"] = 160;
            }
            columns.value.push(col);
          });
          formConfig.value = rule;
          desformName.value = res.data.desformName;
        }
      }
    })
    .finally(() => {
      loading.value = false;
    });
  hasForm.value = flag;
  return flag;
}

const addRef = ref();
const editRef = ref();
const detailsRef = ref();
/** 新增按钮操作 */
function handleAdd() {
  addRef.value.show();
}
/** 修改按钮操作 */
function handleUpdate(row, columns) {
  editRef.value.show(row, columns);
}
/** 详情按钮操作 */
function handleDetail(row, columns) {
  detailsRef.value.show(row, columns);
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  row.desformCode = queryParams.value.desformCode;
  proxy.$modal
    .confirm('是否确认删除当前数据编号为"' + _ids + '"的数据项？')
    .then(function () {
      return deldata(row);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => { });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download2(
    "dpp/data/export",
    {
      ...queryParams.value,
    },
    `data_${new Date().getTime()}.xlsx`
  );
}

onMounted(async () => {
  queryParams.value.desformCode = router.currentRoute.value.params.desformCode;
  //查询字段
  if (await getColumns()) {
    getList();
  }
});
</script>
<style lang="scss" scoped>
.app-container {
  display: flex;

  .pagecont-bottom.desformData {
    flex: 1;
  }
}
</style>
<style lang="scss">
.app-container .el-dialog.form-dialog .el-dialog__body {
  .container {
    background-color: #fff;
    width: 100%;
    height: 100%;

    .fc-form-footer {
      .el-form-item__content {
        justify-content: flex-end;
      }
    }
  }
}
</style>
