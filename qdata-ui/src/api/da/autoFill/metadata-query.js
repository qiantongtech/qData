import request from '@/utils/request'
import {encrypt} from "@/utils/aesEncrypt";

// 数据库信息api

// 获取表名
export function getTables(params) {
  return request({
    url: '/data/dts/metadata/getTables',
    method: 'get',
    params
  })
}

// 获取schema
export function getTableSchema(params) {
  return request({
    url: '/data/dts/metadata/getDBSchema',
    method: 'get',
    params
  })
}

// 获取字段
export function getColumns(params) {
  return request({
    url: '/data/dts/metadata/getColumns',
    method: 'get',
    params
  })
}

// 根据sql获取字段
export function getColumnsByQuerySql(data) {
  data = JSON.parse(JSON.stringify(data))
  data.querySql = encrypt(data.querySql)
  return request({
    url: '/data/dts/metadata/getColumnsByQuerySql2',
    method: 'post',
    data
  })
}

// 根据datasourceID、tablename创建表【目标端】
export function createTable(params) {
  return request({
    url: '/data/dts/metadata/createTable',
    method: 'post',
    params
  })
}
// 判断字段是否存在，存在，即更新值，否则添加字段
export function updateColumnsValue(query) {
  return request({
    url: '/data/dts/metadata/updateColumnsValue',
    method: 'post',
    data: query
  })
}

export function listCountByDbInfo(data) {
  return request({
    url: '/data/dts/metadata/listCountByDbInfo',
    method: 'post',
    data
  })
}
