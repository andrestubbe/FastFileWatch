@echo off
echo === FastFileWatch Benchmark ===
echo.
echo Building FastFileWatch...
call mvn clean package -f pom.xml
echo.
echo Building Benchmark...
cd examples\Benchmark
call mvn clean package -f pom.xml
echo.
echo Running Benchmark...
java --enable-native-access=ALL-UNNAMED -cp "target\fastfilewatch-benchmark-v1.0.0.jar;..\..\target\fastfilewatch-v1.0.0.jar" fastfilewatch.Benchmark
cd ..\..
echo.
echo === Benchmark Complete ===
pause
