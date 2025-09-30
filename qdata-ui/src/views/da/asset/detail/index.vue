<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ daAssetDetail?.name }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">
                {{ daAssetDetail.id || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="desc.span || 8" v-for="desc in descList" :key="desc.label" style="margin-bottom: 2px">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">{{ desc.label }}</div>
              <div class="infotop-row-value">
                <span v-if="desc.key == 'daAssetThemeRelList'">{{desc.value.length > 0 ? desc.value.map((ele) =>
                  ele.themeName).join(", ") : "-"}}</span>
                <span v-else-if="desc.key == 'status'"><dict-tag :options="da_assets_status"
                    :value="desc.value" /></span>
                <span class="li-type" v-else-if="desc.key == 'type'"
                  :style="{ color: desc.value == 2 ? '#c0d043' : desc.value == 1 ? '#21a3dd' : desc.value == 7 ? '#edce2e' : '' }">
                  <img v-if="desc.value == 2" src="@/assets/da/asset/api (1).svg" alt="" />
                  <img v-if="desc.value == 1" src="@/assets/da/asset/api (3).svg" alt="" />
                  <img v-if="desc.value == 7" src="@/assets/da/asset/api (5).svg" alt="" />
                  {{ desc.value == 2 ? "api" : desc.value == 1 ? "库表" : desc.value == 7 ? "文件" : "-" }}
                </span>
                <span v-else>{{ desc.value || "-" }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-col :span="24">
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">描述</div>
            <div class="infotop-row-value">
              <span class="ellipsis-2">
                {{ daAssetDetail.description || "-" }}
              </span>
            </div>
          </div>
        </el-col>
        <el-row :gutter="2" v-if="daAssetDetail.type == 1">
          <template v-if="daAssetDetail.type == 1">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">数据连接</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.datasourceName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top" style="margin: 2px 0;">
                <div class="infotop-row-lable">表描述</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.tableComment || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <el-col :span="8">
            <div class="infotop-row border-top" style="margin: 2px 0;">
              <div class="infotop-row-lable">英文名称</div>
              <div class="infotop-row-value">{{ daAssetDetail.tableName }}</div>
            </div>
          </el-col>
          <template v-if="daAssetDetail.type == 2">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">应用名称</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.appName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">开发者</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.developerName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">服务地址</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetApi?.url || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">请求类型</div>
                <div class="infotop-row-value">
                  <dict-tag :options="da_asset_api_method" :value="daAssetDetail.daAssetApi.httpMethod" />
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 3">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">服务类型</div>
                <div class="infotop-row-value">
                  <dict-tag :options="da_asset_gis_type" :value="daAssetDetail?.daAssetGis?.type" />
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">服务地址</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetGis?.url || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 4">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">文件类型</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetGeo?.fileType || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">上传文件</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail.daAssetGeo?.fileUrl || "-" }}
                </div>
              </div>
            </el-col>
          </template>
          <template v-if="daAssetDetail.type == 5">
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">平台</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.platform || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">平台ip</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.ip || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">平台端口</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.port || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">摄像头编码</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.cameraCode || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">摄像头名称</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.cameraName || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">公钥</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.appkey || "-" }}
                </div>
              </div>
            </el-col>
            <el-col :span="8" style="margin: 2px 0;">
              <div class="infotop-row border-top">
                <div class="infotop-row-lable">私钥</div>
                <div class="infotop-row-value">
                  {{ daAssetDetail?.daAssetVideo?.config?.appSecret || "-" }}
                </div>
              </div>
            </el-col>
          </template>
        </el-row>
      </div>
    </div>
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick"
        v-if="!daAssetDetail.daAssetFiles || ['.xlsx', '.xls', '.csv'].includes(daAssetDetail.daAssetFiles.type)">
        <el-tab-pane v-for="pane in tabPanes" :key="pane.name" :label="pane.label" :name="pane.name">
          <component v-if="activeName === pane.name" :is="pane.component" :form1="daAssetDetail" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script setup name="DaAsset">
