cd /d %~dp0
set SH_DIR=%~dp0
cd ../
set ROOT_DIR=%cd%
netstat -ano |findstr 8082|findstr LISTENING
for /F "tokens=5 delims= " %%i in ('netstat -ano ^|findstr 8082^|findstr LISTENING') do set pid=%%i
echo pid=%pid%
if "%pid%"=="" ( 
  echo "app does not start" 
) else (
   echo "start to kill app"
   taskkill /pid %pid% -F
)
exit 0

