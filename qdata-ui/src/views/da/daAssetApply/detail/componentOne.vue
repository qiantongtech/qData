<template>
  <div class="justify-between mb15">
    <el-row :gutter="15" class="btn-style">
      <el-col :span="1.5">
        <el-button type="primary" plain @click="handleAdd" v-hasPermi="['daAssetApply:daAssetApply:add']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-xinzeng mr5"></i>新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['daAssetApply:daAssetApply:export']"
                   @mousedown="(e) => e.preventDefault()">
          <i class="iconfont-mini icon-download-line mr5"></i>导出
        </el-button>
      </el-col>
    </el-row>
    <div class="justify-end top-right-btn">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </div>
  </div>
  <el-table stripe height="374px" v-loading="loading" :data="daAssetApplyList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
    <el-table-column type="selection" width="55" align="center" />
            <el-table-column v-if="columns[0].visible" label="ID" align="center" prop="id" />
            <el-table-column v-if="columns[1].visible" label="资产id" align="center" prop="assetId">
              <template #default="scope">
                {{ scope.row.assetId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[2].visible" label="项目id" align="center" prop="projectId">
              <template #default="scope">
                {{ scope.row.projectId || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[3].visible" label="项目编码" align="center" prop="projectCode">
              <template #default="scope">
                {{ scope.row.projectCode || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[4].visible" label="申请理由" align="center" prop="applyReason">
              <template #default="scope">
                {{ scope.row.applyReason || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[5].visible" label="审批理由" align="center" prop="approvalReason">
              <template #default="scope">
                {{ scope.row.approvalReason || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[6].visible" label="状态" align="center" prop="status">
              <template #default="scope">
                {{ scope.row.status || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[9].visible" label="创建人" align="center" prop="createBy">
              <template #default="scope">
                {{ scope.row.createBy || '-' }}
              </template>
            </el-table-column>
            <el-table-column v-if="columns[11].visible" label="创建时间" align="center" prop="createTime" width="180">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="columns[15].visible" label="备注" align="center" prop="remark">
              <template #default="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="240">
      <template #default="scope">
        <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                   v-hasPermi="['daAssetApply:daAssetApply:edit']">修改</el-button>
        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
                   v-hasPermi="['daAssetApply:daAssetApply:remove']">删除</el-button>
        <el-button link type="primary" icon="view" @click="handleDetail(scope.row)"
                   v-hasPermi="['daAssetApply:daAssetApply:edit']">详情</el-button>
      </template>
    </el-table-column>

    <template #empty>
      <div class="emptyBg">
        <img src="@/assets/system/images/no_data/noData.png" alt="" />
        <p>暂无记录</p>
      </div>
    </template>
  </el-table>

  <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
  />

  <!-- 添加或修改数据资产申请对话框 -->
  <el-dialog :title="title" v-model="open" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
          <span role="heading" aria-level="2" class="el-dialog__title">
            {{ title }}
          </span>
    </template>
    <el-form ref="daAssetApplyRef" :model="form" :rules="rules" label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="资产id" prop="assetId">
                          <el-input v-model="form.assetId" placeholder="请输入资产id" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="项目id" prop="projectId">
                          <el-input v-model="form.projectId" placeholder="请输入项目id" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="项目编码" prop="projectCode">
                          <el-input v-model="form.projectCode" placeholder="请输入项目编码" />
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="申请理由" prop="applyReason">
                          <el-input v-model="form.applyReason" placeholder="请输入申请理由" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="审批理由" prop="approvalReason">
                          <el-input v-model="form.approvalReason" placeholder="请输入审批理由" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="备注" prop="remark">
                          <el-input v-model="form.remark" placeholder="请输入备注" />
                        </el-form-item>
                      </el-col>
          </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">取 消</el-button>
        <el-button type="primary" size="mini" @click="submitForm">确 定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 数据资产申请详情对话框 -->
  <el-dialog :title="title" v-model="openDetail" width="800px" :append-to="$refs['app-container']" draggable>
    <template #header="{ close, titleId, titleClass }">
        <span role="heading" aria-level="2" class="el-dialog__title">
          {{ title }}
        </span>
    </template>
    <el-form ref="daAssetApplyRef" :model="form"  label-width="80px">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="资产id" prop="assetId">
                          <div>
                            {{ form.assetId }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="项目id" prop="projectId">
                          <div>
                            {{ form.projectId }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="项目编码" prop="projectCode">
                          <div>
                            {{ form.projectCode }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="申请理由" prop="applyReason">
                          <div>
                            {{ form.applyReason }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="审批理由" prop="approvalReason">
                          <div>
                            {{ form.approvalReason }}
                          </div>
                        </el-form-item>
                      </el-col>
                      <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                          <div>
                            {{ form.status }}
                          </div>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <el-form-item label="备注" prop="remark">
                          <div>
                            {{ form.remark }}
                          </div>
                        </el-form-item>
                      </el-col>
          </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="mini" @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ComponentOne">
  import { listDaAssetApply, getDaAssetApply, delDaAssetApply, addDaAssetApply, updateDaAssetApply } from "@/api/da/daAssetApply/daAssetApply";

  const { proxy } = getCurrentInstance();


  const daAssetApplyList = ref([]);

  // 列显隐信息
  const columns = ref([
        { key: 0, label: "ID", visible: true },
        { key: 1, label: "资产id", visible: true },
        { key: 2, label: "项目id", visible: true },
        { key: 3, label: "项目编码", visible: true },
        { key: 4, label: "申请理由", visible: true },
        { key: 5, label: "审批理由", visible: true },
        { key: 6, label: "状态", visible: true },
        { key: 7, label: "是否有效", visible: true },
        { key: 8, label: "删除标志", visible: true },
        { key: 9, label: "创建人", visible: true },
        { key: 10, label: "创建人id", visible: true },
        { key: 11, label: "创建时间", visible: true },
        { key: 12, label: "更新人", visible: true },
        { key: 13, label: "更新人id", visible: true },
        { key: 14, label: "更新时间", visible: true },
        { key: 15, label: "备注", visible: true }
  ]);

  const open = ref(false);
  const openDetail = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const title = ref("");
  const defaultSort = ref({ prop: "createTime", order: "desc" });

  const data = reactive({
          daAssetApplyDetail: {
    },
    form: {},
    queryParams: {
      pageNum: 1,
      pageSize: 10,
                    assetId: null,
                    projectId: null,
                    projectCode: null,
                    applyReason: null,
                    approvalReason: null,
                    status: null,
                    createTime: null,
    },
    rules: {
    }
  });

  const { queryParams, form, daAssetApplyDetail, rules } = toRefs(data);

  /** 查询数据资产申请列表 */
  function getList() {
    loading.value = true;
    listDaAssetApply(queryParams.value).then(response => {
            daAssetApplyList.value = response.data.rows;
      total.value = response.data.total;
      loading.value = false;
    });
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
                    assetId: null,
                    projectId: null,
                    projectCode: null,
                    applyReason: null,
                    approvalReason: null,
                    status: null,
                    validFlag: null,
                    delFlag: null,
                    createBy: null,
                    creatorId: null,
                    createTime: null,
                    updateBy: null,
                    updaterId: null,
                    updateTime: null,
                    remark: null
    };
    proxy.resetForm("daAssetApplyRef");
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm("queryRef");
    handleQuery();
  }

  // 多选框选中数据
  function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }


  /** 排序触发事件 */
  function handleSortChange(column, prop, order) {
    queryParams.value.orderByColumn = column.prop;
    queryParams.value.isAsc = column.order;
    getList();
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = "添加数据资产申请";
  }

  /** 修改按钮操作 */
  function handleUpdate(row) {
    reset();
    const _id = row.id || ids.value
    getDaAssetApply(_id).then(response => {
      form.value = response.data;
      open.value = true;
      title.value = "修改数据资产申请";
    });
  }


  /** 详情按钮操作 */
  function handleDetail(row) {
    reset();
    const _id = row.id || ids.value
    getDaAssetApply(_id).then(response => {
      form.value = response.data;
      openDetail.value = true;
      title.value = "数据资产申请详情";
    });
  }

  /** 提交按钮 */
  function submitForm() {
    proxy.$refs["daAssetApplyRef"].validate(valid => {
      if (valid) {
        if (form.value.id != null) {
          updateDaAssetApply(form.value).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        } else {
          addDaAssetApply(form.value).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          }).catch(error => {
          });
        }
      }
    });
  }

  /** 删除按钮操作 */
  function handleDelete(row) {
    const _ids = row.id || ids.value;
    proxy.$modal.confirm('是否确认删除数据资产申请编号为"' + _ids + '"的数据项？').then(function() {
      return delDaAssetApply(_ids);
    }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    }).catch(() => {});
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('da/daAssetApply/export', {
      ...queryParams.value
    }, `daAssetApply_${new Date().getTime()}.xlsx`)
  }



  getList();

</script>
