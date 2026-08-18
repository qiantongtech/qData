#!/bin/sh

# -----------------------------------------------------------------------------
# qData Docker Management Script
#
# Purpose:
#   Manages the qData Docker Compose deployment on Linux and macOS, including
#   environment validation, startup, status checks, logs, restarts, diagnostics,
#   shutdown, and complete uninstallation.
#
# Usage:
#   sh qdata.sh <command> [mode] [options]
#
# Requirements:
#   POSIX-compatible shell, Docker Engine 20.10.0 or later, and Docker Compose
#   2.20.2 or later. Keep this script POSIX-sh compatible.
# -----------------------------------------------------------------------------

set -u

# Runtime paths, version constraints, and command-line state.
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" 2>/dev/null && pwd)
PROJECT_NAME=${QDATA_PROJECT_NAME:-qdata}
ENV_FILE="$SCRIPT_DIR/.env"
ENV_EXAMPLE="$SCRIPT_DIR/.env.example"
MIN_DOCKER_VERSION=20.10.0
MIN_COMPOSE_VERSION=2.20.2
COMPOSE_KIND=
COMPOSE_FILE=
DB_TYPE=
MODE=
WITH_DEMO=0
OFFLINE=0
FORCE=0

# -----------------------------------------------------------------------------
# Command-line help and logging utilities
# -----------------------------------------------------------------------------

# Print the supported commands, modes, and options.
usage() {
    cat <<'EOF'
qData Docker 管理脚本

用法：
  sh qdata.sh start light [--db dm8|mysql] [--demo] [--offline] [--force]
  sh qdata.sh start all   [--db dm8|mysql] [--demo] [--offline] [--force]
  sh qdata.sh start local [--db dm8|mysql] [--offline] [--force]
  sh qdata.sh status
  sh qdata.sh logs
  sh qdata.sh stop
  sh qdata.sh restart
  sh qdata.sh doctor [light|all|local]
  sh qdata.sh uninstall

说明：
  --db       覆盖 .env 中的 DB_TYPE，并写回 .env
  --demo     启动演示数据库（可与 light/all 叠加）
  --offline  不访问镜像仓库，仅使用本机已有镜像
  --force    忽略资源不足、远程 Docker Context 等非配置类限制
  uninstall  彻底删除 qData 容器、网络、数据卷、运行数据和未被占用的相关镜像
EOF
}

# Return a timestamp suitable for operational log messages.
timestamp() {
    date '+%Y-%m-%d %H:%M:%S'
}

# Write a timestamped informational message.
log() {
    message=$1
    printf '[%s] %s\n' "$(timestamp)" "$message"
}

# Write a non-fatal warning message.
warn() {
    log "警告：$1"
}

# Write an error message and terminate with the requested exit code.
fail() {
    log "错误：$1"
    exit "${2:-1}"
}

# -----------------------------------------------------------------------------
# Environment file and version helpers
# -----------------------------------------------------------------------------

# Read a normalized value from a dotenv-style configuration file.
env_value() {
    key=$1
    file=${2:-$ENV_FILE}
    [ -f "$file" ] || return 1
    awk -v wanted="$key" '
        /^[[:space:]]*#/ { next }
        {
            line=$0
            sub(/^[[:space:]]*/, "", line)
            pos=index(line, "=")
            if (pos == 0) next
            name=substr(line, 1, pos-1)
            gsub(/[[:space:]]/, "", name)
            if (name == wanted) {
                value=substr(line, pos+1)
                sub(/^[[:space:]]*/, "", value)
                sub(/[[:space:]]*$/, "", value)
                if ((substr(value,1,1) == "\"" && substr(value,length(value),1) == "\"") ||
                    (substr(value,1,1) == "\047" && substr(value,length(value),1) == "\047")) {
                    value=substr(value,2,length(value)-2)
                }
                print value
                exit
            }
        }
    ' "$file"
}

# Update or append a key in the active environment file atomically.
write_env_value() {
    key=$1
    value=$2
    tmp_file="$ENV_FILE.qdata.tmp"
    awk -v wanted="$key" -v replacement="$value" '
        BEGIN { found=0 }
        {
            line=$0
            probe=line
            sub(/^[[:space:]]*/, "", probe)
            pos=index(probe, "=")
            name=(pos > 0 ? substr(probe,1,pos-1) : "")
            gsub(/[[:space:]]/, "", name)
            if (name == wanted && line !~ /^[[:space:]]*#/) {
                print wanted "=" replacement
                found=1
            } else {
                print line
            }
        }
        END { if (!found) print wanted "=" replacement }
    ' "$ENV_FILE" >"$tmp_file" || return 1
    mv "$tmp_file" "$ENV_FILE"
}

