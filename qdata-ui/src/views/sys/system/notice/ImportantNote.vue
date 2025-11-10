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
  <el-dialog
    class="importantNote-dialog"
    title="qData数据中台"
    v-model="visible"
    @close="close"
    width="842px"
    :show-close="false"
    :close-on-click-modal="false"
    :append-to="$refs['app-container']"
    draggable
    destroy-on-close
  >
    <template #header>
      <div class="header-title">
        <div class="title-text">
          <img src="@/assets/system/images/notice/icon.png" alt="" />
          <span>qData数据中台</span>
        </div>
        <div class="close">
          <img src="@/assets/system/images/notice/close.png" alt="" @click="close" />
        </div>
      </div>
    </template>
    <div class="content">
      <div class="content-text" v-html="notice.noticeContentText"></div>
      <div class="content-line"></div>
      <div class="href" @click="openDocs">点击获取更多信息</div>
    </div>
  </el-dialog>
</template>

<script setup name="ImportantNote">
const visible = ref(false);
const notice = ref({});

/** 显示弹框 */
function show(row) {
  visible.value = true;
  notice.value = row;
}
function close() {
  visible.value = false;
  notice.value = {};
}
function openDocs() {
  window.open("https://qdata.qiantong.tech", "_blank");
}

defineExpose({
  show,
});
</script>

<style lang="scss">
.importantNote-dialog {
  padding: 0;
  border-radius: 4px;
  margin: auto;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  height: fit-content;
  &:not(.is-fullscreen){
    margin: auto !important;
  }
  .el-dialog__header {
    padding: 0;
  }
  .header-title {
    height: 50px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: url("@/assets/system/images/notice/head.png") no-repeat;
    background-size: 100% 100%;
    color: #ffffff;
    padding: 0 24px 0 35px;
    .title-text {
      display: flex;
      align-items: center;
      img {
        width: 40px;
        margin-right: 12px;
      }
      span {
        font-family: PingFang SC;
        font-weight: 800;
        font-size: 20px;
      }
    }
    .close {
      img {
        cursor: pointer;
        width: 18px;
      }
    }
  }
  .content {
    padding: 30px;
    p {
      margin: 10px 0 10px;
    }
    .content-text {
      min-height: 250px;
      max-height: 500px;
      overflow-y: auto;
      &::-webkit-scrollbar {
        width: 4px;
      }
    }
    .content-line {
      height: 1px;
      background: #eeeff0;
      margin: 16px 0;
    }
    .href {
      cursor: pointer;
      font-family: Microsoft YaHei;
      font-size: 14px;
      color: #108ee9;
      &:hover {
        color: #2666fb;
      }
    }
  }
}
</style>
