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
                <el-form-item label="资产id" prop="assetId">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.assetId"
                        placeholder="请输入资产id"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="中文名称/英文名称" prop="columnName">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.columnName"
                        placeholder="请输入中文名称/英文名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="字段注释/中文名称" prop="columnComment">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.columnComment"
                        placeholder="请输入字段注释/中文名称"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="长度" prop="columnLength">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.columnLength"
                        placeholder="请输入长度"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="小数位" prop="columnScale">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.columnScale"
                        placeholder="请输入小数位"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="是否必填" prop="nullableFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.nullableFlag"
                        placeholder="请输入是否必填"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="是否主键" prop="pkFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.pkFlag"
                        placeholder="请输入是否主键"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="默认值" prop="defaultValue">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.defaultValue"
                        placeholder="请输入默认值"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="是否代码" prop="dataElemCodeFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.dataElemCodeFlag"
                        placeholder="请输入是否代码"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="代码id" prop="dataElemCodeId">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.dataElemCodeId"
                        placeholder="请输入代码id"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="敏感等级id" prop="sensitiveLevelId">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.sensitiveLevelId"
                        placeholder="请输入敏感等级id"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="关联数据元" prop="relDataElmeFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.relDataElmeFlag"
                        placeholder="请输入关联数据元"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="关联清洗规则" prop="relCleanFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.relCleanFlag"
                        placeholder="请输入关联清洗规则"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="关联稽查规则" prop="relAuditFlag">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.relAuditFlag"
                        placeholder="请输入关联稽查规则"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input
                        class="el-form-input-width"
                        v-model="queryParams.description"
                        placeholder="请输入描述"
                        clearable
                        @keyup.enter="handleQuery"
                    />
                </el-form-item>
                <el-form-item label="创建时间" prop="createTime">
                    <el-date-picker
                        class="el-form-input-width"
                        clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择创建时间"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item>
                    <el-button
                        plain
                        type="primary"
                        @click="handleQuery"
                        @mousedown="(e) => e.preventDefault()"
                    >
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            @click="handleAdd"
                            v-hasPermi="['da:assetColumn:assetcolumn:add']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="primary"
                            plain
                            :disabled="single"
                            @click="handleUpdate"
                            v-hasPermi="['da:assetColumn:assetcolumn:edit']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-xiugai--copy mr5"></i>修改
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="danger"
                            plain
                            :disabled="multiple"
                            @click="handleDelete"
                            v-hasPermi="['da:assetColumn:assetcolumn:remove']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-shanchu-huise mr5"></i>删除
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="info"
                            plain
                            @click="handleImport"
                            v-hasPermi="['da:assetColumn:assetcolumn:export']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-upload-cloud-line mr5"></i>导入
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button
                            type="warning"
                            plain
                            @click="handleExport"
                            v-hasPermi="['da:assetColumn:assetcolumn:export']"
                            @mousedown="(e) => e.preventDefault()"
                        >
                            <i class="iconfont-mini icon-download-line mr5"></i>导出
                        </el-button>
                    </el-col>
                </el-row>
                <div class="justify-end top-right-btn">
                    <right-toolbar
                        v-model:showSearch="showSearch"
                        @queryTable="getList"
                        :columns="columns"
                    ></right-toolbar>
                </div>
            </div>
            <el-table
                stripe
                height="60vh"
                v-loading="loading"
                :data="daAssetColumnList"
                @selection-change="handleSelectionChange"
                :default-sort="defaultSort"
                @sort-change="handleSortChange"
            >
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column
                    v-if="getColumnVisibility(0)"
                    label="ID"
                    align="center"
                    prop="id"
                />
                <el-table-column
                    v-if="getColumnVisibility(1)"
                    label="资产id"
                    align="center"
                    prop="assetId"
                >
                    <template #default="scope">
                        {{ scope.row.assetId || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(2)"
                    label="中文名称/英文名称"
                    align="center"
                    prop="columnName"
                >
                    <template #default="scope">
                        {{ scope.row.columnName || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(3)"
                    label="字段注释/中文名称"
                    align="center"
                    prop="columnComment"
                >
                    <template #default="scope">
                        {{ scope.row.columnComment || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(4)"
                    label="数据类型"
                    align="center"
                    prop="columnType"
                >
                    <template #default="scope">
                        {{ scope.row.columnType || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(5)"
                    label="长度"
                    align="center"
                    prop="columnLength"
                >
                    <template #default="scope">
                        {{ scope.row.columnLength || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(6)"
                    label="小数位"
                    align="center"
                    prop="columnScale"
                >
                    <template #default="scope">
                        {{ scope.row.columnScale || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(7)"
                    label="是否必填"
                    align="center"
                    prop="nullableFlag"
                >
                    <template #default="scope">
                        {{ scope.row.nullableFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(8)"
                    label="是否主键"
                    align="center"
                    prop="pkFlag"
                >
                    <template #default="scope">
                        {{ scope.row.pkFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(9)"
                    label="默认值"
                    align="center"
                    prop="defaultValue"
                >
                    <template #default="scope">
                        {{ scope.row.defaultValue || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(10)"
                    label="是否代码"
                    align="center"
                    prop="dataElemCodeFlag"
                >
                    <template #default="scope">
                        {{ scope.row.dataElemCodeFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(11)"
                    label="代码id"
                    align="center"
                    prop="dataElemCodeId"
                >
                    <template #default="scope">
                        {{ scope.row.dataElemCodeId || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(12)"
                    label="敏感等级id"
                    align="center"
                    prop="sensitiveLevelId"
                >
                    <template #default="scope">
                        {{ scope.row.sensitiveLevelId || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(13)"
                    label="关联数据元"
                    align="center"
                    prop="relDataElmeFlag"
                >
                    <template #default="scope">
                        {{ scope.row.relDataElmeFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(14)"
                    label="关联清洗规则"
                    align="center"
                    prop="relCleanFlag"
                >
                    <template #default="scope">
                        {{ scope.row.relCleanFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(15)"
                    label="关联稽查规则"
                    align="center"
                    prop="relAuditFlag"
                >
                    <template #default="scope">
                        {{ scope.row.relAuditFlag || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(16)"
                    label="描述"
                    align="center"
                    prop="description"
                >
                    <template #default="scope">
                        {{ scope.row.description || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(19)"
                    label="创建人"
                    align="center"
                    prop="createBy"
                >
                    <template #default="scope">
                        {{ scope.row.createBy || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(21)"
                    label="创建时间"
                    align="center"
                    prop="createTime"
                    width="180"
                >
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    v-if="getColumnVisibility(25)"
                    label="备注"
                    align="center"
                    prop="remark"
                >
                    <template #default="scope">
                        {{ scope.row.remark || '-' }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    class-name="small-padding fixed-width"
                    fixed="right"
                    width="240"
                >
                    <template #default="scope">
                        <el-button
                            link
                            type="primary"
                            icon="Edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['da:assetColumn:assetcolumn:edit']"
                            >修改</el-button
                        >
                        <el-button
                            link
                            type="danger"
                            icon="Delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['da:assetColumn:assetcolumn:remove']"
                            >删除</el-button
                        >
                        <el-button
                            link
                            type="primary"
                            icon="view"
                            @click="handleDetail(scope.row)"
                            v-hasPermi="['da:assetColumn:assetcolumn:edit']"
                            >详情</el-button
                        >
                        <el-button
                            link
                            type="primary"
                            icon="view"
                            @click="routeTo('/da/assetColumn/daAssetColumnDetail', scope.row)"
                            v-hasPermi="['da:assetColumn:assetcolumn:edit']"
                            >复杂详情</el-button
                        >
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
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
            />
        </div>

        <!-- 新增或修改数据资产字段对话框 -->
        <el-dialog
            :title="title"
            v-model="open"
            width="800px"
            :append-to="$refs['app-container']"
            draggable
        >
            <template #header="{ close, titleId, titleClass }">
                <span role="heading" aria-level="2" class="el-dialog__title">
                    {{ title }}
                </span>
            </template>
            <el-form
                ref="daAssetColumnRef"
                :model="form"
                :rules="rules"
                label-width="80px"
                @submit.prevent
            >
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="资产id" prop="assetId">
                            <el-input v-model="form.assetId" placeholder="请输入资产id" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="中文名称/英文名称" prop="columnName">
                            <el-input
                                v-model="form.columnName"
                                placeholder="请输入中文名称/英文名称"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="字段注释/中文名称" prop="columnComment">
                            <el-input
                                v-model="form.columnComment"
                                placeholder="请输入字段注释/中文名称"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="长度" prop="columnLength">
                            <el-input v-model="form.columnLength" placeholder="请输入长度" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="小数位" prop="columnScale">
                            <el-input v-model="form.columnScale" placeholder="请输入小数位" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="是否必填" prop="nullableFlag">
                            <el-input v-model="form.nullableFlag" placeholder="请输入是否必填" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否主键" prop="pkFlag">
                            <el-input v-model="form.pkFlag" placeholder="请输入是否主键" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="默认值" prop="defaultValue">
                            <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否代码" prop="dataElemCodeFlag">
                            <el-input
                                v-model="form.dataElemCodeFlag"
                                placeholder="请输入是否代码"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="代码id" prop="dataElemCodeId">
                            <el-input v-model="form.dataElemCodeId" placeholder="请输入代码id" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="敏感等级id" prop="sensitiveLevelId">
                            <el-input
                                v-model="form.sensitiveLevelId"
                                placeholder="请输入敏感等级id"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="关联数据元" prop="relDataElmeFlag">
                            <el-input
                                v-model="form.relDataElmeFlag"
                                placeholder="请输入关联数据元"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="关联清洗规则" prop="relCleanFlag">
                            <el-input
                                v-model="form.relCleanFlag"
                                placeholder="请输入关联清洗规则"
                            />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="关联稽查规则" prop="relAuditFlag">
                            <el-input
                                v-model="form.relAuditFlag"
                                placeholder="请输入关联稽查规则"
                            />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="描述" prop="description">
                            <el-input v-model="form.description" placeholder="请输入描述" />
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

        <!-- 数据资产字段详情对话框 -->
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
            <el-form ref="daAssetColumnRef" :model="form" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="资产id" prop="assetId">
                            <div>
                                {{ form.assetId }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="中文名称/英文名称" prop="columnName">
                            <div>
                                {{ form.columnName }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="字段注释/中文名称" prop="columnComment">
                            <div>
                                {{ form.columnComment }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="数据类型" prop="columnType">
                            <div>
                                {{ form.columnType }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="长度" prop="columnLength">
                            <div>
                                {{ form.columnLength }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="小数位" prop="columnScale">
                            <div>
                                {{ form.columnScale }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="是否必填" prop="nullableFlag">
                            <div>
                                {{ form.nullableFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否主键" prop="pkFlag">
                            <div>
                                {{ form.pkFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="默认值" prop="defaultValue">
                            <div>
                                {{ form.defaultValue }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否代码" prop="dataElemCodeFlag">
                            <div>
                                {{ form.dataElemCodeFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="代码id" prop="dataElemCodeId">
                            <div>
                                {{ form.dataElemCodeId }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="敏感等级id" prop="sensitiveLevelId">
                            <div>
                                {{ form.sensitiveLevelId }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="关联数据元" prop="relDataElmeFlag">
                            <div>
                                {{ form.relDataElmeFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="关联清洗规则" prop="relCleanFlag">
                            <div>
                                {{ form.relCleanFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="关联稽查规则" prop="relAuditFlag">
                            <div>
                                {{ form.relAuditFlag }}
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="描述" prop="description">
                            <div>
                                {{ form.description }}
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
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <div class="el-upload__tip">
                            <el-checkbox
                                v-model="upload.updateSupport"
                            />是否更新已经存在的数据资产字段数据
                        </div>
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link
                            type="primary"
                            :underline="false"
                            style="font-size: 12px; vertical-align: baseline"
                            @click="importTemplate"
                            >下载模板</el-link
                        >
                    </div>
                </template>
            </el-upload>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="upload.open = false">取 消</el-button>
                    <el-button type="primary" @click="submitFileForm">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="DaAssetColumn">
    import {
        listDaAssetColumn,
        getDaAssetColumn,
        delDaAssetColumn,
        addDaAssetColumn,
        updateDaAssetColumn
    } from '@/api/da/assetColumn/daAssetColumn';
    import { getToken } from '@/utils/auth.js';

    const { proxy } = getCurrentInstance();

    const daAssetColumnList = ref([]);

    // 列显隐信息
    const columns = ref([
        { key: 1, label: '资产id', visible: true },
        { key: 2, label: '中文名称/英文名称', visible: true },
        { key: 3, label: '字段注释/中文名称', visible: true },
        { key: 4, label: '数据类型', visible: true },
        { key: 5, label: '长度', visible: true },
        { key: 6, label: '小数位', visible: true },
        { key: 7, label: '是否必填', visible: true },
        { key: 8, label: '是否主键', visible: true },
        { key: 9, label: '默认值', visible: true },
        { key: 10, label: '是否代码', visible: true },
        { key: 11, label: '代码id', visible: true },
        { key: 12, label: '敏感等级id', visible: true },
        { key: 13, label: '关联数据元', visible: true },
        { key: 14, label: '关联清洗规则', visible: true },
        { key: 15, label: '关联稽查规则', visible: true },
        { key: 16, label: '描述', visible: true },
        { key: 19, label: '创建人', visible: true },
        { key: 21, label: '创建时间', visible: true },
        { key: 25, label: '备注', visible: true }
    ]);

    const getColumnVisibility = (key) => {
        const column = columns.value.find((col) => col.key === key);
        // 如果没有找到对应列配置，默认显示
        if (!column) return true;
        // 如果找到对应列配置，根据visible属性来控制显示
        return column.visible;
    };

    const open = ref(false);
    const openDetail = ref(false);
    const loading = ref(true);
    const showSearch = ref(true);
    const ids = ref([]);
    const single = ref(true);
    const multiple = ref(true);
    const total = ref(0);
    const title = ref('');
    const defaultSort = ref({ prop: 'createTime', order: 'desc' });
    const router = useRouter();

    /*** 用户导入参数 */
    const upload = reactive({
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: import.meta.env.VITE_APP_BASE_API + '/da/daAssetColumn/importData'
    });

    const data = reactive({
        form: {},
        queryParams: {
            pageNum: 1,
            pageSize: 10,
            assetId: null,
            columnName: null,
            columnComment: null,
            columnType: null,
            columnLength: null,
            columnScale: null,
            nullableFlag: null,
            pkFlag: null,
            defaultValue: null,
            dataElemCodeFlag: null,
            dataElemCodeId: null,
            sensitiveLevelId: null,
            relDataElmeFlag: null,
            relCleanFlag: null,
            relAuditFlag: null,
            description: null,
            createTime: null
        },
        rules: {}
    });

    const { queryParams, form, rules } = toRefs(data);

    /** 查询数据资产字段列表 */
    function getList() {
        loading.value = true;
        listDaAssetColumn(queryParams.value).then((response) => {
            daAssetColumnList.value = response.data.rows;
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
            columnName: null,
            columnComment: null,
            columnType: null,
            columnLength: null,
            columnScale: null,
            nullableFlag: null,
            pkFlag: null,
            defaultValue: null,
            dataElemCodeFlag: null,
            dataElemCodeId: null,
            sensitiveLevelId: null,
            relDataElmeFlag: null,
            relCleanFlag: null,
            relAuditFlag: null,
            description: null,
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
        proxy.resetForm('daAssetColumnRef');
    }

    /** 搜索按钮操作 */
    function handleQuery() {
        queryParams.value.pageNum = 1;
        getList();
    }

    /** 重置按钮操作 */
    function resetQuery() {
        proxy.resetForm('queryRef');
        handleQuery();
    }

    // 多选框选中数据
    function handleSelectionChange(selection) {
        ids.value = selection.map((item) => item.id);
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
        title.value = '新增数据资产字段';
    }

    /** 修改按钮操作 */
    function handleUpdate(row) {
        reset();
        const _id = row.id || ids.value;
        getDaAssetColumn(_id).then((response) => {
            form.value = response.data;
            open.value = true;
            title.value = '修改数据资产字段';
        });
    }

    /** 详情按钮操作 */
    function handleDetail(row) {
        reset();
        const _id = row.id || ids.value;
        getDaAssetColumn(_id).then((response) => {
            form.value = response.data;
            openDetail.value = true;
            title.value = '数据资产字段详情';
        });
    }

    /** 提交按钮 */
    function submitForm() {
        proxy.$refs['daAssetColumnRef'].validate((valid) => {
            if (valid) {
                if (form.value.id != null) {
                    updateDaAssetColumn(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('修改成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                } else {
                    addDaAssetColumn(form.value)
                        .then((response) => {
                            proxy.$modal.msgSuccess('新增成功');
                            open.value = false;
                            getList();
                        })
                        .catch((error) => {});
                }
            }
        });
    }

    /** 删除按钮操作 */
    function handleDelete(row) {
        const _ids = row.id || ids.value;
        proxy.$modal
            .confirm('是否确认删除数据资产字段编号为"' + _ids + '"的数据项？')
            .then(function () {
                return delDaAssetColumn(_ids);
            })
            .then(() => {
                getList();
                proxy.$modal.msgSuccess('删除成功');
            })
            .catch(() => {});
    }

    /** 导出按钮操作 */
    function handleExport() {
        proxy.download(
            'da/daAssetColumn/export',
            {
                ...queryParams.value
            },
            `daAssetColumn_${new Date().getTime()}.xlsx`
        );
    }

    /** ---------------- 导入相关操作 -----------------**/
    /** 导入按钮操作 */
    function handleImport() {
        upload.title = '数据资产字段导入';
        upload.open = true;
    }

    /** 下载模板操作 */
    function importTemplate() {
        proxy.download(
            'system/user/importTemplate',
            {},
            `daAssetColumn_template_${new Date().getTime()}.xlsx`
        );
    }

    /** 提交上传文件 */
    function submitFileForm() {
        proxy.$refs['uploadRef'].submit();
    }

    /**文件上传中处理 */
    const handleFileUploadProgress = (event, file, fileList) => {
        upload.isUploading = true;
    };

    /** 文件上传成功处理 */
    const handleFileSuccess = (response, file, fileList) => {
        upload.open = false;
        upload.isUploading = false;
        proxy.$refs['uploadRef'].handleRemove(file);
        proxy.$alert(
            "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                response.msg +
                '</div>',
            '导入结果',
            { dangerouslyUseHTMLString: true }
        );
        getList();
    };
    /** ---------------------------------**/

    function routeTo(link, row) {
        if (link !== '' && link.indexOf('http') !== -1) {
            window.location.href = link;
            return;
        }
        if (link !== '') {
            if (link === router.currentRoute.value.path) {
                window.location.reload();
            } else {
                router.push({
                    path: link,
                    query: {
                        id: row.id
                    }
                });
            }
        }
    }

    getList();
</script>
