<template>
  <!--表单组件-->
  <el-dialog class="form-dialog" :destroy-on-close="true" v-model="visible" :title="title" width="800px" :append-to="$refs['app-container']" draggable>
    <div class="container">
      <FormCreate v-model="formData" :rule="rule" :option="option" disabled></FormCreate>
    </div>
  </el-dialog>
</template>
<script>
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
  name: "desformEdit",
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
    show(row, columns) {
      columns.forEach((item) => {
        if (item.config.type === "datePicker") {
          row[item.prop] = this.parseTime(row[item.prop], "{y}-{m}-{d}");
        }
      });
      this.dataId = row.id;
      this.formData = Object.assign({}, row);
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
  },
};
</script>