import { getDaAsset } from "@/api/da/asset/asset";
import { useRoute } from "vue-router";
import ComponentOne from "@/views/dpp/asset/detail/table/column.vue";
import DataQualityControl from "@/views/dpp/asset/detail/table/quality.vue";
import ComponentTwo from "@/views/dpp/asset/detail/table/preview.vue";
import ComponentThree from "@/views/dpp/asset/detail/api/simulation.vue";
import authParams from "@/views/dpp/asset/detail/api/authParams";
import RequestParamsForm from "@/views/dpp/asset/detail/api/requestParamsForm";
import ResponseFormatConfig from "@/views/dpp/asset/detail/api/responseFormatConfig";
import lineage from "@/views/dpp/asset/detail/table/lineage.vue";
import info from "@/views/dpp/asset/detail/info.vue";
const { proxy } = getCurrentInstance();
const { da_assets_status, da_asset_gis_type, da_asset_api_method } = proxy.useDict("da_assets_status", "da_asset_gis_type", "da_asset_api_method");
const activeName = ref("0");
function handleClick(tab) {
  // 可根据需要自定义逻辑
  console.log("Tab clicked:", tab);
}

const descList = ref([
  {
    key: "catName",
    label: "所属类目",
    value: "",
  },
  {
    key: "type",
    label: "类型",
    value: "",
  },
  {
    key: "daAssetThemeRelList",
    label: "所属主题",
    value: "",
  },
  {
    key: "tag",
    label: "数据标签",
    value: "",
  },
  {
    key: "status",
    label: "状态",
    value: "",
  },


]);

// 计算属性生成 tab pane 数组
const tabPanes = computed(() => {
  console.log("🚀 ~ tabPanes ~ daAssetDetail.value.type:", daAssetDetail.value.type);
  switch (daAssetDetail.value.type) {
    case "1":
      return [
        { label: "资产字段", name: "0", component: ComponentOne },
        { label: "资产预览", name: "2", component: ComponentTwo },
        { label: '资产质量', name: '3', component: DataQualityControl },
        // { label: '资产血缘', name: '4', component: lineage },
        { label: "资产概览", name: "5", component: info },
      ];
    case "2":
      return [
        { label: "鉴权参数", name: "0", component: authParams },
        { label: "请求参数", name: "1", component: RequestParamsForm },
        { label: "返回格式", name: "2", component: ResponseFormatConfig },
        { label: "预览数据", name: "3", component: ComponentThree },
        { label: "资产概览", name: "4", component: info },

      ];
    case "3":
      return [{ label: "资产概览", name: "0", component: info }];
    case "4":
      return [
        { label: "资产概览", name: "0", component: info },
        { label: "资产预览", name: "1", component: ComponentTwo },
      ];
    case "5":
      return [{ label: "资产概览", name: "0", component: info }];
    case "6":
      return [
        { label: "资产概览", name: "0", component: info },
        { label: "资产字段", name: "1", component: ComponentOne },
        { label: "资产预览", name: "2", component: ComponentTwo },
      ];
    case "7":
      return [{ label: "资产概览", name: "0", component: info }];
    default:
  }
});
const showSearch = ref(true);
const route = useRoute();
let id = route.query.id || null;
// 监听 id 变化
watch(
  () => route.query.id,
  (newId) => {
    if (route.path == '/da/asset/detail' || route.path == '/dpp/asset/detail') {
      id = newId || null; // 如果 id 为空，使用默认值 1
      getDaAssetDetailById();
    }
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);
const data = reactive({
  daAssetDetail: {},
  form: {},
});

const { daAssetDetail } = toRefs(data);

/** 复杂详情页面上方表单查询 */
function getDaAssetDetailById() {
  if (!id) {
    return;
  }
  const _id = id;
  getDaAsset(_id).then((response) => {
    daAssetDetail.value = response.data;
    descList.value.forEach((item) => {
      item.value = response.data[item.key];
    });
    if (response.data.type == "5") {
      daAssetDetail.value.daAssetVideo.config = JSON.parse(response.data.daAssetVideo.config);
    }
  });
}

onActivated(() => {
  activeName.value = "0";
  getDaAssetDetailById();
  // listDaAssetColumn();
});
onBeforeUnmount(() => {
  // 清空参数或重置状态
  data.daAssetDetail = {};
  data.form = {};
  activeName.value = "0"; // 重置tab页
});
// listDaAssetColumn();
</script>
<style lang="scss" scoped>
.li-type {
  display: flex;
  align-items: center;

  img {
    width: 18px;
    margin: 0 5px;
  }


}
</style>