# Extract the numeric portion of a tool version string.
numeric_version() {
    printf '%s\n' "$1" | sed -E 's/^[^0-9]*//; s/[^0-9.].*$//'
}

# Return success when the actual semantic version meets the minimum version.
version_ge() {
    awk -v actual="$1" -v required="$2" 'BEGIN {
        na=split(actual,a,"."); nr=split(required,r,"."); n=(na>nr?na:nr)
        for(i=1;i<=n;i++) { av=(i<=na?a[i]+0:0); rv=(i<=nr?r[i]+0:0); if(av>rv) exit 0; if(av<rv) exit 1 }
        exit 0
    }'
}

# -----------------------------------------------------------------------------
# Docker Compose command wrappers
# -----------------------------------------------------------------------------

# Invoke either the Docker Compose plugin or the legacy standalone executable.
compose() {
    if [ "$COMPOSE_KIND" = plugin ]; then
        docker compose "$@"
    else
        docker-compose "$@"
    fi
}

# Invoke Compose with the shared project, environment, and configuration options.
compose_base() {
    compose --env-file "$ENV_FILE" -p "$PROJECT_NAME" -f "$COMPOSE_FILE" "$@"
}

# Invoke Compose for the currently selected qData deployment profile.
compose_mode() {
    compose_base --profile "$MODE" "$@"
}

# Invoke Compose for the optional demonstration-data profile.
compose_demo() {
    compose_base --profile demo "$@"
}

# Invoke Compose across every profile in the selected Compose file.
compose_everything() {
    compose_base --profile '*' "$@"
}

# Detect the available Compose implementation and enforce its minimum version.
discover_compose() {
    if docker compose version >/dev/null 2>&1; then
        COMPOSE_KIND=plugin
        output=$(docker compose version 2>/dev/null)
    elif command -v docker-compose >/dev/null 2>&1; then
        COMPOSE_KIND=standalone
        output=$(docker-compose version 2>/dev/null)
    else
        fail "未找到 Docker Compose v2。请安装 Docker Compose Plugin ${MIN_COMPOSE_VERSION} 或更高版本。" 2
    fi

    version=$(numeric_version "$output")
    [ -n "$version" ] || fail "无法识别 Docker Compose 版本：$output" 2
    version_ge "$version" "$MIN_COMPOSE_VERSION" ||
        fail "Docker Compose $version 过旧，当前配置使用 include，最低要求 ${MIN_COMPOSE_VERSION}。" 2
    log "Docker Compose 检查通过：$version"
}

# -----------------------------------------------------------------------------
# Deployment preflight checks
# -----------------------------------------------------------------------------

# Validate Docker availability, compatibility, security mode, and context scope.
check_docker() {
    # Confirm that the Docker CLI is installed and the daemon is reachable.
    command -v docker >/dev/null 2>&1 ||
        fail "未安装 Docker。请先安装 Docker Desktop 或 Docker Engine。" 2

    info_output=$(docker info 2>&1)
    if [ $? -ne 0 ]; then
        case "$info_output" in
            *permission\ denied*|*Permission\ denied*)
                fail "当前用户没有 Docker 权限。Linux 可将用户加入 docker 组后重新登录；不要递归修改部署目录权限。" 2
                ;;
            *Cannot\ connect*|*cannot\ connect*|*no\ such\ file*|*connection\ refused*)
                fail "Docker 服务未运行。macOS 请启动 Docker Desktop；Linux 请执行 sudo systemctl start docker。" 2
                ;;
            *) fail "无法连接 Docker：$info_output" 2 ;;
        esac
    fi

    # Enforce the minimum engine version and Linux-container mode.
    server_version=$(docker version --format '{{.Server.Version}}' 2>/dev/null)
    server_version=$(numeric_version "$server_version")
    [ -n "$server_version" ] || fail "无法获取 Docker Engine 版本。" 2
    version_ge "$server_version" "$MIN_DOCKER_VERSION" ||
        fail "Docker Engine $server_version 过旧，最低要求 ${MIN_DOCKER_VERSION}，建议使用 24 或更高版本。" 2

    os_type=$(docker info --format '{{.OSType}}' 2>/dev/null)
    [ "$os_type" = linux ] || fail "当前 Docker 不是 Linux Containers 模式，请切换到 Linux Containers。" 2

    # Reject unsupported rootless engines and unsafe remote bind-mount contexts.
    security_options=$(docker info --format '{{json .SecurityOptions}}' 2>/dev/null || printf '')
    case "$security_options" in
        *rootless*) fail "当前使用 Rootless Docker，但 qData 的 DM8、Neo4j 等服务需要 privileged 能力，请改用普通 Docker Engine。" 2 ;;
    esac

    context=$(docker context show 2>/dev/null || printf 'unknown')
    endpoint=$(docker context inspect "$context" --format '{{.Endpoints.docker.Host}}' 2>/dev/null || printf '')
    case "$endpoint" in
        ssh://*|tcp://*)
            if [ "$FORCE" -ne 1 ]; then
                fail "当前 Docker Context 指向远程主机（${endpoint}），本地目录挂载会失败。确认了解风险后可使用 --force。" 2
            fi
            warn "正在使用远程 Docker Context：$endpoint"
            ;;
    esac
    log "Docker Engine 检查通过：${server_version}，架构 $(docker info --format '{{.Architecture}}' 2>/dev/null)"
}

