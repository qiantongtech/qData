<template>
  <!-- 新增或修改数据发现任务对话框 -->
  <el-dialog v-model="visibleDialog" class="medium-dialog " draggable destroy-on-close>
    <template #header="{ close, titleId, titleClass }">
      <span role="heading" aria-level="2" class="el-dialog__title">
        {{ title }}
      </span>
    </template>
    <el-form ref="daDiscoveryTaskRef" :model="form" label-width="110px" @submit.prevent>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产名称" prop="name" :rules="[
            { required: true, message: '请输入资产名称', trigger: 'blur' },
          ]">
            <el-input v-model="form.name" placeholder="请输入资产名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属类目" prop="catCode" :rules="[
            { required: true, message: '请输入类目编码', trigger: 'change' },
          ]">
            <el-tree-select v-model="form.catCode" :data="deptOptions"
              :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID" placeholder="请选择所属类目"
              check-strictly /></el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属主题" prop="themeIdList" :rules="[
            { required: true, message: '请选择主题', trigger: 'change' },
          ]">
            <el-select v-model="form.themeIdList" collapse-tags multiple placeholder="请选择主题名称">
              <el-option v-for="dict in themeList" :key="dict.id" :label="dict.name" :value="dict.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产状态" prop="status" :rules="[
            {
              required: true,
              message: '请选择资产状态',
              trigger: 'change',
            },
          ]">
            <el-radio-group v-model="form.status">
              <el-radio value="1">启用</el-radio>
              <el-radio value="0">停用</el-radio>
            </el-radio-group>

          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产类型" prop="type" :rules="[
            {
              required: true,
              message: '请选择资产类型',
              trigger: 'change',
            },
          ]">
            <el-select v-model="form.type" placeholder="请输入类型" filterable :disabled="form.id"
              @change="handleTypeChange">
              <el-option v-for="dict in da_asset_type" :key="dict.value" :label="dict.label"
                :value="dict.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="资产描述" prop="description">
            <el-input type="textarea" :min-height="192" v-model="form.description" placeholder="请输入资产描述" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="center">
        <span class="blue-text">参数配置</span>
      </el-divider>
      <component :is="currentFormComponent" v-model:form="form" ref="ApiConfigRef" />
      <el-row :gutter="20" v-if="form.type == '3'">
        <el-col :span="12">
          <el-form-item label="服务类型" prop="daAssetGis.type" :rules="[
            { required: true, message: '请输入服务类型', trigger: 'blur' },
          ]">
            <el-select v-model="form.daAssetGis.type" placeholder="请选择参数类型">
              <el-option v-for="dict in da_asset_gis_type" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务地址" prop="daAssetGis.url" :rules="[
            { required: true, message: '请输入服务地址', trigger: 'blur' },
          ]">
            <el-input v-model="form.daAssetGis.url" placeholder="请输入服务地址" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="form.type == '3'">
        <el-col :span="12">
          <el-form-item label="请求类型" prop="daAssetGis.type" :rules="[
            { required: true, message: '请选择请求类型', trigger: 'blur' },
          ]">
            <el-select v-model="form.daAssetGis.httpMethod" placeholder="请选择请求类型">
              <el-option v-for="dict in da_asset_api_method" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="form.type == '4'">
        <el-col :span="12">
          <el-form-item label="文件类型" prop="daAssetGeo.fileType" :rules="[
            { required: true, message: '请输入文件类型', trigger: 'blur' },
          ]">
            <el-select v-model="form.daAssetGeo.fileType" placeholder="请选择参数类型">
              <el-option v-for="dict in da_asset_geo_file_type" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
            <!-- <el-input v-model="form.daAssetGeo.fileType" placeholder="请输入文件类型" /> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上传文件" prop="daAssetGeo.fileUrl" :rules="[
            { required: true, message: '请上传文件', trigger: 'fileUrl' },
          ]">
            <FileUploadbtn :limit="1" v-model="form.daAssetGeo.fileUrl" :dragFlag="false" :fileType="['geojson']"
              :fileSize="50" :isShowTip="false" v-model:fileSize="form.fileSize" v-model:fileExt="form.fileType" />
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">取消</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData" :loading="loading">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from "vue";
import Crontab from "@/components/Crontab/index.vue";
import { getDaDiscoveryTask } from "@/api/da/discovery/daDiscoveryTask";

