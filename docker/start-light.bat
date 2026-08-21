@echo off
REM =============================================================================
REM qData Light-Mode Docker Launcher
REM
REM Purpose:
REM   Validates the local Docker environment and deployment package, initializes
REM   the configured database, and starts the lightweight qData service stack.
REM
REM Usage:
REM   Run start-light.bat from Command Prompt or double-click it in File Explorer.
REM
REM Requirements:
REM   Windows with Docker Desktop in Linux Containers mode and Docker Compose v2.
REM =============================================================================

REM Initialize the command environment and resolve all relative paths from this file.
setlocal EnableExtensions
chcp 65001 >nul
cd /d "%~dp0"

REM Define the light-mode profile, project identity, and default configuration files.
set "QDATA_MODE=light"
set "QDATA_PROJECT=qdata"
set "QDATA_ENV=.env"
set "QDATA_COMPOSE=docker-compose.yml"
set "QDATA_EXIT_CODE=0"

REM Display the launcher banner before beginning the main workflow.
echo ========================================
echo qData 轻量版一键启动
echo ========================================

REM Run all preflight checks before pulling images or creating containers.
call :prepare
if errorlevel 1 goto :failed

REM Pull all images required by the lightweight deployment profile.
call :pull_images
if errorlevel 1 goto :failed

REM Initialize the selected database schema and wait for database readiness.
echo [信息] 正在初始化数据库...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile schema up -d --wait --wait-timeout 1200
if errorlevel 1 (
echo [错误] 数据库初始化失败。
    goto :failed
)

REM Start the lightweight qData application stack.
echo [信息] 正在启动 qData 轻量模式...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" up -d --wait --wait-timeout 1200
if errorlevel 1 (
echo [错误] qData 轻量模式启动失败。
    goto :failed
)

REM Report container status and print the qData access credentials.
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" ps
echo.
echo [成功] qData 轻量模式已启动。
echo 访问地址：http://localhost
echo 账号：admin
echo 密码：qData123
goto :finished

REM Prepare the host and deployment configuration for a light-mode startup.
:prepare
REM Confirm that Docker is installed and its daemon is reachable.
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

REM Require Linux Containers mode because all qData images target Linux.
for /f "usebackq delims=" %%O in (`docker info --format "{{.OSType}}" 2^>nul`) do set "QDATA_OS_TYPE=%%O"
if /i not "%QDATA_OS_TYPE%"=="linux" (
echo [错误] Docker Desktop 必须切换到 Linux Containers 模式。
    exit /b 1
)

REM Require the Docker Compose v2 command used by this deployment package.
docker compose version >nul 2>&1
if errorlevel 1 (
echo [错误] 需要安装 Docker Compose v2。
    exit /b 1
)

REM Verify that Docker Desktop has enough CPU and memory for light mode.
call :check_resources
if errorlevel 1 exit /b 1

REM Create the active environment file from the example when it does not exist.
if not exist "%QDATA_ENV%" (
    if not exist ".env.example" (
echo [错误] 缺少 .env 和 .env.example 文件。
        exit /b 1
    )
    copy /y ".env.example" "%QDATA_ENV%" >nul
echo [信息] 已根据 .env.example 创建 .env。
)

REM Select the Compose file that matches the configured database implementation.
for /f "tokens=1,* delims==" %%A in ('findstr /B /I "DB_TYPE=" "%QDATA_ENV%"') do set "QDATA_DB_TYPE=%%B"
if /i "%QDATA_DB_TYPE%"=="mysql" set "QDATA_COMPOSE=docker-compose-mysql.yml"
if not defined QDATA_DB_TYPE set "QDATA_DB_TYPE=dm8"

REM Validate the frontend and backend artifacts required by light mode.
if not exist "nginx\dist\index.html" (
echo [错误] 缺少 nginx\dist\index.html，部署包不完整。
    exit /b 1
)
if not exist "qdata-server\datax\" (
echo [错误] 缺少 qdata-server\datax 目录，部署包不完整。
    exit /b 1
)

