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
-->

<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="8">
        <el-card style="height: calc(100vh - 125px)">
          <template #header>
            <Collection style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ t('sys.monitor.cacheList.title') }}</span>
            <el-button
              style="float: right; padding: 3px 0"
              link
              type="primary"
              icon="Refresh"
              @click="refreshCacheNames()"
            ></el-button>
          </template>
          <el-table
            stripe
            v-loading="loading"
            :data="cacheNames"
            :height="tableHeight"
            highlight-current-row
            @row-click="getCacheKeys"
            style="width: 100%"
          >
            <el-table-column
              :label="t('common.display.index')"
              width="80"
              type="index"
            ></el-table-column>

            <el-table-column
              :label="t('sys.monitor.cacheList.cacheName')"
              align="center"
              prop="cacheName"
              :show-overflow-tooltip="true"
              :formatter="nameFormatter"
            ></el-table-column>

            <el-table-column
              :label="t('common.texts.remark')"
              align="center"
              prop="remark"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              :label="t('common.texts.operation')"
              width="60"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template #default="scope">
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleClearCacheName(scope.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card style="height: calc(100vh - 125px)">
          <template #header>
            <Key style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ t('sys.monitor.cacheList.keyList') }}</span>
            <el-button
              style="float: right; padding: 3px 0"
              link
              type="primary"
              icon="Refresh"
              @click="refreshCacheKeys()"
            ></el-button>
          </template>
          <el-table
            stripe
            v-loading="subLoading"
            :data="cacheKeys"
            :height="tableHeight"
            highlight-current-row
            @row-click="handleCacheValue"
            style="width: 100%"
          >
            <el-table-column
              :label="t('common.display.index')"
              width="80"
              type="index"
            ></el-table-column>
            <el-table-column
              :label="t('sys.monitor.cacheList.cacheKeyName')"
              align="center"
              :show-overflow-tooltip="true"
              :formatter="keyFormatter"
            >
            </el-table-column>
            <el-table-column
              :label="t('common.texts.operation')"
              width="60"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template #default="scope">
                <el-button
                  link
                  type="danger"
                  icon="Delete"
                  @click="handleClearCacheKey(scope.row)"
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card :bordered="false" style="height: calc(100vh - 125px)">
          <template #header>
            <Document style="width: 1em; height: 1em; vertical-align: middle;" /> <span style="vertical-align: middle;">{{ t('sys.monitor.cacheList.cacheContent') }}</span>
            <el-button
              style="float: right; padding: 3px 0"
              link
              type="primary"
              icon="Refresh"
              @click="handleClearCacheAll()"
              >{{ t('sys.monitor.cacheList.clearAll') }}</el-button
            >
          </template>
          <el-form :model="cacheForm">
            <el-row :gutter="32">
              <el-col :offset="1" :span="22">
                <el-form-item :label="t('sys.monitor.cacheList.cacheName') + ':'" prop="cacheName">
                  <el-input v-model="cacheForm.cacheName" :readOnly="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item :label="t('sys.monitor.cacheList.cacheKeyName') + ':'" prop="cacheKey">
                  <el-input v-model="cacheForm.cacheKey" :readOnly="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item :label="t('sys.monitor.cacheList.cacheContent') + ':'" prop="cacheValue">
                  <el-input
                    v-model="cacheForm.cacheValue"
                    type="textarea"
                    :rows="8"
                    :readOnly="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="CacheList">
import { useI18n } from 'vue-i18n'
import { listCacheName, listCacheKey, getCacheValue, clearCacheName, clearCacheKey, clearCacheAll } from "@/api/system/monitor/cache.js";

const { t } = useI18n();
const { proxy } = getCurrentInstance();

const cacheNames = ref([]);
const cacheKeys = ref([]);
const cacheForm = ref({});
const loading = ref(true);
const subLoading = ref(false);
const nowCacheName = ref("");
const tableHeight = ref(window.innerHeight - 200);

/** 查询缓存名称列表 */
function getCacheNames() {
  loading.value = true;
  listCacheName().then(response => {
    cacheNames.value = response.data;
    loading.value = false;
  });
}

/** 刷新缓存名称列表 */
function refreshCacheNames() {
  getCacheNames();
  proxy.$modal.msgSuccess(t('sys.monitor.cacheList.refreshSuccess'));
}

/** 清理指定名称缓存 */
function handleClearCacheName(row) {
  clearCacheName(row.cacheName).then(response => {
    proxy.$modal.msgSuccess(t('sys.monitor.cacheList.clearNameSuccess', { name: row.cacheName }));
    getCacheKeys();
  });
}

/** 查询缓存键名列表 */
function getCacheKeys(row) {
  const cacheName = row !== undefined ? row.cacheName : nowCacheName.value;
  if (cacheName === "") {
    return;
  }
  subLoading.value = true;
  listCacheKey(cacheName).then(response => {
    cacheKeys.value = response.data;
    subLoading.value = false;
    nowCacheName.value = cacheName;
  });
}

/** 刷新缓存键名列表 */
function refreshCacheKeys() {
  getCacheKeys();
  proxy.$modal.msgSuccess(t('sys.monitor.cacheList.refreshKeySuccess'));
}

/** 清理指定键名缓存 */
function handleClearCacheKey(cacheKey) {
  clearCacheKey(cacheKey).then(response => {
    proxy.$modal.msgSuccess(t('sys.monitor.cacheList.clearKeySuccess', { key: cacheKey }));
    getCacheKeys();
  });
}

/** 列表前缀去除 */
function nameFormatter(row) {
  return row.cacheName.replace(":", "");
}

/** 键名前缀去除 */
function keyFormatter(cacheKey) {
  return cacheKey.replace(nowCacheName.value, "");
}

/** 查询缓存内容详细 */
function handleCacheValue(cacheKey) {
  getCacheValue(nowCacheName.value, cacheKey).then(response => {
    cacheForm.value = response.data;
  });
}

/** 清理全部缓存 */
function handleClearCacheAll() {
  clearCacheAll().then(response => {
    proxy.$modal.msgSuccess(t('sys.monitor.cacheList.clearAllSuccess'));
  });
}

getCacheNames();
</script>
