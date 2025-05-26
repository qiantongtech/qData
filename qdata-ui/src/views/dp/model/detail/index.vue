<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ dpModelDetail.modelComment }}
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">表英文名称</div>
              <div class="infotop-row-value">
                {{ dpModelDetail.modelName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">所属类目</div>
              <div class="infotop-row-value">
                {{ dpModelDetail.catName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">状态</div>
              <div class="infotop-row-value">
                <dict-tag
                  :options="dp_model_status"
                  :value="dpModelDetail.status"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">创建方式</div>
              <div class="infotop-row-value">
                <dict-tag
                  :options="dp_model_create_type"
                  :value="dpModelDetail.createType"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">联系人</div>
              <div class="infotop-row-value">
                {{ getNickNameById(dpModelDetail.contact) || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">联系电话</div>
              <div class="infotop-row-value">
                {{ dpModelDetail.contactNumber || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ dpModelDetail.description || "-" }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="属性字段" name="1">
          <component-one></component-one>
        </el-tab-pane>
        <el-tab-pane label="逻辑物化" name="2">
          <component-two
            :modelId="route.query.id"
            :row="dpModelDetail"
          ></component-two>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="DpModel">
import { getDpModel } from "@/api/dp/model/dpModel";
import { useRoute } from "vue-router";
import { deptUserTree } from "@/api/system/system/user.js";
import ComponentOne from "@/views/dp/model/detail/componentOne.vue";
import ComponentTwo from "@/views/dp/model/detail/componentTwo.vue";
const { proxy } = getCurrentInstance();
const { dp_model_status, dp_model_create_type } = proxy.useDict(
  "dp_model_status",
  "dp_model_create_type"
);

const activeName = ref("1");
const getNickNameById = (userId) => {
  if (!userList.value || !Array.isArray(userList.value)) {
    return null;
  }

  if (!userId) return null;

  const user = userList.value.find((user) => user.userId == userId);
  return user ? user.nickName : null;
};

const handleClick = (tab, event) => {
  console.log(tab, event);
};

const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || 1;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    id = newId || -1;
    activeName.value = "1";
    getDpModelDetailById();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  dpModelDetail: {},
  form: {},
});

const { dpModelDetail, rules } = toRefs(data);
const userList = ref();
/** 复杂详情页面上方表单查询 */
function getDpModelDetailById() {
  const _ID = id;
  if (_ID == -1) {
    return;
  }
  getDpModel(_ID).then((response) => {
    dpModelDetail.value = response.data;
    console.log("🚀 ~ getDpModel ~ response.data:", response.data);
  });
  deptUserTree().then((res) => {
    userList.value = res.data;
    console.log("userList", userList.value);
  });
}

getDpModelDetailById();
</script>

<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;
  .pagecont-bottom {
    min-height: calc(100vh - 362px) !important;
  }
}
</style>
