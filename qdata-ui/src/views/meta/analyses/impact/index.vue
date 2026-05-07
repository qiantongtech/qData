
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
  <div class="app-container">
    <GuideTip tip-id="meta/analyses/impact" />
    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        v-bind="store.dept"
        placeholder="请输入业务域名称"
      />
      <el-main class="pagecont-bottom">
        <div class="shape-content" v-loading="!store.shape.loaded">
          <ImpactShape
            :origin="store.shape.origin"
            :targets="store.shape.targets"
            :type="store.shape.type"
            v-if="store.shape.loaded"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup name="MetaAnalysesImpact">
import DeptTree from "@/components/DeptTree/index.vue";
import ImpactShape from "@/views/meta/components/ImpactShape.vue";
import { getCurrentInstance, reactive } from "vue";
import { listDomain } from "@/api/att/domain/domain.js";
import { tableDataRes, fieldsDataRes } from "@/views/meta/components/data.js";

const { proxy } = getCurrentInstance();

const store = reactive({
  dept: {
    leftWidth: 300,
    deptOptions: [],
    defaultExpand: true,
  },
  shape: {
    loaded: false,
    origin: {},
    targets: [],
    type: "Table",
  },
});

// 获取业务域列表
function getDomains() {
  listDomain().then((res) => {
    store.dept.deptOptions.splice(0, store.dept.deptOptions.length);
    const domains = {
      id: 0,
      code: 0,
      name: "顶级节点",
      children: proxy.handleTree(res.data, "id", "parentId"),
    };
    store.dept.deptOptions.push(domains);
  });
}

function getList() {
  store.shape.loaded = false;
  setTimeout(() => {
    store.shape.origin = tableDataRes.origin;
    store.shape.targets = tableDataRes.targets;
    store.shape.loaded = true;
    store.shape.type = "Table";
  }, 1000);
}

// function handleFieldsShape() {
//     store.shape.loaded = false;
//     setTimeout(() => {
//         store.shape.origin = fieldsDataRes.origin;
//         store.shape.targets = fieldsDataRes.targets;
//         store.shape.loaded = true;
//         store.shape.type = 'Field';
//     }, 1000);
// }

getList();
getDomains();
</script>

<style lang="scss" scoped>
.pagecont-bottom {
  overflow: hidden;
  position: relative;
  .demo-actions {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 9999;
  }
  .shape-content {
    width: 100%;
    height: 100%;
  }
}
</style>
