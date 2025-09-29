<template>
  <!--表单组件-->
  <el-dialog v-model="visible" :title="title" width="800px" :append-to="$refs['app-container']" draggable
    destroy-on-close>
    <el-form ref="form" :model="form" :rules="rules" label-width="125px" v-loading="loading">
      <el-form-item label="表单名称" prop="desformName">
        <el-input v-model="form.desformName" style="width: 100%" placeholder="请输入表单名称" />
      </el-form-item>
      <el-form-item label="表单编码" prop="desformCode">
        <el-input v-model="form.desformCode" style="width: 100%" placeholder="请输入表单编码" />
      </el-form-item>

      <el-form-item label="存储指定数据库" prop="saveTableFlag">
        <el-switch v-model="form.saveTableFlag" active-value="1" inactive-value="0" @change="onSaveTableChange" />
      </el-form-item>

      <template v-if="form.saveTableFlag == 1">
        <el-form-item label="目标数据库源" prop="datasourceId">
          <el-select v-model="form.datasourceId" filterable style="width: 100%" @change="rDbChange">
            <el-option v-for="item in rDbList" :key="item.id" :label="item.datasourceName" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="目标Schema" prop="databaseName"
          v-show="dataSource.datasourceConfig && dataSource.datasourceConfig.dbname">
          <el-input v-model="form.databaseName" style="width: 100%" placeholder="请选择目标数据库源" disabled />
        </el-form-item>

        <el-form-item label="目标数据库" prop="tableName">
          <el-select value-key="id" v-model="form.tableName" allow-create default-first-option filterable
            style="width: 100%" @change="rTbChange" :loading="dbLoading">
            <el-option v-for="item in rTbList" :key="item.id" :label="item.tableComment" :value="item.tableName" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="rColumnList && rColumnList.length > 0" label="目标表所有字段：" prop="columnName">
          <div style="width: 100%">
            <el-tooltip class="item" effect="dark" content="请勿选择主键字段，非程序生成主键的值时可选" placement="top-end">
              <el-icon>
                <QuestionFilled />
              </el-icon>
            </el-tooltip>
            <el-checkbox v-model="form.checkAll" :indeterminate="form.isIndeterminate"
              @change="rHandleCheckAllChange">全选 </el-checkbox>
            <el-checkbox-group v-model="form.columnName" @change="rHandleCheckedChange" style="width: 100%">
              <el-checkbox v-for="c in rColumnList" :key="c.columnName" :label="c.columnName">
                <div style="width: 200px" class="ellipsis" :title="`${c.columnName}(${c.columnComment})`">{{
                  c.columnName }}({{ c.columnComment }})</div>
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>

        <el-form-item v-if="rColumnList && rColumnList.length > 0" label="唯一标识字段：" prop="pkColumnName">
          <el-radio-group v-model="form.pkColumnName" style="width: 100%">
            <el-radio v-for="c in rColumnList" :key="c.columnName" :label="c.columnName">
              <div style="width: 200px" class="ellipsis" :title="`${c.columnName}(${c.columnComment})`">{{ c.columnName
              }}({{ c.columnComment }})</div>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="自动生成主键" prop="createPkDataFlag">
          <el-tooltip class="item" effect="dark" content="选择否就是数据库的自增ID(数据库需要支持)或在表单中自己填写，选择是程序会生成唯一的雪花ID"
            placement="top-end">
            <el-icon>
              <QuestionFilled />
            </el-icon>
          </el-tooltip>
          <el-switch v-model="form.createPkDataFlag" active-value="1" inactive-value="0" />
        </el-form-item>
      </template>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script>
