#!/bin/sh
set -e

DM_PATH=/home/dmdba/dmdbms
DM_DATA_DIR=/home/dmdba/data/DAMENG

INIT_SQL=/home/dmdba/initdata/init-qdata.sql
FIRST_RUN_FLAG=/var/run/qdata_init.done
PORT=${PORT_NUM:-5236}

check_is_init() {
  if [ -d "${DM_DATA_DIR}" ]; then
    DATABASE_ALREADY_EXISTS=true
  fi
}

db_init() {
  echo "[dm8] init db ..."
  mkdir -p "${DM_DATA_DIR}"
  chown -R dmdba "${DM_DATA_DIR}"
  cd "${DM_PATH}/bin"

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

wait_dm_ready() {
  echo "[dm8] wait dmserver tcp on 127.0.0.1:${PORT} ..."

  i=0
  max=6000

  while [ $i -lt $max ]; do
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

run_init_sql_once() {
  if [ -f "${FIRST_RUN_FLAG}" ]; then
    echo "[dm8] qdata init already done, skip."
    return 0
  fi

  if [ ! -f "${INIT_SQL}" ]; then
    echo "[dm8] ${INIT_SQL} not found, skip qdata init."
    mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
    touch "${FIRST_RUN_FLAG}"
    return 0
  fi

  if [ -z "${QDATA_USER}" ] || [ -z "${QDATA_PWD}" ] || [ -z "${SYSDBA_PWD}" ]; then
    echo "[dm8] QDATA_USER / QDATA_PWD / SYSDBA_PWD not set, skip qdata init."
    mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
    touch "${FIRST_RUN_FLAG}"
    return 0
  fi

  wait_dm_ready

  echo "[dm8] start create business user and import init-qdata.sql ..."

  set +e

  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "${QDATA_USER}" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "${QDATA_USER}";
commit;
EOF

  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "ods" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ods";
commit;
EOF

  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "dwd" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dwd";
commit;
EOF

  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "dws" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dws";
commit;
EOF

  gosu dmdba "${DM_PATH}/bin/disql" "SYSDBA/${SYSDBA_PWD}@127.0.0.1:${PORT}" <<EOF
create user "ads" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ads";
commit;
EOF

  gosu dmdba "${DM_PATH}/bin/disql" "${QDATA_USER}/${QDATA_PWD}@127.0.0.1:${PORT}" <<EOF
set define off;
set CHAR_CODE UTF8;
\`${INIT_SQL}
set define on;
EOF

  set -e

  mkdir -p "$(dirname "${FIRST_RUN_FLAG}")"
  touch "${FIRST_RUN_FLAG}"
  echo "[dm8] qdata init finished."
}

check_is_init
if [ -z "${DATABASE_ALREADY_EXISTS}" ]; then
  db_init
else
  echo "[dm8] db already exists, skip dminit."
fi

if [ ! -f "${DM_PATH}/bin/DmAPService" ]; then
  "${DM_PATH}/script/root/dm_service_installer.sh" -s "${DM_PATH}/bin/DmAPService"
fi

if [ ! -f "${DM_PATH}/bin/DmServiceDMSERVER" ]; then
  "${DM_PATH}/script/root/dm_service_installer.sh" -t dmserver -p "DMSERVER" -dm_ini "${DM_DATA_DIR}/dm.ini"
fi

gosu dmdba "${DM_PATH}/bin/DmAPService" start
gosu dmdba "${DM_PATH}/bin/DmServiceDMSERVER" start

run_init_sql_once &

exec gosu dmdba tail -f /home/dmdba/dmdbms/log/DmServiceDMSERVER.log