# Create the active environment file from the distributed example when needed.
ensure_env() {
    if [ ! -f "$ENV_FILE" ]; then
        [ -f "$ENV_EXAMPLE" ] || fail "缺少 .env 和 .env.example，部署包不完整。" 3
        cp "$ENV_EXAMPLE" "$ENV_FILE" || fail "无法根据 .env.example 创建 .env。" 3
        log "已根据 .env.example 创建 .env"
    fi
}

# Validate the requested database and select its matching Compose file.
select_database() {
    requested=$1
    current=$(env_value DB_TYPE 2>/dev/null || printf '')
    [ -n "$current" ] || current=dm8

    if [ -n "$requested" ]; then
        case "$requested" in dm8|mysql) ;; *) fail "数据库类型只能是 dm8 或 mysql。" 3 ;; esac
        if [ "$requested" != "$current" ]; then
            write_env_value DB_TYPE "$requested" || fail "无法更新 .env 中的 DB_TYPE。" 3
            log "数据库类型已从 $current 切换为 ${requested}"
        fi
        DB_TYPE=$requested
    else
        DB_TYPE=$current
    fi

    case "$DB_TYPE" in
        dm8) COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml" ;;
        mysql) COMPOSE_FILE="$SCRIPT_DIR/docker-compose-mysql.yml" ;;
        *) fail ".env 中 DB_TYPE=$DB_TYPE 无效，只能使用 dm8 或 mysql。" 3 ;;
    esac
    [ -f "$COMPOSE_FILE" ] || fail "缺少 Compose 文件：$COMPOSE_FILE" 3
    log "部署选择：数据库=${DB_TYPE}，模式=${MODE}，演示数据=$WITH_DEMO"
}

# Convert an entrypoint to Unix line endings and ensure it is executable.
normalize_entrypoint() {
    file=$1
    [ -f "$file" ] || return 0
    if LC_ALL=C grep -q "$(printf '\r')$" "$file" 2>/dev/null; then
        tmp_file="$file.qdata.tmp"
        carriage_return=$(printf '\r')
        sed "s/${carriage_return}\$//" "$file" >"$tmp_file" && mv "$tmp_file" "$file" ||
            fail "无法修复脚本换行符：$file" 3
        log "已将 CRLF 转换为 LF：${file#$SCRIPT_DIR/}"
    fi
    chmod u+x "$file" 2>/dev/null || warn "无法设置执行权限：${file#$SCRIPT_DIR/}"
}

# Normalize every database entrypoint shipped in the deployment package.
prepare_files() {
    normalize_entrypoint "$SCRIPT_DIR/database/dm8/entrypoint.sh"
    normalize_entrypoint "$SCRIPT_DIR/database/dm8/entrypoint-arm64.sh"
    normalize_entrypoint "$SCRIPT_DIR/demo/dm8/entrypoint.sh"
    normalize_entrypoint "$SCRIPT_DIR/demo/dm8/entrypoint-arm64.sh"
}

# Fail with a package-integrity error when a required path is missing.
require_path() {
    path=$1
    description=$2
    [ -e "$path" ] || fail "部署包不完整：缺少${description}（${path#$SCRIPT_DIR/}）。" 3
}