import TableConfigForm from "./TableConfigForm";
import ApiConfigForm from "./ApiConfigForm";
import daAssetVideo from "./daAssetVideo.vue";
import { getThemeList } from '@/api/att/theme/attTheme';
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const { da_asset_type, da_asset_gis_type, da_asset_geo_file_type, da_asset_video_platform, da_asset_gis_typ, da_asset_api_method } = proxy.useDict("da_asset_type", "da_asset_gis_type", "da_asset_geo_file_type", "da_asset_video_platform", "da_asset_gis_typ", "da_asset_api_method");
import {
  addDaAsset,
  updateDaAsset,
} from "@/api/da/asset/daAsset";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  type: { type: Boolean, default: "" },
});
let loading = ref(false); // 加载状态（全局）
const emit = defineEmits(["update:visible", "confirm"]);
const currentFormComponent = computed(() => {
  switch (form.value.type) {
    case "1":
      return TableConfigForm;
    case "2":
      return ApiConfigForm;
    case "5":
      return daAssetVideo;
    default:
      return;
  }
});
let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
}
const createTypeList = ref([]); // 数据源列表
// const getDatasourceList = async () => {
//   try {
//     loading.value = true;
//     const response = await listDaDatasourceNoKafkaByProjectCode({
//       projectCode: userStore.projectCode,
//       projectId: userStore.projectId,
//     });
//     createTypeList.value = response.data || [];
//   } finally {
//     loading.value = false;
//   }
// };
let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}
/** 确定后回传值 */
function crontabFill(value) {
  form.value.cronExpression = value;
}
// 创建一个本地响应式数据，用来修改表单内容
const form = ref({
  type: "1",
  catCode: "",
  sourceType: "0",
  name: "",
  themeIdList: [

  ],
  status: '0',
  description: "",
  source: "3",
  // 1
  tableName: "",
  datasourceId: "",
  tableComment: "",
  datasourceType: "",
  dbname: "",
  // 2
  daAssetApi: {
    appName: "",
    url: "",
    httpMethod: "",
    developerName: "",
  },
  daAssetApiParamList: [],
  // 4
  daAssetGeo: {
    fileUrl: "",
    fileType: "",
    elementType: "",
    coordinateSystem: "",
    example: "",
    fileName: ""
  },
  daAssetGis: {
    url: "",
    type: "",
    httpMethod: "",
    coordinateSystem: "",
    remark: "",
  },
  daAssetVideo: {
    ip: "",
    port: "",
    protocol: "",
    platform: "",

    config: {
      cameraName: "",
      cameraCode: "",
      appkey: "",
      appSecret: "",
      artemisPath: ""
    },

  }

});
watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      getAssetThemeList()
      if (props.data.id) {
      } else {
        form.value.catCode = props.data.catCode || "";
        console.log("🚀 ~ props.data:", props.data.catCode);
      }
    } else {
      clearForm();
    }
  }
);
const handleTypeChange = (newType) => {
  // 清空表格字段
  form.value.tableName = "";
  form.value.datasourceId = "";
  form.value.tableComment = "";
  form.value.datasourceType = "";
  form.value.dbname = "";
  form.value.daAssetApi = {
    appName: "",
    url: "",
    httpMethod: "",
    developerName: ""
  };
  form.value.daAssetApiParamList = [];
  form.value.daAssetGeo = {
    fileUrl: "",
    fileType: "",
    elementType: "",
    coordinateSystem: "",
    example: "",
    fileName: ""
  };
  form.value.daAssetGis = {
    url: "",
    type: "",
    httpMethod: "",
    coordinateSystem: "",
    remark: ""
  };
  form.value.daAssetVideo = {
    ip: "",
    port: "",
    protocol: "",
    platform: "",
    cameraName: "",
    cameraCode: "",
    artemisPath: "",
    config: {
      cameraName: "",
      cameraCode: "",
      appkey: "",
      appSecret: "",
      artemisPath: ""
    }
  };
}
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});

// 关闭对话框的方法
const closeDialog = async () => {
  emit("update:visible", false);
};

watch(
  () => props.data,
  (newVal) => {
    if (newVal && Object.keys(newVal).length > 0) {
      if (props.data.id) {
        form.value = JSON.parse(JSON.stringify(newVal));
        form.value.datasourceId = Number(form.value.datasourceId) || "";
        form.value.misfirePolicy = Number(form.value.misfirePolicy) || "";
        form.value.concurrent = Number(form.value.concurrent) || "";
        // form.value.status = Number(form.value.status) || "";
        form.value.themeIdList = form.value?.daAssetThemeRelList?.map(item => Number(item.themeId)) || [];
        // 视频配置处理
        if (props.data.type == '5') {
          form.value.daAssetVideo.config = JSON.parse(props.data?.daAssetVideo?.config)
        }
      }
    }
  },
  {
    immediate: true,
    deep: true
  }
);
function removeIdFields(data) {
  if (!Array.isArray(data)) return [];
  return data.map(item => {
    if (item == null || typeof item != 'object') return item;
    const newItem = { ...item };
    delete newItem.id;
    delete newItem.parentId;
    for (const key in newItem) {
      if (Array.isArray(newItem[key])) {
        newItem[key] = removeIdFields(newItem[key]);
      }
    }
    return newItem;
  });
}

