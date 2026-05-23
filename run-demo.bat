@echo off
echo === FastFileWatch Demo ===
echo.
echo Building FastFileWatch...
call mvn clean package -f pom.xml
echo.
echo Building Demo...
cd examples\Demo
call mvn clean package -f pom.xml
echo.
echo Running Demo...
java --enable-native-access=ALL-UNNAMED -cp "target\fastfilewatch-demo-v1.0.0.jar;..\..\target\fastfilewatch-v1.0.0.jar" fastfilewatch.Demo
cd ..\..
echo.
echo === Demo Complete ===
pause