# Verify that all artifacts required by the selected mode and database exist.
check_package() {
    # Light and full modes require the web application and backend assets.
    if [ "$MODE" = light ] || [ "$MODE" = all ]; then
        require_path "$SCRIPT_DIR/nginx/dist/index.html" "前端文件 nginx/dist/index.html"
        require_path "$SCRIPT_DIR/qdata-server/application-prod.yml" "qData服务配置"
        require_path "$SCRIPT_DIR/qdata-service-quality/application-prod.yml" "质量服务配置"
        require_path "$SCRIPT_DIR/qdata-service-ai/application-prod.yml" "AI服务配置"
        require_path "$SCRIPT_DIR/qdata-server/datax" "DataX目录"
    fi

    # Full and local modes require the scheduler runtimes and qData ETL artifact.
    if [ "$MODE" = all ] || [ "$MODE" = local ]; then
        require_path "$SCRIPT_DIR/dolphinscheduler/soft/spark" "DolphinScheduler使用的Spark目录"
        require_path "$SCRIPT_DIR/dolphinscheduler/soft/flink" "DolphinScheduler使用的Flink目录"
        etl_jar_found=0
        for etl_jar in "$SCRIPT_DIR"/dolphinscheduler/resource/default/resources/spark-jar/qdata-executor-etl*.jar; do
            [ -f "$etl_jar" ] && etl_jar_found=1
        done
        [ "$etl_jar_found" -eq 1 ] ||
            fail "部署包不完整：缺少 qData ETL jar（dolphinscheduler/resource/default/resources/spark-jar/）。" 3
    fi

    # Each database implementation must include its corresponding initialization SQL.
    if [ "$DB_TYPE" = dm8 ]; then
        require_path "$SCRIPT_DIR/database/dm8/init-qdata.sql" "DM8初始化SQL"
    else
        require_path "$SCRIPT_DIR/database/mysql/init/init.sql" "MySQL初始化SQL"
    fi
    log "部署包完整性检查通过"
}

# Ask Compose to validate the selected application and optional demo profiles.
validate_compose() {
    output=$(compose_mode config --quiet 2>&1)
    if [ $? -ne 0 ]; then
        fail "Compose 配置校验失败：$output" 3
    fi
    if [ "$WITH_DEMO" -eq 1 ]; then
        output=$(compose_demo config --quiet 2>&1)
        [ $? -eq 0 ] || fail "Demo Compose 配置校验失败：$output" 3
    fi
    log "Compose 配置校验通过"
}

# Verify Docker CPU and memory allocations and warn about low host disk space.
check_resources() {
    # Read the resources assigned to the active Docker engine.
    resource_info=$(docker info --format '{{.NCPU}}|{{.MemTotal}}|{{.Architecture}}' 2>/dev/null)
    cpu=$(printf '%s' "$resource_info" | awk -F'|' '{print $1+0}')
    memory_bytes=$(printf '%s' "$resource_info" | awk -F'|' '{print $2+0}')
    memory_gb=$(awk -v bytes="$memory_bytes" 'BEGIN { printf "%d", bytes/1073741824 }')

    # Select recommended resource thresholds for the requested deployment mode.
    case "$MODE" in
        light) min_cpu=4; min_memory=8; min_disk=20 ;;
        all|local) min_cpu=8; min_memory=14; min_disk=40 ;;
        *) min_cpu=4; min_memory=8; min_disk=20 ;;
    esac

    # Stop on insufficient Docker resources unless the caller explicitly forces startup.
    if [ "$cpu" -lt "$min_cpu" ] || [ "$memory_gb" -lt "$min_memory" ]; then
        message="Docker可用资源不足：当前 ${cpu}CPU/${memory_gb}GB，$MODE 模式最低建议 ${min_cpu}CPU/${min_memory}GB。"
        [ "$FORCE" -eq 1 ] && warn "$message" || fail "$message 可使用 --force 自行承担风险。" 2
    else
        log "资源检查通过：${cpu}CPU/${memory_gb}GB"
    fi

    # Disk space is advisory because Docker storage may reside on another filesystem.
    disk_kb=$(df -Pk "$SCRIPT_DIR" 2>/dev/null | awk 'NR==2 {print $4+0}')
    if [ -n "$disk_kb" ] && [ "$disk_kb" -gt 0 ]; then
        disk_gb=$((disk_kb / 1048576))
        [ "$disk_gb" -ge "$min_disk" ] || warn "宿主机剩余空间约 ${disk_gb}GB，建议至少保留 ${min_disk}GB。"
    fi
}

# Return the Compose project and container currently publishing a host port.
port_owner() {
    port=$1
    docker ps --filter "publish=$port" --format '{{.Label "com.docker.compose.project"}}|{{.Names}}' 2>/dev/null | sed -n '1p'
}

