@echo off
REM =============================================================================
REM qData Docker Uninstaller
REM
REM Purpose:
REM   Removes qData containers, networks, volumes, runtime data, and related images
REM   that are not used by other containers while retaining deployment scripts,
REM   Docker Compose files, and the environment configuration file.
REM
REM Usage:
REM   Run uninstall.bat and enter DELETE QDATA when prompted for confirmation.
REM
REM Requirements:
REM   Windows with Docker Desktop running and Docker Compose v2 available.
REM
REM Warning:
REM   This operation permanently deletes qData runtime data and Docker volumes.
REM =============================================================================

REM Initialize the command environment and resolve all relative paths from this file.
setlocal EnableExtensions
chcp 65001 >nul
cd /d "%~dp0"

REM Define the Compose project, environment file, and final process result.
set "QDATA_PROJECT=qdata"
set "QDATA_ENV=.env"
set "QDATA_EXIT_CODE=0"

REM Explain the destructive scope before requesting confirmation.
echo ========================================
echo qData 完整卸载
echo ========================================
echo 此操作会删除 qData 容器、网络、数据卷、运行数据，
echo 以及未被其他容器使用的相关镜像。
echo 部署脚本、Compose 文件和 .env 将保留。
echo.
REM Require an exact confirmation phrase before deleting any qData resources.
set /p "QDATA_CONFIRM=请输入 DELETE QDATA 确认卸载："
if not "%QDATA_CONFIRM%"=="DELETE QDATA" (
echo 确认内容不正确，已取消卸载。
    goto :finished
)

REM Confirm that Docker, its daemon, and Docker Compose v2 are available.
where docker >nul 2>&1
if errorlevel 1 (
echo [错误] 未安装 Docker。
    goto :failed
)
docker info >nul 2>&1
if errorlevel 1 (
echo [错误] Docker Desktop 未启动或当前无法访问。
    goto :failed
)
docker compose version >nul 2>&1
if errorlevel 1 (
echo [错误] 需要安装 Docker Compose v2。
    goto :failed
)

REM Use the example environment only when the active file is unavailable.
if not exist "%QDATA_ENV%" set "QDATA_ENV=.env.example"

REM Tear down both database variants and remove their Compose-managed volumes.
echo [信息] 正在删除 qData 容器、网络和数据卷...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose.yml" --profile "*" down --volumes --remove-orphans >nul 2>&1
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose-mysql.yml" --profile "*" down --volumes --remove-orphans >nul 2>&1

REM Remove labeled resources left behind by interrupted or partial Compose operations.
for /f "usebackq delims=" %%I in (`docker ps -aq --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker rm -f "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker volume ls -q --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker volume rm "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker network ls -q --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker network rm "%%I" >nul 2>&1

REM Remove referenced images only when Docker reports that no other container uses them.
echo [信息] 正在删除未被其他容器使用的相关镜像...
for /f "usebackq delims=" %%I in (`docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose.yml" --profile "*" config --images 2^>nul`) do docker image rm "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose-mysql.yml" --profile "*" config --images 2^>nul`) do docker image rm "%%I" >nul 2>&1

REM Delete generated logs and runtime data while retaining deployment configuration.
echo [信息] 正在删除运行数据目录...
for %%D in (
    "nginx\logs"
    "qdata-server\logs"
    "qdata-server\upload"
    "qdata-service-quality\logs"
    "qdata-service-quality\job-log"
    "qdata-service-ai\logs"
    "dolphinscheduler\logs"
    "neo4j\data"
    "neo4j\logs"
    "hadoop\data"
    "database\mysql\data"
    "database\mongoDB"
    ".qdata"
) do if exist "%%~D" rmdir /s /q "%%~D"

echo.
echo [成功] qData 已彻底卸载。
goto :finished

REM Convert prerequisite failures into a non-zero process exit code.
:failed
set "QDATA_EXIT_CODE=1"
echo.
echo qData 卸载失败，请查看上方错误信息。

REM Pause for interactive launchers and return the final workflow result.
:finished
echo.
pause
exit /b %QDATA_EXIT_CODE%
