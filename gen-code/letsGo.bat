@echo off

echo.
echo [信息] 开始生成代码
call java -jar apec-gen-code-1.0.0-RELEASE.jar "application-conf.properties"
echo [信息] 停止生成代码
echo. 
echo. 


pause
