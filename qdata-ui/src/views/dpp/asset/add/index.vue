<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <!-- 新增或修改数据资产地图任务对话框 -->
  <el-dialog
      :title="title"
      v-model="visibleDialog"
      class="medium-dialog"
      draggable
      destroy-on-close
  >
    <el-form
        ref="daDiscoveryTaskRef"
        :model="form"
        label-width="110px"
        @submit.prevent
        class="column-form"
    >
      <el-form-item label="资产类型" prop="type">
        <el-select
            v-model="form.type"
            placeholder="请输入类型"
            filterable
            disabled
            style="width: 100%"
        >
          <el-option
              v-for="dict in da_asset_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <component
          :is="currentFormComponent"
          v-model:form="form"
          ref="ApiConfigRef"
          v-if="form.createType == '2'"
          :isRegister="props.isRegister"
          :type="props.type"
      />

      <qt-form-item
          label="资产状态"
          prop="status"
          :rules="[
          {
            required: true,
            message: '请选择资产状态',
            trigger: 'change',
          },
        ]"
          :tip="{
          content: `未发布：仅在开发或测试环境中使用，不可被下游调用<br/>已发布：已在生产环境上线，可供其他系统或用户访问`,
          custom: true,
        }"
      >
        <el-radio-group v-model="form.status">
          <el-radio value="1">未发布</el-radio>
          <el-radio value="2">已发布</el-radio>
        </el-radio-group>
      </qt-form-item>
<!--      <el-form-item-->
<!--          v-if="!props.isRegister"-->
<!--          label="创建类型"-->
<!--          prop="status"-->
<!--          :rules="[-->
<!--          {-->
<!--            required: true,-->
<!--            message: '请选择创建类型',-->
<!--            trigger: 'change',-->
<!--          },-->
<!--        ]"-->
<!--          :tip="{-->
<!--          content: `注册资产：正式纳入资产管理，支持后续治理、质量监控、权限控制<br/>暂不注册资产：仅记录信息，不参与后续流程，适用于临时测试或非核心资产【不确定】`,-->
<!--          custom: true,-->
<!--        }"-->
<!--      >-->
<!--        <el-radio-group-->
<!--            v-model="form.createType"-->
<!--            :disabled="form.id"-->
<!--            @change="handleCreateChange"-->
<!--        >-->
<!--          <el-radio value="1">暂不注册资产</el-radio>-->
<!--          <el-radio value="2">注册资产</el-radio>-->
<!--        </el-radio-group>-->
<!--      </el-form-item>-->

      <template
          v-if="
          form.type == '111' && (form.id != undefined || form.createType == '2')
        "
      >
        <el-form-item
            label="服务类型"
            prop="daAssetGis.type"
            :rules="[
            { required: true, message: '请输入服务类型', trigger: 'blur' },
          ]"
        >
          <el-select
              v-model="form.daAssetGis.type"
              placeholder="请选择参数类型"
          >
            <el-option
                v-for="dict in da_asset_gis_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
            label="服务地址"
            prop="daAssetGis.url"
            :rules="[
            { required: true, message: '请输入服务地址', trigger: 'blur' },
          ]"
        >
          <el-input
              v-model="form.daAssetGis.url"
              placeholder="请输入服务地址"
          />
        </el-form-item>
        <el-form-item
            label="请求类型"
            prop="daAssetGis.type"
            :rules="[
            { required: true, message: '请选择请求类型', trigger: 'blur' },
          ]"
        >
          <el-select
              v-model="form.daAssetGis.httpMethod"
              placeholder="请选择请求类型"
          >
            <el-option
                v-for="dict in da_asset_api_method"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </template>

      <template
          v-if="
          form.type == '111' && (form.id != undefined || form.createType == '2')
        "
      >
        <el-form-item
            label="文件类型"
            prop="daAssetGeo.fileType"
            :rules="[
            { required: true, message: '请输入文件类型', trigger: 'blur' },
          ]"
        >
          <el-select
              v-model="form.daAssetGeo.fileType"
              placeholder="请选择参数类型"
          >
            <el-option
                v-for="dict in da_asset_geo_file_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
            label="上传文件"
            prop="daAssetGeo.fileUrl"
            :rules="[
            { required: true, message: '请上传文件', trigger: 'fileUrl' },
          ]"
        >
          <FileUploadbtn
              :limit="1"
              v-model="form.daAssetGeo.fileUrl"
              :dragFlag="false"
              :fileType="['geojson']"
              :fileSize="50"
              :isShowTip="false"
              v-model:fileSize="form.fileSize"
              v-model:fileExt="form.fileType"
          />
        </el-form-item>
      </template>

      <excelAdd ref="excelAddRef" class="row-full" />

      <el-form-item :label="t('common.texts.description')" prop="description" class="row-full">
        <el-input
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            :min-height="192"
            v-model="form.description"
            :placeholder="t('common.form.descriptionPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="t('common.texts.remark')" prop="remark" class="row-full">
        <el-input
            type="textarea"
            maxlength="500个字符"
            show-word-limit
            :min-height="192"
            v-model="form.remark"
            :placeholder="t('common.form.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <!-- 关闭按钮 -->
        <el-button @click="closeDialog">{{ t('common.button.cancel') }}</el-button>
        <!-- 保存按钮 -->
        <el-button type="primary" @click="saveData" :loading="loading"
        >{{ t('common.button.confirm') }}</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { defineProps, defineEmits, ref, computed, watch } from "vue";
