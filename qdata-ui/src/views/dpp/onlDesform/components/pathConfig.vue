<template>
  <!--表单组件-->
  <el-dialog v-model="visible" :title="title" width="800px" :append-to="$refs['app-container']" draggable>
    <el-input v-for="(item, index) in urls" :key="index" readonly placeholder="数据列表地址" v-model="item.url"
      style="margin-bottom: 20px">
      <template #prepend>{{ item.name }}</template>
      <template #append>
        <a class="open-button" @click="open(item)">打开</a>
      </template>
    </el-input>
    <div style="text-align: right; color: red">注意：{dataId} 为数据id</div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="cancel">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script>
export default {
  name: "desformAdd",
  data() {
    return {
      visible: false,
      title: "配置地址",
      desformCode: "",
      urls: [],
      url: "", //待打开的路径
      router: false, //是否路由打开
    };
  },
  methods: {
    show(record) {
      this.desformCode = record.desformCode;
      let origin = window.document.location.origin;
      this.urls = [];
      this.urls.push({
        name: "数据列表地址",
        url: `/dpp/onlDesform/data/list/${this.desformCode}`,
        router: true,
        dataId: false,
      });
      this.urls.push({
        name: "后台新增地址",
        url: `${origin}/dpp/onlDesform/data/add/${this.desformCode}`,
        router: false,
        dataId: false,
      });
      this.urls.push({
        name: "后台修改地址",
        url: `${origin}/dpp/onlDesform/data/edit/${this.desformCode}/{dataId}`,
        router: false,
        dataId: true,
      });
      this.urls.push({
        name: "后台详情地址",
        url: `${origin}/dpp/onlDesform/data/details/${this.desformCode}/{dataId}`,
        router: false,
        dataId: true,
      });
      this.visible = true;
    },
    open(data) {
      if (data.dataId) {
        this.router = data.router;
        this.url = data.url;
        this.$emit("addDataId");
        return;
      }
      this.openWindow(data.url, data.router);
    },
    addDataId(dataId) {
      this.openWindow(this.url, this.router, dataId);
    },
    openWindow(url, router, dataId) {
      let oUrl = url;
      if (dataId) {
        oUrl = url.replace("{dataId}", dataId);
      }
      if (router) {
        this.$router.push(oUrl);
      } else {
        window.open(oUrl, "_blank");
      }
    },
    cancel() {
      this.visible = false;
    },
  },
};
</script>
<style scoped>
.open-button {
  color: var(--el-color-primary);
  cursor: pointer;
}
</style>
