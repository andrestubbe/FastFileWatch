@echo off
chcp 65001 >nul
cd /d "%~dp0"
echo [FastFileWatch] Running Demo (via JitPack)...
cd examples\Demo
call mvn -q compile exec:java -Dexec.mainClass=fastfilewatch.Demo
cd ..\..
pause
