<template>
  <!--表单组件-->
  <el-dialog class="form-dialog" :destroy-on-close="true" v-model="visible" :title="title" width="800px" :append-to="$refs['app-container']" draggable>
    <div class="container">
      <FormCreate v-model="formData" :rule="rule" :option="option"></FormCreate>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script>
import { adddata } from "@/api/dpp/onlDesformData/desformData.js";
import FormCreate from "@form-create/element-ui";

export default {
  props: {
    title: {
      type: String,
      default: "添加",
    },
    formConfig: {
      type: Object,
      request: true,
      default: () => {},
    },
    desformCode: {
      type: String,
      request: true,
      default: "",
    },
  },
  components: {
    FormCreate,
  },
  name: "desformAdd",
  data() {
    return {
      visible: false,
      dataId: "", //数据id
      loading: false,
      formData: {},
      rule: [],
      option: {},
    };
  },
  mounted() {},
  methods: {
    show() {
      this.formData = Object.assign({}, {});
      this.rule = JSON.parse(JSON.stringify(this.formConfig));
      this.option = {
        submitBtn: {
          show: false,
          innerText: "确定",
          click: () => {
            this.handleSubmit();
          },
        },
        resetBtn: {
          show: false,
          innerText: "取消",
          click: () => {
            this.close();
          },
        },
        form: {
          inline: false,
          hideRequiredAsterisk: false,
          labelPosition: "right",
          size: "default",
          labelWidth: "125px",
        },
      };
      this.visible = true;
    },
    close() {
      this.visible = false;
    },
    ok() {
      this.$emit("ok");
      this.close();
    },
    cancel() {
      this.close();
    },
    handleSubmit() {
      let params = {
        desformCode: this.desformCode,
        desformData: JSON.stringify(this.formData),
      };
      adddata(params).then((res) => {
        if (res.code == 200) {
          this.$message.success("操作成功");
          this.$emit("ok");
          this.close();
        } else {
          this.$message.warning("操作失败，请检查后重试");
        }
      });
    },
  },
};
</script>
