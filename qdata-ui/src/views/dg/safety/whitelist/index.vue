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
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ===================================================
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
    <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
      <template #search>
        <qt-search-bar
          v-bind="searchStore"
          :params="tableStore.params"
          @query="handleQueryClick"
          @reset="handleResetQueryClick"
        />
      </template>
      <template #actions-data>
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['dg:desensitizewhitelist:add']"
        >
          新增
        </el-button>
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="!store.rows.length"
          @click="handleDelete"
          v-hasPermi="['dg:desensitizewhitelist:remove']"
        >
          删除
        </el-button>
      </template>

      <qt-table v-bind="tableStore" ref="tableRef">
        <template #whitelistNameDesc="{ row }">
          <div class="name-label task-title">
            <div class="task-title-row">
              <div class="task-name-ellipsis">
                <span
                  class="task-name-ellipsis__inner"
                  :title="row.name || '-'"
                >
                  {{ row.name || "-" }}
                </span>
              </div>
            </div>
            <div class="desc-text" :title="row.description || '-'">
              {{ row.description || "-" }}
            </div>
          </div>
        </template>
        <template #effectiveTimeRange="{ row }">
          <span>{{ formatTimeRange(row.startTime, row.endTime) }}</span>
        </template>
        <template #effectiveAccount="{ row }">
          <span>{{
            (row.userList || [])
              .map((u) => u.userName)
              .filter(Boolean)
              .join("、") || "-"
          }}</span>
        </template>
        <template #createByInfo="{ row }">
          <div class="creator-info">
            <div class="creator-info__name">{{ row.createBy || "-" }}</div>
            {{ row.createAccount || "-" }}
          </div>
        </template>

        <template #status="scope">
          <el-switch
            v-model="scope.row.validFlag"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="(e) => handleStatusChange(scope.row.id, scope.row, e)"
          />
        </template>

        <template #handle="{ row }">
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(row)"
            v-hasPermi="['dg:desensitizewhitelist:query']"
          >
            详情
          </el-button>
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(row)"
            v-hasPermi="['dg:desensitizewhitelist:edit']"
          >
            修改
          </el-button>
          <!-- :disabled="row.validFlag == true" -->
          <!--  -->
          <el-button
            link
            type="danger"
            icon="Delete"
            :disabled="row.validFlag == true"
            @click="handleDelete(row)"
            v-hasPermi="['dg:desensitizewhitelist:remove']"
          >
            删除
          </el-button>
        </template>
      </qt-table>
    </qt-wrap>

    <el-dialog
      :title="title"
      v-model="open"
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
        ref="whitelistRef"
        :model="form"
        :rules="rules"
        label-width="110px"
        @submit.prevent
      >
        <el-form-item label="白名单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入白名单名称" />
        </el-form-item>
        <el-form-item label="数据分类" prop="dataCategoryId">
          <el-tree-select
            v-if="!form.id"
            v-model="form.dataCategoryId"
            :data="dataCategoryList"
            placeholder="请选择数据分类"
            filterable
            clearable
            check-strictly
            default-expand-all
            :loading="dataCategoryLoading"
            style="width: 100%"
            :props="{ label: 'name', value: 'id', children: 'children' }"
          />
          <el-input
            v-else
            v-model="form.dataCategoryName"
            placeholder="请选择数据分类"
            disabled
          />
        </el-form-item>
        <el-form-item label="生效分类" prop="effectiveCategory">
          <el-radio-group
            v-model="form.effectiveCategory"
            @change="handleEffectiveCategoryChange"
          >
            <el-radio
              v-for="opt in effective_category_type"
              :key="opt.value"
              :label="opt.value"
              :disabled="String(opt.value) == '2' || String(opt.value) == '3'"
            >
              {{ opt.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生效账号" prop="effectiveAccount">
          <div class="effective-account__row">
            <el-input
              style="width: 84%"
              v-model="form.effectiveAccountNameList"
              placeholder="请选择生效账号"
              disabled
            />
            <el-button
              style="margin-left: 12px"
              type="primary"
              @click="openEffectiveAccountPicker"
            >
              选择账号
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="生效时段" prop="effectiveTimeRange">
          <el-date-picker
            v-model="form.effectiveTimeRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            :start-placeholder="td('common.form.startTimePlaceholder')"
            :end-placeholder="td('common.form.endTimePlaceholder')"
          />
        </el-form-item>
        <qt-form-item label="状态" prop="validFlag">
          <el-radio-group v-model="form.validFlag">
            <el-radio
              v-for="opt in validFlagOptions"
              :key="opt.label"
              :label="opt.value"
            >
              {{ opt.label }}
            </el-radio>
          </el-radio-group>
        </qt-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入描述"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            :min-height="192"
            show-word-limit
            maxlength="500个字符"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

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
        ref="whitelistDetailRef"
        :model="form"
        label-width="110px"
        class="column-form"
      >
        <el-form-item label="编号:" prop="id">
          <div class="form-readonly">{{ form.id ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="白名单名称" prop="name">
          <div class="form-readonly">{{ form.name ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="数据分类" prop="dataCategoryId">
          <div class="form-readonly">
            {{ formatDataCategory(form.dataCategoryId) }}
          </div>
        </el-form-item>
        <el-form-item label="生效分类" prop="effectiveCategory">
          <div class="form-readonly">
            {{ formatEffectiveCategory(form.effectiveCategory) }}
          </div>
        </el-form-item>
        <el-form-item label="生效账号" prop="effectiveAccount" class="row-full">
          <div
            class="form-readonly effective-account-readonly"
            :title="formatEffectiveAccountDetail()"
          >
            {{ formatEffectiveAccountDetail() }}
          </div>
        </el-form-item>
        <el-form-item label="生效时段" prop="effectiveTimeRange">
          <div class="form-readonly">
            {{ formatTimeRange(form.startTime, form.endTime) }}
          </div>
        </el-form-item>
        <el-form-item label="状态" prop="validFlag">
          <el-tag v-if="form.validFlag === true" type="primary">启用</el-tag>
          <el-tag v-else-if="form.validFlag === false" type="danger"
            >禁用</el-tag
          >
        </el-form-item>
        <el-form-item label="描述" prop="description" class="row-full">
          <div class="form-readonly textarea">
            {{ form.description ?? "-" }}
          </div>
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="row-full">
          <div class="form-readonly textarea">{{ form.remark ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="创建人" prop="createBy">
          <div class="form-readonly">{{ form.createBy ?? "-" }}</div>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <div class="form-readonly">
            {{ parseTime(form.createTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新人" prop="updateBy">
          <div class="form-readonly">
            {{ form.updateBy ?? "-" }}
          </div>
        </el-form-item>

        <el-form-item label="更新时间" prop="updateTime">
          <div class="form-readonly">
            {{ parseTime(form.updateTime, "{y}-{m}-{d} {h}:{i}") || "-" }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      :title="effectiveAccountPickerTitle"
      v-model="effectiveAccountPickerOpen"
      width="1000px"
      :append-to="$refs['app-container']"
      draggable
      destroy-on-close
    >
      <template #header>
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ effectiveAccountPickerTitle }}
        </span>
      </template>

      <template v-if="String(form.effectiveCategory) == '1'">
        <el-form
          class="btn-style"
          :model="userQueryParams"
          ref="userQueryRef"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="登录账号" prop="userName">
            <el-input
              v-model="userQueryParams.userName"
              placeholder="请输入登录账号"
              clearable
              class="el-form-input-width"
              @keyup.enter="handleQueryUser"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="userQueryParams.phonenumber"
              placeholder="请输入手机号码"
              clearable
              class="el-form-input-width"
              @keyup.enter="handleQueryUser"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              plain
              type="primary"
              @click="handleQueryUser"
              @mousedown="(e) => e.preventDefault()"
            >
              查询
            </el-button>
            <el-button
              @click="resetQueryUser"
              @mousedown="(e) => e.preventDefault()"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-table
          ref="effectiveUserTableRef"
          stripe
          v-loading="loadingUser"
          :data="userList"
          row-key="userId"
          @selection-change="handleSelectionChangeUser"
        >
          <el-table-column type="selection" width="70" align="center" />
          <el-table-column
            label="编号"
            width="80"
            align="center"
            prop="userId"
          />
          <el-table-column
            label="登录账号"
            align="center"
            prop="userName"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
          <el-table-column
            label="用户姓名"
            align="center"
            prop="nickName"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
          <el-table-column
            label="部门"
            width="180"
            align="center"
            prop="dept.deptName"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
          <el-table-column
            label="手机号码"
            width="180"
            align="center"
            prop="phonenumber"
          />
        </el-table>
        <pagination
          v-show="totalUser > 0"
          :total="totalUser"
          v-model:page="userQueryParams.pageNum"
          v-model:limit="userQueryParams.pageSize"
          @pagination="getListUser"
        />
      </template>

      <template v-else-if="String(form.effectiveCategory) == '2'">
        <el-form
          class="btn-style"
          :model="roleQueryParams"
          ref="roleQueryRef"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="角色名称" prop="roleName">
            <el-input
              v-model="roleQueryParams.roleName"
              placeholder="请输入角色名称"
              clearable
              class="el-form-input-width"
              @keyup.enter="handleQueryRole"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              plain
              type="primary"
              @click="handleQueryRole"
              @mousedown="(e) => e.preventDefault()"
            >
              查询
            </el-button>
            <el-button
              @click="resetQueryRole"
              @mousedown="(e) => e.preventDefault()"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-table
          ref="effectiveRoleTableRef"
          stripe
          v-loading="loadingRole"
          :data="roleList"
          row-key="roleId"
          @selection-change="handleSelectionChangeRole"
        >
          <el-table-column type="selection" width="70" align="center" />
          <el-table-column
            label="编号"
            width="80"
            align="center"
            prop="roleId"
          />
          <el-table-column
            label="角色名称"
            align="center"
            prop="roleName"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
          <el-table-column
            label="权限字符"
            align="center"
            prop="roleKey"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
          <el-table-column
            label="备注"
            align="center"
            prop="remark"
            :show-overflow-tooltip="{ effect: 'light' }"
          />
        </el-table>
        <pagination
          v-show="totalRole > 0"
          :total="totalRole"
          v-model:page="roleQueryParams.pageNum"
          v-model:limit="roleQueryParams.pageSize"
          @pagination="getListRole"
        />
      </template>

      <template v-else>
        <el-tree
          ref="effectiveDeptTreeRef"
          class="effective-dept-tree"
          :data="deptTreeOptions"
          node-key="id"
          show-checkbox
          default-expand-all
          :props="{ children: 'children', label: 'label' }"
        />
      </template>

      <template #footer>
        <div class="dialog-footer">
          <el-button size="mini" @click="effectiveAccountPickerOpen = false">
            取 消
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="confirmEffectiveAccount"
          >
            确 定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Whitelist">
import {
  addDesensitizeWhitelist,
  delDesensitizeWhitelist,
  getDesensitizeWhitelist,
  listDesensitizeWhitelist,
  updateDesensitizeWhitelist,
} from "@/api/dg/safety/whitelist/desensitizeWhitelist";
import { selectTreeDataCategory } from "@/api/dg/safety/dataCategory/dataCategory";
import { deptTreeSelect, listUser } from "@/api/system/system/user";
import { listRole } from "@/api/system/system/role";
import {
  getCurrentInstance,
  nextTick,
  onMounted,
  reactive,
  ref,
  toRefs,
  computed,
} from "vue"
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();;
const { proxy } = getCurrentInstance();
const { effective_category_type } = proxy.useDict(
  "dp_model_status",
  "effective_category_type"
);

const validFlagOptions = [
  { label: "禁用", value: false },
  { label: "启用", value: true },
];

const store = reactive({
  rows: [],
});

const dataCategoryLoading = ref(false);
const dataCategoryList = ref([]);
const allDataCategoryList = ref([]);

function formatDataCategory(v) {
  if (v == null || v === "") return "-";
  const vv = String(v);
  const findInTree = (nodes) => {
    for (const node of nodes) {
      if (String(node.id) === vv) return node.name;
      if (node.children && node.children.length > 0) {
        const found = findInTree(node.children);
        if (found) return found;
      }
    }
    return null;
  };
  return findInTree(allDataCategoryList.value) ?? vv;
}

async function getDataCategoryList() {
  dataCategoryLoading.value = true;
  try {
    const res = await selectTreeDataCategory({ type: 2 });
    const rawData = res?.data || [];
    allDataCategoryList.value = rawData;
    const processTree = (nodes) => {
      return nodes
        .filter((node) => String(node.desensitizationRulesFlag) !== "1")
        .map((node) => {
          const newNode = { ...node };
          newNode.disabled = String(node.type) === "1";
          if (node.children && node.children.length > 0) {
            newNode.children = processTree(node.children);
          }
          return newNode;
        });
    };
    dataCategoryList.value = processTree(rawData);
  } catch {
    dataCategoryList.value = [];
    allDataCategoryList.value = [];
  } finally {
    dataCategoryLoading.value = false;
  }
}

function formatDateOnly(v) {
  if (v == null || v === "") return "";
  if (typeof v === "number") {
    const d = new Date(v);
    if (Number.isNaN(d.getTime())) return String(v);
    const yyyy = String(d.getFullYear());
    const mm = String(d.getMonth() + 1).padStart(2, "0");
    const dd = String(d.getDate()).padStart(2, "0");
    return `${yyyy}-${mm}-${dd}`;
  }
  const s = String(v).trim();
  const m = s.match(/^(\d{4}-\d{2}-\d{2})/);
  if (m) return m[1];
  const tIdx = s.indexOf("T");
  if (tIdx > 0) return s.slice(0, tIdx);
  const spIdx = s.indexOf(" ");
  if (spIdx > 0) return s.slice(0, spIdx);
  return s;
}

function formatTimeRange(start, end) {
  const s = formatDateOnly(start);
  const e = formatDateOnly(end);
  if (!s || !e) return "-";
  return `${s}~${e}`;
}

function formatEffectiveAccount(v) {
  if (Array.isArray(v)) return v.length ? v.join("、") : "-";
  return v || "-";
}

function formatEffectiveAccountDetail() {
  const userList = Array.isArray(form.value?.userList)
    ? form.value.userList
    : [];
  const fromUsers = userList
    .map((u) => {
      if (!u) return null;
      const name =
        u.userName ?? u.user_name ?? u.loginName ?? u.account ?? null;
      const rawStatus = u.status ?? u.userStatus ?? u.validFlag ?? null;
      const isDisabled =
        rawStatus != null && (String(rawStatus) === "1" || rawStatus === false);
      if (!name) return null;
      return isDisabled ? `${name}(禁用)` : String(name);
    })
    .filter(Boolean);
  if (fromUsers.length) return fromUsers.join("、");
  if (form.value?.effectiveAccountNameList)
    return form.value.effectiveAccountNameList;
  const ea = normalizeEffectiveAccount(form.value?.effectiveAccount);
  return ea.length ? ea.join("、") : "-";
}

function formatEffectiveCategory(v) {
  const vv = v == null ? "" : String(v);
  return (
    (effective_category_type.value || []).find((o) => String(o.value) == vv)
      ?.label || "-"
  );
}

function getStatusLabel(v) {
  if (v == true) return "有效";
  if (v == false) return "禁用";
  return "-";
}

function normalizeValidFlag(v) {
  if (v == true || v == false) return v;
  if (v == null || v == "") return null;
  const s = String(v).trim().toLowerCase();
  if (s == "1" || s == "true") return true;
  if (s == "0" || s == "false") return false;
  return Boolean(v);
}

const tableRef = ref(null);
const tableStore = reactive({
  config: {
    sort: true,
    table: {
      stripe: true,
      rowKey: "id",
      defaultSort: { prop: "create_time", order: "descending" },
      onSelectionChange: function (rows) {
        store.rows = rows;
      },
      onRowDblclick: handleDetail,
    },
  },
  columns: [
    { type: "selection", width: 55, align: "left" },
    { label: "编号", prop: "id", width: 60, sortable: true },
    {
      label: "白名单名称/描述",
      prop: "name",
      align: "left",
      width: 260,
      slot: "whitelistNameDesc",
    },
    {
      label: "数据分类",
      prop: "dataCategoryId",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
      formatter: (row) => formatDataCategory(row?.dataCategoryId),
    },
    {
      label: "生效分类",
      prop: "effectiveCategory",
      align: "left",
      width: 120,
      formatter: (row) => formatEffectiveCategory(row?.effectiveCategory),
    },
    {
      label: "生效账号",
      slot: "effectiveAccount",
      align: "left",
      width: 180,
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "生效时段",
      prop: "startTime",
      slot: "effectiveTimeRange",
      width: 190,
      align: "left",
    },
    {
      label: "状态",
      prop: "validFlag",
      slot: "status",
      width: 120,
    },
    {
      label: "创建人",
      prop: "createBy",
      showOverflowTooltip: { effect: "light" },
    },
    {
      label: "创建时间",
      prop: "createTime",
      width: 150,
      sortable: true,
      sortableKey: "create_time",
      date: true,
    },
    {
      label: "操作",
      width: 220,
      fixed: "right",
      slot: "handle",
    },
  ],
  func: listDesensitizeWhitelist,

  params: {
    orderByColumn: "create_time",
    isAsc: "descending",
  },
});

const searchStore = reactive({
  items: [
    {
      label: "白名单名称",
      prop: "name",
      align: "left",
      component: { is: "input", placeholder: "请输入白名单名称" },
    },
    {
      label: "数据分类",
      prop: "dataCategoryId",
      component: {
        is: "tree-select",
        placeholder: "请选择数据分类",
        data: allDataCategoryList,
        props: { label: "name", value: "id", children: "children" },
        "check-strictly": true,
        filterable: true,
      },
    },
    {
      label: "状态",
      prop: "validFlag",
      component: {
        is: "select",
        placeholder: "请选择状态",
        options: validFlagOptions,
      },
    },
  ],
});

onMounted(() => {
  getDataCategoryList();
});

function handleQueryClick() {
  tableRef.value.getList();
}
function handleResetQueryClick() {
  tableRef.value.resetQuery();
}

function handleStatusChange(id, row, e) {
  const text = e ? "有效" : "禁用";
  proxy.$modal
    .confirm('确认要"' + text + '","' + (row.name || "-") + '"吗？')
    .then(async function () {
      await updateDesensitizeWhitelist({ id, validFlag: row.validFlag });
      proxy.$modal.msgSuccess("操作成功");
      tableRef.value.getList();
    })
    .catch(function () {
      row.validFlag = !row.validFlag;
    });
}

const open = ref(false);
const openDetail = ref(false);
const title = ref("");
const effectiveAccountPickerOpen = ref(false);
const effectiveAccountPickerTitle = computed(() => {
  const c = String(form.value?.effectiveCategory || "1");
  if (c == "2") return "选择角色";
  if (c == "3") return "选择部门";
  return "选择用户";
});
const data = reactive({
  form: {},
  rules: {
    name: [{ required: true, message: "白名单名称不能为空", trigger: "blur" }],
    dataCategoryId: [
      { required: true, message: "数据分类不能为空", trigger: "change" },
    ],
    effectiveCategory: [
      { required: true, message: "生效分类不能为空", trigger: "change" },
    ],
    effectiveAccount: [
      { required: true, message: "生效账号不能为空", trigger: "change" },
    ],
    effectiveTimeRange: [
      { required: true, message: "生效时段不能为空", trigger: "change" },
    ],
    validFlag: [{ required: true, message: "状态不能为空", trigger: "change" }],
  },
});

const { form, rules } = toRefs(data);

function cancel() {
  open.value = false;
  openDetail.value = false;
  reset();
}

function reset() {
  form.value = {
    id: null,
    name: null,
    dataCategoryId: null,
    effectiveCategory: "1",
    effectiveAccount: [],
    userList: [],
    effectiveAccountNameList: "",
    effectiveTimeRange: [],
    startTime: null,
    endTime: null,
    sortOrder: null,
    description: null,
    validFlag: false,
    remark: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
  };
  proxy.resetForm("whitelistRef");
}

function applyApiToForm(api) {
  const src = api || {};
  const timeRange = Array.isArray(src.effectiveTimeRange)
    ? src.effectiveTimeRange
    : null;
  const startTime = src.startTime ?? (timeRange ? timeRange[0] : null);
  const endTime = src.endTime ?? (timeRange ? timeRange[1] : null);
  const apiUserList = Array.isArray(src.userList) ? src.userList : [];
  const normalizedUserList = apiUserList
    .map((u) => {
      if (!u) return null;
      const userId = u.userId ?? u.id ?? u.user_id ?? null;
      const userName =
        u.userName ?? u.user_name ?? u.loginName ?? u.account ?? null;
      const effectiveCategory = u.effectiveCategory ?? null;
      const status = u.status ?? u.userStatus ?? u.validFlag ?? null;
      if (userId == null && (userName == null || userName === "")) return null;
      return { userId, userName, effectiveCategory, status };
    })
    .filter(Boolean);

  form.value = {
    ...form.value,
    ...src,
    name: src.name ?? src.whitelistName ?? null,
    dataCategoryId: src.dataCategoryId ?? src.dataCategoryCode ?? null,
    effectiveCategory:
      src.effectiveCategory == null || src.effectiveCategory == ""
        ? "1"
        : String(src.effectiveCategory),
    effectiveTimeRange:
      startTime && endTime
        ? [startTime, endTime]
        : form.value.effectiveTimeRange,
    startTime,
    endTime,
    validFlag:
      normalizeValidFlag(
        src.validFlag == null || src.validFlag == ""
          ? src.status
          : src.validFlag
      ) ?? false,
    effectiveAccount: normalizeEffectiveAccount(src.effectiveAccount),
    userList: normalizedUserList,
    effectiveAccountNameList: src.effectiveAccountNameList || "",
  };
  if (
    String(form.value.effectiveCategory || "1") === "1" &&
    (!form.value.effectiveAccount?.length ||
      form.value.effectiveAccountNameList === "") &&
    normalizedUserList.length
  ) {
    if (!form.value.effectiveAccount?.length) {
      form.value.effectiveAccount = normalizedUserList
        .map((u) => u.userName ?? (u.userId == null ? null : String(u.userId)))
        .filter(Boolean);
    }
    if (!form.value.effectiveAccountNameList) {
      form.value.effectiveAccountNameList = (form.value.effectiveAccount || [])
        .filter(Boolean)
        .join("、");
    }
  }
  syncEffectiveAccountDisplay();
}

function buildSubmitPayload() {
  const payload = { ...form.value };
  payload.effectiveCategory = String(form.value.effectiveCategory || "1");
  payload.validFlag = Boolean(form.value.validFlag);
  if (Array.isArray(form.value.effectiveTimeRange)) {
    const [start, end] = form.value.effectiveTimeRange;
    payload.startTime = start ?? null;
    payload.endTime = end ?? null;
  }
  delete payload.effectiveTimeRange;
  return payload;
}

function normalizeEffectiveAccount(v) {
  if (Array.isArray(v)) return v;
  if (v == null || v == "") return [];
  if (typeof v == "string") {
    return v
      .split(",")
      .map((s) => s.trim())
      .filter(Boolean);
  }
  return [v];
}

function syncEffectiveAccountDisplay() {
  const v = normalizeEffectiveAccount(form.value?.effectiveAccount);
  form.value.effectiveAccount = v;
  if (form.value.effectiveAccountNameList) return;
  form.value.effectiveAccountNameList = v.length ? v.join("、") : "";
}

function handleAdd() {
  reset();
  getDataCategoryList();
  open.value = true;
  title.value = "新增脱敏白名单";
}

async function handleUpdate(row) {
  reset();
  getDataCategoryList();
  const _id = row?.id;
  if (_id != null) {
    const res = await getDesensitizeWhitelist(_id);
    applyApiToForm(res?.data || res || {});
  } else {
    applyApiToForm(row || {});
  }
  open.value = true;
  title.value = "修改脱敏白名单";
}

async function handleDetail(row) {
  reset();
  getDataCategoryList();
  const _id = row?.id;
  if (_id != null) {
    const res = await getDesensitizeWhitelist(_id);
    applyApiToForm(res?.data || res || {});
  } else {
    applyApiToForm(row || {});
  }
  openDetail.value = true;
  title.value = "脱敏白名单详情";
}

function handleEffectiveCategoryChange() {
  clearEffectiveAccount();
}

function clearEffectiveAccount() {
  form.value.effectiveAccount = [];
  form.value.userList = [];
  form.value.effectiveAccountNameList = "";
}

const userQueryRef = ref(null);
const roleQueryRef = ref(null);
const effectiveUserTableRef = ref(null);
const effectiveRoleTableRef = ref(null);
const effectiveDeptTreeRef = ref(null);

const loadingUser = ref(false);
const loadingRole = ref(false);
const userList = ref([]);
const roleList = ref([]);
const deptTreeOptions = ref([]);
const totalUser = ref(0);
const totalRole = ref(0);
const userSelection = ref([]);
const roleSelection = ref([]);

const userQueryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userName: undefined,
  phonenumber: undefined,
});
const roleQueryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: undefined,
});

async function ensureDeptTree() {
  if (deptTreeOptions.value?.length) return;
  const res = await deptTreeSelect();
  deptTreeOptions.value = res?.data || res || [];
}

async function getListUser() {
  loadingUser.value = true;
  const res = await listUser(userQueryParams);
  userList.value = res?.rows || res?.data?.rows || [];
  totalUser.value = res?.total || res?.data?.total || 0;
  loadingUser.value = false;
  await nextTick();
  const selected = normalizeEffectiveAccount(form.value.effectiveAccount);
  userList.value.forEach((u) => {
    if (selected.includes(String(u.userName))) {
      effectiveUserTableRef.value?.toggleRowSelection(u, true);
    }
  });
}

function handleQueryUser() {
  userQueryParams.pageNum = 1;
  getListUser();
}

function resetQueryUser() {
  proxy.resetForm("userQueryRef");
  userQueryParams.pageNum = 1;
  userQueryParams.pageSize = 10;
  userQueryParams.userName = undefined;
  userQueryParams.phonenumber = undefined;
  handleQueryUser();
}

function handleSelectionChangeUser(selection) {
  userSelection.value = selection || [];
}

async function getListRole() {
  loadingRole.value = true;
  const res = await listRole(roleQueryParams);
  roleList.value = res?.rows || res?.data?.rows || [];
  totalRole.value = res?.total || res?.data?.total || 0;
  loadingRole.value = false;
  await nextTick();
  const selected = normalizeEffectiveAccount(form.value.effectiveAccount).map(
    (v) => String(v)
  );
  roleList.value.forEach((r) => {
    if (selected.includes(String(r.roleId))) {
      effectiveRoleTableRef.value?.toggleRowSelection(r, true);
    }
  });
}

function handleQueryRole() {
  roleQueryParams.pageNum = 1;
  getListRole();
}

function resetQueryRole() {
  proxy.resetForm("roleQueryRef");
  roleQueryParams.pageNum = 1;
  roleQueryParams.pageSize = 10;
  roleQueryParams.roleName = undefined;
  handleQueryRole();
}

function handleSelectionChangeRole(selection) {
  roleSelection.value = selection || [];
}

async function openEffectiveAccountPicker() {
  if (!form.value.effectiveCategory) form.value.effectiveCategory = "1";
  if (String(form.value.effectiveCategory) !== "1") {
    proxy.$modal.msgWarning("当前仅支持选择用户");
    return;
  }
  effectiveAccountPickerOpen.value = true;
  if (String(form.value.effectiveCategory) == "1") {
    await getListUser();
    return;
  }
  if (String(form.value.effectiveCategory) == "2") {
    await getListRole();
    return;
  }
  await ensureDeptTree();
  await nextTick();
  effectiveDeptTreeRef.value?.setCheckedKeys(
    normalizeEffectiveAccount(form.value.effectiveAccount)
      .map((v) => Number(v))
      .filter((v) => !Number.isNaN(v))
  );
}

async function confirmEffectiveAccount() {
  const c = String(form.value.effectiveCategory || "1");
  if (c == "1") {
    const rows = userSelection.value || [];
    form.value.effectiveAccount = rows.map((u) => u.userName).filter(Boolean);
    form.value.userList = rows
      .map((u) => {
        const userId = u?.userId ?? null;
        const userName = u?.nickName || u?.userName || null;
        if (userId == null && (userName == null || userName === ""))
          return null;
        return { userId, userName, effectiveCategory: c };
      })
      .filter(Boolean);
    form.value.effectiveAccountNameList = rows
      .map((u) => u.nickName || u.userName)
      .filter(Boolean)
      .join("、");
    effectiveAccountPickerOpen.value = false;
    return;
  }
  if (c == "2") {
    const rows = roleSelection.value || [];
    form.value.effectiveAccount = rows
      .map((r) => String(r.roleId))
      .filter(Boolean);
    form.value.userList = rows
      .map((r) => {
        const userId = String(r.roleId);
        const userName = r.roleName || r.roleKey || r.roleId;
        return { userId, userName, effectiveCategory: c };
      })
      .filter(Boolean);
    form.value.effectiveAccountNameList = rows
      .map((r) => r.roleName || r.roleKey || r.roleId)
      .filter(Boolean)
      .join("、");
    effectiveAccountPickerOpen.value = false;
    return;
  }
  const tree = effectiveDeptTreeRef.value;
  const nodes = tree?.getCheckedNodes?.(true, false) || [];
  form.value.effectiveAccount = nodes.map((n) => String(n.id)).filter(Boolean);
  form.value.userList = nodes
    .map((n) => {
      const userId = String(n.id);
      const userName = n.label || n.name || n.id;
      return { userId, userName, effectiveCategory: c };
    })
    .filter(Boolean);
  form.value.effectiveAccountNameList = nodes
    .map((n) => n.label || n.name || n.id)
    .filter(Boolean)
    .join("、");
  effectiveAccountPickerOpen.value = false;
}

function submitForm() {
  proxy.$refs["whitelistRef"].validate(async (valid) => {
    if (!valid) return;
    const payload = buildSubmitPayload();
    if (form.value.id != null) {
      await updateDesensitizeWhitelist(payload);
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      tableRef.value.getList();
      return;
    }
    await addDesensitizeWhitelist(payload);
    proxy.$modal.msgSuccess("新增成功");
    open.value = false;
    tableRef.value.getList();
  });
}

/*function handleDelete(row) {
  let _ids = null;
  if (row?.id) {
    _ids = row.id;
  } else {
    _ids = store.rows.map((item) => item.id).join(",");
  }
  if (!_ids) return;

  proxy.$modal
    .confirm('是否确认删除编号为"' + _ids + '"的数据项？')
    .then(async () => {
      await delDesensitizeWhitelist(_ids);
      tableRef.value.getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}*/


function handleDelete(row) {
  const invalidIds = [];
  const message=ref("确定要删除记录吗");
  if (row?.id) {
    invalidIds.push(row.id);
    message.value=`是否确认删除编号为${row.id} 的数据项？`
  }else {
    store.rows.forEach(item => {
      // 当 validFlag 为 false 时，记录 id
      if (item.validFlag === false) {
        invalidIds.push(item.id);
      }
    });
    message.value=`可删除${invalidIds.length}个，不可删除${store.rows.length-invalidIds.length}个，是否删除可删部分`
  }
  proxy.$modal
      .confirm(message.value)
      .then(async () => {
        await delDesensitizeWhitelist(invalidIds);
        tableRef.value.getList();
        proxy.$modal.msgSuccess("删除成功");
      })
      .catch(() => {});
}

</script>

<style scoped lang="scss">
.creator-info__account {
  color: var(--el-text-color-secondary);
}
.effective-account__row {
  display: flex;
  align-items: center;
  width: 100%;
}
.effective-dept-tree {
  max-height: 520px;
  overflow: auto;
  padding: 8px 4px;
}
.effective-account-readonly {
  width: 100%;
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
