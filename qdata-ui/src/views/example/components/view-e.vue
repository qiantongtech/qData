<template>
    <dp-main>
        <dp-desc :data="data" v-model="active"></dp-desc>
    </dp-main>
</template>

<script setup name="DetailPopResViewE">
    const data = [
        {
            label: '基本信息',
            value: 'jbxx',
            config: {
                column: 2,
                border: true
            },
            children: [
                {
                    label: '水库名称',
                    value: '加格斯台水库'
                },
                {
                    label: '水库代码',
                    value: 'HP0016540220000013'
                },
                {
                    label: '水库所在位置',
                    value: '察布查尔县加格斯台乡'
                },
                {
                    label: '工程规模',
                    value: '小（1）型'
                },
                {
                    label: '总库容(万m³)',
                    value: '617'
                },
                {
                    label: '工程建设情况',
                    value: '已建'
                }
            ]
        },
        {
            label: '特征值信息',
            value: 'tzzxx',
            config: {
                column: 2,
                border: true
            },
            children: [
                {
                    label: '死水位：',
                    value: '1419.5'
                },
                {
                    label: '死库容：',
                    value: '50'
                },
                {
                    label: '正常蓄水位：',
                    value: '1448.1'
                },
                {
                    label: '兴利库容：',
                    value: '529'
                },
                {
                    label: '防洪高水位：',
                    value: '1449.32'
                },
                {
                    label: '防洪库容：',
                    value: '38'
                },
                {
                    label: '设计洪水位：',
                    value: '1448.89'
                }
            ]
        },
        {
            label: '汛限水位',
            value: 'xxsw',
            config: {
                direction: 'vertical',
                column: 4,
                border: true
            },
            children: [
                {
                    label: '开始月日',
                    value: '-.-'
                },
                {
                    label: '结束月日',
                    value: '-.-'
                },
                {
                    label: '汛限水位',
                    value: '-.-'
                },
                {
                    label: '汛期类别',
                    value: '-.-'
                }
            ]
        },
        {
            label: '水库责任人',
            value: 'skzrr',
            config: {
                column: 2,
                border: true
            },
            children: [
                {
                    label: '政府责任人姓名：',
                    value: '关晓军'
                },
                {
                    label: '政府单位名称：',
                    value: '察布查尔县人民政府'
                },
                {
                    label: '政府责任人职务：',
                    value: '县委副书记、县长'
                },
                {
                    label: '政府责任人电话：',
                    value: '18097846738'
                },
                {
                    label: '主管部门责任人姓名：',
                    value: '关俊辉'
                },
                {
                    label: '主管部门名称：',
                    value: '农水集团伊南供水公司'
                },
                {
                    label: '主管部门责任人职务：',
                    value: '执行董事'
                },
                {
                    label: '主管部门责任人电话：',
                    value: '13779158608'
                }
            ]
        }
    ];
    const active = ref('jbxx');
</script>

<style lang="scss" scoped>
    ::v-deep .dp-main--body {
        padding: var(--dp-main-gap);
        box-sizing: border-box;
    }

    ::v-deep .dp-desc--label {
        width: 240px;
    }
</style>