// import Crontab from "@/components/Crontab/ruleBase.vue";
// import { getDaDiscoveryTask } from "@/api/da/discovery/discoveryTask";
// 数据库表
import tableConfigForm from "./tableAdd.vue";
// 外部api
import apiConfigForm from "./apiAdd.vue";
// 视频
import daAssetVideo from "./videoAdd.vue";
// 矢量数据 上传
import excelAdd from "./excelAdd.vue";
// 非结构化数据
import Unstructured from "./unstructuredAdd.vue";
import { getThemeList } from "@/api/att/theme/theme.js";
import useUserStore from "@/store/system/user";
const userStore = useUserStore();
const { proxy } = getCurrentInstance();
const {
  da_asset_type,
  da_asset_gis_type,
  da_asset_geo_file_type,
  da_asset_api_method,
} = proxy.useDict(
    "da_asset_type",
    "da_asset_gis_type",
    "da_asset_geo_file_type",
    "da_asset_api_method"
);
import { addDaAsset, updateDaAsset, bindResources } from "@/api/da/asset/asset";

const { t } = useI18n();
// import ExcelUploadDialog from "@/views/dpp/etl/components/formComponents/components/ExcelUploadDialog.vue";
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "表单标题" },
  data: { type: Object, default: () => ({}) },
  deptOptions: { type: Object, default: () => ({}) },
  type: { type: Boolean, default: false },
  isEdit: { type: Boolean, default: false },
  isRegister: { type: Boolean, default: false },
});
const excelAddRef = ref(null);
let loading = ref(false); // 加载状态（全局）
const isInitializing = ref(false);
const isResetting = ref(false);

const emit = defineEmits(["update:visible", "confirm"]);

const daDiscoveryTaskRef = ref(null);
let ApiConfigRef = ref();

// 创建一个本地响应式数据，用来修改表单内容
const form = ref({
  type: "1",
  createType: "2",
  catCode: "",
  sourceType: "0",
  name: "",
  themeIdList: [],
  status: "1",
  description: "",
  source: "3",
  // 1
  tableName: "",
  datasourceId: "",
  tableComment: "",
  datasourceType: "",
  dbname: "",

  // 新增字段
  tableType: "1",
  dataLayerId: null,
  businessDomainId: null,
  businessCategoryCode: "",
  dataDomainId: null,
  themeDomainId: null,
  themeDomainCode: "",
  tableCase: 1,
  namingSpec: "",

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
    fileName: "",
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
      artemisPath: "",
    },
  },
  daAssetFiles: {
    url: null,
    startData: "",
    tableFields: [],
    startColumn: "",
  },
  //   7
  fileInfo: {},
  filePath: "",
});

const currentFormComponent = computed(() => {
  switch (form.value.type) {
    case "1":
      return tableConfigForm;
    case "2":
      return apiConfigForm;
      // case "5":
      //   return daAssetVideo;
      // case "6":
      //   return excelAdd;
    case "7":
      return Unstructured;
    default:
      return null;
  }
});

