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
  For brand customization, please contact the official team for authorization.
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
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<template>
  <div class="app-container">
    <div class="pagecont-top" style="padding-bottom: 15px">
      <div class="infotop">
        <div class="infotop-title mb15">
          {{ form.name || "-" }}
        </div>
        <el-row :gutter="2">
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">编号</div>
              <div class="infotop-row-value">
                {{ form.id || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">英文名称</div>
              <div class="infotop-row-value">
                {{ form.engName || "-" }}
              </div>
            </div>
          </el-col>
          <!-- <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">中文名称</div>
              <div class="infotop-row-value">
                {{ form.name || "-" }}
              </div>
            </div>
          </el-col> -->
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">类型</div>
              <div class="infotop-row-value">
                <dict-tag :options="dp_data_elem_code_type" :value="form.type" />
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin: 2px 0;">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">描述</div>
              <div class="infotop-row-value">
                {{ form.description || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">数据元类目</div>
              <div class="infotop-row-value">
                {{ form.catName || "-" }}
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">字段类型</div>
              <div class="infotop-row-value">
                <dict-tag :options="column_type" :value="form.columnType" />
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="infotop-row border-top">
              <div class="infotop-row-lable">状态</div>
              <div class="infotop-row-value">
                <dict-tag :options="sys_disable" :value="form.status" />
              </div>
            </div>
          </el-col>

        </el-row>
      </div>
    </div>
    <!-- 标签页部分 -->
    <div class="pagecont-bottom">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="关联清洗规则" name="1" lazy>
          <cleanRule :dataElemId="dataElemId" dataType="2" />
        </el-tab-pane>
        <el-tab-pane label="关联稽查规则" name="2" lazy>
          <auditRule :dataElemId="dataElemId" dataType="1" />
        </el-tab-pane>
        <el-tab-pane label="关联信息" name="3" lazy>
          <asset />
        </el-tab-pane>
        <el-tab-pane label="详细信息" name="5" lazy>
          <info :daDiscoveryTaskDetail="form" />

        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup name="dataElemDetailDialog">
import { onMounted } from "vue";

const { proxy } = getCurrentInstance();

import { getDpDataElem } from "@/api/dp/dataElem/dataElem";

import cleanRule from "@/views/dp/dataElem/detail/column/cleanRule";
import auditRule from "@/views/dp/dataElem/detail/column/auditRule";
import asset from "@/views/dp/dataElem/detail/components/asset.vue";
import info from "@/views/dp/dataElem/detail/column/info.vue";
import { useRoute } from "vue-router";
const { column_type, sys_disable, dp_data_elem_code_type } = proxy.useDict(
  "column_type",
  "sys_disable",
  "dp_data_elem_code_type"
);

const dpDataElemRuleRelList = ref([]);

const data = reactive({
  form: {},
  activeName: "1",
});
const { form, activeName } = toRefs(data);
const dataElemId = ref("");
const route = useRoute();
dataElemId.value = route.query.id;

/** 详情按钮操作 */
function getDetail() {
  const id = dataElemId.value;
  if (!id) return;
  getDpDataElem(id).then((response) => {
    form.value = response.data;
  });
}

// 页面加载时获取数据
onMounted(() => {
  getDetail();
});

// 返回列表页
function goBack() {
  router.go(-1);
}
</script>

<style scoped lang="scss">
.app-container {
  margin: 15px 15px 0px 15px;

  .pagecont-bottom {
    min-height: calc(100vh - 345px) !important;
  }
}
</style>
