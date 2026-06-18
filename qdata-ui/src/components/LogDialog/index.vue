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
                {{ td('common.button.copy') }}
            </el-button>
        </div>
        <div class="dialog-content" v-html="content"></div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="handleCancel">{{ td('common.button.close') }}</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup name="LogDialog">
import useDefaultLang from "@/composables/useDefaultLang";
import { defineProps, defineEmits, computed } from 'vue';
    import { merge } from 'lodash-es';
    import { ElMessage } from 'element-plus';

const { td } = useDefaultLang();

const DEFAULT_CONFIG = {
        title:  td("mc.instance.structured.logDetail"),
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
        ElMessage.success(td('components.logDialog.copySuccess'));
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