# Detect a non-Docker process listening on a host TCP port.
port_in_use_host() {
    port=$1
    if command -v lsof >/dev/null 2>&1; then
        lsof -nP -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1
    elif command -v ss >/dev/null 2>&1; then
        ss -ltn 2>/dev/null | awk -v p=":$port" '$4 ~ p"$" {found=1} END {exit !found}'
    elif command -v netstat >/dev/null 2>&1; then
        netstat -an 2>/dev/null | awk -v p=".$port" '$0 ~ /LISTEN/ && $4 ~ p"$/ {found=1} END {exit !found}'
    else
        return 1
    fi
}

# Ensure all ports required by the selected profile are available to qData.
check_ports() {
    # Build the required port list from the active database and deployment mode.
    nginx_port=$(env_value EXPOSE_NGINX_PORT 2>/dev/null || printf '80')
    redis_port=$(env_value EXPOSE_REDIS_PORT 2>/dev/null || printf '6379')
    db_port=5236
    [ "$DB_TYPE" = mysql ] && db_port=3306
    if [ "$MODE" = local ]; then
        ports="$redis_port 5672 15672 27017 $db_port 5432 12345 25333"
    else
        ports="$nginx_port $redis_port 5672 15672 27017 7474 7687 $db_port"
        [ "$MODE" = all ] && ports="$ports 5432 12345 25333"
    fi

    # Allow ports already owned by this project and reject all other listeners.
    for port in $ports; do
        owner=$(port_owner "$port")
        if [ -n "$owner" ]; then
            owner_project=${owner%%|*}
            owner_name=${owner#*|}
            [ "$owner_project" = "$PROJECT_NAME" ] || fail "端口 $port 已被容器 $owner_name 占用。" 3
        elif port_in_use_host "$port"; then
            fail "端口 $port 已被宿主机进程占用，请释放端口或修改 Compose 端口配置。" 3
        fi
    done
    log "端口检查通过"
}

# Detect IPv4 subnet collisions with the fixed qData bridge network.
check_network_subnet() {
    subnet=172.28.0.0/16

    # Compare two IPv4 CIDR ranges and return success when they overlap.
    cidr_overlaps() {
        awk -v first="$1" -v second="$2" '
            function range(cidr, out, parts, octets, prefix, ip, size) {
                if (split(cidr, parts, "/") != 2) return 0
                prefix=parts[2]+0
                if (prefix < 0 || prefix > 32 || split(parts[1], octets, ".") != 4) return 0
                ip=((octets[1]*256+octets[2])*256+octets[3])*256+octets[4]
                size=2^(32-prefix)
                out[1]=int(ip/size)*size
                out[2]=out[1]+size-1
                return 1
            }
            BEGIN {
                if (!range(first, a) || !range(second, b)) exit 1
                exit !(a[1] <= b[2] && b[1] <= a[2])
            }
        '
    }

    # Inspect networks outside this Compose project for conflicting IPv4 ranges.
    for network_id in $(docker network ls -q 2>/dev/null); do
        network_name=$(docker network inspect "$network_id" --format '{{.Name}}' 2>/dev/null || true)
        network_project=$(docker network inspect "$network_id" --format '{{index .Labels "com.docker.compose.project"}}' 2>/dev/null || true)
        [ "$network_project" = "$PROJECT_NAME" ] && continue

        network_subnets=$(docker network inspect "$network_id" --format '{{range .IPAM.Config}}{{println .Subnet}}{{end}}' 2>/dev/null || true)
        for network_subnet in $network_subnets; do
            case "$network_subnet" in *.*/*) ;; *) continue ;; esac
            if cidr_overlaps "$subnet" "$network_subnet"; then
                fail "qData网段 $subnet 与已有Docker网络 $network_name（$network_subnet）冲突。请先停止并删除冲突网络，或将 docker-compose.yml 和 docker-compose-mysql.yml 中的 qdatanet 网段改为未占用网段后重试。" 3
            fi
        done
    done
    log "Docker网段检查通过：$subnet"
}

# -----------------------------------------------------------------------------
# Image preparation
# -----------------------------------------------------------------------------

# List the unique images required by the selected application profiles.
selected_images() {
    compose_mode config --images 2>/dev/null
    [ "$WITH_DEMO" -eq 1 ] && compose_demo config --images 2>/dev/null
}

# Ensure every required image is already present when running offline.
check_offline_images() {
    missing=0
    for image in $(selected_images | sort -u); do
        if ! docker image inspect "$image" >/dev/null 2>&1; then
            log "缺少本地镜像：$image"
            missing=1
        fi
    done
    [ "$missing" -eq 0 ] || fail "离线模式缺少镜像。请联网启动一次，或先使用 docker load 导入离线镜像包。" 4
    log "离线镜像检查通过"
}

# Pull one profile with bounded retries and actionable registry error handling.
pull_profile() {
    profile=$1
    attempt=1
    # Retry transient registry failures up to three times with incremental delays.
    while [ "$attempt" -le 3 ]; do
        log "正在拉取 $profile 模式镜像（第 $attempt/3 次）..."
        if [ "$profile" = demo ]; then
            output=$(compose_demo pull 2>&1)
        else
            output=$(compose_mode pull 2>&1)
        fi
        if [ $? -eq 0 ]; then
            log "$profile 模式镜像准备完成"
            return 0
        fi
        case "$output" in
            *429*|*rate\ limit*) warn "Docker Hub触发限流，请登录 Docker Hub或使用镜像加速。" ;;
            *no\ matching\ manifest*) fail "镜像不支持当前CPU架构：$output" 4 ;;
            *unauthorized*|*authentication\ required*) fail "镜像仓库需要登录或无拉取权限：$output" 4 ;;
            *timeout*|*TLS*|*connection*|*resolve*) warn "镜像仓库网络异常。中国大陆环境请配置可用的 Docker 镜像加速源。" ;;
        esac
        attempt=$((attempt + 1))
        [ "$attempt" -le 3 ] && sleep $((attempt * 3))
    done
    fail "镜像拉取失败：$output" 4
}

# Select online image pulls or strict local-image validation.
prepare_images() {
    if [ "$OFFLINE" -eq 1 ]; then
        check_offline_images
    else
        pull_profile "$MODE"
        [ "$WITH_DEMO" -eq 1 ] && pull_profile demo
    fi
}

# -----------------------------------------------------------------------------
# Service startup and readiness checks
# -----------------------------------------------------------------------------

# Return the runtime and health states for a container ID.
container_state() {
    docker inspect --format '{{.State.Status}}|{{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}' "$1" 2>/dev/null
}

# Wait until every requested service is running and healthy or the timeout expires.
wait_services() {
    profile=$1
    timeout=$2
    shift 2
    services="$*"
    start_time=$(date +%s)
    # Poll all containers together so failures include a complete state summary.
    while :; do
        ready=1
        summary=
        for service in $services; do
            if [ "$profile" = schema ]; then
                id=$(compose_base --profile schema ps -a -q "$service" 2>/dev/null)
            elif [ "$profile" = demo ]; then
                id=$(compose_demo ps -a -q "$service" 2>/dev/null)
            else
                id=$(compose_mode ps -a -q "$service" 2>/dev/null)
            fi
            if [ -z "$id" ]; then
                ready=0
                summary="$summary $service=missing"
                continue
            fi
            state=$(container_state "$id")
            status=${state%%|*}
            health=${state#*|}
            summary="$summary $service=$status/$health"
            if [ "$status" = exited ] || [ "$status" = dead ]; then
                log "服务异常：$summary"
                docker logs --tail 80 "$id" 2>&1
                return 1
            fi
            [ "$status" = running ] || ready=0
            [ "$health" = none ] || [ "$health" = healthy ] || ready=0
        done
        [ "$ready" -eq 1 ] && return 0
        now=$(date +%s)
        elapsed=$((now - start_time))
        [ "$elapsed" -lt "$timeout" ] || {
            log "等待服务超时：$summary"
            return 1
        }
        if [ $((elapsed % 30)) -lt 5 ]; then
            log "等待服务就绪（${elapsed}s）：$summary"
        fi
        sleep 5
    done
}

# Discover and wait for all services in the active deployment profile.
wait_profile() {
    timeout=$1
    services=$(compose_mode config --services 2>/dev/null | tr '\n' ' ')
    [ -n "$services" ] || return 1
    wait_services "$MODE" "$timeout" $services
}

# Confirm that the qData web endpoint responds after its containers become healthy.
wait_web() {
    [ "$MODE" = local ] && return 0
    command -v curl >/dev/null 2>&1 || {
        warn "系统没有 curl，跳过网页可用性检查。"
        return 0
    }
    port=$(env_value EXPOSE_NGINX_PORT 2>/dev/null || printf '80')
    url="http://127.0.0.1:$port/"
    elapsed=0
    while [ "$elapsed" -lt 180 ]; do
        code=$(curl -L -sS -o /dev/null -w '%{http_code}' --max-time 5 "$url" 2>/dev/null || printf '000')
        case "$code" in 2*|3*) log "qData页面检查通过：$url"; return 0 ;; esac
        sleep 5
        elapsed=$((elapsed + 5))
    done
    warn "容器已运行，但网页暂时无法访问：${url}。请使用 sh qdata.sh logs 查看日志。"
    return 0
}

# Run all preflight checks, initialize databases, and start the requested stack.
start_qdata() {
    # Complete all non-mutating validation before creating containers.
    log "开始启动 qData"
    check_docker
    discover_compose
    prepare_files
    check_package
    validate_compose
    check_resources
    check_ports
    check_network_subnet
    prepare_images

    # Initialize the selected primary database and shared MongoDB service first.
    log "正在初始化数据库..."
    output=$(compose_base --profile schema up -d 2>&1)
    [ $? -eq 0 ] || fail "数据库启动失败：$output" 5
    db_service=dm8
    [ "$DB_TYPE" = mysql ] && db_service=mysql
    wait_services schema 1200 "$db_service" mongodb || fail "数据库未能正常就绪。" 6

    # Start the application stack after the databases report healthy states.
    log "数据库已就绪，正在启动 $MODE 模式..."
    output=$(compose_mode up -d 2>&1)
    [ $? -eq 0 ] || fail "qData启动失败：$output" 5

    # Start and validate the optional demonstration databases independently.
    if [ "$WITH_DEMO" -eq 1 ]; then
        log "正在启动演示数据库..."
        output=$(compose_demo up -d 2>&1)
        [ $? -eq 0 ] || fail "演示数据库启动失败：$output" 5
        demo_services=$(compose_demo config --services 2>/dev/null | tr '\n' ' ')
        wait_services demo 1200 $demo_services || fail "演示数据库存在未就绪或异常容器。" 6
    fi

    # Apply the longer readiness timeout required by scheduler-enabled modes.
    timeout=600
    if [ "$MODE" = all ] || [ "$MODE" = local ]; then
        timeout=1200
    fi
    wait_profile "$timeout" || fail "$MODE 模式存在未就绪或异常容器。" 6
    wait_web

    # Print access details only after the deployment has passed readiness checks.
    log "qData启动完成"
    if [ "$MODE" != local ]; then
        port=$(env_value EXPOSE_NGINX_PORT 2>/dev/null || printf '80')
        printf '\nqData访问地址：http://localhost:%s\n账号：admin\n密码：qData123\n' "$port"
    fi
    if [ "$MODE" = all ]; then
        printf '\nDolphinScheduler：http://localhost:12345/dolphinscheduler/ui/home\n账号：admin\n密码：dolphinscheduler123\n'
    fi
    printf '状态查看：sh qdata.sh status\n日志查看：sh qdata.sh logs\n'
}

# Restore the database and Compose selection used by management commands.
load_saved_selection() {
    ensure_env
    MODE=light
    DB_TYPE=$(env_value DB_TYPE "$ENV_FILE" 2>/dev/null || printf 'dm8')
    WITH_DEMO=0
    case "$DB_TYPE" in
        mysql) COMPOSE_FILE="$SCRIPT_DIR/docker-compose-mysql.yml" ;;
        *) DB_TYPE=dm8; COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yml" ;;
    esac
}

# Execute a non-destructive lifecycle action against every project profile.
manage_qdata() {
    action=$1
    check_docker
    discover_compose
    load_saved_selection
    case "$action" in
        status) compose_everything ps ;;
        logs) compose_everything logs --tail 200 ;;
        stop)
            compose_everything stop
            log "qData已停止，数据和容器均已保留。"
            ;;
        restart)
            compose_everything restart
            log "qData重启命令已执行，请稍后使用 status 查看状态。"
            ;;
    esac
}

# Remove a runtime path only when it is contained by the deployment directory.
safe_remove_runtime_path() {
    path=$1
    case "$path" in
        "$SCRIPT_DIR"/*)
            [ -e "$path" ] && rm -rf -- "$path"
            ;;
        *) fail "拒绝删除部署目录之外的路径：$path" 7 ;;
    esac
}

# Remove qData Docker resources and generated runtime data after confirmation.
uninstall_qdata() {
    # Require an exact confirmation phrase before any destructive operation.
    printf '%s\n' "该操作将彻底删除 qData 容器、网络、数据卷、运行数据及未被占用的相关镜像。"
    printf '%s' "请输入 DELETE QDATA 确认："
    IFS= read -r confirmation
    [ "$confirmation" = "DELETE QDATA" ] || {
        printf '%s\n' "输入不匹配，已取消卸载。"
        exit 0
    }

    ensure_env
    check_docker
    discover_compose
    load_saved_selection
    log "开始彻底卸载 qData项目 $PROJECT_NAME"

    # Capture referenced images before removing the Compose resources.
    images=$(
        for file in "$SCRIPT_DIR/docker-compose.yml" "$SCRIPT_DIR/docker-compose-mysql.yml"; do
            COMPOSE_FILE=$file
            compose_everything config --images 2>/dev/null || true
        done | sort -u
    )

    # Tear down both database variants so prior configuration changes leave no resources.
    for file in "$SCRIPT_DIR/docker-compose.yml" "$SCRIPT_DIR/docker-compose-mysql.yml"; do
        COMPOSE_FILE=$file
        compose_everything down --volumes --remove-orphans >/dev/null 2>&1 || true
    done

    # Remove any labeled resources left behind by incomplete Compose operations.
    for container in $(docker ps -aq --filter "label=com.docker.compose.project=$PROJECT_NAME" 2>/dev/null); do
        docker rm -f "$container" >/dev/null 2>&1 || true
    done
    for volume in $(docker volume ls -q --filter "label=com.docker.compose.project=$PROJECT_NAME" 2>/dev/null); do
        docker volume rm "$volume" >/dev/null 2>&1 || true
    done
    for network in $(docker network ls -q --filter "label=com.docker.compose.project=$PROJECT_NAME" 2>/dev/null); do
        docker network rm "$network" >/dev/null 2>&1 || true
    done

    # Remove project images only when Docker confirms they are not in use elsewhere.
    for image in $images; do
        if docker image inspect "$image" >/dev/null 2>&1; then
            docker image rm "$image" >/dev/null 2>&1 || warn "镜像仍被其他容器使用，已保留：$image"
        fi
    done

    # Delete generated logs and data while retaining scripts and configuration files.
    for runtime_path in \
        "$SCRIPT_DIR/nginx/logs" \
        "$SCRIPT_DIR/qdata-server/logs" \
        "$SCRIPT_DIR/qdata-server/upload" \
        "$SCRIPT_DIR/qdata-service-quality/logs" \
        "$SCRIPT_DIR/qdata-service-quality/job-log" \
        "$SCRIPT_DIR/qdata-service-ai/logs" \
        "$SCRIPT_DIR/dolphinscheduler/logs" \
        "$SCRIPT_DIR/neo4j/data" \
        "$SCRIPT_DIR/neo4j/logs" \
        "$SCRIPT_DIR/hadoop/data" \
        "$SCRIPT_DIR/database/mysql/data" \
        "$SCRIPT_DIR/database/mongoDB" \
        "$SCRIPT_DIR/.qdata"; do
        safe_remove_runtime_path "$runtime_path"
    done

    printf '%s\n' "qData已彻底卸载。部署脚本、Compose文件和 .env 配置已保留。"
}

# Run the complete preflight suite without starting any services.
doctor_qdata() {
    check_docker
    discover_compose
    prepare_files
    check_package
    validate_compose
    check_resources
    check_ports
    check_network_subnet
    log "环境诊断通过，可以启动 qData。"
}

# Parse start or doctor options and resolve the requested database configuration.
parse_start_options() {
    requested_db=
    while [ $# -gt 0 ]; do
        case "$1" in
            --db)
                [ $# -ge 2 ] || fail "--db 后必须指定 dm8 或 mysql。" 3
                requested_db=$2
                shift 2
                ;;
            --db=*) requested_db=${1#*=}; shift ;;
            --demo) WITH_DEMO=1; shift ;;
            --offline) OFFLINE=1; shift ;;
            --force) FORCE=1; shift ;;
            *) fail "未知参数：$1" 3 ;;
        esac
    done
    ensure_env
    select_database "$requested_db"
}

# Dispatch the requested top-level command and validate its positional arguments.
main() {
    action=${1:-help}
    [ $# -gt 0 ] && shift

    case "$action" in
        start)
            MODE=${1:-}
            [ $# -gt 0 ] && shift
            case "$MODE" in light|all|local) ;; *) fail "启动模式必须是 light、all 或 local。" 3 ;; esac
            parse_start_options "$@"
            start_qdata
            ;;
        doctor)
            MODE=${1:-light}
            [ $# -gt 0 ] && shift
            case "$MODE" in light|all|local) ;; *) fail "诊断模式必须是 light、all 或 local。" 3 ;; esac
            parse_start_options "$@"
            doctor_qdata
            ;;
        status|logs|stop|restart) manage_qdata "$action" ;;
        uninstall) uninstall_qdata ;;
        help|-h|--help) usage ;;
        *) usage; fail "未知命令：$action" 3 ;;
    esac
}

# Preserve all original command-line arguments when entering the dispatcher.
main "$@"
