cd /d %~dp0
set SH_DIR=%~dp0
cd ../
set ROOT_DIR=%cd%
echo %ROOT_DIR%
echo %SH_DIR%
set LOG_DIR=%ROOT_DIR%\log
if not exist %LOG_DIR% (
  md %LOG_DIR%
)
start /b java -jar %ROOT_DIR%/consul-product.jar >> %LOG_DIR%/start.log
set result=%errorlevel%
echo result=%result%
exit %result% 


