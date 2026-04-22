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
    <el-dialog v-model="dialogVisible" class="deleteConfirmDialog" :title="'删除' + (deleTitle ? deleTitle : '')"
        width="520px" :close-on-click-modal="false" @close="handleClose" draggable>
        <div class="confirm-content">
            <!-- 警告信息 -->
            <div class="warning-message">
                <svg-icon iconClass="warning" class="tip-icon" /> 警告：{{ deleTitle }}删除无法撤消！请慎重操作！
            </div>

            <div class="repo-info">
                该操作将永久删除编号 <span class="repo-id">{{ deleteId }}</span> 的数据项，同时可能取消与之关联的关系。<br>
                <br>
                为防止意外，确认继续操作请输入以下内容:
                <p><span class="delete-id-display">{{ verificationText }}</span></p>
            </div>

            <div class="input-section">
                <el-input v-model="inputValue" :placeholder="`请输入${verificationText}以确认继续操作`" clearable
                    @input="handleInput" class="input-field" />
            </div>

            <div v-if="inputError" class="error-message">
                {{ inputError }}
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="confirmDelete" :disabled="!isInputValid">
                    确认删除
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'

const dialogVisible = ref(false)
const inputValue = ref('')
const inputError = ref('')
const deleteId = ref(null) // 接收id
const deleTitle = ref() // 接收title
const repoName = ref('') // 验证名称

// 计算验证文本，如果没有传入name则默认为"立即删除"
// 去除repoName中的空格
const verificationText = computed(() => {
    const cleanRepoName = (repoName.value || '立即删除').replace(/\s/g, '')
    // 如果有deleteId，则在验证文本中加入编号信息
    return deleteId.value ? `${cleanRepoName}编号 ${deleteId.value} 的数据项` : cleanRepoName
})

// 计算输入是否有效
const isInputValid = computed(() => {
    return inputValue.value && inputValue.value === verificationText.value
})
// 打开对话框
/**
 * 打开删除确认对话框
 * @param {string|number} id - 要删除的项目的ID
 * @param {string} title - 删除标题，显示在对话框标题和警告信息中
 * @param {string} [name] - 验证文本名称，用于确认删除操作的关键词，默认为"立即删除"
 */
// 打开对话框
const open = (id, title, name) => {
    dialogVisible.value = true
    inputValue.value = ''
    inputError.value = ''
    deleTitle.value = title
    deleteId.value = id // 设置id
    // 去除传入的name参数中的空格
    repoName.value = (name || '').replace(/\s/g, '')
}

// 关闭对话框
const handleClose = () => {
    inputValue.value = ''
    inputError.value = ''
    deleteId.value = null
    repoName.value = ''
}

// 输入变化时的处理
const handleInput = () => {
    if (inputValue.value === verificationText.value) {
        inputError.value = ''
    }
}

// 确认删除
const confirmDelete = () => {
    if (inputValue.value !== verificationText.value) {
        inputError.value = `请输入正确的验证文本: ${verificationText.value}`
        return
    }

    // 触发确认事件并传递id
    emit('confirm-delete', deleteId.value)
    dialogVisible.value = false
    inputValue.value = ''
    inputError.value = ''
    deleteId.value = null
    repoName.value = ''
}

// 暴露方法给父组件使用
const emit = defineEmits(['confirm-delete'])
defineExpose({ open })
</script>

<style scoped lang="less">
.deleteConfirmDialog {
    .warning-message {
        background-color: #ffecec;
        color: #DB2828;
        padding: 10px 20px;
        border: 1px solid red;
        border-radius: 4px;
        font-weight: bold;

        .tip-icon {
            font-size: 16px;
            margin-right: 7px;
        }
    }

    .repo-info {
        background-color: #FFE8E6;
        color: #333333;
        padding: 10px 20px;
        padding-bottom: 0px;
        margin-top: 20px;
        border: 1px solid red;
        border-radius: 4px;
        font-weight: normal;
        line-height: 1;
    }

    .repo-id {
        font-weight: normal;
        color: #333333;
        border-bottom: 1px dashed #333333;
    }

    .delete-id-display {
        margin-top: 10px;
        font-weight: normal;
        color: #DB2828;

        border-bottom: 1px dashed #DB2828;
    }

    .input-section {
        margin-top: 15px;
    }

    .input-field {
        width: 100%;
    }

    .error-message {
        color: #DB2828;
        margin-top: 5px;
    }
}
</style>
<style>
.el-dialog.deleteConfirmDialog .el-dialog__body {
    padding: 20px !important;
}

.el-dialog.deleteConfirmDialog:not(.is-fullscreen) {
    margin-top: 30vh !important;
}
</style>