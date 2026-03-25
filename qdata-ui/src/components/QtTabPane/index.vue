<template>
    <el-tab-pane v-bind="$attrs">
        <template #default="scope">
            <slot name="default" v-bind="scope" />
        </template>

        <template #label>
            <div class="label-wrap">
                <slot name="label" >
                    {{ $attrs.label }}
                </slot>
                <el-tooltip
                    v-bind="props.tip"
                    :effect="props.tip.effect || 'light'"
                    :placement="props.tip.placement || 'top'"
                    v-if="props.tip && Object.keys(props.tip).length"
                >
                    <el-icon class="tip-icon"> <InfoFilled /> </el-icon>
                    <template #content v-if="props.tip.custom">
                        <div class="tip-content" v-html="props.tip.content"></div>
                    </template>
                </el-tooltip>
            </div>
        </template>
    </el-tab-pane>
</template>

<script setup name="QtTabPane">
    const props = defineProps({
        tip: {
            type: Object
        }
    });
</script>

<style lang="scss" scoped>
    .label-wrap{
        display: flex;
        align-items: center;
        gap: 2px;
        .el-icon{
             color: #888;
        }
    }
</style>
