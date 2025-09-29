#!/bin/bash
set -e

DB_PATH=${DB_PATH:-"/home/dmdba/data"}
INSTANCE_NAME=${INSTANCE_NAME:-"DMSERVER"}
DB_NAME=${DB_NAME:-"DAMENG"}
DMDB_INSTALL_PATH=${DMDB_INSTALL_PATH:-"/home/dmdba/dmdb"}
INIT_PARAMS=""
PORT_NUM=${PORT_NUM:-"5236"}
TIME_ZONE=${TIME_ZONE:-"+08:00"}
BUFFER=${BUFFER:-"8000"}
PAGE_CHECK=${PAGE_CHECK:-"3"}
PAGE_SIZE=${PAGE_SIZE:-"8"}
LOG_SIZE=${LOG_SIZE:-"4096"}
EXTENT_SIZE=${EXTENT_SIZE:-"16"}
CHARSET=${CHARSET:-"0"}
USE_DB_NAME=${USE_DB_NAME:-"1"}
AUTO_OVERWRITE=${AUTO_OVERWRITE:-"0"}
BLANK_PAD_MODE=${BLANK_PAD_MODE:-"0"}
DPC_MODE=${DPC_MODE:-"0"}
CASE_SENSITIVE=${CASE_SENSITIVE:-"y"}

OTHER_PARAMS=${OTHER_PARAMS:-""}

SYSDBA_PWD=${SYSDBA_PWD:-""}
SYSAUDITOR_PWD=${SYSAUDITOR_PWD:-""}

# ===== 新增：一次性执行标记 & 日志路径 =====
FIRST_RUN_FLAG="/var/run/dm8_first_run.done"   # 首次执行标记
INIT_LOG="/init.log"                            # 初始化日志文件

function init_db() {
    if [ -z "$SYSDBA_PWD" ]; then
        echo "SYSDBA_PWD is empty, please set it in environment variables"
        exit 1
    fi
    if [ -z "$SYSAUDITOR_PWD" ]; then
        echo "SYSAUDITOR_PWD is empty, please set it in environment variables"
        exit 1
    fi
    # 判断DB_PATH文件夹内是否存在文件
    if [ -d "$DB_PATH" ]; then
        if [ "$(ls -A $DB_PATH)" ]; then
            echo "DB_PATH is not empty, please check it"
            exit 1
        fi
    else
        echo "DB_PATH is not exist, create it"
        mkdir -p $DB_PATH
        chown -R dmdba $DB_PATH
    fi
    INIT_PARAMS="$INIT_PARAMS PATH=$DB_PATH SYSDBA_PWD=$SYSDBA_PWD SYSAUDITOR_PWD=$SYSAUDITOR_PWD INSTANCE_NAME=$INSTANCE_NAME"
    INIT_PARAMS="$INIT_PARAMS PORT_NUM=$PORT_NUM DB_NAME=$DB_NAME TIME_ZONE=$TIME_ZONE BUFFER=$BUFFER PAGE_CHECK=$PAGE_CHECK PAGE_SIZE=$PAGE_SIZE"
    INIT_PARAMS="$INIT_PARAMS LOG_SIZE=$LOG_SIZE EXTENT_SIZE=$EXTENT_SIZE CHARSET=$CHARSET USE_DB_NAME=$USE_DB_NAME"
    INIT_PARAMS="$INIT_PARAMS AUTO_OVERWRITE=$AUTO_OVERWRITE BLANK_PAD_MODE=$BLANK_PAD_MODE DPC_MODE=$DPC_MODE CASE_SENSITIVE=$CASE_SENSITIVE"
    INIT_PARAMS="$INIT_PARAMS $OTHER_PARAMS"
    echo "Initializing database..."
    echo "Initializing database with parameters:"
    echo $INIT_PARAMS
    sudo -u dmdba /home/dmdba/dmdb/bin/dminit $INIT_PARAMS
    echo "Database initialized"
}
function start_dmap() {
    echo "Starting DmAPService..."
    sudo -u dmdba /home/dmdba/dmdb/bin/dmap dmap_ini=/home/dmdba/dmdb/bin/dmap.ini &
    echo "DmAPService started"
}

# 创建一个函数，用来修改文件的权限
function modify_db_permissions() {
    echo "Modifying $DB_PATH permissions..."
    chown -R dmdba $DB_PATH
    echo "$DB_PATH permissions modified"
}

function check_initialized() {
    # 判断 $DB_PATH/$DB_NAME/dm.ini 是否存在
    if [ -f "$DB_PATH/$DB_NAME/dm.ini" ]; then
        echo "Database already initialized"
        modify_db_permissions
    else
        echo "Database not initialized"
        init_db
    fi
}

# ===== 新增：等待 TCP 就绪的小函数 =====
wait_tcp_ready() {
  local waited=0
  local timeout=600
  echo "Waiting for dmserver TCP on 127.0.0.1:${PORT_NUM} ..."
  # /dev/tcp 存在即可用；若镜像不支持，可改用 nc -z
  while ! (echo >"/dev/tcp/127.0.0.1/${PORT_NUM}") >/dev/null 2>&1; do
    sleep 2
    waited=$((waited+2))
    if [ $waited -ge $timeout ]; then
      echo "ERROR: TCP 127.0.0.1:${PORT_NUM} not ready in ${timeout}s"
      return 1
    fi
  done
  echo "TCP ready."
  return 0
}

# ===== 新增：首次执行的后台任务（按你的两段 disql 顺序）=====
post_boot_first_run() {
  # 已执行过则直接退出
  if [ -f "$FIRST_RUN_FLAG" ]; then
    return 0
  fi

  # 等 TCP 就绪（服务启动完成）
  if ! wait_tcp_ready; then
    echo "WARN: dmserver TCP not ready; skip first-run init."
    return 0
  fi

  # 准备日志
  if [ ! -f "$INIT_LOG" ]; then
    echo "init database at $(date +'%Y-%m-%d %H:%M:%S')" > "$INIT_LOG"
  fi

  echo "Creating business user & grant ..." | tee -a "$INIT_LOG"
  # 第一步：SYSDBA 执行建用户与授权
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "${QDATA_USER}" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "${QDATA_USER}";
COMMIT;
EOF
  # 原始层
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "ods" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ods";
COMMIT;
EOF
  # 明细层
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "dwd" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dwd";
COMMIT;
EOF
  # 主题层
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "dws" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dws";
COMMIT;
EOF
  # 应用层
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "ads" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ads";
COMMIT;
EOF

  echo "Importing initial data ..." | tee -a "$INIT_LOG"
  # 第二步：业务用户导入数据（确保 /home/dmdba/initdata/init-qdata.sql 可读）
  /home/dmdba/dmdb/bin/disql ${QDATA_USER}/${QDATA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
set define off;
set CHAR_CODE UTF8;
\`/home/dmdba/initdata/init-qdata.sql
set define on;
EOF

  # 落标记：仅首次执行
  touch "$FIRST_RUN_FLAG"
  echo "First-run initialization finished." | tee -a "$INIT_LOG"
}

cd $DMDB_INSTALL_PATH/bin
# 检查DB是否初始化，如果没有初始化则执行初始化
check_initialized

# 启动 DmAPServer
start_dmap

# ===== 新增：后台启动首次执行任务（不阻塞主进程）=====
post_boot_first_run &

# 启动数据库实例（前台）
echo "Starting DmServer..."
exec sudo -u dmdba /home/dmdba/dmdb/bin/dmserver path=$DB_PATH/$DB_NAME/dm.ini
