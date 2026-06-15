<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
-->

<template>
    <el-form>
        <el-form-item>
            <el-radio v-model="radioValue" :value="1"> {{ td('common.crontab.month.wildcard') }} </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="2">
                {{ td('common.crontab.cycleFrom') }}
                <el-input-number v-model="cycle01" :min="1" :max="11" /> -
                <el-input-number v-model="cycle02" :min="cycle01 + 1" :max="12" /> {{ td('common.crontab.month.cycleSuffix') }}
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="3">
                {{ td('common.crontab.month.averagePrefix') }}
                <el-input-number v-model="average01" :min="1" :max="11" /> {{ td('common.crontab.month.averageMiddle') }}
                <el-input-number v-model="average02" :min="1" :max="12 - average01" /> {{ td('common.crontab.month.averageSuffix') }}
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
                    :multiple-limit="8"
                >
                    <el-option
                        v-for="item in monthList"
                        :key="item.key"
                        :label="item.value"
                        :value="item.key"
                    />
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
    const cycle01 = ref(1);
    const cycle02 = ref(2);
    const average01 = ref(1);
    const average02 = ref(1);
    const checkboxList = ref([]);
    const checkCopy = ref([1]);
    const monthList = computed(() => [
        { key: 1, value: td('common.crontab.month.names.jan') },
        { key: 2, value: td('common.crontab.month.names.feb') },
        { key: 3, value: td('common.crontab.month.names.mar') },
        { key: 4, value: td('common.crontab.month.names.apr') },
        { key: 5, value: td('common.crontab.month.names.may') },
        { key: 6, value: td('common.crontab.month.names.jun') },
        { key: 7, value: td('common.crontab.month.names.jul') },
        { key: 8, value: td('common.crontab.month.names.aug') },
        { key: 9, value: td('common.crontab.month.names.sep') },
        { key: 10, value: td('common.crontab.month.names.oct') },
        { key: 11, value: td('common.crontab.month.names.nov') },
        { key: 12, value: td('common.crontab.month.names.dec') }
    ]);
    const cycleTotal = computed(() => {
        cycle01.value = props.check(cycle01.value, 1, 11);
        cycle02.value = props.check(cycle02.value, cycle01.value + 1, 12);
        return cycle01.value + '-' + cycle02.value;
    });
    const averageTotal = computed(() => {
        average01.value = props.check(average01.value, 1, 11);
        average02.value = props.check(average02.value, 1, 12 - average01.value);
        return average01.value + '/' + average02.value;
    });
    const checkboxString = computed(() => {
        return checkboxList.value.join(',');
    });
    watch(
        () => props.cron.month,
        (value) => changeRadioValue(value)
    );
    watch([radioValue, cycleTotal, averageTotal, checkboxString], () => onRadioChange());
    function changeRadioValue(value) {
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
                emit('update', 'month', '*', 'month');
                break;
            case 2:
                emit('update', 'month', cycleTotal.value, 'month');
                break;
            case 3:
                emit('update', 'month', averageTotal.value, 'month');
                break;
            case 4:
                if (checkboxList.value.length === 0) {
                    checkboxList.value.push(checkCopy.value[0]);
                } else {
                    checkCopy.value = checkboxList.value;
                }
                emit('update', 'month', checkboxString.value, 'month');
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
