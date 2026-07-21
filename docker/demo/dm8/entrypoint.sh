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

# ===== One-time execution marker and log path =====
FIRST_RUN_FLAG="/var/run/dm8_first_run.done"   # First-run marker
INIT_LOG="/init.log"                            # Initialization log file

function init_db() {
    if [ -z "$SYSDBA_PWD" ]; then
        echo "SYSDBA_PWD is empty, please set it in environment variables"
        exit 1
    fi
    if [ -z "$SYSAUDITOR_PWD" ]; then
        echo "SYSAUDITOR_PWD is empty, please set it in environment variables"
        exit 1
    fi
    # Check whether the DB_PATH directory contains files
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

# Create a function to update file permissions
function modify_db_permissions() {
    echo "Modifying $DB_PATH permissions..."
    chown -R dmdba $DB_PATH
    echo "$DB_PATH permissions modified"
}

function check_initialized() {
    # Check whether $DB_PATH/$DB_NAME/dm.ini exists
    if [ -f "$DB_PATH/$DB_NAME/dm.ini" ]; then
        echo "Database already initialized"
        modify_db_permissions
    else
        echo "Database not initialized"
        init_db
    fi
}

# ===== Helper function that waits for TCP readiness =====
wait_tcp_ready() {
  local waited=0
  local timeout=600
  echo "Waiting for dmserver TCP on 127.0.0.1:${PORT_NUM} ..."
  # Use /dev/tcp when available; use nc -z if the image does not support it
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

# ===== First-run background task (execute the two disql stages in order)=====
post_boot_first_run() {
  # Exit immediately if this task has already run
  if [ -f "$FIRST_RUN_FLAG" ]; then
    return 0
  fi

  # Wait for TCP readiness (service startup complete)
  if ! wait_tcp_ready; then
    echo "WARN: dmserver TCP not ready; skip first-run init."
    return 0
  fi

  # Prepare the log
  if [ ! -f "$INIT_LOG" ]; then
    echo "init database at $(date +'%Y-%m-%d %H:%M:%S')" > "$INIT_LOG"
  fi

  echo "Creating business user & grant ..." | tee -a "$INIT_LOG"
  # Step 1: create the user and grant permissions as SYSDBA
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "${QDATA_USER}" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "${QDATA_USER}";
COMMIT;
EOF
  # Operational data store layer
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "ods" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ods";
COMMIT;
EOF
  # Detail data layer
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "dwd" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dwd";
COMMIT;
EOF
  # Subject data layer
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "dws" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "dws";
COMMIT;
EOF
  # Application data layer
  /home/dmdba/dmdb/bin/disql SYSDBA/${SYSDBA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
create user "ads" identified by "${QDATA_PWD}" hash with SHA512 salt;
grant "PUBLIC","SOI","DBA" to "ads";
COMMIT;
EOF

  echo "Importing initial data ..." | tee -a "$INIT_LOG"
  # Step 2: import data as the application user (ensure /home/dmdba/initdata/init-qdata.sql is readable)
  /home/dmdba/dmdb/bin/disql ${QDATA_USER}/${QDATA_PWD}@localhost:${PORT_NUM} <<EOF >> "$INIT_LOG" 2>&1
set define off;
set CHAR_CODE UTF8;
\`/home/dmdba/initdata/init-qdata.sql
set define on;
EOF

  # Write the marker so this runs only once
  touch "$FIRST_RUN_FLAG"
  echo "First-run initialization finished." | tee -a "$INIT_LOG"
}

cd $DMDB_INSTALL_PATH/bin
# Initialize the database if it has not been initialized
check_initialized

# Start DmAPServer
start_dmap

# ===== Start the first-run task in the background without blocking the main process=====
post_boot_first_run &

# Start the database instance in the foreground
echo "Starting DmServer..."
exec sudo -u dmdba /home/dmdba/dmdb/bin/dmserver path=$DB_PATH/$DB_NAME/dm.ini
