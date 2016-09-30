@echo off

echo ...............................................................................................................................................................
echo This shell script does not put lib directory into package. If you want to, please just upload '*.war' in target directory.
echo ...............................................................................................................................................................


set SVN_ROOT=D:\svn\uya\trunk\svr\enough
set TARGET_PATH=%SVN_ROOT%\target\enough
set DEPLOY=%SVN_ROOT%\deploy

rd /s /q %DEPLOY%
md %DEPLOY%
echo %TARGET_PATH%
xcopy %TARGET_PATH%\* %DEPLOY%\ /s /e
rd /s /q %DEPLOY%\WEB-INF\lib
del /f /s /q %DEPLOY%\WEB-INF\classes\enough.properties
del /f /s /q %DEPLOY%\WEB-INF\classes\log4j.properties

cd /d %DEPLOY%

jar -cvf enough.war *

rd /s /q %DEPLOY%\META-INF
rd /s /q %DEPLOY%\WEB-INF
rd /s /q %DEPLOY%\static
del /s /q %DEPLOY%\index.jsp

pause