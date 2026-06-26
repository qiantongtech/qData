<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <el-form>
        <el-form-item>
            <el-radio v-model="radioValue" :value="1"> {{ td('common.crontab.hour.wildcard') }} </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="2">
                {{ td('common.crontab.cycleFrom') }}
                <el-input-number v-model="cycle01" :min="0" :max="22" /> -
                <el-input-number v-model="cycle02" :min="cycle01 + 1" :max="23" /> {{ td('common.crontab.hour.cycleSuffix') }}
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="3">
                {{ td('common.crontab.hour.averagePrefix') }}
                <el-input-number v-model="average01" :min="0" :max="22" /> {{ td('common.crontab.hour.averageMiddle') }}
                <el-input-number v-model="average02" :min="1" :max="23 - average01" /> {{ td('common.crontab.hour.averageSuffix') }}
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="4">
                {{ td('common.crontab.specify') }}
                <el-select
                    clearable
                    v-model="checkboxList"
                    :placeholder="td('common.crontab.multiSelect')"
                    multiple
                    :multiple-limit="10"
                >
                    <el-option v-for="item in 24" :key="item" :label="item - 1" :value="item - 1" />
                </el-select>
            </el-radio>
        </el-form-item>
    </el-form>
</template>

<script setup>
    import useDefaultLang from "@/composables/useDefaultLang.js";
    const { td } = useDefaultLang();
    const emit = defineEmits(['update']);
    const props = defineProps({
        cron: {
            type: Object,
            default: {
                second: '0',
                min: '*',
                hour: '*',
                day: '*',
                month: '*',
                week: '?',
                year: ''
            }
        },
        check: {
            type: Function,
            default: () => {}
        }
    });
    const radioValue = ref(1);
    const cycle01 = ref(0);
    const cycle02 = ref(1);
    const average01 = ref(0);
    const average02 = ref(1);
    const checkboxList = ref([]);
    const checkCopy = ref([0]);
    const cycleTotal = computed(() => {
        cycle01.value = props.check(cycle01.value, 0, 22);
        cycle02.value = props.check(cycle02.value, cycle01.value + 1, 23);
        return cycle01.value + '-' + cycle02.value;
    });
    const averageTotal = computed(() => {
        average01.value = props.check(average01.value, 0, 22);
        average02.value = props.check(average02.value, 1, 23 - average01.value);
        return average01.value + '/' + average02.value;
    });
    const checkboxString = computed(() => {
        return checkboxList.value.join(',');
    });
    watch(
        () => props.cron.hour,
        (value) => changeRadioValue(value)
    );
    watch([radioValue, cycleTotal, averageTotal, checkboxString], () => onRadioChange());
    function changeRadioValue(value) {
        if (props.cron.min === '*') {
            emit('update', 'min', '0', 'hour');
        }
        if (props.cron.second === '*') {
            emit('update', 'second', '0', 'hour');
        }
        if (value === '*') {
            radioValue.value = 1;
        } else if (value.indexOf('-') > -1) {
            const indexArr = value.split('-');
            cycle01.value = Number(indexArr[0]);
            cycle02.value = Number(indexArr[1]);
            radioValue.value = 2;
        } else if (value.indexOf('/') > -1) {
            const indexArr = value.split('/');
            average01.value = Number(indexArr[0]);
            average02.value = Number(indexArr[1]);
            radioValue.value = 3;
        } else {
            checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))];
            radioValue.value = 4;
        }
    }
    function onRadioChange() {
        switch (radioValue.value) {
            case 1:
                emit('update', 'hour', '*', 'hour');
                break;
            case 2:
                emit('update', 'hour', cycleTotal.value, 'hour');
                break;
            case 3:
                emit('update', 'hour', averageTotal.value, 'hour');
                break;
            case 4:
                if (checkboxList.value.length === 0) {
                    checkboxList.value.push(checkCopy.value[0]);
                } else {
                    checkCopy.value = checkboxList.value;
                }
                emit('update', 'hour', checkboxString.value, 'hour');
                break;
        }
    }
</script>

<style lang="scss" scoped>
    .el-input-number--small,
    .el-select,
    .el-select--small {
        margin: 0 0.2rem;
    }
    .el-select,
    .el-select--small {
        width: 18.8rem;
    }
</style>
