@echo off
echo === FastFileWatch Benchmark ===
echo.
echo Building FastFileWatch...
call mvn clean package -f pom.xml
echo.
echo Building Benchmark...
call mvn clean compile -f examples\Benchmark\pom.xml
echo.
echo Running Benchmark...
java --enable-native-access=ALL-UNNAMED -cp "examples\Benchmark\target\classes;target\fastfilewatch-v1.0.0.jar" fastfilewatch.Benchmark
echo.
echo === Benchmark Complete ===
pause
