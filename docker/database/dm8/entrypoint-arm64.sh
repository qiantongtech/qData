#!/bin/sh
set -e

DM_PATH=/home/dmdba/dmdbms
DM_DATA_DIR=/home/dmdba/data/DAMENG

INIT_SQL=/home/dmdba/initdata/init-qdata.sql
FIRST_RUN_FLAG=/var/run/qdata_init.done
PORT=${PORT_NUM:-5236}

# ---------- 检查是否已经初始化 ----------
check_is_init() {
  if [ -d "${DM_DATA_DIR}" ]; then
    DATABASE_ALREADY_EXISTS=true
  fi
}

# ---------- 初始化数据库 ----------
db_init() {
  echo "[dm8] init db ..."
  mkdir -p "${DM_DATA_DIR}"
  chown -R dmdba "${DM_DATA_DIR}"
  cd "${DM_PATH}/bin"

  # 初始化参数，参考你原来的需求
  INIT_ARGS="PATH=/home/dmdba/data DB_NAME=DAMENG PORT_NUM=${PORT} PAGE_SIZE=16 CHARSET=1 LENGTH_IN_CHAR=1 CASE_SENSITIVE=${CASE_SENSITIVE:-0}"

  if [ -n "${SYSDBA_PWD}" ]; then
    INIT_ARGS="${INIT_ARGS} SYSDBA_PWD=${SYSDBA_PWD}"
  fi
  if [ -n "${SYSAUDITOR_PWD}" ]; then
    INIT_ARGS="${INIT_ARGS} SYSAUDITOR_PWD=${SYSAUDITOR_PWD}"
  fi

  echo "[dm8] dminit ${INIT_ARGS}"
  gosu dmdba "${DM_PATH}/bin/dminit" ${INIT_ARGS}
  echo "[dm8] db init done."
}

# ---------- 等待 dmserver TCP 端口可用（参考老版本逻辑） ----------
wait_dm_ready() {
  echo "[dm8] wait dmserver tcp on 127.0.0.1:${PORT} ..."

  i=0
  max=6000   # 最多等 6000 秒

  while [ $i -lt $max ]; do
    # 使用 /dev/tcp 检测端口是否可连
    if (echo >"/dev/tcp/127.0.0.1/${PORT}") >/dev/null 2>&1; then
      echo "[dm8] dmserver tcp ready."
      return 0
    fi
    i=`expr $i + 1`
    sleep 1
  done

  echo "[dm8] WARN: dmserver tcp not ready after ${max}s, continue anyway."
  return 1
}

# ---------- 首次启动时导入 init-qdata.sql ----------
run_init_sql_once() {
  # 已经执行过了就直接跳过
  if [ -f "${FIRST_RUN_FLAG}" ]; then
    echo "[dm8] qdata init already done, skip."
    return 0
  fi

  # 没有 SQL 文件就不导入，避免每次都尝试
  if [ ! -f "${INIT_SQL}" ]; then
    echo "[dm8] ${INIT_SQL} not found, skip qdata init."
    mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
    touch "${FIRST_RUN_FLAG}"
    return 0
  fi

  # 必要环境变量没配也跳过
  if [ -z "${QDATA_USER}" ] || [ -z "${QDATA_PWD}" ] || [ -z "${SYSDBA_PWD}" ]; then
    echo "[dm8] QDATA_USER / QDATA_PWD / SYSDBA_PWD not set, skip qdata init."
    mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
    touch "${FIRST_RUN_FLAG}"
    return 0
  fi

  # 仅用 TCP 端口检测等待服务就绪
  wait_dm_ready

  echo "[dm8] start create business user and import init-qdata.sql ..."

  # 为了防止“用户已存在”等导致整个容器退出，这里临时关掉 set -e
  set +e

  # 1) SYSDBA 创建业务用户并授权
  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "${QDATA_USER}" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "${QDATA_USER}";
commit;
EOF

  # 2) 业务用户执行初始化脚本
  gosu dmdba "${DM_PATH}/bin/disql" "${QDATA_USER}/${QDATA_PWD}@127.0.0.1:${PORT}" <<EOF
set define off;
set CHAR_CODE UTF8;
\`${INIT_SQL}
set define on;
EOF

  # 恢复 set -e
  set -e

  mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
  touch "${FIRST_RUN_FLAG}"
  echo "[dm8] qdata init finished."
}

# ---------- 主流程 ----------

check_is_init
if [ -z "${DATABASE_ALREADY_EXISTS}" ]; then
  db_init
else
  echo "[dm8] db already exists, skip dminit."
fi

# 安装服务（修复你原脚本里的那一处 `DmAPService}` 的 bug）
if [ ! -f "${DM_PATH}/bin/DmAPService" ]; then
  "${DM_PATH}/script/root/dm_service_installer.sh" -s "${DM_PATH}/bin/DmAPService"
fi

if [ ! -f "${DM_PATH}/bin/DmServiceDMSERVER" ]; then
  "${DM_PATH}/script/root/dm_service_installer.sh" -t dmserver -p "DMSERVER" -dm_ini "${DM_DATA_DIR}/dm.ini"
fi

# 启动服务
gosu dmdba "${DM_PATH}/bin/DmAPService" start
gosu dmdba "${DM_PATH}/bin/DmServiceDMSERVER" start

# 首次导入任务放到后台，不阻塞主进程
run_init_sql_once &

# 保持容器前台进程：沿用原脚本 tail 日志
exec gosu dmdba tail -f /home/dmdba/dmdbms/log/DmServiceDMSERVER.log
