@echo off
setlocal EnableExtensions
chcp 65001 >nul
cd /d "%~dp0"

set "QDATA_PROJECT=qdata"
set "QDATA_ENV=.env"
set "QDATA_EXIT_CODE=0"

echo ========================================
echo qData 完整卸载
echo ========================================
echo 此操作会删除 qData 容器、网络、数据卷、运行数据，
echo 以及未被其他容器使用的相关镜像。
echo 部署脚本、Compose 文件和 .env 将保留。
echo.
set /p "QDATA_CONFIRM=请输入 DELETE QDATA 确认卸载："
if not "%QDATA_CONFIRM%"=="DELETE QDATA" (
echo 确认内容不正确，已取消卸载。
    goto :finished
)

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

if not exist "%QDATA_ENV%" set "QDATA_ENV=.env.example"

echo [信息] 正在删除 qData 容器、网络和数据卷...
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose.yml" --profile "*" down --volumes --remove-orphans >nul 2>&1
docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose-mysql.yml" --profile "*" down --volumes --remove-orphans >nul 2>&1

for /f "usebackq delims=" %%I in (`docker ps -aq --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker rm -f "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker volume ls -q --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker volume rm "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker network ls -q --filter "label=com.docker.compose.project=%QDATA_PROJECT%" 2^>nul`) do docker network rm "%%I" >nul 2>&1

echo [信息] 正在删除未被其他容器使用的相关镜像...
for /f "usebackq delims=" %%I in (`docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose.yml" --profile "*" config --images 2^>nul`) do docker image rm "%%I" >nul 2>&1
for /f "usebackq delims=" %%I in (`docker compose --env-file "%QDATA_ENV%" -p "%QDATA_PROJECT%" -f "docker-compose-mysql.yml" --profile "*" config --images 2^>nul`) do docker image rm "%%I" >nul 2>&1

echo [信息] 正在删除运行数据目录...
for %%D in (
    "nginx\logs"
    "qdata-server\logs"
    "qdata-server\upload"
    "qdata-quality\logs"
    "qdata-quality\job-log"
    "qdata-ai\logs"
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

:failed
set "QDATA_EXIT_CODE=1"
echo.
echo qData 卸载失败，请查看上方错误信息。

:finished
echo.
pause
exit /b %QDATA_EXIT_CODE%
