@echo off
setlocal EnableExtensions
chcp 65001 >nul
cd /d "%~dp0"

set "QDATA_MODE=all"
set "QDATA_PROJECT=qdata"
set "QDATA_ENV=.env"
set "QDATA_COMPOSE=docker-compose.yml"
set "QDATA_EXIT_CODE=0"

echo ========================================
echo qData 完整版一键启动
echo ========================================

call :prepare
if errorlevel 1 goto :failed

call :pull_images
if errorlevel 1 goto :failed

echo [信息] 正在初始化数据库...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile schema up -d --wait --wait-timeout 1200
if errorlevel 1 (
echo [错误] 数据库初始化失败。
    goto :failed
)

echo [信息] 正在启动 qData 完整模式...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" up -d --wait --wait-timeout 1800
if errorlevel 1 (
echo [错误] qData 完整模式启动失败。
    goto :failed
)

docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" ps
echo.
echo [成功] qData 完整模式已启动。
echo.
echo qData 数据中台
echo qData 地址：http://localhost
echo 账号：admin
echo 密码：qData123
echo.
echo 调度器 DolphinScheduler
echo 访问地址：http://localhost:12345/dolphinscheduler/ui/home
echo 账号：admin
echo 密码：dolphinscheduler123
goto :finished

:prepare
where docker >nul 2>&1
if errorlevel 1 (
echo [错误] 未安装 Docker，请先安装 Docker Desktop。
    exit /b 1
)

docker info >nul 2>&1
if errorlevel 1 (
echo [错误] Docker Desktop 未启动或当前无法访问。
    exit /b 1
)

for /f "usebackq delims=" %%O in (`docker info --format "{{.OSType}}" 2^>nul`) do set "QDATA_OS_TYPE=%%O"
if /i not "%QDATA_OS_TYPE%"=="linux" (
echo [错误] Docker Desktop 必须切换到 Linux Containers 模式。
    exit /b 1
)

docker compose version >nul 2>&1
if errorlevel 1 (
echo [错误] 需要安装 Docker Compose v2。
    exit /b 1
)

call :check_resources
if errorlevel 1 exit /b 1

if not exist "%QDATA_ENV%" (
    if not exist ".env.example" (
echo [错误] 缺少 .env 和 .env.example 文件。
        exit /b 1
    )
    copy /y ".env.example" "%QDATA_ENV%" >nul
echo [信息] 已根据 .env.example 创建 .env。
)

for /f "tokens=1,* delims==" %%A in ('findstr /B /I "DB_TYPE=" "%QDATA_ENV%"') do set "QDATA_DB_TYPE=%%B"
if /i "%QDATA_DB_TYPE%"=="mysql" set "QDATA_COMPOSE=docker-compose-mysql.yml"
if not defined QDATA_DB_TYPE set "QDATA_DB_TYPE=dm8"

if not exist "nginx\dist\index.html" (
echo [错误] 缺少 nginx\dist\index.html，部署包不完整。
    exit /b 1
)
if not exist "qdata-server\datax\" (
echo [错误] 缺少 qdata-server\datax 目录，部署包不完整。
    exit /b 1
)
if not exist "dolphinscheduler\soft\spark\" (
echo [错误] 缺少 dolphinscheduler\soft\spark 目录。
    exit /b 1
)
if not exist "dolphinscheduler\soft\flink\" (
echo [错误] 缺少 dolphinscheduler\soft\flink 目录。
    exit /b 1
)
if not exist "dolphinscheduler\resource\default\resources\spark-jar\qdata-etl*.jar" (
echo [错误] 缺少 qData ETL JAR 文件。
    exit /b 1
)

docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" config --quiet
if errorlevel 1 (
echo [错误] Compose 配置校验失败，建议使用 Docker Compose 2.20.2 或更高版本。
    exit /b 1
)

call :check_network_subnet
if errorlevel 1 exit /b 1

