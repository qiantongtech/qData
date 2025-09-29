<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom:15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ clientDetail.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">{{ clientDetail.id }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">应用秘钥</div>
              <div class="infotop-row-value">
                {{ clientDetail.secret || '-' }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">应用图标</div>
              <div class="infotop-row-value">
                <image-preview :src="clientDetail.logo || noDataImg" :width="50" :height="50" />
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                <span class="ellipsis-2" :title="clientDetail.description">{{ clientDetail.description || '-' }}</span>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">备注</div>
              <div class="infotop-row-value">
                <span class="ellipsis" :title="clientDetail.remark">{{ clientDetail.remark || '-' }}</span>
              </div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="API授权" name="1">
          <api :clientDetail="clientDetail"></api>
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="2">
          <info :clientDetail="clientDetail"></info>
        </el-tab-pane>
      </el-tabs>

    </div>


  </div>
</template>

<script setup name="Client">
import { getClient } from "@/api/ds/client/client";
import { useRoute } from 'vue-router';
import info from "@/views/ds/client/detail/info.vue";
import api from "@/views/ds/client/detail/api.vue";
const noDataImg = new URL('@/assets/system/images/D.png', import.meta.url).href

const { proxy } = getCurrentInstance();
const { auth_public, auth_app_type } = proxy.useDict('auth_public', 'auth_app_type');

const activeName = ref('1')

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || 1;  // 如果 id 为空，使用默认值 1
    getClientDetailById();

  },
  { immediate: true }  // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  clientDetail: {
  },
  form: {},
});

const { clientDetail, rules } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getClientDetailById() {
  const _id = id;
  getClient(_id).then(response => {
    clientDetail.value = response.data;
  });
}
// 保存 没有code
const closeDialog = () => {
  if (!currentNode.value.data.code) {
    graph.removeNode(currentNode.value.id); // 根据组件 ID 删除组件
  }
  drawer.value = false;
};
getClientDetailById();

</script>
<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 343px) !important;

  }
}
</style>