REM Validate the resolved Compose model before changing Docker state.
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "%QDATA_COMPOSE%" --profile "%QDATA_MODE%" config --quiet
if errorlevel 1 (
echo [错误] Compose 配置校验失败，建议使用 Docker Compose 2.20.2 或更高版本。
    exit /b 1
)

REM Ensure the fixed qData bridge subnet can be allocated safely.
call :check_network_subnet
if errorlevel 1 exit /b 1

echo [信息] 数据库：%QDATA_DB_TYPE%
echo [信息] Compose 文件：%QDATA_COMPOSE%
exit /b 0

REM Check whether the qData subnet already exists or conflicts with another network.
:check_network_subnet
REM Reuse the network when it already belongs to this Compose project.
docker network inspect "%QDATA_PROJECT%_qdatanet" >nul 2>&1
if not errorlevel 1 (
    echo [信息] Docker 网段检查通过：已存在 %QDATA_PROJECT%_qdatanet 网络。
    exit /b 0
)

REM Probe subnet availability with a temporary bridge network.
set "QDATA_NETWORK_CHECK=%QDATA_PROJECT%_subnet_check_%RANDOM%"
docker network create --driver bridge --subnet 172.28.0.0/16 "%QDATA_NETWORK_CHECK%" >nul 2>&1
if errorlevel 1 (
    echo [错误] qData 网段 172.28.0.0/16 与已有 Docker 网络冲突。
    echo 请删除不再使用的冲突网络，或修改 Compose 中的 qdatanet 网段后重试。
    exit /b 1
)

REM Remove the temporary network after a successful allocation test.
docker network rm "%QDATA_NETWORK_CHECK%" >nul 2>&1
if errorlevel 1 (
    echo [错误] Docker 网段预检网络清理失败：%QDATA_NETWORK_CHECK%
    exit /b 1
)

echo [信息] Docker 网段检查通过：172.28.0.0/16
exit /b 0

REM Check Docker resource allocations against the light-mode recommendations.
:check_resources
REM Define minimum resources and clear values populated from Docker Desktop.
set "QDATA_MIN_CPU=4"
set "QDATA_MIN_MEMORY_GB=8"
set "QDATA_CPU="
set "QDATA_MEMORY_GB="

REM Read the CPU count and convert the Docker memory allocation to whole gigabytes.
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

REM Compare the detected allocations with the light-mode minimums.
set "QDATA_RESOURCE_INSUFFICIENT=0"
if %QDATA_CPU% LSS %QDATA_MIN_CPU% set "QDATA_RESOURCE_INSUFFICIENT=1"
if %QDATA_MEMORY_GB% LSS %QDATA_MIN_MEMORY_GB% set "QDATA_RESOURCE_INSUFFICIENT=1"

REM Require explicit user confirmation before starting with insufficient resources.
if "%QDATA_RESOURCE_INSUFFICIENT%"=="1" (
    echo [警告] Docker 可用资源不足：当前 %QDATA_CPU%CPU/%QDATA_MEMORY_GB%GB，轻量模式最低建议 %QDATA_MIN_CPU%CPU/%QDATA_MIN_MEMORY_GB%GB。
    choice /C YN /N /M "是否仍要强制启动？[Y/N] "
    if errorlevel 2 (
        echo [信息] 已取消启动。
        exit /b 1
    )
    echo [警告] 已选择强制启动，请自行承担资源不足导致的运行风险。
)

echo [信息] Docker 可用资源：%QDATA_CPU%CPU/%QDATA_MEMORY_GB%GB
exit /b 0

REM Pull light-mode images with bounded retries for transient registry failures.
:pull_images
set "QDATA_PULL_ATTEMPT=1"
REM Retry the same pull operation up to three times before returning failure.
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

REM Convert any workflow failure into a non-zero process exit code.
:failed
set "QDATA_EXIT_CODE=1"
echo.
echo qData 启动失败，请查看上方错误信息。

REM Pause for interactive launchers and return the final workflow result.
:finished
echo.
pause
exit /b %QDATA_EXIT_CODE%
