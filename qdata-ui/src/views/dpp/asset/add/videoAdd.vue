<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <!-- // video  -->
  <template v-if="form.type == '5'">
    <el-form-item
        :label="td('dpp.asset.add.video.platform')"
        prop="daAssetVideo.platform"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.platformRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-select v-model="form.daAssetVideo.platform" :placeholder="td('dpp.asset.add.video.platformPlaceholder')">
        <el-option
            v-for="dict in da_asset_video_platform"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item
        :label="td('dpp.asset.add.video.platformIp')"
        prop="daAssetVideo.ip"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.platformIpRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input v-model="form.daAssetVideo.ip" :placeholder="td('dpp.asset.add.video.platformIpPlaceholder')" />
    </el-form-item>

    <el-form-item
        :label="td('dpp.asset.add.video.platformPort')"
        prop="daAssetVideo.port"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.platformPortRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.port"
          :placeholder="td('dpp.asset.add.video.platformPortPlaceholder')"
      />
    </el-form-item>
    <el-form-item
        :label="td('dpp.asset.add.video.cameraCode')"
        prop="daAssetVideo.config.cameraCode"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.cameraCodeRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.config.cameraCode"
          :placeholder="td('dpp.asset.add.video.cameraCodePlaceholder')"
      />
    </el-form-item>

    <el-form-item
        :label="td('dpp.asset.add.video.cameraName')"
        prop="daAssetVideo.config.cameraName"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.cameraNameRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.config.cameraName"
          :placeholder="td('dpp.asset.add.video.cameraNamePlaceholder')"
      />
    </el-form-item>
    <el-form-item
        :label="td('dpp.asset.add.video.artemisPath')"
        prop="daAssetVideo.config.artemisPath"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.artemisPathRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.config.artemisPath"
          placeholder="/artemis"
      />
    </el-form-item>

    <el-form-item
        :label="td('dpp.asset.add.video.appkey')"
        prop="daAssetVideo.config.appkey"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.appkeyRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.config.appkey"
          :placeholder="td('dpp.asset.add.video.appkeyPlaceholder')"
      />
    </el-form-item>
    <el-form-item
        :label="td('dpp.asset.add.video.appSecret')"
        prop="daAssetVideo.config.appSecret"
        :rules="[
        {
          required: true,
          message: td('dpp.asset.add.video.appSecretRequired'),
          trigger: 'blur',
        },
      ]"
    >
      <el-input
          v-model="localForm.daAssetVideo.config.appSecret"
          :placeholder="td('dpp.asset.add.video.appSecretPlaceholder')"
      />
    </el-form-item>
  </template>
  <!-- tableComment -->
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, watch, getCurrentInstance } from "vue";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/system/user.js";

const { td } = useDefaultLang();
import {
  getTablesByDataSourceId,
  getColumnByAssetId,
} from "@/api/dpp/task/index.js";
import { dppNoPageList } from "@/api/da/asset/asset.js";
import { getDaDatasource, listDaDatasourceNoKafkaByProjectCode } from "@/api/da/dataSource/dataSource.js";
const props = defineProps({
  form: Object,
});
const emit = defineEmits(["update:form"]);

const { proxy } = getCurrentInstance();
const { da_asset_video_platform } = proxy.useDict("da_asset_video_platform");

let localForm = ref({});

watchEffect(() => {
  localForm.value = { ...props.form };

});

</script>
