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
            <el-radio v-model="radioValue" :value="1"> {{ td('common.crontab.week.wildcard') }} </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="2"> {{ td('common.crontab.notSpecify') }} </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="3">
                {{ td('common.crontab.cycleFrom') }}
                <el-select clearable v-model="cycle01">
                    <el-option
                        v-for="(item, index) of weekList"
                        :key="index"
                        :label="item.value"
                        :value="item.key"
                        :disabled="item.key === 7"
                        >{{ item.value }}</el-option
                    >
                </el-select>
                -
                <el-select clearable v-model="cycle02">
                    <el-option
                        v-for="(item, index) of weekList"
                        :key="index"
                        :label="item.value"
                        :value="item.key"
                        :disabled="item.key <= cycle01"
                        >{{ item.value }}</el-option
                    >
                </el-select>
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="4">
                {{ td('common.crontab.week.nthWeekPrefix') }}
                <el-input-number v-model="average01" :min="1" :max="4" /> {{ td('common.crontab.week.nthWeekSuffix') }}
                <el-select clearable v-model="average02">
                    <el-option
                        v-for="item in weekList"
                        :key="item.key"
                        :label="item.value"
                        :value="item.key"
                    />
                </el-select>
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="5">
                {{ td('common.crontab.week.lastPrefix') }}
                <el-select clearable v-model="weekday">
                    <el-option
                        v-for="item in weekList"
                        :key="item.key"
                        :label="item.value"
                        :value="item.key"
                    />
                </el-select>
            </el-radio>
        </el-form-item>

        <el-form-item>
            <el-radio v-model="radioValue" :value="6">
                {{ td('common.crontab.specify') }}
                <el-select
                    class="multiselect"
                    clearable
                    v-model="checkboxList"
                    :placeholder="td('common.crontab.multiSelect')"
                    multiple
                    :multiple-limit="6"
                >
                    <el-option
                        v-for="item in weekList"
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
    const radioValue = ref(2);
    const cycle01 = ref(2);
    const cycle02 = ref(3);
    const average01 = ref(1);
    const average02 = ref(2);
    const weekday = ref(2);
    const checkboxList = ref([]);
    const checkCopy = ref([2]);
    const weekList = computed(() => [
        { key: 1, value: td('common.crontab.week.names.sun') },
        { key: 2, value: td('common.crontab.week.names.mon') },
        { key: 3, value: td('common.crontab.week.names.tue') },
        { key: 4, value: td('common.crontab.week.names.wed') },
        { key: 5, value: td('common.crontab.week.names.thu') },
        { key: 6, value: td('common.crontab.week.names.fri') },
        { key: 7, value: td('common.crontab.week.names.sat') }
    ]);
    const cycleTotal = computed(() => {
        cycle01.value = props.check(cycle01.value, 1, 6);
        cycle02.value = props.check(cycle02.value, cycle01.value + 1, 7);
        return cycle01.value + '-' + cycle02.value;
    });
    const averageTotal = computed(() => {
        average01.value = props.check(average01.value, 1, 4);
        average02.value = props.check(average02.value, 1, 7);
        return average02.value + '#' + average01.value;
    });
    const weekdayTotal = computed(() => {
        weekday.value = props.check(weekday.value, 1, 7);
        return weekday.value + 'L';
    });
    const checkboxString = computed(() => {
        return checkboxList.value.join(',');
    });
    watch(
        () => props.cron.week,
        (value) => changeRadioValue(value)
    );
    watch([radioValue, cycleTotal, averageTotal, weekdayTotal, checkboxString], () =>
        onRadioChange()
    );
    function changeRadioValue(value) {
        if (value === '*') {
            radioValue.value = 1;
        } else if (value === '?') {
            radioValue.value = 2;
        } else if (value.indexOf('-') > -1) {
            const indexArr = value.split('-');
            cycle01.value = Number(indexArr[0]);
            cycle02.value = Number(indexArr[1]);
            radioValue.value = 3;
        } else if (value.indexOf('#') > -1) {
            const indexArr = value.split('#');
            average01.value = Number(indexArr[1]);
            average02.value = Number(indexArr[0]);
            radioValue.value = 4;
        } else if (value.indexOf('L') > -1) {
            const indexArr = value.split('L');
            weekday.value = Number(indexArr[0]);
            radioValue.value = 5;
        } else {
            checkboxList.value = [...new Set(value.split(',').map((item) => Number(item)))];
            radioValue.value = 6;
        }
    }
    function onRadioChange() {
        console.log('cccc');

        if (radioValue.value === 2 && props.cron.day === '?') {
            emit('update', 'day', '*', 'week');
        }
        if (radioValue.value !== 2 && props.cron.day !== '?') {
            emit('update', 'day', '?', 'week');
        }
        switch (radioValue.value) {
            case 1:
                emit('update', 'week', '*', 'week');
                break;
            case 2:
                emit('update', 'week', '?', 'week');
                break;
            case 3:
                emit('update', 'week', cycleTotal.value, 'week');
                break;
            case 4:
                emit('update', 'week', averageTotal.value, 'week');
                break;
            case 5:
                emit('update', 'week', weekdayTotal.value, 'week');
                break;
            case 6:
                if (checkboxList.value.length === 0) {
                    checkboxList.value.push(checkCopy.value[0]);
                } else {
                    checkCopy.value = checkboxList.value;
                }
                emit('update', 'week', checkboxString.value, 'week');
                break;
        }
    }
</script>

<style lang="scss" scoped>
    .el-input-number--small,
    .el-select,
    .el-select--small {
        margin: 0 0.5rem;
    }
    .el-select,
    .el-select--small {
        width: 8rem;
    }
    .el-select.multiselect,
    .el-select--small.multiselect {
        width: 17.8rem;
    }
</style>
