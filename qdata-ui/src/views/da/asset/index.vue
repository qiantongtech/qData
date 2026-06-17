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
  <div class="app-container" ref="app-container">
    <GuideTip tip-id="da/daAsset.list" />

    <el-container>
      <DeptTree
        ref="DeptTreeRef"
        type="asset"
        :deptOptions="deptOptions"
        :placeholder="td('da.asset.deptTreePlaceholder')"
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
            <el-form-item :label="td('da.asset.assetType')" prop="status">
              <el-select
                style="width: 166px"
                v-model="queryParams.type"
                :placeholder="td('da.asset.assetTypePlaceholder')"
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
            <el-form-item :label="td('da.asset.assetName')" prop="name" :label-position="labelPosition">
              <el-input
                v-model="queryParams.name"
                :placeholder="td('da.asset.assetNamePlaceholder')"
                clearable
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item :label="td('da.asset.publishStatus')" prop="status">
              <el-select
                style="width: 166px"
                v-model="queryParams.status"
                :placeholder="td('da.asset.publishStatusPlaceholder')"
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
              <el-dropdown
                @command="handleAdd"
                v-hasPermi="['da:asset:add']"
                class="create-dropdown"
                placement="bottom-start"
                popper-class="create-table-dropdown-popper"
                @mousedown="(e) => e.preventDefault()"
                @visible-change="(val) => (dropdownVisible = val)"
              >
                <el-button type="primary" plain>
                  <el-icon><Plus /></el-icon>
                  <span>{{ td('da.asset.register') }}</span>
                  <div class="divider"></div>
                  <el-icon
                    class="arrow-icon el-icon--right"
                    :class="{ 'is-reverse': dropdownVisible }"
                  >
                    <ArrowDown />
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu class="create-table-dropdown-menu">
                    <el-dropdown-item
                      v-for="dict in da_asset_type"
                      :key="dict.value"
                      :command="dict.value"
                    >
                      <svg-icon
                        color="#3367FC"
                        :icon-class="
                          dict.value == '1'
                            ? 'da-database'
                            : dict.value == '2'
                            ? 'da-api'
                            : 'da-document'
                        "
                      />
                      <span>{{ dict.label }}</span>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
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
                  <img
                    class="title-icon"
                    src="@/assets/da/asset2/tit.svg"
                    alt=""
                  />
                  <el-link
                    type="primary"
                    :underline="false"
                    class="item-title-name ellipsis"
                    @click="
                      routeTo(
                        type == 1 ? '/dpp/asset/detail' : '/da/asset/detail',
                        item
                      )
                    "
                    >{{ item.name }}</el-link
                  >
                  <div v-for="btn in titleBtns" :key="btn.id">
                    <div
                      class="title-btn"
                      :class="{ act: item.type == btn.id }"
                      v-if="item.type == btn.id"
                    >
                      <svg-icon :icon-class="btn.icon" />
                      <span>{{ btn.name }}</span>
                    </div>
                  </div>
                  <div class="title-tag" v-if="!unregistered(item)">{{ td('da.asset.unregistered') }}</div>
                  <div
                    class="title-tag"
                    :class="{ success: item.status == 2 }"
                    v-else
                  >
                    {{ item.status == 2 ? td('da.asset.published') : td('da.asset.unpublished') }}
                  </div>
                </div>
                <div
                  class="item-title-right"
                  v-if="item.type == 1 && unregistered(item)"
                >
                  <el-tooltip
                    effect="light"
                    :content="td('da.asset.dataCountTooltip')"
                    placement="top"
                  >
                    <div class="li-tab">
                      <img src="@/assets/da/asset2/fen (2).svg" alt="" />
                      <span>{{ item.dataCount }}{{ td('da.asset.rowUnit') }}</span>
                    </div>
                  </el-tooltip>
                  <div class="li-bar"></div>
                  <el-tooltip
                    effect="light"
                    :content="td('da.asset.fieldCountTooltip')"
                    placement="top"
                  >
                    <div class="li-tab">
                      <img src="@/assets/da/asset2/fen (3).svg" alt="" />
                      <span>{{ item.fieldCount }}{{ td('da.asset.columnUnit') }}</span>
                    </div>
                  </el-tooltip>
                  <div class="li-bar"></div>
                  <el-tooltip
                    effect="light"
                    :content="td('da.asset.qualityScoreTooltip')"
                    placement="top"
                  >
                    <div class="li-tab">
                      <img src="@/assets/da/asset2/fen (4).svg" alt="" />
                      <span>
                        <overflow-tooltip :text="'93.33' + td('da.asset.scoreUnit')" />
                      </span>
                    </div>
                  </el-tooltip>
                  <div class="li-bar" v-if="item.datasourceType"></div>
                  <el-tooltip
                    effect="light"
                    :content="td('da.asset.dataSourceTooltip')"
                    placement="top"
                  >
                    <div class="li-tab" v-if="item.datasourceType">
                      <img src="@/assets/da/asset2/fen (1).svg" alt="" />
                      <span>
                        <overflow-tooltip
                          :text="item.datasourceName"
                          max-width="150px"
                        />
                      </span>
                    </div>
                  </el-tooltip>
                </div>
              </div>
              <div class="item-con">
                <div class="item-con-left">
                  <div class="flex-wrap">
                    <!-- 库表类型 -->
                    <template v-if="item.type == 1">
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.tableName') }}:</div>
                        <div class="form-value" :title="item.tableName">
                          {{ item.tableName || "-" }}
                        </div>
                      </div>
                      <div class="item-form item-form-type">
                        <div class="form-label">{{ td('da.asset.tableType') }}:</div>
                        <div class="form-value">
                          <dict-tag
                            size="small"
                            :options="table_type"
                            :value="item.tableType"
                          />
                        </div>
                      </div>
                      <div class="item-form item-form-hierarchy">
                        <div class="form-label">{{ td('da.asset.belongLevel') }}:</div>
                        <div
                          class="form-value"
                          :title="
                            formatHierarchyDisplayName(item, item.tableType)
                          "
                        >
                          {{ formatHierarchyDisplayName(item, item.tableType) }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.createdTime') }}:</div>
                        <div class="form-value" :title="item.createTime">
                          {{
                            parseTime(item.createTime, "{y}-{m}-{d} {h}:{i}") ||
                            "-"
                          }}
                        </div>
                      </div>
                    </template>
                    <!-- 文件类型 -->
                    <template v-else-if="item.type == 7">
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.fileName') }}:</div>
                        <div class="form-value" :title="item.name">
                          {{ item.name || "-" }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.dataDomain') }}:</div>
                        <div class="form-value" :title="item.dataDomainName">
                          {{ item.dataDomainName || "-" }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.belongTopic') }}:</div>
                        <div class="form-value" :title="themeNames(item)">
                          {{ themeNames(item) }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.createdTime') }}:</div>
                        <div class="form-value" :title="item.createTime">
                          {{
                            parseTime(item.createTime, "{y}-{m}-{d} {h}:{i}") ||
                            "-"
                          }}
                        </div>
                      </div>
                    </template>
                    <!-- API类型 -->
                    <template v-else-if="item.type == 2">
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.appName') }}:</div>
                        <div class="form-value" :title="item.appName">
                          {{ item.appName || "-" }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.dataDomain') }}:</div>
                        <div class="form-value" :title="item.dataDomainName">
                          {{ item.dataDomainName || "-" }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.belongTopic') }}:</div>
                        <div class="form-value" :title="themeNames(item)">
                          {{ themeNames(item) }}
                        </div>
                      </div>
                      <div class="item-form item-form1">
                        <div class="form-label">{{ td('da.asset.createdTime') }}:</div>
                        <div class="form-value" :title="item.createTime">
                          {{
                            parseTime(item.createTime, "{y}-{m}-{d} {h}:{i}") ||
                            "-"
                          }}
                        </div>
                      </div>
                    </template>
                  </div>
                  <div class="item-form item-form">
                    <div class="form-label">{{ td('da.asset.assetDesc') }}:</div>
                    <div class="form-value textarea" :title="item.description">
                      {{ item.description || "-" }}
                    </div>
                  </div>
                  <div class="flex-wrap">
                    <div class="item-form">
                      <div class="form-label">{{ td('da.asset.dataTag') }}:</div>
                      <div class="form-value tag-container">
                        <span v-if="!item.tagNames?.length">-</span>
                        <template v-else>
                          <el-tag
                            v-for="tag in item.tagNames"
                            :key="tag"
                            class="mr10"
                          >
                            {{ tag }}
                          </el-tag>
                        </template>
                      </div>
                    </div>
                    <div class="form-btns">
                      <div
                        class="form-btn"
                        v-if="!unregistered(item)"
                        @click="handleUpdate(item, 'register')"
                      >
                        <img src="@/assets/da/asset2/btn (2).svg" alt="" />
                        <span>{{ td('da.asset.register') }}</span>
                      </div>
                      <div
                        class="form-btn"
                        v-if="unregistered(item)"
                        @click="handleView(item)"
                      >
                        <img src="@/assets/da/asset2/btn (2).svg" alt="" />
                        <span>{{ td('common.button.details') }}</span>
                      </div>
                      <!-- danger -->
                      <!--  -->
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
                          src="@/assets/da/asset2/btn (1).svg"
                          alt=""
                        />
                        <img
                          v-else
                          src="@/assets/da/asset2/btn (4).svg"
                          alt=""
                        />
                        <span
                          >{{ item.status == 2 ? td('da.asset.revokePublish') : td('da.asset.publish') }}
                        </span>
                      </div>

                      <el-dropdown>
                        <div class="form-btn">
                          <img src="@/assets/da/asset2/btn (3).svg" alt="" />
                          <span>{{ td('da.asset.more') }}</span>
                        </div>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item
                              v-if="unregistered(item) && item.type == 1"
                            >
                              <el-text
                                type="primary"
                                @click="handleUpdate(item)"
                              >
                                <el-icon> <Edit /> </el-icon>{{ td('common.button.update') }}
                              </el-text>
                            </el-dropdown-item>
                            <el-dropdown-item
                              v-if="unregistered(item) && item.type != 1"
                            >
                              <el-text
                                type="primary"
                                @click="handleRefresh(item)"
                              >
                                <el-icon> <Refresh /> </el-icon>{{ td('da.asset.updateData') }}
                              </el-text>
                            </el-dropdown-item>
                            <el-dropdown-item v-if="unregistered(item)">
                              <el-text
                                type="primary"
                                @click="addAttTagData(item)"
                              >
                                <el-icon> <Pointer /> </el-icon>{{ td('da.asset.addTag') }}
                              </el-text>
                            </el-dropdown-item>
                            <!-- <el-dropdown-item
                              v-if="unregistered(item) && type != 1"
                            >
                              <el-text
                                type="primary"
                                @click="handleApply(item)"
                              >
                                <el-icon> <EditPen /> </el-icon>{{ td('da.asset.apply') }}
                              </el-text>
                            </el-dropdown-item> -->
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
            <img src="@/assets/da/asset/empty.png" alt="" />
            <span>{{ td('common.noSearchResult') }}</span>
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
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
      </template>
      <el-form ref="daAssetRef" :model="form" label-width="80px" :label-position="labelPosition">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.assetName')" prop="name">
              <div>
                {{ form.name }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.catCode')" prop="catCode" :label-position="labelPosition">
              <div>
                {{ form.catCode }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.topicId')" prop="themeId" :label-position="labelPosition">
              <div>
                {{ form.themeId }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.datasourceId')" prop="datasourceId" :label-position="labelPosition">
              <div>
                {{ form.datasourceId }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.tableName')" prop="tableName" :label-position="labelPosition">
              <div>
                {{ form.tableName }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.tableDesc')" prop="tableComment" :label-position="labelPosition">
              <div>
                {{ form.tableComment }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.dataCount')" prop="dataCount" :label-position="labelPosition">
              <div>
                {{ form.dataCount }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.fieldCount')" prop="fieldCount" :label-position="labelPosition">
              <div>
                {{ form.fieldCount }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.status')" prop="status" :label-position="labelPosition">
              <div>
                {{ form.status }}
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.description')" prop="description" :label-position="labelPosition">
              <div>
                {{ form.description }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.remark')" prop="remark" :label-position="labelPosition">
              <div>
                {{ form.remark }}
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="cancel">{{ td('common.button.close') }}</el-button>
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
      type="0"
    />
    <!-- 用户导入对话框 -->
    <el-dialog
      :title="td('da.asset.addTagTitle')"
      class="tag-view"
      v-model="tagMultiple"
      width="600px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <el-col :span="24">
        <el-form-item :label="td('da.asset.tagLabel')" :label-position="labelPosition">
          <el-select
            v-model="tagIds"
            :placeholder="td('da.asset.tagPlaceholder')"
            filterable
            multiple
            collapse-tags
            collapse-tags-tooltip
            :max-collapse-tags="5"
            style="width: calc(100% - 117px)"
          >
            <el-option
              v-for="dict in AttTagList"
              :key="dict.id + ''"
              :label="dict.name"
              :value="dict.id + ''"
            ></el-option>
          </el-select>
          <el-button
            type="primary"
            style="margin-left: 14px"
            plain
            @click="tagOpened = true"
            @mousedown="(e) => e.preventDefault()"
          >
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('da.asset.addTagButton') }}
          </el-button>
        </el-form-item>
      </el-col>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="tagMultiple = false">{{ td('common.button.cancel') }}</el-button>
          <el-button type="primary" @click="submitTag">{{ td('common.button.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
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
            <el-form-item :label="td('da.asset.assetName')" :label-position="labelPosition">
              <el-input v-model="formApply.name" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.englishName')" :label-position="labelPosition">
              <el-input v-model="formApply.tableName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.topicName')" :label-position="labelPosition">
              <el-input v-model="formApply.themeName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.datasource')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.dbAddress')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceIp" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="td('da.asset.dbType')" :label-position="labelPosition">
              <el-input v-model="formApply.datasourceType" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.asset.description')" :label-position="labelPosition">
              <el-input
                type="textarea"
                maxlength="500"
                show-word-limit
                v-model="formApply.description"
                :rows="3"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="td('da.asset.applyProject')" prop="projectCode" :label-position="labelPosition">
              <el-select
                v-model="formApply.projectCode"
                @change="handleSelectProject"
                :placeholder="td('da.asset.applyProjectPlaceholder')"
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
            <el-form-item :label="td('da.asset.contactPhone')" prop="phone" :label-position="labelPosition">
              <el-input
                v-model="formApply.phone"
                :placeholder="td('da.asset.contactPhonePlaceholder')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="td('da.asset.applyReason')" prop="applyReason" :label-position="labelPosition">
              <el-input
                type="textarea"
                maxlength="500"
                show-word-limit
                v-model="formApply.applyReason"
                :rows="3"
                :placeholder="td('da.asset.applyReasonPlaceholder')"
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
    <tagForm v-model="tagOpened" @confirm="getListTag" ref="addTagFormRef" />
  </div>
</template>

<script setup name="Asset">
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
  getTreeData,
} from "@/api/da/asset/asset";
import CreateEditModal from "@/views/dpp/asset/add/index.vue";
import tagForm from "@/views/dpp/asset/add/tagForm.vue";
import OverflowTooltip from "@/components/OverflowTooltip";
import { currentUser } from "@/api/att/project/project.js";
import DeptTree from "@/components/DeptTree/index1.vue";
import { getToken } from "@/utils/auth.js";
import { addDaAssetApply } from "@/api/da/assetApply/assetApply";
import useUserStore from "@/store/system/user";
import { useProjectStore } from "@/store/project/project";
import { getThemeList } from "@/api/att/theme/theme.js";
import { listDict } from "@/api/att/tag/tag.js";
import { addAttTagAssetRel } from "@/api/att/tag/tagAssetRel.js";
import { formatHierarchyDisplayName } from "@/utils/dm/utils";
import { usePageRefresh } from "@/composables/usePageRefresh";
const { proxy } = getCurrentInstance();
const { da_assets_status, da_asset_type, table_type } = proxy.useDict(
  "da_assets_status",
  "da_asset_type",
  "table_type"
);

usePageRefresh("da_asset", () => {
  getList();
  getAssetCat();
});

const unregistered = (item) => {
  return item.createType == undefined || item.createType == 2;
};
const tagMultiple = ref(false);
const assetId = ref(null);
const tagIds = ref([]);
const daAssetList = ref([]);
const AttTagList = ref([]);
const isRegister = ref(false);
let tagOpened = ref(false);
const titleBtns = [
  {
    id: 2,
    name: td('da.asset.apiType'),
    icon: "da-api",
  },
  {
    id: 1,
    name: td('da.asset.dbTable'),
    icon: "da-database",
  },
  {
    id: 7,
    name: td('da.asset.unstructuredData'),
    icon: "da-document",
  },
];
// 列显隐信息
const columns = ref([
  { key: 0, label: td('da.asset.columnVisibility.id'), visible: true },
  { key: 1, label: td('da.asset.columnVisibility.assetName'), visible: true },
  { key: 2, label: td('da.asset.columnVisibility.assetDesc'), visible: true },
  { key: 3, label: td('da.asset.columnVisibility.assetCat'), visible: true },
  { key: 8, label: td('da.asset.columnVisibility.assetType'), visible: true },
  { key: 4, label: td('da.asset.columnVisibility.topicName'), visible: true },
  { key: 5, label: td('da.asset.columnVisibility.dataCount'), visible: true },
  { key: 6, label: td('da.asset.columnVisibility.status'), visible: true },
  { key: 7, label: td('da.asset.columnVisibility.createTime'), visible: true },
  { key: 8, label: td('da.asset.columnVisibility.updateTime'), visible: true },
]);
let themeList = ref([]);
async function getAssetThemeList() {
  const response = await getThemeList();
  themeList.value = response.data;
}

const deptOptions = ref(undefined);
const open = ref(false);
const openDetail = ref(false);
const dropdownVisible = ref(false);
const openApply = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const total = ref(0);
const title = ref("");
const titleApply = ref("");
const projectOptions = ref([]);
const defaultSort = ref({ prop: "create_time", order: "desc" });
const router = useRouter();
const userStore = useUserStore();
const projectStore = useProjectStore();
const route = useRoute();
let type = route.query.type || null;

const data = reactive({
  form: {},
  formApply: {
    projectCode: null,
    phone: null,
    applyReason: null,
  },
  queryParams: {
    themeIdList: [],
    type: null,
    pageNum: 1,
    pageSize: 10,
    name: null,
    catCode: null,
    id: null,
    dataLayerId: null,
    themeId: null,
    datasourceId: null,
    tableName: null,
    tableComment: null,
    dataCount: null,
    fieldCount: null,
    status: null,
    description: null,
    createTime: null,
    businessCategoryCode: null,
    dataDomainId: null,
    themeDomainCode: null,
    params: {
      sourceType: [0, 1],
    },
  },
  rules: {
    name: [{ required: true, message: td('da.asset.assetNameRequired'), trigger: "blur" }],
    catCode: [{ required: true, message: td('da.asset.catCodeRequired'), trigger: "blur" }],
    themeId: [{ required: true, message: td('da.asset.topicIdRequired'), trigger: "blur" }],
    datasourceId: [
      { required: true, message: td('da.asset.datasourceIdRequired'), trigger: "blur" },
    ],
    tableName: [{ required: true, message: td('da.asset.tableNameRequired'), trigger: "blur" }],
  },
  rulesApply: {
    projectCode: [
      { required: true, message: td('da.asset.applyProjectRequired'), trigger: "change" },
    ],
    phone: [{ required: true, message: td('da.asset.contactPhoneRequired'), trigger: "blur" }],
  },
});

const { queryParams, form, formApply, rules, rulesApply } = toRefs(data);

function submitApplyForm() {
  proxy.$refs["daAssetApplyRef"].validate((valid) => {
    if (valid) {
      formApply.value.id = null;
      formApply.value.updateBy = null;
      formApply.value.updaterId = null;
      formApply.value.updateTime = null;
      formApply.value.validFlag = null;
      formApply.value.delFlag = null;
      formApply.value.status = null;
      const payload = { ...formApply.value };
      delete payload.themeName;
      addDaAssetApply(payload).then((response) => {
        proxy.$modal.msgSuccess(td('da.asset.applySuccess'));
        openApply.value = false;
        getList();
      });
    }
  });
}

function addAttTagData(row) {
  assetId.value = row.id;
  tagIds.value = row.tagIds;
  tagMultiple.value = true;
}
function handleRefresh(row) {
  const _id = row.id;
  loading.value = true;
  startDaDiscoveryTask({ id: _id })
    .then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess(td('da.asset.updateSuccess'));
        getList();
      } else {
        proxy.$modal.msgWarning(td('da.asset.updateFailed'));
      }
    })
    .finally(() => {
      loading.value = false;
    });
}

function handleApply(row) {
  const _id = row.id || ids.value;
  getDaAsset(_id).then((response) => {
    formApply.value = response.data;
    openApply.value = true;
    titleApply.value = td('da.asset.applyTitle');
    formApply.value.phone = userStore.phonenumber;
    formApply.value.assetId = _id;
    formApply.value.assetName = response.data.name;
    const relList = response.data?.daAssetThemeRelList || [];
    if (relList.length) {
      formApply.value.themeName = relList
        .map((ele) => ele.themeName)
        .join(", ");
    } else if (response.data?.themeId && Array.isArray(themeList.value)) {
      const theme = themeList.value.find((t) => t.id == response.data.themeId);
      formApply.value.themeName = theme?.name || "";
    } else {
      formApply.value.themeName = "";
    }
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

function handleView(row) {
  if (!unregistered(row)) {
    return proxy.$modal.msgWarning(td('da.asset.unregisteredTip'));
  }
  routeTo(type == 1 ? "/dpp/asset/detail" : "/da/asset/detail", row);
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
  getListTag();
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
  queryParams.value.catCode = null;
  queryParams.value.id = null;
  queryParams.value.dataLayerId = null;
  queryParams.value.businessCategoryCode = null;
  queryParams.value.dataDomainId = null;
  queryParams.value.themeDomainCode = null;
  queryParams.value.pageNum = 1;
  queryParams.value.type = null;
  reset();
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 查询部门下拉树结构 */
function getAssetCat() {
  console.log(`getAssetCat->`);
  projectStore.getAssetDeptTree().then((data) => {
    deptOptions.value = data;
  });
}

/** 新增按钮操作 */
function handleAdd(command) {
  const dict = da_asset_type.value.find((item) => item.value == command);
  if (dict && dict.label == td('dict.da_asset_type.1')) {
    router.push("/da/asset/addAsset");
    return;
  }
  return proxy.$modal.msgWarning(td('da.asset.developing'));
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
  getDaAsset(_id).then((response) => {
    form.value = response.data;
    open.value = true;
    title.value = td('da.asset.editTitle');
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal
    .confirm(td('da.asset.confirmDelete', '', { id: _ids }))
    .then(function () {
      return delDaAsset(_ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess(td('da.asset.deleteSuccess'));
    })
    .catch(() => {});
}
function handleNodeClick(data) {
  if (data.type === "0") {
    return;
  }
  queryParams.value.businessCategoryCode = null;
  queryParams.value.dataDomainId = null;
  queryParams.value.themeDomainCode = null;
  queryParams.value.dataLayerId = null;
  queryParams.value.catCode = null;
  queryParams.value.id = null;
  if (data.type === "1") {
    queryParams.value.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "2") {
    queryParams.value.dataDomainId = data.id;
    queryParams.value.businessCategoryCode = data.otherData?.code;
  } else if (data.type === "3") {
    queryParams.value.themeDomainCode = data.otherData?.code;
  } else if (data.type === "5") {
    queryParams.value.dataLayerId = data.id;
  }
  handleQuery();
}
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

/**
 * 标签管理
 */
function getListTag() {
  listDict().then((response) => {
    AttTagList.value = response.data.sort((a, b) => {
      // 按创建时间倒序排序
      return new Date(b.createTime) - new Date(a.createTime);
    });
  });
}

function submitTag() {
  let map = {
    tagIds: tagIds.value,
    assetId: assetId.value,
  };

  addAttTagAssetRel(map).then((res) => {
    tagMultiple.value = false;
    proxy.$modal.msgSuccess(td('da.asset.operationSuccess'));
    getList();
  });
  // proxy.$modal
  //   .confirm("是否确定打标该资产？")
  //   .then(function () {
  //
  //   })
  //   .then(() => {
  //
  //   })
  //   .catch(() => {
  //     tagMultiple.value = false;
  //   });
}

const themeNames = (item) => {
  if (item.daAssetThemeRelList && item.daAssetThemeRelList.length > 0) {
    return item.daAssetThemeRelList.map((t) => t.themeName).join(", ");
  }
  return "-";
};
/** 启用禁用开关 */
function handleStatusChange(row) {
  const text = row.status === "2" ? td('da.asset.revokePublish') : td('da.asset.publish');
  const status = row.status === "2" ? "1" : "2";
  proxy.$modal
      .confirm(td('da.asset.confirmStatusChange', '', {text, name: row.name}))
      .then(function () {
        updateDaAsset({id: row.id, status: status}).then((res) => {
        if (res.code == 200) {
          proxy.$modal.msgSuccess(td('da.asset.statusSuccess', '', { text: text }));
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
.create-dropdown {
  margin-left: 12px;
  margin-right: 12px;
  vertical-align: middle;

  &:focus-visible {
    outline: none;
  }

  :deep(.el-button) {
    &:focus {
      outline: none;
      box-shadow: none;
    }
  }

  .arrow-icon {
    transition: transform 0.3s;
    &.is-reverse {
      transform: rotate(180deg);
    }
  }

  .divider {
    width: 1px;
    height: 12px;
    background: currentColor;
    opacity: 0.3;
    margin: 0 4px 0 12px;
  }

  .el-button:hover,
  .el-button:focus,
  .el-button:active {
    .divider {
      opacity: 0.6;
    }
  }
}

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
          margin-right: 16px;

          :deep(.el-link__inner) {
            font-size: inherit;
            font-weight: inherit;
          }
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
            font-size: 13px;
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
            color: #717171;
            white-space: nowrap;
          }

          .form-value {
            flex:1;
            font-size: 14px;
            color: #333333;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-family: Helvetica Neue, Helvetica, PingFang SC,
              Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
            &.textarea {
              color: #999;
              font-size: 12px;
              white-space: normal;
              display: -webkit-box !important;
              overflow: hidden;
              text-overflow: ellipsis;
              word-break: break-all;
              -webkit-line-clamp: 1;
              -webkit-box-orient: vertical !important;
            }
          }

          &.item-form2 {
            width: 66%;
          }

          &.item-form1 {
            width: 24%;
          }

          &.item-form-type {
            width: 15%;
          }

          &.item-form-hierarchy {
            width: 33%;
          }
        }
      }

      .flex-wrap {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .item-form {
          flex: 1;
          min-width: 0;

          .form-value {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;

            &.tag-container {
              display: inline-block;
              max-width: 100%;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              vertical-align: middle;
            }
          }
        }

        .form-btns {
          flex-shrink: 0;
          margin-left: 10px;
        }
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
          display: flex;
          align-items: center;
          justify-content: center;
          color: #3d446e;

          &:last-child {
            margin-right: 0;
          }

          span {
            margin-left: 4px;
            font-family: PingFang SC;
            font-weight: normal;
            font-size: 12px;
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

<style lang="scss">
.create-table-dropdown-popper {
  min-width: 150px !important;

  .el-dropdown-menu {
    padding: 4px 0 !important;
  }

  .el-dropdown-menu__item {
    padding: 6px 16px !important;
    font-size: 14px !important;
    line-height: 22px !important;
    display: flex !important;
    align-items: center !important;
    justify-content: flex-start !important;

    .svg-icon {
      margin-right: 8px !important;
      font-size: 16px !important;
    }

    span {
      white-space: nowrap;
    }
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
      &.item-form1,
      &.item-form-type,
      &.item-form-hierarchy {
        width: 50%;
      }
    }

    .item-con .flex-wrap {
      flex-wrap: wrap;
    }
  }
}
</style>