function getFormDataByType(type) {
  const commonFields = {
    type: form.value.type,
    catCode: form.value.catCode,
    sourceType: form.value.sourceType,
    name: form.value.name,
    status: form.value.status,
    description: form.value.description,
    source: form.value.source,
    id: form.value.id,
    themeIdList: form.value.themeIdList,
  };

  switch (type) {
    case "1":
      return {
        ...commonFields,
        tableName: form.value.tableName,
        datasourceId: form.value.datasourceId,
        tableComment: form.value.tableComment,
        datasourceType: form.value.datasourceType,
        dbname: form.value.dbname,
      };
    case "2":
      return {
        ...commonFields,
        daAssetApi: { ...form.value.daAssetApi },
        daAssetApiParamList: form.value.daAssetApiParamList,
      };
    case "3":
      return {
        ...commonFields,
        daAssetGis: { ...form.value.daAssetGis },
      };
    case "4":
      return {
        ...commonFields,
        daAssetGeo: { ...form.value.daAssetGeo },
      };
    case "5":
      return {
        ...commonFields,
        daAssetVideo: {
          ip: form.value.daAssetVideo.ip,
          port: form.value.daAssetVideo.port,
          protocol: form.value.daAssetVideo.protocol,
          platform: form.value.daAssetVideo.platform,
          cameraName: form.value.daAssetVideo.config.cameraName,
          cameraCode: form.value.daAssetVideo.config.cameraCode,
          artemisPath: form.value.daAssetVideo.config.artemisPath,
          config: JSON.stringify({
            cameraName: form.value.daAssetVideo.config.cameraName,
            cameraCode: form.value.daAssetVideo.config.cameraCode,
            artemisPath: form.value.daAssetVideo.config.artemisPath,
          }),
        },
      };
    default:
      return commonFields;
  }
}


let daDiscoveryTaskRef = ref(); // 保存数据的方法
let ApiConfigRef = ref()
const saveData = async () => {

  loading.value = true; // 开始加载
  try {
    if (form.value.type === '2') {
      const valid = await ApiConfigRef.value.validateForms();
      if (!valid) {
        proxy.$message.error("请完善表格信息");
        return;
      }
    }
    const valid = await proxy.$refs["daDiscoveryTaskRef"].validate();
    if (valid) {
      if (props.data.type == '5') {
        form.value.daAssetVideo.config = JSON.stringify(form.value?.daAssetVideo?.config)
      }
      form.value = getFormDataByType(form.value.type)
      if (form.value.id != null) {
        await updateDaAsset(form.value);
        proxy.$modal.msgSuccess("修改成功");
      } else {
        if (form.value.type == 2) {
          form.value.daAssetApiParamList = removeIdFields(form.value.daAssetApiParamList);
        }
        let payload = {
          ...form.value,
        }
        if (props.type == 0) {
          payload.projectCode = userStore.projectCode
          payload.projectId = userStore.projectId
        }
        await addDaAsset({
          ...payload
        });
        proxy.$modal.msgSuccess("新增成功");
      }
      emit("confirm", form.value);
      open.value = false;
      emit("update:visible", false);

    } else {
      proxy.$message.error("表单验证失败，请检查表单信息");
    }
  } finally {
    loading.value = false; // 结束加载
  }
};

// 清空表单数据
const clearForm = () => {
  form.value = {
    type: "1",
    catCode: "",
    sourceType: "0",
    name: "",
    themeIdList: [

    ],
    status: 0,
    description: "",
    source: "3",
    // 1
    tableName: "",
    datasourceId: "",
    tableComment: "",
    datasourceType: "",
    dbname: "",
    // 2
    daAssetApi: {
      appName: "",
      url: "",
      httpMethod: "",
      developerName: "",
    },
    daAssetApiParamList: [],
    // 4
    daAssetGeo: {
      fileUrl: "",
      fileType: "",
      elementType: "",
      coordinateSystem: "",
      example: "",
      fileName: ""
    },
    daAssetGis: {
      url: "",
      type: "",
      httpMethod: "",
      coordinateSystem: "",
      remark: "",
    },
    // 5
    daAssetVideo: {
      ip: "",
      port: "",
      protocol: "",
      platform: "",
      cameraName: "",
      cameraCode: "",
      artemisPath: "",//服务上下文
      config: JSON.stringify({
        cameraName: "",
        cameraCode: "",
        artemisPath: "",
      })
    }

  }
};
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
