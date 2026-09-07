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
  <div class="app-container">
    <CatPage
      :list-func="listAttDataDevCat"
      :get-func="getAttDataDevCat"
      :del-func="delAttDataDevCat"
      :add-func="addAttDataDevCat"
      :update-func="updateAttDataDevCat"
      :status-change-check-func="checkStatusChange"
      :before-submit="beforeFormSubmit"
      :rules="rules"
      show-remark
      :name-label="td('dpp.setting.dataDevCat.dataDevCatName')"
      :title-base="td('dpp.setting.dataDevCat.dataDevCatName')"
      perm-base="att:dataDevCat"
      check-project-params
      status-change-pass-row
    />
  </div>
</template>

<script setup name="DataDevCat">
import { ref, getCurrentInstance } from "vue";
import CatPage from "@/views/att/cat/components/CatPage.vue";
import {
  listAttDataDevCat,
  getAttDataDevCat,
  delAttDataDevCat,
  addAttDataDevCat,
  updateAttDataDevCat,
  hasDataDevelopmentTask,
  isDataDevCatNameUsed,
  getDataDevelopmentTaskCount,
} from "@/api/att/cat/dataDevCat/dataDevCat";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();

const rules = {
  name: [
    {
      required: true,
      message: td("dpp.setting.dataDevCat.nameRequired"),
      trigger: "blur",
    },
    {
      validator: async (_rule, value, callback) => {
        if (!value) {
          callback();
          return;
        }
        // 在 CatPage 中，form 数据通过 CatEditDialog 内部管理，这里需要特殊处理或简化
        // 由于 CatEditDialog 内部会处理基础校验，这里主要保留异步校验逻辑
        try {
          const response = await isDataDevCatNameUsed({
            name: value.trim(),
          }, { hideErrorMessage: true });
          if (response.data === true) {
            callback(new Error(td("dpp.setting.dataDevCat.nameUsed", "名称已被使用")));
            return;
          }
          callback();
        } catch (error) {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

/** 状态变更前的业务校验 */
async function checkStatusChange(row) {
  if (row.validFlag === false) {
    const response = await hasDataDevelopmentTask(row.id, { hideErrorMessage: true });
    if (response.data === true) {
      proxy.$modal.msgWarning(td('dpp.setting.dataDevCat.developmentTaskExistsCannotDisable', '存在数据开发任务，不允许禁用'));
      return false;
    }
  }
  return true;
}

/** 提交前的业务校验（如重命名确认） */
async function beforeFormSubmit(formData) {
  if (formData.id) {
    try {
      // 获取原数据以判断是否修改了名称
      const oldData = await getAttDataDevCat(formData.id, { hideErrorMessage: true });
      const originalName = oldData.data.name || "";
      if ((formData.name || "").trim() !== originalName.trim()) {
        const response = await getDataDevelopmentTaskCount(formData.id, { hideErrorMessage: true });
        const taskCount = Number(response.data || 0);
        if (taskCount > 0) {
          await proxy.$modal.confirm(
            td(
              "dpp.setting.dataDevCat.confirmRenameWithTaskCount",
              "该类目已被 {count} 个数据开发任务使用，修改名称后任务归属显示将同步变化。",
              { count: taskCount }
            )
          );
        }
      }
    } catch (error) {
      if (error === 'cancel' || error === 'close') return false;
    }
  }
  return true;
}
</script>
