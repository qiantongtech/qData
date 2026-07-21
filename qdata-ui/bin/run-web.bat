@echo off
echo.
echo [INFO] Starting the web application with Vite.
echo.

%~d0
cd %~dp0

cd ..
yarn dev

pause