import { add, duplicateCheck } from "@/api/dpp/onlDesform/desForm.js";
import { listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource.js";
import { getTablesByDataSourceId, getColumnByAssetId } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user";

export default {
  name: "desformAdd",
  data() {
    return {
      visible: false,
      loading: false,
      title: "添加表单",
      form: {
        desformName: undefined,
        desformCode: undefined,
        saveTableFlag: "0",
        datasourceId: undefined,
        databaseName: undefined,
        tableName: undefined,
        columnName: [],
        pkColumnName: undefined,
        createPkDataFlag: "1",
        checkAll: false,
        isIndeterminate: true,
      },
      rules: {
        desformName: [{ required: true, message: "表单名称不能为空", trigger: "blur" }],
        datasourceId: [{ required: true, message: "目标数据源不能为空", trigger: "change" }],
        tableName: [{ required: true, message: "目标数据库表名不能为空", trigger: "change" }],
        columnName: [{ required: true, message: "目标表所有字段不能为空", trigger: "change" }],
        pkColumnName: [{ required: true, message: "唯一标识字段不能为空", trigger: "change" }],
        createPkDataFlag: [{ required: true, message: "请选择是否程序生产主键的值", trigger: "change" }],
        saveTableFlag: [{ required: true, message: "请选择是否存储到指定表中", trigger: "change" }],
        desformCode: [
          { required: true, message: "表单编码不能为空", trigger: "blur" },
          { validator: this.validateDesformCode, trigger: "blur" },
        ],
      },

      //数据相关数据
      dbLoading: false,
      dataSource: { datasourceConfig: { dbname: null } },
      rDbList: [],
      rTbList: [],
      schemaList: [],
      rColumnList: [],
    };
  },
  watch: {
    "form.datasourceId": function (newVal) {
      if (newVal) {
        this.form.databaseName = (this.dataSource.datasourceConfig && this.dataSource.datasourceConfig.dbname) || "";
        this.getTables("rdbmsReader");
      }
    },
  },
  created() { },
  methods: {
    //存储指定数据库开关发生变化
    onSaveTableChange(val) {
      if (val === 0) {
        this.dataSource = "";
        this.rTbList = [];
        this.schemaList = [];
        this.rColumnList = [];
        this.form.datasourceId = undefined;
        this.form.databaseName = undefined;
        this.form.tableName = undefined;
        this.form.columnName = [];
        this.form.pkColumnName = undefined;
        this.form.createPkDataFlag = "1";
        this.form.checkAll = false;
        this.form.isIndeterminate = true;
      }
    },
    //异步校验code
    validateDesformCode(rule, value, callback) {
      duplicateCheck(this.form).then((response) => {
        if (response.code == 200) {
          callback();
        } else {
          callback(new Error("当前系统中已存在该编码！"));
        }
      });
    },
    add() {
      this.rColumnList = [];
      this.dataSource = "";
      this.rTbList = [];
      this.schemaList = [];
      this.visible = true;
      this.getJdbcDs();
    },
    submit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.form.columnName = this.form.columnName.join(",");
          add(this.form).then((response) => {
            this.loading = false;
            if (response.code == 200) {
              this.$message.success(response.msg);
              this.$emit("ok");
              this.close();
            } else {
              this.$message.warning('操作失败，请检查后重试');
            }
          });
        }
      });
    },
    close() {
      this.form = {
        desformName: undefined,
        desformCode: undefined,
        saveTableFlag: "0",
        datasourceId: undefined,
        databaseName: undefined,
        tableName: undefined,
        columnName: [],
        pkColumnName: undefined,
        checkAll: false,
        isIndeterminate: true,
      };
      this.visible = false;
    },
    cancel() {
      this.close();
    },
    // 获取可用数据源
    async getJdbcDs() {
      this.dbLoading = true;
      const userStore = await useUserStore();
      listDaDatasourceNoKafkaByProjectCode({
        projectCode: userStore.projectCode,
        projectId: userStore.projectId,
      }).then((response) => {
        this.rDbList = response.data;
        this.dbLoading = false;
      });
    },
    // 获取表名
    getTables(type) {
      if (type === "rdbmsReader") {
        let obj = {
          datasourceId: this.form.datasourceId,
        };
        // 组装
        this.rTbList = [];
        getTablesByDataSourceId(obj).then((response) => {
          if (response) {
            this.rTbList = response.data;
          }
        });
      }
    },
    // 表切换
    rTbChange(t) {
      this.form.tableName = t;
      this.rColumnList = [];
      this.form.columnName = [];
      this.getColumns("reader");
    },
    // 目标数据源切换
    rDbChange(e) {
      // 清空
      this.schemaList = [];
      // 清空表空间
      this.form.databaseName = undefined;
      this.form.tableName = "";
      this.form.columnName = [];
      this.rColumnList = [];
      this.form.parseTargetDatasourceId = e;
      this.rDbList.find((item) => {
        if (item.id === e) {
          this.dataSource = {
            ...item,
            datasourceConfig: JSON.parse(item.datasourceConfig),
          };
        }
      });
    },
    // 获取表字段
    getColumns(type) {
      if (type === "reader") {
        this.getTableColumns();
      }
    },
    getTableColumns() {
      const obj = {
        id: this.form.datasourceId,
        tableName: this.form.tableName,
      };
      this.loading = true;
      getColumnByAssetId(obj)
        .then((response) => {
          this.rColumnList = response.data;
          this.form.columnName = response.data.map((item) => item.columnName);
          this.form.checkAll = true;
          this.form.isIndeterminate = false;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    rHandleCheckAllChange(val) {
      this.form.columnName = val ? this.rColumnList.map((item) => item.columnName) : [];
      this.form.isIndeterminate = false;
    },
    rHandleCheckedChange(value) {
      const checkedCount = value.length;
      this.form.checkAll = checkedCount === this.rColumnList.length;
      this.form.isIndeterminate = checkedCount > 0 && checkedCount < this.rColumnList.length;
    },
  },
};
</script>
