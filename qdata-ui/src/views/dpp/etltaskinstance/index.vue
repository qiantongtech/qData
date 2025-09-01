<template>
  <div class="app-container" ref="app-container">
    <el-container style="90%">
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="'请输入作业类目名称'"
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
      />
      <el-main>
        <el-tabs
          v-model="activeName"
          class="demo-tabs"
          @tab-click="handleClick"
        >
          <el-tab-pane label="数据集成" name="1">
            <dppEtlTaskLog ref="taskLog" @resetCat="resetCat" />
          </el-tab-pane>
          <el-tab-pane label="实时集成" disabled name="2">
          </el-tab-pane>
          <el-tab-pane label="数据开发" name="3">
            <dppEtlDevTaskLog ref="taskLog" @resetCat="resetCat" />
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="DppEtlTaskLog">
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import useUserStore from "@/store/system/user";

import dppEtlTaskLog from "./components/dppEtlTaskLog.vue";
import dppEtlDevTaskLog from "./components/dppEtlDevTaskLog.vue";

import { listAttTaskCat } from "@/api/att/cat/attTaskCat/attTaskCat";
import { listAttDataDevCat } from "@/api/att/cat/attDataDevCat/attDataDevCat";

const { proxy } = getCurrentInstance();
let activeName = ref("1");
const userStore = useUserStore();
import DeptTree from "@/components/DeptTree";

const deptOptions = ref([]);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {});
  }
};

function handleNodeClick(data) {
  taskLog.value.queryParams.catCode = data.code;
  taskLog.value.queryParams.pageNum = 1;
  taskLog.value.handleQuery();
}

/** 下拉树结构 */
function getDeptTree(catName = "数据集成类目", paneName = "1") {
  var fun = listAttTaskCat;
  if (paneName == "3") {
    fun = listAttDataDevCat;
  }
  var params = {
    projectId: userStore.projectId,
    projectCode: userStore.projectCode,
  };
  fun(params).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        name: catName,
        value: "",
        children: deptOptions.value,
      },
    ];
  });
}

const DeptTreeRef = ref(null);

//重置左侧类目
function resetCat() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
}

const taskLog = ref(null);
function handleClick(e) {
  var catName = "";
  switch (e.paneName) {
    case "1":
      catName = "数据集成类目";
      break;
    case "2":
      catName = "实时集成类目";
      break;
    case "3":
      catName = "数据开发类目";
      break;
  }
  //刷新左侧树
  getDeptTree(catName, e.paneName);
  //刷新列表
  taskLog.value.queryParams.taskType = e.paneName;
  taskLog.value.queryParams.catCode = null;
  taskLog.value.handleQuery();
}

watch(
  () => userStore.projectCode,
  (newCode) => {
    if (newCode) {
      getDeptTree();
    }
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
</script>
<style scoped lang="less">
::v-deep(.el-tabs__header) {
  margin: 0 0 0px;
  padding: 0px 15px 0px 15px;
  position: relative;
  background-color: #fff;
}
.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}
</style>
