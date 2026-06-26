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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="dpp/dppAsset.list" />

    <el-container style="90%">
      <DeptTree
        :deptOptions="deptOptions"
        :leftWidth="leftWidth"
        :placeholder="td('dpp.asset.searchCategoryName')"
        ref="DeptTreeRef"
        @node-click="handleNodeClick"
      />
      <el-main>
        <div class="pagecont-top" v-show="showSearch">
          <el-form
            class="btn-style"
            :model="queryParams"
            ref="queryRef"
            :inline="true"

            v-show="showSearch"
            @submit.prevent
          >
            <el-form-item :label="td('dpp.asset.assetName')" prop="name" :label-position="labelPosition">
              <el-input
                style="width: 166px"
                v-model="queryParams.name"
                :placeholder="td('dpp.asset.inputAssetName')"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item :label="td('dpp.asset.publishStatus')" prop="status">
              <el-select
                style="width: 166px"
                v-model="queryParams.status"
                :placeholder="td('dpp.asset.selectPublishStatus')"
                clearable
              >
                <el-option
                  v-for="dict in da_assets_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="td('dpp.asset.belongsToTheme')" prop="themeIdList">
              <el-select
                style="width: 166px"
                v-model="queryParams.themeIdList"
                collapse-tags
                multiple
                :placeholder="td('dpp.asset.selectTheme')"
              >
                <el-option
                  v-for="dict in themeList"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="td('dpp.asset.add.assetType')" prop="status">
              <el-select
                style="width: 166px"
                v-model="queryParams.type"
                :placeholder="td('dpp.asset.selectAssetType')"
                clearable
              >
                <el-option
                  v-for="dict in da_asset_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <div class="form-item-btn">
              <el-button
                plain
                type="primary"
                @click="handleQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
              </el-button>
              <el-button
                @click="resetQuery"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
              </el-button>
              <el-button
                type="primary"
                plain
                @click="handleAdd"
                v-hasPermi="['da:asset:add']"
                @mousedown="(e) => e.preventDefault()"
              >
                <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
              </el-button>
            </div>
          </el-form>
        </div>
        <div class="pagecont-bottom pagecont-bottoms" v-loading="loading">
          <div class="page-list" v-if="total > 0">
            <div
              class="page-item"
              v-for="(item, index) in daAssetList"
              :key="index"
            >
              <div class="item-title">
                <div class="item-title-left">
                  <!-- <img class="title-icon" src="@/assets/images/da/assetTwo/icon-title.svg" alt="" /> -->
                  <span
                    class="item-title-name ellipsis"
                    @click="
                      routeTo(
                        type == 1 ? '/dpp/asset/detail' : '/da/asset/detail',
                        item
                      )
                    "
                    >{{ item.name }}</span
                  >
                  <div v-for="btn in titleBtns" :key="btn.id">
                    <el-tag
                      v-if="item.type == btn.id"
                      style="margin-right: 10px"
                      >{{ btn.name }}</el-tag
                    >
                  </div>
                  <el-tag v-if="!unregistered(item)" style="margin-right: 10px">
                    {{ td('dpp.asset.unregistered') }}
                  </el-tag>
                  <el-tag :type="item.status == 2 ? 'success' : 'warning'">{{
                    item.status == 2 ? td('dpp.asset.published') : td('dpp.asset.unpublished')
                  }}</el-tag>
                </div>
                <div
                  class="item-title-right"
                  v-if="item.type == 1 && unregistered(item)"
                >
                  <div class="li-tab">
                    <span>{{ item.dataCount }}{{ td('dpp.asset.rows') }}</span>
                  </div>
                  <div class="li-bar"></div>
                  <div class="li-tab">
                    <span>{{ item.fieldCount }}{{ td('dpp.asset.cols') }}</span>
                  </div>
                  <div class="li-bar"></div>
                  <div class="li-tab">
                    <span>
                      <overflow-tooltip :text="'93.33' + td('dpp.asset.points')" />
                    </span>
                  </div>
                  <div class="li-bar" v-if="item.datasourceType"></div>
                  <div class="li-tab" v-if="item.datasourceType">
                    <img src="@/assets/images/da/assetTwo/icon-fen-one.svg" alt="" />
                    <span>
                      <overflow-tooltip
                        :text="item.datasourceName"
                        max-width="150px"
                      />
                    </span>
                  </div>
                </div>
              </div>
              <div class="item-con">
                <div class="item-con-left">
                  <div class="item-form item-form1">
                    <div class="form-label">{{ td('dpp.asset.listTableName') }}:</div>
                    <div class="form-value" :title="item.tableName">
                      {{
                        item.tableName && item.tableName != -1
                          ? item.tableName
                          : "-"
                      }}
                    </div>
                  </div>
                  <div class="item-form item-form1">
                    <div class="form-label">{{ td('dpp.asset.belongsToCategory') }}:</div>
                    <div class="form-value" :title="item.catName">
                      {{ item.catName }}
                    </div>
                  </div>
                  <div class="item-form item-form1">
                    <div class="form-label">{{ td('dpp.asset.belongsToTheme') }}:</div>
                    <div
                      class="form-value"
                      :title="
                        item.daAssetThemeRelList?.length
                          ? item.daAssetThemeRelList
                              .map((ele) => ele.themeName)
                              .join(', ')
                          : '-'
                      "
                    >
                      {{
                        item.daAssetThemeRelList?.length
                          ? item.daAssetThemeRelList
                              .map((ele) => ele.themeName)
                              .join(", ")
                          : "-"
                      }}
                    </div>
                  </div>
                  <div class="item-form item-form1">
                    <div class="form-label">{{ td('dpp.asset.createTime') }}:</div>
                    <div class="form-value" :title="item.createTime">
                      {{
                        parseTime(item.createTime, "{y}-{m}-{d} {h}:{i}") || "-"
                      }}
                    </div>
                  </div>
                  <div class="item-form item-form">
                    <div class="form-label">{{ td('dpp.asset.assetDescription') }}:</div>
                    <div class="form-value textarea" :title="item.description">
                      {{ item.description || "-" }}
                    </div>
                  </div>
                  <div class="flex-wrap">
                    <div class="item-form"></div>
                    <div class="form-btns">
                      <div
                        class="form-btn"
                        v-if="!unregistered(item)"
                        @click="handleUpdate(item, 'register')"
                      >
                        <img src="@/assets/images/da/assetTwo/icon-action-button-two.svg" alt="" />
                        <span>{{ td('dpp.asset.register') }}</span>
                      </div>
                      <div
                        class="form-btn"
                        v-if="unregistered(item)"
                        @click="handleView(item)"
                      >
                        <img src="@/assets/images/da/assetTwo/icon-action-button-two.svg" alt="" />
                        <span>{{ td('common.button.details') }}</span>
                      </div>
                      <div
                        class="form-btn"
                        :class="{
                          danger: item.status == 2,
                          warn: item.status != 2,
                        }"
                        v-if="unregistered(item)"
                        @click="handleStatusChange(item)"
                      >
                        <img
                          v-if="item.status == 2"
                          src="@/assets/images/da/assetTwo/icon-action-button-one.svg"
                          alt=""
                        />
                        <img
                          v-else
                          src="@/assets/images/da/assetTwo/icon-action-button-four.svg"
                          alt=""
                        />
                        <span>{{
                          item.status == 2 ? td('dpp.asset.unpublish') : td('dpp.asset.publish')
                        }}</span>
                      </div>
                      <el-dropdown>
                        <div class="form-btn">
                          <img src="@/assets/images/da/assetTwo/icon-action-button-three.svg" alt="" />
                          <span>{{ td('common.button.more') }}</span>
                        </div>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item v-if="unregistered(item)">
                              <el-text
                                type="primary"
                                @click="handleUpdate(item)"
                              >
                                <el-icon> <Edit /> </el-icon>{{ td('common.button.update') }}
                              </el-text>
                            </el-dropdown-item>
                            <el-dropdown-item
                              v-if="unregistered(item) && item.type == 1"
                            >
                              <el-text
                                type="primary"
                                @click="handleRefresh(item)"
                              >
                                <el-icon> <Refresh /> </el-icon>{{ td('dpp.asset.updateData') }}
                              </el-text>
                            </el-dropdown-item>
                            <el-dropdown-item
                              v-if="unregistered(item) && type != 1"
                            >
                              <el-text
                                type="primary"
                                @click="handleApply(item)"
                              >
                                <el-icon> <EditPen /> </el-icon>{{ td('dpp.asset.apply') }}
                              </el-text>
                            </el-dropdown-item>
                            <el-dropdown-item
                              v-if="type != 1 || item.sourceType == 1"
                            >
                              <el-text
                                type="danger"
                                @click="handleDelete(item)"
                              >
                                <el-icon> <Delete /> </el-icon>{{ td('common.button.delete') }}
                              </el-text>
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="empty" v-else>
            <img src="@/assets/images/da/asset/empty.png" alt="" />
            <span>{{ td("common.noSearchResult") }}</span>
          </div>
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </el-main>
    </el-container>

    <!-- 数据资产详情对话框 -->
    <el-dialog
      :title="title"
      v-model="openDetail"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="daAssetRef" :model="form" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.assetName')" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailCategoryCode')" prop="catCode" :label-position="labelPosition">
              <div>
                {{ form.catCode }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailThemeId')" prop="themeId" :label-position="labelPosition">
              <div>
                {{ form.themeId }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailDatasourceId')" prop="datasourceId" :label-position="labelPosition">
              <div>
                {{ form.datasourceId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.listTableName')" prop="tableName" :label-position="labelPosition">
              <div>
                {{ form.tableName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailTableComment')" prop="tableComment" :label-position="labelPosition">
              <div>
                {{ form.tableComment }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailDataCount')" prop="dataCount" :label-position="labelPosition">
              <div>
                {{ form.dataCount }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.detailFieldCount')" prop="fieldCount" :label-position="labelPosition">
              <div>
                {{ form.fieldCount }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.status')" prop="status" :label-position="labelPosition">
              <div>
                {{ form.status }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('common.texts.description')" prop="description" :label-position="labelPosition">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('common.texts.remark')" prop="remark" :label-position="labelPosition">
              <div>
                {{ form.remark }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="small" @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      v-model="upload.open"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-upload
        ref="uploadRef"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">{{ td('common.upload.dragOrClick') }}</div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox
                v-model="upload.updateSupport"
              />{{ td('common.upload.updateExistingData') }}
            </div>
            <span>{{ td('common.upload.fileFormat') }}</span>
            <el-link
              type="primary"
              :underline="false"
              style="font-size: 12px; vertical-align: baseline"
              @click="importTemplate"
              >{{ td('common.upload.downloadTemplate') }}</el-link
            >
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="upload.open = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitFileForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <CreateEditModal
      :deptOptions="deptOptions"
      :visible="open"
      :title="title"
      @update:visible="open = $event"
      @confirm="getList"
      :data="form"
      :isRegister="isRegister"
      type="1"
    />

    <!-- 申请数据资产对话框 -->
    <el-dialog
      :title="titleApply"
      v-model="openApply"
      width="800px"
      :append-to="$refs['app-container']"
      draggable
    >
      <el-form
        ref="daAssetApplyRef"
        :model="formApply"
        :rules="rulesApply"
        label-width="100px"
       :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.assetName')" :label-position="labelPosition">
              <el-input v-model="formApply.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyEnglishName')" :label-position="labelPosition">
              <el-input v-model="formApply.tableName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyThemeName')" :label-position="labelPosition">
              <el-input v-model="formApply.themeName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyDatasource')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyDatabaseAddress')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceIp" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyDatabaseType')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceType" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.asset.applyAssetDescription')" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="formApply.description"
                :rows="3"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyProject')" prop="projectCode" :label-position="labelPosition">
              <el-select
                v-model="formApply.projectCode"
                @change="handleSelectProject"
                :placeholder="td('dpp.asset.selectApplyProject')"
              >
                <el-option
                  v-for="item in projectOptions"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('dpp.asset.applyContactPhone')" prop="phone" :label-position="labelPosition">
              <el-input
                v-model="formApply.phone"
                :placeholder="td('dpp.asset.inputContactPhone')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('dpp.asset.applyReason')" prop="applyReason" :label-position="labelPosition">
              <el-input
                type="textarea"
                v-model="formApply.applyReason"
                :rows="3"
                :placeholder="td('dpp.asset.inputApplyReason')"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="openApply = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitApplyForm">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup name="DppAsset">
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import {
  listDaAsset,
  getDaAsset,
  delDaAsset,
  addDaAsset,
  updateDaAsset,
  listDppAsset,
  startDaDiscoveryTask,
  dataLineage,
} from "@/api/da/asset/asset";

import CreateEditModal from "./add/index.vue";
import { currentUser } from "@/api/att/project/project.js";
import DeptTree from "@/components/DeptTree";
import { listAttAssetCat } from "@/api/att/cat/assetCat/assetCat.js";
import { getToken } from "@/utils/auth.js";
import { addDaAssetApply } from "@/api/da/assetApply/assetApply";
import useUserStore from "@/store/system/user";
import { getThemeList } from "@/api/att/theme/theme.js";
import OverflowTooltip from "@/components/OverflowTooltip";
const { proxy } = getCurrentInstance();
const { da_assets_status, da_asset_source, da_asset_type } = proxy.useDict(
  "da_assets_status",
  "da_asset_source",
  "da_asset_type"
);
const tagMultiple = ref(false);
const assetId = ref(null);
const tagIds = ref([]);
const daAssetList = ref([]);
const AttTagList = ref([]);
const isRegister = ref(false);

const unregistered = (item) => {
  return item.createType == undefined || item.createType == 2;
};
// 列显隐信息
const columns = ref([
  { key: 0, label: td('common.texts.number'), visible: true },
  { key: 1, label: td('dpp.asset.assetName'), visible: true },
  { key: 2, label: td('dpp.asset.assetDescription'), visible: true },
  { key: 3, label: td('dpp.asset.belongsToCategory'), visible: true },
  { key: 9, label: td('dpp.asset.add.assetType'), visible: true },
  { key: 4, label: td('dpp.asset.applyThemeName'), visible: true },
  { key: 5, label: td('dpp.asset.detailDataCount'), visible: true },
  { key: 6, label: td('common.texts.status'), visible: true },
  { key: 7, label: td('dpp.asset.createTime'), visible: true },
  { key: 8, label: td('common.texts.updatedTime'), visible: true },
]);
let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
}
const getColumnVisibility = (key) => {
  const column = columns.value.find((col) => col.key === key);
  // 如果没有找到对应列配置，默认显示
  if (!column) return true;
  // 如果找到对应列配置，根据visible属性来控制显示
  return column.visible;
};
const deptOptions = ref(undefined);
const leftWidth = ref(300); // 初始左侧宽度
const isResizing = ref(false); // 判断是否正在拖拽
let startX = 0; // 鼠标按下时的初始位置// 初始左侧宽度
const open = ref(false);
const openDetail = ref(false);
const openApply = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const titleApply = ref("");
const projectOptions = ref([]);
const defaultSort = ref({ prop: "create_time", order: "desc" });
const router = useRouter();
const userStore = useUserStore();
const route = useRoute();
let type = route.query.type || null;
// 图标
const getDatasourceIcon = (type) => {
  switch (type) {
    case "DM8":
      return new URL("@/assets/images/common/dpp/ds-dm.png", import.meta.url).href;
    case "Oracle11":
      return new URL("@/assets/images/common/dpp/img-oracle-one.png", import.meta.url)
        .href;
    case "MySql":
      return new URL("@/assets/images/common/dpp/ds-mysql.png", import.meta.url)
        .href;
    case "Hive":
      return new URL("@/assets/images/common/dpp/ds-hive.png", import.meta.url)
        .href;
    case "Sqlerver":
      return new URL(
        "@/assets/images/common/dpp/ds-sqlserver.png",
        import.meta.url
      ).href;
    case "Kafka":
      return new URL("@/assets/images/common/dpp/ds-kafka.png", import.meta.url)
        .href;
    case "HDFS":
      return new URL("@/assets/images/common/dpp/hdfs.png", import.meta.url)
        .href;
    case "SHELL":
      return new URL("@/assets/images/common/dpp/img-shell-one.png", import.meta.url)
        .href;
    case "Kingbase8":
      return new URL("@/assets/images/common/dpp/ds-kingbase.png", import.meta.url)
        .href;
    default:
      return null;
  }
};
/*** 用户导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/da/daAsset/importData",
});
const options = [
  {
    value: 0,
    label: td('dpp.asset.applyAsset'),
  },
  {
    value: 1,
    label: td('dpp.asset.createAsset'),
  },
];
const data = reactive({
  form: {},
  formApply: {
    projectCode: null,
    phone: null,
    applyReason: null,
  },
  queryParams: {
    themeIdList: [],
    pageNum: 1,
    pageSize: 10,
    name: null,
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    status: null,
    description: null,
    createTime: null,
    type: null,
    params: {
      sourceType: [0, 1],
    },
  },
  rules: {
    name: [{ required: true, message: td('dpp.asset.assetNameRequired'), trigger: "blur" }],
    catCode: [{ required: true, message: td('dpp.asset.catCodeRequired'), trigger: "blur" }],
    themeId: [{ required: true, message: td('dpp.asset.themeIdRequired'), trigger: "blur" }],
    datasourceId: [
      { required: true, message: td('dpp.asset.datasourceIdRequired'), trigger: "blur" },
    ],
    tableName: [{ required: true, message: td('dpp.asset.tableNameRequired'), trigger: "blur" }],
  },
  rulesApply: {
    projectCode: [
      { required: true, message: td('dpp.asset.applyProjectRequired'), trigger: "change" },
    ],
    phone: [{ required: true, message: td('dpp.asset.phoneRequired'), trigger: "blur" }],
  },
});

const { queryParams, form, formApply, rules, rulesApply } = toRefs(data);

watch(
  () => userStore.projectCode,
  (newCode) => {
    getList();
  },
  { immediate: true } // `immediate` 为 true 表示页面加载时也会立即执行一次 watch
);

function submitApplyForm() {
  proxy.$refs["daAssetApplyRef"].validate((valid) => {
    if (valid) {
      formApply.value.id = null;
      formApply.value.updateBy = null;
      formApply.value.updaterId = null;
      formApply.value.updateTime = null;
      formApply.value.validFlag = null;
      formApply.value.delFlag = null;
      addDaAssetApply(formApply.value).then((response) => {
        proxy.$modal.msgSuccess(td('dpp.asset.applySuccess'));
        openApply.value = false;
        getList();
      });
    }
  });
}

function handleApply(row) {
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    formApply.value = response.data;
    openApply.value = true;
    titleApply.value = td('dpp.asset.applyDataAsset');
    formApply.value.phone = userStore.phonenumber;
    formApply.value.assetId = _id;
    formApply.value.assetName = response.data.name;
  });
  currentUser().then((response) => {
    projectOptions.value = response.data;
  });
}

function handleSelectProject(value) {
  formApply.value.projectCode = value;
  const project = projectOptions.value.find((item) => item.code === value);
  formApply.value.projectId = project.id;
  formApply.value.projectName = project.name;
}

/** 查询数据资产列表 */
function getList() {
  if (!queryParams.value?.orderByColumn) {
    queryParams.value.orderByColumn = defaultSort.value.prop;
    queryParams.value.isAsc = defaultSort.value.order;
  }
  loading.value = true;
  console.log(type);

  if (type == 1) {
    queryParams.value.projectCode = userStore.projectCode;
    queryParams.value.projectId = userStore.projectId;
    listDppAsset(queryParams.value).then((response) => {
      daAssetList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  } else {
    listDaAsset(queryParams.value).then((response) => {
      daAssetList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
  }
}

// 取消按钮
function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    name: null,
    catCode: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    status: null,
    description: null,
    validFlag: null,
    delFlag: null,
    createBy: null,
    creatorId: null,
    createTime: null,
    type: null,
    updateBy: null,
    updaterId: null,
    updateTime: null,
    remark: null,
  };
  proxy.resetForm("daAssetRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
const DeptTreeRef = ref(null);
/** 重置按钮操作 */
function resetQuery() {
  if (DeptTreeRef.value?.resetTree) {
    DeptTreeRef.value.resetTree();
  }
  queryParams.value.params.sourceType = [0, 1];
  queryParams.value.catCode = "";
  queryParams.value.pageNum = 1;
  queryParams.value.type = null;
  queryParams.value.sourceType = null;
  reset();
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

const startResize = (event) => {
  isResizing.value = true;
  startX = event.clientX;
  document.addEventListener("mousemove", updateResize);
  document.addEventListener("mouseup", stopResize);
};
const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener("mousemove", updateResize);
  document.removeEventListener("mouseup", stopResize);
};
const updateResize = (event) => {
  if (isResizing.value) {
    const delta = event.clientX - startX; // 计算鼠标移动距离
    leftWidth.value += delta; // 修改左侧宽度
    startX = event.clientX; // 更新起始位置
    // 使用 requestAnimationFrame 来减少页面重绘频率
    requestAnimationFrame(() => {});
  }
}; /** 查询部门下拉树结构 */
/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
  getList();
}
function getAssetCat() {
  listAttAssetCat({ validFlag: true }).then((response) => {
    deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
    deptOptions.value = [
      {
        label: "",
        name: td('dpp.asset.myApplications'),
        value: "",
        id: "wdsq",
        children: [],
      },
      {
        label: "",
        name: td('dpp.asset.assetCategory'),
        value: "",
        id: 0,
        children: deptOptions.value,
      },
    ];
  });
}
/** 新增按钮操作 */
function handleAdd() {
  isRegister.value = false;
  reset();
  open.value = true;
  title.value = td('dpp.asset.addDataAsset');
}

/** 修改按钮操作 */
function handleUpdate(row, register) {
  if (register == "register") {
    isRegister.value = true;
  } else {
    isRegister.value = false;
  }
  reset();
  const _id = row.id || ids.value;
  loading.value = true;
  getDaAsset(_id).then((response) => {
    if (register == "register") {
      response.data.datasourceId = "";
      response.data.tableName = "";
    }
    form.value = response.data;
    open.value = true;
    title.value = td('dpp.asset.editDataAsset');
    loading.value = false;
  });
}
/** 删除按钮操作 */
function handleDelete(row) {
  // proxy.$message.error("功能开发中....");
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('dpp.asset.confirmDeleteAsset') + _ids + td('dpp.asset.confirmDeleteAssetEnd'))
    .then(function () {
      return delDaAsset(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
    })
    .catch(() => {});
}
function handleView(row) {
  if (!unregistered(row)) {
    return proxy.$modal.msgWarning(td('dpp.asset.unregisteredWarning'));
  }
  console.log("直接跳转数据资产的详情页面");
  routeTo("/dpp/asset/detail", row);
}
function addAttTagData(row) {
  assetId.value = row.id;
  tagIds.value = row.tagIds;
  tagMultiple.value = true;
}
/** 详情按钮操作 */
function handleDetail(row) {
  reset();
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    form.value = response.data;
    openDetail.value = true;
    title.value = td('dpp.asset.dataAssetDetail');
  });
}
const titleBtns = [
  {
    id: 2,
    name: td('dpp.asset.assetTypeAPI'),
    icon: "da-api",
  },
  {
    id: 1,
    name: td('dpp.asset.assetTypeTable'),
    icon: "da-database",
  },
  {
    id: 7,
    name: td('dpp.asset.assetTypeFile'),
    icon: "da-document",
  },
];
function handleRefresh(row) {
  const _id = row.id;
  loading.value = true;
  startDaDiscoveryTask({ id: _id })
    .then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess(td('dpp.asset.updateSuccess'));
        getList();
      } else {
        proxy.$modal.msgWarning(td('dpp.asset.updateFailed'));
      }
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "da/asset/export",
    {
      ...queryParams.value,
    },
    `daAsset_${new Date().getTime()}.xlsx`
  );
}

/** ---------------- 导入相关操作 -----------------**/
/** 导入按钮操作 */
function handleImport() {
  upload.title = td('dpp.asset.dataAssetImport');
  upload.open = true;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `daAsset_template_${new Date().getTime()}.xlsx`
  );
}

/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
}

function handleNodeClick(data) {
  queryParams.value.catCode = data.code;
  if (data.id == "wdsq") {
    queryParams.value.sourceType = "0";
  } else {
    queryParams.value.sourceType = null;
  }
  handleQuery();
}

/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert(
    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
      response.msg +
      "</div>",
    td('dpp.asset.importResult'),
    { dangerouslyUseHTMLString: true }
  );
  getList();
};
/** ---------------------------------**/

function routeTo(link, row) {
  if (link !== "" && link.indexOf("http") !== -1) {
    window.location.href = link;
    return;
  }
  if (link !== "") {
    if (link === router.currentRoute.value.path) {
      window.location.reload();
    } else {
      router.push({
        path: link,
        query: {
          id: row.id,
        },
      });
    }
  }
}

/** 启用禁用开关 */
function handleStatusChange(row) {
  const text = row.status === "2" ? td('dpp.asset.unpublish') : td('dpp.asset.publish');
  const status = row.status === "2" ? "1" : "2";
  proxy.$modal
    .confirm(td('dpp.asset.confirmPublish') + text + '"' + row.name + '"' + td('dpp.asset.confirmPublishEnd'))
    .then(function () {
      updateDaAsset({ id: row.id, status: status }).then((res) => {
        if (res.code == 200) {
          proxy.$modal.msgSuccess(text + td('dpp.asset.publishSuccess'));
          getList();
        }
      });
    });
}
queryParams.value.orderByColumn = defaultSort.value.prop;
queryParams.value.isAsc = defaultSort.value.order;
getList();
getAssetCat();
getAssetThemeList();
</script>

<style scoped lang="scss">
.form-item-btn {
  display: inline-flex;
  margin-bottom: 14px;
  vertical-align: middle;
}

.butgdlist {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.button-inner {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  /* 图标和文字间距 */
}

.fix-icon {
  //width: 16px; /* 固定图标占位宽度，使图标文字对齐一致 */
  //text-align: center;
  margin-left: -10px;
}

::v-deep {
  .selectlist .el-tag.el-tag--info {
    background: #f3f8ff !important;
    border: 0px solid #6ba7ff !important;
    color: #2666fb !important;
  }
}

.app-container {
  margin: 13px 15px;
}

.el-main {
  padding: 2px 0px;
  // box-shadow: 1px 1px 3px rgba(0, 0, 0, .2);
}

//上传附件样式调整
::v-deep {
  // .el-upload-list{
  //    display: flex;
  // }
  .el-upload-list__item {
    width: 100%;
    height: 25px;
  }
}

.pagecont-bottom {
  padding: 0;
  background-color: transparent;
  box-shadow: none;

  .pagination-container {
    height: 60px;
    background: #ffffff;
    border-radius: 2px;
    margin: 15px 0 0;
    padding: 14px 20px !important;

    :deep(.el-pagination) {
      right: 20px;
    }
  }
}

.page-list {
  height: 69.6vh;
  height: auto;
  /* 或者直接删掉这行 */
  max-height: none;
  /* 保证不被限制高度 */
  overflow: visible;
  /* 不产生内部滚动条 */

  &::-webkit-scrollbar {
    width: 2px;
  }

  .page-item {
    padding: 18px 18px 14px;
    background: #fff;
    margin-bottom: 14px;
    border-radius: 2px;

    .item-title {
      width: 100%;
      padding-bottom: 10px;
      margin-bottom: 8px;
      border-bottom: 1px solid #eeeeee;

      .item-title-left {
        width: 60%;
        display: inline-flex;
        align-items: center;

        .title-icon {
          width: 22px;
          height: 20px;
          margin-right: 8px;
        }

        .item-title-name {
          font-family: PingFang SC;
          font-size: 16px;
          font-weight: 600;
          color: #3d446e;
          margin-right: 16px;
        }

        .title-btn {
          min-width: 58px;
          padding: 0px 8px;
          height: 24px;
          display: flex;
          justify-content: center;
          align-items: center;
          background: #f4f4f5;
          border: 1px solid #d9d9d9;
          color: #9d9fa2;
          margin-right: 8px;
          border-radius: 2px;

          svg {
            font-size: 12px;
          }

          span {
            margin-left: 3px;
            font-family: PingFang SC;
            font-weight: normal;
            font-size: 12px;
          }

          &.act {
            background: #ecf9ff;
            border: 1px solid #91d5ff;
            color: #1d6fe9;
          }
        }

        .title-tag {
          min-width: 52px;
          width: 52px;
          height: 24px;
          background: #ff9800;
          color: #ffffff;
          display: flex;
          justify-content: center;
          align-items: center;
          border-radius: 4px;
          font-family: PingFang SC;
          font-weight: normal;
          font-size: 12px;

          &.success {
            background: #0baa84;
          }
        }
      }

      .item-title-right {
        width: 40%;
        display: inline-flex;
        align-items: center;
        justify-content: flex-end;

        .li-tab {
          display: flex;
          align-items: center;

          img {
            width: 16px;
            height: 16px;
            margin-top: 2px;
            margin-right: 4px;
          }

          span {
            font-family: PingFang SC;
            font-weight: normal;
            font-size: 13px;
            color: #3d446e;
          }
        }

        .li-bar {
          width: 1px;
          height: 12px;
          background: #c9cfd8;
          margin: 0 10px;
        }
      }
    }

    .item-con {
      width: 100%;
      display: flex;

      .item-con-left {
        width: 100%;
        display: flex;
        flex-wrap: wrap;

        .item-form {
          width: 100%;
          display: flex;
          font-family: PingFang SC;
          line-height: 28px;
          gap: 10px;

          .form-label {
            font-weight: 400;
            font-size: 14px;
            color: #8c8c8c;
            white-space: nowrap;
          }

          .form-value {
            flex:1;
            font-weight: 500;
            font-size: 14px;
            color: #262626;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;

            &.textarea {
              line-height: 30px;
              white-space: normal;
              display: -webkit-box !important;
              overflow: hidden;
              text-overflow: ellipsis;
              word-break: break-all;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical !important;
            }
          }

          &.item-form2 {
            width: 66%;
          }

          &.item-form1 {
            width: 20%;
          }
        }
      }

      .flex-wrap {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }

      .form-btns {
        display: flex;
        align-items: flex-end;
        justify-content: flex-end;
        .form-btn {
          margin-right: 10px;
          cursor: pointer;
          min-width: 64px;
          height: 24px;
          background: #e8f1ff;
          border-radius: 2px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          color: #3d446e;
          padding: 0px 8px;

          &:last-child {
            margin-right: 0;
          }

          span {
            margin-left: 4px;
            font-family: PingFang SC;
            font-weight: normal;
            font-size: 12px;
            white-space: nowrap;
          }

          &.warn {
            min-width: 82px;
            background: #e1f9fc;
            color: #039792;
          }

          &.danger {
            min-width: 82px;
            background: #fbefdd;
            color: #ff7a00;
          }

          &.last-child {
            margin-right: 0;
          }
        }
      }
    }
  }
}

.empty {
  min-height: calc(100vh - 250px);
  background: #ffffff;
  border-radius: 2px;
  display: flex;
  flex-direction: column;
  align-items: center;

  img {
    width: 200px;
    height: 180px;
    margin: 240px 0 40px;
  }

  span {
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 18px;
    color: rgba(0, 0, 0, 0.65);
  }
}

:deep(.tag-view) {
  &:not(.is-fullscreen) {
    margin-top: 25vh !important;
  }

  .el-dialog__body {
    height: 195px;
  }
}
</style>
<style scoped lang="scss">
@media screen and (max-width: 1366px) {
  .page-list .page-item {
    .item-title {
      display: block;

      .item-title-left {
        width: 100%;
      }

      .item-title-right {
        width: 100%;
        justify-content: flex-start;
      }
    }

    .item-con .item-con-left .item-form {
      &.item-form1 {
        width: 50%;
      }
    }

    .item-con .flex-wrap {
      flex-wrap: wrap;
    }
  }
}
</style>
