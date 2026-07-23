import { ref, reactive, toRefs, nextTick, getCurrentInstance } from "vue";
import { i18n } from "@/plugins/vueI18n";
import useDefaultLang from "@/composables/useDefaultLang";

export default function useCatManager({
  listFunc,
  getFunc,
  delFunc,
  addFunc,
  updateFunc,
  nameLabel = i18n.global.t('components.catEditDialog.nameLabel'),
} = {}) {
  const { td } = useDefaultLang();
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
      name: [{ required: true, message: i18n.global.t('components.catEditDialog.nameRequired', { nameLabel }), trigger: "blur" }],
      parentId: [{ required: true, message: i18n.global.t('components.catEditDialog.parentIdRequired'), trigger: "blur" }],
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
      const root = { id: 0, name: i18n.global.t('common.texts.topNode'), children: [] };
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
    title.value = td('components.catEditDialog.addTitle', 'Add {nameLabel}', { nameLabel });
  }

  async function handleUpdate(row) {
    reset();
    if (!listFunc || !getFunc) return;
    const responseAll = await listFunc();
    options.value = [];
    const filtered = responseAll.data.filter((d) => {
      return (
        d.id !== row.id &&
        !(
          d.parentId != null &&
          d.parentId.toString().split(",").includes(row.id.toString())
        )
      );
    });
    const root = { id: 0, name: i18n.global.t('common.texts.topNode'), children: [] };
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
      title.value = td('components.catEditDialog.modifyTitle', 'Edit {nameLabel}', { nameLabel });
    });
  }

  function handleStatusChange(row) {
    if (!updateFunc) return;
    const text = row.validFlag === true ? i18n.global.t('common.texts.enable') : i18n.global.t('common.texts.disable');
    proxy.$modal
      .confirm(i18n.global.t('components.catPage.confirmEnableDisable', { text, name: row.name, titleBase: nameLabel }))
      .then(function () {
        updateFunc({ id: row.id, parentId: row.parentId, validFlag: row.validFlag })
          .then(() => {
            proxy.$modal.msgSuccess(i18n.global.t('components.catPage.operationSuccess', { text }));
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
        proxy.$modal.msgSuccess(t('common.message.editSuccess'));
        getList();
        open.value = false;
      });
    } else {
      addFunc && addFunc(payload).then(() => {
        proxy.$modal.msgSuccess(t('common.message.addSuccess'));
        getList();
        open.value = false;
      });
    }
  }

  function handleDelete(row) {
    const ids = row.id;
    proxy.$modal
      .confirm(i18n.global.t('components.catPage.deleteConfirm', { titleBase: nameLabel, id: ids }))
      .then(function () {
        return delFunc && delFunc(ids);
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess(i18n.global.t('common.message.deleteSuccess'));
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
