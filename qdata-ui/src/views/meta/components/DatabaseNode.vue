
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
  <div class="database-node">
    <div class="database-header">
      <span>{{ store.name }}</span>
    </div>
    <div class="database-content">
      <div class="black-item">
        <div class="label">状态：</div>
        <div class="content">
          <el-button type="primary" size="small">启用</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup name="DatabaseNode">
const store = reactive({
  name: "",
});

const getNode = inject("getNode");

function setupData(data) {
  store.name = data.name;
}

onMounted(() => {
  const node = getNode();
  setupData(node.data);
  node.on("change:data", (data) => {
    setupData(data);
  });
});
</script>

<style lang="scss" scoped>
.database-node {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
  border: 2px solid #1890ff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.database-header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: white;
  padding: 6px 12px;
  font-weight: 600;
  font-size: 13px;
  text-align: center;
  border-radius: 6px 6px 0 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  line-height: 20px;
  span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.black-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 8px;
  font-size: 12px;
  transition: background-color 0.2s ease;
}
</style>
