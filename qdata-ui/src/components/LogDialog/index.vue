<template>
    <el-dialog
        v-bind="config"
        :modelValue="props.modelValue"
        @update:modelValue="handleModelUpdate"
        @close="handleClose"
    >
        <div class="actions-wrap">
            <el-button
                link
                icon="copyDocument"
                type="primary"
                v-copyText="content"
                v-copyText:callback="copyTextSuccess"
            >
                复制
            </el-button>
        </div>
        <div class="dialog-content" v-html="content"></div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleCancel">关闭</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="LogDialog">
    import { defineProps, defineEmits, computed } from 'vue';
    import { merge } from 'lodash-es';
    import { ElMessage } from 'element-plus';

    const DEFAULT_CONFIG = {
        title: '日志详情',
        width: '800',
        draggable: true,
        'destroy-on-close': true,
        class: 'log-dialog'
    };

    const props = defineProps({
        modelValue: {
            type: Boolean,
            default: false
        },
        config: {
            type: Object,
            default: () => {
                return {};
            }
        },
        content: {
            type: String,
            default: ''
        }
    });

    const config = computed(() => {
        return merge({}, DEFAULT_CONFIG, props.config);
    });

    const content = computed(() => {
        return props.content ? props.content.replace(/\n/g, '<br>') : '';
    });

    const emit = defineEmits(['update:modelValue']);

    const handleModelUpdate = (newVisible) => {
        emit('update:modelValue', newVisible);
    };

    const handleCancel = () => {
        emit('update:modelValue', false);
    };

    const handleClose = () => {
        emit('update:modelValue', false);
    };

    function copyTextSuccess() {
        ElMessage.success('复制成功！');
    }
</script>

<style lang="scss">
    .log-dialog {
        .el-dialog__body {
            position: relative;
        }

        .actions-wrap {
            position: absolute;
            top: 0;
            left: 0;
            padding: 10px 20px;
            width: 100%;
            display: flex;
            justify-content: flex-end;
        }

        .dialog-content {
            height: 100%;
            overflow: auto;
        }
    }
</style>
