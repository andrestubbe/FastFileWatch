@echo off
echo === FastFileWatch Demo ===
echo.
echo Building FastFileWatch...
call mvn clean package -f pom.xml
echo.
echo Running Demo...
java --enable-native-access=ALL-UNNAMED -cp "target\fastfilewatch-v1.0.0.jar" fastfilewatch.Demo
echo.
echo === Demo Complete ===
pause
