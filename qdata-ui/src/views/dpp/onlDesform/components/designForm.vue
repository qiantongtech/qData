<template>
  <div class="app-container no-header-dialog">
    <!-- <f-render @save="handleSave" @close="close" :loading="loading" height="calc(100vh - 60px)" :config="formConfig" :saveTable="saveTable" :fieldName="fieldName" /> -->
    <FcDesigner v-loading="loading" ref="designer" :config="designerConfig" height="100vh" @save="handleSave" @drag="handleDrag" @active="handleActive">
      <template #handle>
        <el-button size="small" icon="Close" @click="fcClose">关闭</el-button>
      </template>
    </FcDesigner>
  </div>
  <el-dialog v-model="selectShow" title="选择字段" width="500px" :append-to="$refs['app-container']" draggable destroy-on-close :show-close="false">
    <el-select v-model="selectVal" style="width: 75%">
      <el-option v-for="item in columnName" :key="item" :label="item" :value="item" />
    </el-select>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="selectCancel">取 消</el-button>
        <el-button type="primary" @click="selectSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>
import { edit } from "@/api/dpp/onlDesform/desForm.js";

export default {
  data() {
    return {
      loading: true,
      visible: false,
      formConfig: {},
      id: "",
      designerConfig: {
        formOptions: {
          form: {
            size: "default", // 设置默认尺寸
          },
          submitBtn: {
            show: false,
          },
          resetBtn: {
            show: false,
          },
        },
        handleBtn: true,
        showCustomProps: false, //显示右侧属性：自定义props按钮
        fieldReadonly: false, //字段ID可以手动输入
        hiddenMenu: ["subform"], //隐藏左侧子菜单组件
        showMenuBar: true, //隐藏左侧工具栏
        showConfig: true, //右侧的配置界面
        showSaveBtn: true, //显示保存按钮
        showStyleForm: false, //组件的样式配置表单
        showJsonPreview: true, //显示左侧的JSON预览模块
        showTemplate: false, //显示左侧的模板列表
        showDevice: true, //显示多端适配选项
      },
      saveTable: false,
      activeRule: {},
      selectShow: false,
      selectVal: "",
      fieldName: [],
      record: {},
    };
  },
  computed: {
    columnName() {
      const rules = this.$refs.designer.getRule();
      let newRule = this.fieldName.filter((item) => !rules.find((rule) => rule.field == item));
      return newRule;
    },
  },
  watch: {
    "$route.query": {
      handler(val) {
        if (val) {
          this.design(val);
        }
      },
      immediate: true,
      deep: true,
    },
  },
  mounted() {
    console.log(this.$route.query);
  },
  methods: {
    design(record) {
      this.record = record;
      this.id = record.id;
      this.visible = true;
      this.$nextTick(() => {
        if (record.desformJson) {
          let data = JSON.parse(record.desformJson);
          this.$refs.designer.setOptions(data.options);
          this.$refs.designer.setRule(data.rule);
        }
        setTimeout(() => {
          console.log(this.$refs.designer);
          this.loading = false;
        }, 500);
      });
      if (record.saveTableFlag == 1) {
        this.saveTable = true;
        this.fieldName = record.columnName ? record.columnName.split(",") : [];
        // 如果勾选【是否程序生成主键的值】：会导致设计表单那边不展示主键列
        // if (this.record.createPkDataFlag == "1") {
        //   this.fieldName = this.fieldName.filter((item) => item != this.record.pkColumnName);
        // }
      } else {
        this.saveTable = false;
        this.fieldName = [];
      }
    },
    fcClose() {
      this.$confirm("系统可不会保存您的更改，确定要关闭吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.close();
      });
    },
    close() {
      this.formConfig = {};
      this.visible = false;
      this.saveTable = false;
      this.fieldName = [];
      this.$tab.closeOpenPage({ path: "/dpp/onlDesform" });
    },
    handleActive(rule) {
      if (this.saveTable) {
        console.log(rule, "handleActive");
        this.activeRule = rule;
      }
    },
    handleDrag() {
      if (this.saveTable) {
        this.selectShow = true;
      }
    },
    // 取消字段
    selectCancel() {
      console.log("取消");
      // 删除拖入组件
      const rules = this.$refs.designer.getRule();
      let newRule = rules.filter((item) => item._fc_id != this.activeRule._fc_id);
      this.$refs.designer.setRule(newRule);

      this.selectShow = false;
      this.selectVal = "";
      this.activeRule = {};
    },
    // 选择字段
    selectSubmit() {
      // 更改拖入组件字段
      const rules = this.$refs.designer.getRule();
      let newRule = rules.map((item) => {
        if (item._fc_id == this.activeRule._fc_id) {
          item.field = this.selectVal;
        }
        return item;
      });
      // 更改字段并选中
      this.$refs.designer.setRule(newRule);
      setTimeout(() => {
        this.$refs.designer.triggerActive(this.activeRule._fc_id);
        this.selectShow = false;
        this.selectVal = "";
        this.activeRule = {};
      }, 500);
    },
    handleSave({ rule, options }) {
      const desformJson = JSON.stringify({ rule, options });
      edit({
        id: this.id,
        desformJson: desformJson,
      }).then((res) => {
        if (res.code == 200) {
          this.$message.success("操作成功");
          this.close();
        } else {
          this.$message.warning('操作失败，请检查后重试');
        }
      });
    },
  },
};
</script>
<style lang="scss">
.no-header-dialog {
  max-height: 100vh;
  background: #fff;
}
</style>
