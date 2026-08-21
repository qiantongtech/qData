/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

import { useI18n } from 'vue-i18n'
import { ref, reactive, toRefs, nextTick, getCurrentInstance } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { t } = useI18n();
const { td } = useDefaultLang();
export default function useCatManager({
  listFunc,
  getFunc,
  delFunc,
  addFunc,
  updateFunc,
  nameLabel = td('att.common.categoryName'),
} = {}) {
  const { proxy } = getCurrentInstance();

  const list = ref([]);
  const options = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const title = ref("");
  const isExpandAll = ref(false);
  const refreshTable = ref(true);
  const total = ref(0);

  const data = reactive({
    form: {},
    queryParams: {
      name: null,
      parentId: null,
    },
    rules: {
      name: [{ required: true, message: td('att.common.nameRequired', { name: nameLabel }), trigger: "blur" }],
      parentId: [{ required: true, message: td('att.common.parentCatRequired'), trigger: "blur" }],
    },
  });

  const { queryParams, form, rules } = toRefs(data);

  function getList() {
    loading.value = true;
    if (!listFunc) return;
    listFunc(queryParams.value).then((response) => {
      list.value = proxy.handleTree(response.data, "id", "parentId");
      loading.value = false;
    });
  }

  function getDataTree() {
    if (!listFunc) return;
    listFunc().then((response) => {
      options.value = [];
      const root = { id: 0, name: td('common.texts.topNode'), children: [] };
      root.children = proxy.handleTree(response.data, "id", "parentId");
      options.value.push(root);
    });
  }

  function reset() {
    form.value = {
      id: null,
      name: null,
      parentId: null,
      sortOrder: 0,
      description: null,
      code: null,
      validFlag: true,
      delFlag: null,
      createBy: null,
      creatorId: null,
      createTime: null,
      updateBy: null,
      updaterId: null,
      updateTime: null,
      remark: null,
    };
  }

  function cancel() {
    open.value = false;
    reset();
  }

  function handleAdd(row) {
    reset();
    getDataTree();
    if (row != null && row.id) {
      form.value.parentId = row.id;
    } else {
      form.value.parentId = 0;
    }
    open.value = true;
    title.value = td('att.common.add') + nameLabel;
  }

  async function handleUpdate(row) {
    reset();
    if (!listFunc || !getFunc) return;
    const responseAll = await listFunc();
    options.value = [];
    const filtered = responseAll.data.filter((d) => {
      return d.ID !== row.id && !d.parentId.toString().split(",").includes(row.id.toString());
    });
    const root = { id: 0, name: td('common.texts.topNode'), children: [] };
    root.children = proxy.handleTree(filtered, "id", "parentId");
    options.value.push(root);
    if (row != null) {
      form.value.parentId = row.parentId;
    }
    getFunc(row.id).then((res) => {
      delete res.data.createTime;
      delete res.data.updateTime;
      form.value = res.data;
      open.value = true;
      title.value = td('att.common.edit') + nameLabel;
    });
  }

  function handleStatusChange(row) {
    if (!updateFunc) return;
    const text = row.validFlag === true ? td('att.common.enable') : td('att.common.disable');
    proxy.$modal
      .confirm(td('att.common.confirmStatusChangeGeneric', '', { status: text, type: nameLabel, name: row.name }))
      .then(function () {
        updateFunc({ id: row.id, parentId: row.parentId, validFlag: row.validFlag })
          .then(() => {
            proxy.$modal.msgSuccess(td('att.common.statusSuccess', '', { status: text }));
            getList();
          })
          .catch(() => {
            row.validFlag = !row.validFlag;
          });
      })
      .catch(function () {
        row.validFlag = !row.validFlag;
      });
  }

  function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
      refreshTable.value = true;
    });
  }

  function onDialogSubmit(payload) {
    if (payload.id != null) {
      updateFunc && updateFunc(payload).then(() => {
        proxy.$modal.msgSuccess(td('common.message.editSuccess'));
        getList();
        open.value = false;
      });
    } else {
      addFunc && addFunc(payload).then(() => {
        proxy.$modal.msgSuccess(td('common.message.addSuccess'));
        getList();
        open.value = false;
      });
    }
  }

  function handleDelete(row) {
    const ids = row.id;
    proxy.$modal
      .confirm(td('att.common.confirmDeleteItem', '', { ids }))
      .then(function () {
        return delFunc && delFunc(ids);
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess(td('common.message.deleteSuccess'));
      })
      .catch(() => { });
  }

  // initialize tree options
  getDataTree();

  return {
    list,
    options,
    open,
    loading,
    showSearch,
    title,
    isExpandAll,
    total,
    refreshTable,
    queryParams,
    form,
    rules,
    getList,
    getDataTree,
    handleAdd,
    handleUpdate,
    cancel,
    onDialogSubmit,
    toggleExpandAll,
    handleStatusChange,
    handleDelete,
    reset,
  };
}

