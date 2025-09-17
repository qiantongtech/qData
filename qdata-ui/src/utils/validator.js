export function createNatureIntegerValidator(fieldName = '该字段') {
    return function(rule, value, callback) {
        const num = Number(value);
        if (!Number.isInteger(num)) {
            callback(new Error(`${fieldName}必须为整数`));
        } else if (num < 0) {
            callback(new Error(`${fieldName}必须大于等于0`));
        } else {
            callback();
        }
    };
}

export function createNonnegativeValidator(fieldName = '该字段') {
    return function(rule, value, callback) {
        const num = Number(value);
        if (num < 0) {
            callback(new Error(`${fieldName}必须大于等于0`));
        } else {
            callback();
        }
    };
}