let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
  excelAddRef.value.show(form.value);
}
// eslint-disable-next-line no-unused-vars
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
// eslint-disable-next-line no-unused-vars
function handleShowCron() {
  expression.value = form.value.cronExpression;
  openCron.value = true;
}
/** 确定后回传值 */
// eslint-disable-next-line no-unused-vars
function crontabFill(value) {
  form.value.cronExpression = value;
}
watch(
    () => props.visible,
    async (newVal) => {
      if (newVal) {
        isInitializing.value = true;
        getAssetThemeList();
        if (props.data.id) {
          // props.data.id
        } else {
          form.value.catCode = props.data.catCode || "";
          console.log("🚀 ~ props.data:", props.data.catCode);
        }
        isInitializing.value = false;
      } else {
        clearForm();
      }
    }
);
const handleTypeChange = () => {
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
    developerName: "",
  };
  form.value.daAssetApiParamList = [];
  form.value.daAssetGeo = {
    fileUrl: "",
    fileType: "",
    elementType: "",
    coordinateSystem: "",
    example: "",
    fileName: "",
  };
  form.value.daAssetGis = {
    url: "",
    type: "",
    httpMethod: "",
    coordinateSystem: "",
    remark: "",
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
      artemisPath: "",
    },
  };
  form.value.daAssetFiles = {
    url: null,
    startData: "",
    tableFields: [],
    startColumn: "",
  };
  excelAddRef.value && excelAddRef.value.show(form.value);
};
const handleCreateChange = () => {
  excelAddRef.value && excelAddRef.value.show(form.value);
};
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
        isInitializing.value = true;
        if (props.data.id) {
          form.value = JSON.parse(JSON.stringify(newVal));
          form.value.datasourceId = Number(form.value.datasourceId) || "";
          form.value.misfirePolicy = Number(form.value.misfirePolicy) || "";
          form.value.concurrent = Number(form.value.concurrent) || "";
          // form.value.status = Number(form.value.status) || "";
          form.value.themeIdList =
              form.value?.daAssetThemeRelList?.map((item) =>
                  Number(item.themeId)
              ) || [];
          form.value.createType = props.isRegister ? "2" : form.value.createType;

          // 回显新增字段
          form.value.tableType = form.value.tableType || "1";
          form.value.dataLayerId = form.value.dataLayerId
              ? Number(form.value.dataLayerId)
              : null;
          form.value.businessDomainId = form.value.businessDomainId
              ? Number(form.value.businessDomainId)
              : form.value.businessCategoryId
                  ? Number(form.value.businessCategoryId)
                  : null;
          form.value.dataDomainId = form.value.dataDomainId
              ? Number(form.value.dataDomainId)
              : null;
          form.value.themeDomainId = form.value.themeDomainId
              ? Number(form.value.themeDomainId)
              : null;
          form.value.tableCase =
              form.value.tableCase !== undefined ? Number(form.value.tableCase) : 1;

          // 视频配置处理
          if (props.data.type == "5") {
            if (form.value.daAssetVideo == null) {
              form.value.daAssetVideo = {
                ip: "",
                port: "",
                protocol: "",
                platform: "",
                cameraName: "",
                cameraCode: "",
                artemisPath: "", //服务上下文
                config: JSON.stringify({
                  cameraName: "",
                  cameraCode: "",
                  artemisPath: "",
                }),
              };
            } else {
              form.value.daAssetVideo.config = JSON.parse(
                  props.data?.daAssetVideo?.config
              );
            }
          }
          if (form.value.daAssetGis == null) {
            form.value.daAssetGis = {
              url: "",
              type: "",
              httpMethod: "",
              coordinateSystem: "",
              remark: "",
            };
          }
          if (form.value.daAssetApi == null) {
            form.value.daAssetApi = {
              appName: "",
              url: "",
              httpMethod: "",
              developerName: "",
            };
          }
          if (form.value.daAssetGeo == null) {
            form.value.daAssetGeo = {
              fileUrl: "",
              fileType: "",
              elementType: "",
              coordinateSystem: "",
              example: "",
              fileName: "",
            };
          }
        } else {
          // 新增模式，如果传入了 type，则同步到 form 中
          if (newVal.type) {
            form.value.type = newVal.type;
          }
          if (newVal.catCode) {
            form.value.catCode = newVal.catCode;
          }
        }
        setTimeout(() => {
          isInitializing.value = false;
        }, 200);
      }
    },
    {
      immediate: true,
      deep: true,
    }
);
function removeIdFields(data) {
  if (!Array.isArray(data)) return [];
  return data.map((item) => {
    if (item == null || typeof item != "object") return item;
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
    createType: form.value.createType,
    description: form.value.description,
    source: form.value.source,
    id: form.value.id,
    themeIdList: form.value.themeIdList,

    // 新增字段
    tableType: form.value.tableType,
    dataLayerId: form.value.dataLayerId,
    businessDomainId: form.value.businessDomainId,
    businessCategoryId: form.value.businessDomainId, // 兼容性映射
    businessCategoryCode: form.value.businessCategoryCode,
    dataDomainId: form.value.dataDomainId,
    themeDomainId: form.value.themeDomainId,
    themeDomainCode: form.value.themeDomainCode,
    tableCase: form.value.tableCase,
    namingSpec: form.value.namingSpec,
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
    case "6":
      return {
        ...commonFields,

        daAssetFiles: {
          url: proxy.$refs.excelAddRef.form.daAssetFiles.url,
          startData: proxy.$refs.excelAddRef.form.daAssetFiles.startData,
          startColumn: proxy.$refs.excelAddRef.form.daAssetFiles.startColumn,
          type: proxy.$refs.excelAddRef.form.daAssetFiles.type,
          name: proxy.$refs.excelAddRef.form.daAssetFiles.name,
        },
      };
    case "7": {
      const fileInfo = ApiConfigRef.value
          ? Object.fromEntries(
              ApiConfigRef.value.fileDesc.map((item) => [item.key, item.value])
          )
          : {};
      return {
        ...commonFields,
        datasourceId: form.value.datasourceId,
        fileInfo: fileInfo,
        filePath: fileInfo.path,
      };
    }
    default:
      return commonFields;
  }
}

const saveData = async () => {
  loading.value = true; // 开始加载
  try {
    if (form.value.type === "2" && form.value.createType == "2") {
      const valid = await ApiConfigRef.value.validateForms();
      if (!valid) {
        proxy.$message.warning("校验未通过，请完善表格信息");
        return;
      }
    }
    const valid = await proxy.$refs["daDiscoveryTaskRef"].validate();
    if (valid) {
      if (props.data.type == "5") {
        if (form.value.daAssetVideo == null) {
          form.value.daAssetVideo = {
            ip: "",
            port: "",
            protocol: "",
            platform: "",
            cameraName: "",
            cameraCode: "",
            artemisPath: "", //服务上下文
            config: JSON.stringify({
              cameraName: "",
              cameraCode: "",
              artemisPath: "",
            }),
          };
        } else {
          form.value.daAssetVideo.config = JSON.stringify(
              form.value?.daAssetVideo?.config
          );
        }
      }
      form.value = getFormDataByType(form.value.type);
      if (form.value.id != null) {
        if (form.value.createType == "1") {
          await bindResources(form.value);
          proxy.$modal.msgSuccess("配置成功");
        } else {
          await updateDaAsset(form.value);
          proxy.$modal.msgSuccess(t('common.message.editSuccess'));
        }
      } else {
        if (form.value.type == 2) {
          form.value.daAssetApiParamList = removeIdFields(
              form.value.daAssetApiParamList
          );
        }
        let payload = {
          ...form.value,
        };
        if (props.type == 1) {
          payload.projectCode = userStore.projectCode;
          payload.projectId = userStore.projectId;
        }
        await addDaAsset({
          ...payload,
        });
        proxy.$modal.msgSuccess(t('common.message.addSuccess'));
      }
      emit("update:visible", false);
      emit("confirm", form.value);
    } else {
      proxy.$message.warning("验证失败，请检查表单信息");
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
    themeIdList: [],
    status: "1",
    createType: "2",
    description: "",
    source: "3",

    // 新增字段
    tableType: "1",
    dataLayerId: null,
    businessDomainId: null,
    businessCategoryCode: "",
    dataDomainId: null,
    themeDomainId: null,
    themeDomainCode: "",
    tableCase: 1,
    namingSpec: "",

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
      fileName: "",
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
      artemisPath: "", //服务上下文
      config: JSON.stringify({
        cameraName: "",
        cameraCode: "",
        artemisPath: "",
      }),
    },
    daAssetFiles: {
      url: null,
      startData: "",
      startColumn: "",
      tableFields: [],
    },
    //   7
    filePath: "",
  };
};
</script>

<style scoped lang="less">
.blue-text {
  color: #2666fb;
}
</style>
