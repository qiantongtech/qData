<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qData Data Middle Platform (Commercial Edition)
  Software Copyright Registration No. 16069171
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qData 数据中台（商业版） | 软著登字第16069171号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <div class="app-container" ref="app-container">
    <div class="pagecont-top" v-show="showSearch">
      <el-form
        class="btn-style"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="75px"
        v-show="showSearch"
        @submit.prevent
      >
        <el-form-item :label="td('dg.dataLevel.levelName')" prop="name">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.name"
            :placeholder="td('dg.dataLevel.levelNamePlaceholder')"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="td('dg.dataLevel.sensitiveLevel')" prop="sensitiveLevel">
          <el-input
            class="el-form-input-width"
            v-model="queryParams.sensitiveLevel"
            :placeholder="td('dg.dataLevel.sensitiveLevelPlaceholder')"
            clearable
            @keyup.enter="handleQuery"
            @input="
              queryParams.sensitiveLevel = queryParams.sensitiveLevel.replace(
                /[^\d]/g,
                ''
              )
            "
          />
        </el-form-item>
        <el-form-item :label="td('common.texts.status')" prop="validFlag">
          <el-select
            class="el-form-input-width"
            v-model="queryParams.validFlag"
            :placeholder="td('common.form.statusPlaceholder')"
            clearable
          >
            <el-option :label="td('dg.dataLevel.enabledLabel')" :value="true" />
            <el-option :label="td('dg.dataLevel.disabledLabel')" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item class="search-btns">
          <el-button type="primary" plain @click="handleQuery">
            <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
          </el-button>
          <el-button @click="resetQuery">
            <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
          </el-button>
          <el-button type="primary" plain @click="handleAdd">
            <i class="iconfont-mini icon-xinzeng mr5"></i>{{ td('common.button.add') }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="pagecont-bottom" v-loading="loading">
      <div class="card-box" v-if="total > 0">
        <el-card v-for="item in list" :key="item.id" shadow="never">
          <div class="card-item">
            <div class="item-header">
              <div class="header-left ellipsis">
                <LevelBadge :levelData="item.shortName" />
                <span class="item-name" :title="item.levelName || item.name">
                  {{ item.levelName || item.name || "-" }}
                </span>
              </div>
              <el-popover
                placement="bottom"
                trigger="click"
                popper-class="custom-popover-width"
                :popper-style="{ minWidth: '60px', width: '80px' }"
              >
                <template #reference>
                  <div class="imgbox" @click.stop>
                    <img :src="moreIcon" alt="" />
                  </div>
                </template>
                <div class="butgdlist">
                  <el-button
                    link
                    type="primary"
                    icon="View"
                    @click="handleDetail(item)"
                    v-hasPermi="['dg:dataleve:query']"
                    >{{ td('common.button.details') }}</el-button
                  >
                  <el-button
                    v-if="!item.colors"
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleUpdate(item)"
                    :disabled="item.status == 1"
                    v-hasPermi="['dg:dataleve:edit']"
                    style="padding-right: 25px"
                    >{{ td('common.button.update') }}</el-button
                  >
                  <el-button
                    v-if="!item.colors"
                    link
                    type="danger"
                    icon="Delete"
                    :disabled="item.status == 1"
                    @click="handleDelete(item)"
                    style="padding-right: 25px"
                    v-hasPermi="['dg:dataleve:remove']"
                    >{{ td('common.button.delete') }}</el-button
                  >
                </div>
              </el-popover>
            </div>
            <div class="item-body">
              <div class="info-row">
                <span class="info-label">{{ td('dg.dataLevel.sensitiveLevel') }}</span>
                <span class="info-value">{{ item.sensitiveLevel ?? "-" }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">{{ td('common.texts.updatedBy') }}</span>
                <span
                  class="info-value ellipsis"
                  :title="item.updateBy || item.updateUser || item.updateName"
                  >{{
                    item.updateBy || item.updateUser || item.updateName || "-"
                  }}</span
                >
              </div>
              <div class="info-row">
                <span class="info-label">{{ td('common.texts.updatedTime') }}</span>
                <span
                  class="info-value ellipsis"
                  :title="
                    item.updateTime || item.updatedTime || item.createTime
                  "
                  >{{
                    item.updateTime ||
                    item.updatedTime ||
                    item.createTime ||
                    "-"
                  }}</span
                >
              </div>
              <div class="info-row">
                <span class="info-label">{{ td('dg.dataLevel.levelDesc') }}</span>
                <span
                  class="info-value ellipsis"
                  :title="item.description || item.remark || item.levelDesc"
                  >{{
                    item.description || item.remark || item.levelDesc || "-"
                  }}</span
                >
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <div class="empty" v-else>
        <img src="@/assets/da/asset/empty.png" alt="" />
        <span>{{td('common.noData')}}</span>
      </div>
      <!-- <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      /> -->
    </div>

    <DataLevelDialog
      v-model:open="open"
      :id="currentId"
      :title="title"
      @success="getList"
    />

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
      <el-form
        ref="formDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
      >
        <el-form-item :label="td('common.texts.number') + ':'" prop="id">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('dg.dataLevel.levelName')" prop="name">
          <div class="form-readonly">
            {{ form.name ?? form.levelName ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.dataLevel.levelAbbr')" prop="shortName">
          <div class="form-readonly">
            {{ form.shortName ?? form.levelCode ?? form.code ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('dg.dataLevel.sensitiveLevel')" prop="sensitiveLevel">
          <div class="form-readonly">{{ form.sensitiveLevel ?? "-" }}</div>
        </el-form-item>

        <el-form-item :label="td('common.texts.status')" prop="validFlag">
          <el-tag v-if="form.validFlag === true" type="primary">{{ td('dg.dataLevel.enabledLabel') }}</el-tag>
          <el-tag v-else-if="form.validFlag === false" type="danger"
            >{{ td('dg.dataLevel.disabledLabel') }}</el-tag
          >
        </el-form-item>
        <el-form-item :label="td('common.texts.description')" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description || form.remark || form.levelDesc || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.remark')" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdBy')" prop="createBy">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item :label="td('common.texts.createdTime')" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedBy')" prop="updateBy">
          <div class="form-readonly">
            {{ form.updateBy || form.updateUser || form.updateName || "-" }}
          </div>
        </el-form-item>
        <el-form-item :label="td('common.texts.updatedTime')" prop="updateTime">
          <div class="form-readonly">
            {{
              parseTime(
                form.updateTime || form.updatedTime || form.createTime,
                "{y}-{m}-{d} {h}:{i}"
              ) || "-"
            }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">{{ td('common.button.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="DataLevel">
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
import {
  delDataLevel,
  getDataLevel,
  listDataLevel,
  listAllDataLevel,
} from "@/api/dg/dataLevel/dataLevel";
import moreIcon from "@/assets/dg/safety/dataLevel/more.svg";
import LevelBadge from "./components/LevelBadge.vue";
import DataLevelDialog from "./components/DataLevelDialog.vue";

const { proxy } = getCurrentInstance();

const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const list = ref([]);
const allDataLevelList = ref([]);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 999,
    name: null,
    sensitiveLevel: null,
    validFlag: null,
    orderByColumn: "create_time",
    isAsc: "desc",
  },
});
const { queryParams } = toRefs(data);

function getList() {
  loading.value = true;
  listDataLevel(queryParams.value)
    .then((response) => {
      list.value = response.data.rows || [];
      total.value = response.data.total || 0;
    })
    .finally(() => {
      loading.value = false;
    });
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

function getAllList() {
  listAllDataLevel()
    .then((response) => {
      allDataLevelList.value = response.data || [];
    })
    .catch(() => {
      allDataLevelList.value = [];
    });
}

function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const currentId = ref(null);
const formRef = ref();
const formDetailRef = ref();
const data2 = reactive({
  form: {},
});
const { form } = toRefs(data2);

function resetForm() {
  form.value = {
    id: null,
    name: "",
    shortName: "",
    sensitiveLevel: "",
    description: "",
    validFlag: false,
    remark: "",
  };
}

function cancel() {
  open.value = false;
  openDetail.value = false;
  resetForm();
}

function handleAdd() {
  currentId.value = null;
  title.value = td('dg.dataLevel.addTitle');
  open.value = true;
}

function handleUpdate(row) {
  currentId.value = row.id;
  title.value = td('dg.dataLevel.editTitle');
  open.value = true;
}

function handleDetail(row) {
  resetForm();
  title.value = td('dg.dataLevel.detailTitle');
  getDataLevel(row.id).then((res) => {
    const data = res.data || {};
    form.value = {
      ...form.value,
      ...data,
      name: data.name || data.levelName || "",
      shortName: data.shortName || data.levelCode || data.code || "",
      validFlag: data.validFlag ?? (data.status === "1" || data.status === 1),
    };
    openDetail.value = true;
  });
}

function handleDelete(row) {
  if (!row?.id) return;
  proxy.$modal
    .confirm(
      td('dg.dataLevel.confirmDelete').replace('{name}', row.levelName || row.name || row.id)
    )
    .then(() => delDataLevel(row.id))
    .then(() => {
      proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
      getList();
    })
    .catch(() => {});
}

getList();
</script>

<style lang="scss" scoped>
.app-container {
  background-color: #f2f4f7;
}

.pagecont-top {
  margin-bottom: 12px;
}

.search-btns {
  .el-button {
    height: 30px;
    padding: 8px 11px;
    font-size: 12px;
  }
}

:deep(.btn-style .el-button i) {
  font-size: 12px;
}
.pagecont-bottom {
  padding: 0;
  background-color: transparent;
  box-shadow: none;
  position: relative;
  flex: 1;
  min-height: calc(100vh - 250px);
}

.card-box {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-gap: 15px;
  padding: 0;
  overflow: auto;
}

:deep(.el-card) {
  height: 188px;
  cursor: pointer;
  border: 0px;
  border-radius: 2px 2px 2px 2px !important;
}

.card-box :deep(.el-card .el-card__body) {
  padding: 16px 16px 0;
}

.card-item {
  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-left {
      display: flex;
      align-items: center;
      width: 85%;
      font-weight: bold;
      font-size: 16px;
      overflow: hidden;

      .item-name {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        flex-grow: 1;
      }
    }

    .header-right {
      .more-btn {
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        transition: background-color 0.2s;
        img {
          width: 20px;
        }
        &:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }

  .item-body {
    display: flex;
    flex-direction: column;
    border-top: 1px solid #ebeef5;
    margin-top: 14px;
    padding-top: 5px;

    .info-row {
      display: flex;
      align-items: center;
      margin-top: 11px;
      font-size: 14px;
      line-height: 1.4;

      .info-label {
        width: 72px;
        color: rgb(144, 136, 156);
        flex-shrink: 0;
      }

      .info-value {
        color: #333;
        word-break: break-all;
        flex: 1;
        overflow: hidden;

        &.ellipsis {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          word-break: normal;
        }
      }
    }
  }
}

.empty {
  min-height: calc(100vh - 250px);
  background: #fff;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  img {
    width: 160px;
    margin-bottom: 16px;
  }

  span {
    font-size: 14px;
    color: #909399;
  }
}
.pagination-container {
  height: 60px;
  background: #ffffff;
  border-radius: 2px;
  margin: 0px 0 0;
  padding: 14px 20px !important;

  :deep(.el-pagination) {
    right: 20px;
  }
}
</style>
<style lang="scss">
.custom-popover-width {
  min-width: 60px;
  width: 90px;
}

.custom-popover-width {
  .butgdlist {
    display: flex;
    flex-direction: column;
    align-items: stretch;

    .el-button {
      width: 100%;
      justify-content: center;
      padding-left: 0;
      padding-right: 0;
    }
  }
}
</style>
