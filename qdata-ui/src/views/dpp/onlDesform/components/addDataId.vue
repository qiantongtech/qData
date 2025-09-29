<template>
  <!--表单组件-->
  <el-dialog class="addDataId" v-model="visible" :title="title" width="600px" :append-to="$refs['app-container']" draggable>
    <el-input placeholder="请输入dataId" v-model="dataId" />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script>
export default {
  name: "addDataId",
  data() {
    return {
      visible: false,
      title: "请输入dataId",
      dataId: "",
    };
  },
  methods: {
    show() {
      this.dataId = "";
      this.visible = true;
    },
    submit() {
      if (this.dataId) {
        this.$emit("ok", this.dataId);
      }
      this.visible = false;
    },
    cancel() {
      this.visible = false;
    },
  },
};
</script>
<style lang="scss">
.el-dialog.addDataId{
  &:not(.is-fullscreen){
    margin-top: 20vh !important;
  }
  .el-dialog__body{
    height: auto;
  }
}
</style>
