<template>
  <div class="app-container">
    <template v-if="hasForm">
      <el-row>
        <el-col>
          <el-button class="filter-item module-space" style="margin-bottom: 8px; margin-top: 0px" size="small" plain
            type="primary" icon="el-icon-plus" @click="handleCreate"> 新增 </el-button>
          <el-button class="filter-item" style="margin-bottom: 8px; margin-top: 0px" :loading="downloadLoading"
            size="small" type=" warning" icon="el-icon-download" @click="downloadMethod">
            导出
          </el-button>

          <!--        <el-button-->
          <!--          class="filter-item"-->
          <!--          style="margin-bottom: 8px;margin-top: 0px"-->
          <!--          :loading="downloadLoading"-->
          <!--          size="small"-->
          <!--          type="warning"-->
          <!--          icon="el-icon-download"-->
          <!--          @click="downloadMethod"-->
          <!--        >高级查询-->
          <!--        </el-button>-->
          <right-toolbar :search="false" @queryTable="getList" :columns="tableColumns"></right-toolbar>
        </el-col>
      </el-row>

      <el-table v-loading="loading" :data="list" border tooltip-effect="dark" :size="tableSize" :height="tableHeight"
        style="width: 100%; margin: 15px 0">
        <el-table-column label="序号" width="75" align="left">
          <template #default="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <template v-for="(item, index) in tableColumns">
          <el-table-column v-if="item.visible" :key="index" :prop="item.prop" :label="item.label"
            :formatter="item.formatter" :width="item.width != null ? item.width : null" align="left"
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

              <span v-else>
                {{ scope.row[item.prop] }}
              </span>
            </template>
          </el-table-column>
        </template>
        <el-table-column label="操作" width="350px" align="left">
          <template #default="scope">
            <el-button size="small" type=" text" icon="el-icon-edit" @click="handleDetails(scope.row)">查看 </el-button>
            <el-button size="small" type=" text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改 </el-button>
            <el-button size="small" type=" text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除 </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        :v-model:limit="queryParams.pageSize" @pagination="getList" />
    </template>
    <div v-else style="text-align: center">你还没有设计任何表单项，不能查看表单数据！</div>

    <add v-if="formConfig" ref="add" @ok="ok" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
    <edit v-if="formConfig" ref="edit" @ok="ok" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
    <dialog-details v-if="formConfig" ref="details" :title="desformName" :formConfig="formConfig"
      :desformCode="queryParams.desformCode" />
  </div>
</template>

<script>
// import { pageData, deleted } from "@/api/masterdata/onlDesformData";
// import { getByDesformCode } from "@/api/masterdata/onlDesform";

import RightToolbar from "@/components/RightToolbar/index.vue";

import Add from "./components/add.vue";
import Edit from "./components/edit.vue";
import DialogDetails from "./components/details.vue";
// import { download2 } from "@/api/data";

export default {
  name: "DesformDataList",
  components: {
    Add,
    Edit,
    DialogDetails,
    RightToolbar,
  },
  data() {
    return {
      // 展示切换
      showOptions: {
        data: {},
        showList: false,
        showDetail: true,
      },
      tableHeight: document.body.offsetHeight - 310 + "px",
      // 表单名称
      desformName: "",
      // 遮罩层
      loading: true,
      hasForm: false,
      downloadLoading: false,
      // 表格头
      tableColumns: [],
      tableSize: "medium",
      // 数据集表格数据
      list: [],
      // 总数据条数
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        desformCode: null,
        desformName: null,
      },
      typeFrom: {},
      types: {
        //transfer {key: 1, label: "选项1"},
        // cascader-panel 级联面板
        optionsTypes: ["select", "checkbox", "radio", "radio-button", "checkbox-button", "transfer"],
        cascaderTypes: ["cascader", "cascader-panel"],
      },
      noOverflow: ["rate", "slider", "color"],
      formConfig: undefined,
    };
  },
  async created() {
    this.queryParams.desformCode = this.$route.params.desformCode;
    //查询字段
    if (await this.getColumns()) {
      this.getList();
    }
  },
  methods: {
    //级联
    computedCascaderOptions(arrVal, options) {
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
    },
    //单选多选
    computedOptions(val, options) {
      if (!val) {
        return "";
      }
      let result = "";
      if (val instanceof Array) {
        val.forEach((e) => {
          result += this.getText(e, options) + ",";
        });
        if (result.length > 0) {
          result = result.substr(0, result.length - 1);
        }
      } else {
        result = this.getText(val, options);
      }
      return result;
    },
    getText(val, options) {
      let result = "";
      options.forEach((e) => {
        if ((e.value != undefined && e.value == val) || (e.key != undefined && e.key == val)) {
          result = e.text || e.label;
        }
      });
      return result;
    },
    async getColumns() {
      let flag = true;
      await getByDesformCode(this.queryParams.desformCode).then((res) => {
        if (res.success) {
          let desformJson = JSON.parse(res.data.desformJson);
          desformJson.order.forEach((key) => {
            let f = desformJson.formDesc[key];
            let columns = {
              prop: key,
              label: f.label,
              visible: true,
              config: f,
            };
            if (f.type == "rate") {
              columns["width"] = 160;
            }
            this.tableColumns.push(columns);
          });
          if (!desformJson.order || desformJson.order.length == 0) {
            flag = false;
          }
          this.formConfig = JSON.parse(res.data.desformJson);
          this.desformName = res.data.desformName;
        }
      });
      this.hasForm = flag;
      return flag;
    },
    //导出
    downloadMethod() {
      download2("/data/masterdata/onlDesformData/export", "get", this.queryParams).then(() => { });
    },
    handleCreate() {
      this.$refs.add.add();
    },
    handleUpdate(record) {
      this.$refs.edit.edit(record);
    },
    handleDetails(record) {
      this.$refs.details.details(record);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm("选中数据将被永久删除, 是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleted({ id: row.id, desformCode: this.queryParams.desformCode }).then((response) => {
          if (response.success) {
            this.$message.success("删除成功");
            this.getList();
          }
        });
      });
    },
    ok() {
      this.queryParams.pageNum = 1;
      this.queryParams.pageSize = 20;
      this.handleQuery();
    },
    /** 查询数据Api列表 */
    getList() {
      this.loading = true;
      pageData(this.queryParams).then((response) => {
        this.loading = false;
        if (response.success) {
          const { data } = response;
          let list = [];
          data.data.forEach((e) => {
            let obj = { id: e.id };
            let json = JSON.parse(e.desformDataJson);
            for (let key in json) {
              obj[key] = json[key];
            }
            list.push(obj);
          });
          this.list = list;
          this.total = data.total;
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        desformName: "",
        desformCode: "",
      };
      this.handleQuery();
    },
    /** 刷新列表 */
    handleRefresh() {
      this.getList();
    },
  },
};
</script>

<style lang="scss" scoped>
.el-card ::v-deep .el-card__body {
  height: calc(100vh - 170px);
}

.j-table-slot-view-color {
  width: 25px;
  height: 25px;
  margin: 0 auto;
}
</style>
