<template>
    <!-- 带！号的  -->
    <el-form-item v-bind="$attrs">
        <template #default="scope">
            <div class="default-wrap">
                <slot name="default" v-bind="scope || {}" />
                <div class="tip-content" v-if="isString && props.tip">
                    <el-icon> <InfoFilled /> </el-icon>
                    <span v-html="props.tip"></span>
                </div>
            </div>
        </template>

        <template #label="scope">
            <div class="label-wrap">
                <slot name="label" v-bind="scope">
                    {{ scope.label }}
                </slot>
                <el-tooltip
                    v-if="!isString"
                    v-bind="props.tip"
                    :effect="props.tip.effect || 'light'"
                    :placement="props.tip.placement || 'top'"
                >
                    <el-icon class="tip-icon"> <InfoFilled /> </el-icon>
                    <template #content v-if="props.tip.custom">
                        <div class="tip-content" v-html="props.tip.content"></div>
                    </template>
                </el-tooltip>
            </div>
        </template>
    </el-form-item>
</template>

<script setup name="QtFromItem">
    import { computed } from 'vue';

    const props = defineProps({
        tip: {
            type: [String, Object],
            default: ''
        }
    });

    const isString = computed(() => {
        return typeof props.tip === 'string';
    });
</script>

<style lang="scss" scoped>
    .default-wrap {
        width: 100%;
        position: relative;

        .tip-content {
            display: flex;
            align-items: center;
            gap: 2px;
            color: #888;
            font-size: 12px;
            line-height: 1.5;
            padding-top: 4px;
            white-space: wrap;
        }
    }

    .label-wrap{
        display: flex;
        align-items: center;
        gap: 2px;
        .el-icon{
             color: #888;
        }
    }

    ::v-deep(.el-form-item__error) {
        padding-top: 6px;
    }

    .el-form-item.is-error {
        padding-bottom: 16px;
        .tip-content {
            display: none;
        }
    }
</style>
