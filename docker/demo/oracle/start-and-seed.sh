#!/usr/bin/env bash
# 不用 -e，避免 init 脚本非零返回码中断
set -u

# 固定使用 XE 标准路径（你的镜像里 sqlplus 可以跑）
export ORACLE_HOME=/u01/app/oracle/product/11.2.0/xe
export ORACLE_SID="${ORACLE_SID:-XE}"
export PATH="$ORACLE_HOME/bin:$PATH"
export LD_LIBRARY_PATH="$ORACLE_HOME/lib:${LD_LIBRARY_PATH:-}"

SYS_PASS=${SYS_PASS:-"Your!Str0ngPass"}
HTTP_PORT=${HTTP_PORT:-8080}
LISTENER_PORT=${LISTENER_PORT:-1521}
SEED_SQL=${SEED_SQL:-/opt/seed.sql}
CFG_FILE="/etc/default/oracle-xe"

echo "[INFO] ORACLE_HOME=${ORACLE_HOME}"
"$ORACLE_HOME/bin/sqlplus" -v || true

# 1) 如未配置则生成配置并 configure
if /etc/init.d/oracle-xe status 2>&1 | grep -qi "not configured"; then
  echo "[INFO] Writing XE non-interactive config -> ${CFG_FILE}"
  {
    echo "ORACLE_HTTP_PORT=${HTTP_PORT}"
    echo "ORACLE_LISTENER_PORT=${LISTENER_PORT}"
    echo "ORACLE_PASSWORD=${SYS_PASS}"
    echo "ORACLE_CONFIRM_PASSWORD=${SYS_PASS}"
    echo "ORACLE_DBENABLE=true"
  } > "${CFG_FILE}"
  chmod 600 "${CFG_FILE}"
  echo "[INFO] Running configure ..."
  /etc/init.d/oracle-xe configure || echo "[WARN] configure returned non-zero (ignore)"
else
  echo "[INFO] XE seems configured already. Skipping configure."
fi

# 2) 启动服务
echo "[INFO] Starting oracle-xe ..."
/etc/init.d/oracle-xe start || echo "[WARN] start returned non-zero (will still probe)"

# 3) 等待数据库 ready（用 sqlplus 探活）
echo "[INFO] Waiting for database to be ready ..."
READY=0
for i in $(seq 1 240); do
  if echo "select 1 from dual;" | "$ORACLE_HOME/bin/sqlplus" -s / as sysdba | grep -q "1"; then
    READY=1
    break
  fi
  # 若状态又说没配置，再兜底一次
  if /etc/init.d/oracle-xe status 2>&1 | grep -qi "not configured"; then
    echo "[WARN] status says 'not configured'; re-configuring ..."
    /etc/init.d/oracle-xe configure || true
    /etc/init.d/oracle-xe start || true
  fi
  sleep 2
done
[ "$READY" -ne 1 ] && echo "[WARN] DB not confirmed ready; will still try seed."

# 4) 执行 seed.sql（建议“DROP USER … CASCADE → CREATE → 建表 → INSERT”的重置式）
if [ -f "$SEED_SQL" ]; then
  echo "[INFO] Running seed: $SEED_SQL"
  "$ORACLE_HOME/bin/sqlplus" -s / as sysdba @"$SEED_SQL" || echo "[WARN] seed failed (check SQL/logs)"
else
  echo "[WARN] Seed file not found: $SEED_SQL (skipped)"
fi

# 5) 前台守护
echo "[INFO] Tailing alert log ..."
tail -F /u01/app/oracle/diag/rdbms/*/*/trace/alert_*.log 2>/dev/null || \
tail -F /home/oracle/app/oracle/diag/rdbms/*/*/trace/alert_*.log 2>/dev/null || \
tail -f /dev/null
