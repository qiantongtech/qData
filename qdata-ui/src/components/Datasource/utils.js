import { DatasourceTypes, config } from './config';
import { listDaDatasource } from '@/api/da/dataSource/dataSource';

// 根据flag获取数据源类型
export function getDatasourceTypes(flag) {
    if (!flag || !config[flag]) return Object.values(DatasourceTypes);
    const rules = config[flag];

    return Object.keys(DatasourceTypes)
        .reduce((acc, key) => {
            if (rules[key] && rules[key] === 3) return acc;
            acc.push({ ...DatasourceTypes[key], disabled: rules[key] === 2 });
            return acc;
        }, [])
        .sort((a, b) => {
            if (a.disabled == b.disabled) return 0;
            return a.disabled ? 1 : -1;
        });
}

// 获取全部数据数据源
export function getDatasourceData(params) {
    return listDaDatasource(params).then((res) => {
        return res.data.rows
    });
}

// 根据flag获取可用数据源
export function getAvailableDatasource(data, flag) {
    if (!data) return [];
    if (!flag || !config[flag]) return data;
    const rules = config[flag];
    const types = Object.values(DatasourceTypes);
    return data
        .reduce((acc, item) => {
            if (!item.datasourceType) return acc;
            const type = types.find((type) => type.value === item.datasourceType);
            if (!type) return acc;
            if (rules[type.key] && rules[type.key] === 3) return acc;
            acc.push({ ...item, disabled: rules[type.key] === 2 });
            return acc;
        }, [])
        .sort((a, b) => {
            if (a.disabled == b.disabled) return 0;
            return a.disabled ? 1 : -1;
        });
}

export function getDatasourceActive(value) {
    return Object.values(DatasourceTypes).find((item) => item.value === value);
}