echo [信息] 数据库：%QDATA_DB_TYPE%
echo [信息] Compose 文件：%QDATA_COMPOSE%
exit /b 0

:check_network_subnet
docker network inspect "%QDATA_PROJECT%_qdatanet" >nul 2>&1
if not errorlevel 1 (
    echo [信息] Docker 网段检查通过：已存在 %QDATA_PROJECT%_qdatanet 网络。
    exit /b 0
)

set "QDATA_NETWORK_CHECK=%QDATA_PROJECT%_subnet_check_%RANDOM%"
docker network create --driver bridge --subnet 172.28.0.0/16 "%QDATA_NETWORK_CHECK%" >nul 2>&1
if errorlevel 1 (
    echo [错误] qData 网段 172.28.0.0/16 与已有 Docker 网络冲突。
    echo 请删除不再使用的冲突网络，或修改 Compose 中的 qdatanet 网段后重试。
    exit /b 1
)

docker network rm "%QDATA_NETWORK_CHECK%" >nul 2>&1
if errorlevel 1 (
    echo [错误] Docker 网段预检网络清理失败：%QDATA_NETWORK_CHECK%
    exit /b 1
)

echo [信息] Docker 网段检查通过：172.28.0.0/16
exit /b 0

:check_resources
set "QDATA_MIN_CPU=8"
set "QDATA_MIN_MEMORY_GB=14"
set "QDATA_CPU="
set "QDATA_MEMORY_GB="

for /f "usebackq delims=" %%O in (`docker info --format "{{.NCPU}}" 2^>nul`) do set "QDATA_CPU=%%O"
for /f "usebackq delims=" %%O in (`powershell -NoProfile -Command "[math]::Floor([double](docker info --format '{{.MemTotal}}') / 1GB)" 2^>nul`) do set "QDATA_MEMORY_GB=%%O"

if not defined QDATA_CPU (
echo [错误] 无法读取 Docker CPU 资源。
    exit /b 1
)
if not defined QDATA_MEMORY_GB (
echo [错误] 无法读取 Docker 内存资源。
    exit /b 1
)

set "QDATA_RESOURCE_INSUFFICIENT=0"
if %QDATA_CPU% LSS %QDATA_MIN_CPU% set "QDATA_RESOURCE_INSUFFICIENT=1"
if %QDATA_MEMORY_GB% LSS %QDATA_MIN_MEMORY_GB% set "QDATA_RESOURCE_INSUFFICIENT=1"

if "%QDATA_RESOURCE_INSUFFICIENT%"=="1" (
    echo [警告] Docker 可用资源不足：当前 %QDATA_CPU%CPU/%QDATA_MEMORY_GB%GB，完整模式最低建议 %QDATA_MIN_CPU%CPU/%QDATA_MIN_MEMORY_GB%GB。
    choice /C YN /N /M "是否仍要强制启动？[Y/N] "
    if errorlevel 2 (
        echo [信息] 已取消启动。
        exit /b 1
    )
    echo [警告] 已选择强制启动，请自行承担资源不足导致的运行风险。
)

echo [信息] Docker 可用资源：%QDATA_CPU%CPU/%QDATA_MEMORY_GB%GB
exit /b 0

:pull_images
set "QDATA_PULL_ATTEMPT=1"
:pull_retry
echo [信息] 正在拉取镜像（第 %QDATA_PULL_ATTEMPT%/3 次）...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" pull
if not errorlevel 1 exit /b 0
if "%QDATA_PULL_ATTEMPT%"=="3" (
echo [错误] 镜像拉取失败，请检查 DNS、代理、镜像仓库权限；中国大陆网络可配置 Docker 镜像加速地址。
    exit /b 1
)
set /a QDATA_PULL_ATTEMPT+=1
timeout /t 5 /nobreak >nul
goto :pull_retry

:failed
set "QDATA_EXIT_CODE=1"
echo.
echo qData 启动失败，请查看上方错误信息。

:finished
echo.
pause
exit /b %QDATA_EXIT_CODE%
