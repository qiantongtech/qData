import { ref, reactive, toRefs, nextTick, getCurrentInstance } from "vue";

export default function useCatManager({
  listFunc,
  getFunc,
  delFunc,
  addFunc,
  updateFunc,
  nameLabel = "类目名称",
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
      name: [{ required: true, message: `${nameLabel}不能为空`, trigger: "blur" }],
      parentId: [{ required: true, message: "上级类目不能为空", trigger: "blur" }],
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
      const root = { id: 0, name: "顶级节点", children: [] };
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
    title.value = `新增${nameLabel}`;
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
    const root = { id: 0, name: "顶级节点", children: [] };
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
      title.value = `修改${nameLabel}`;
    });
  }

  function handleStatusChange(row) {
    if (!updateFunc) return;
    const text = row.validFlag === true ? "启用" : "禁用";
    proxy.$modal
      .confirm('确认要"' + text + '","' + row.name + `"吗？`)
      .then(function () {
        updateFunc({ id: row.id, parentId: row.parentId, validFlag: row.validFlag })
          .then(() => {
            proxy.$modal.msgSuccess(text + "成功");
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
        proxy.$modal.msgSuccess("修改成功");
        getList();
        open.value = false;
      });
    } else {
      addFunc && addFunc(payload).then(() => {
        proxy.$modal.msgSuccess("新增成功");
        getList();
        open.value = false;
      });
    }
  }

  function handleDelete(row) {
    const ids = row.id;
    proxy.$modal
      .confirm('是否确认删除编号为"' + ids + '"的数据项？')
      .then(function () {
        return delFunc && delFunc(ids);
      })
      .then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
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

