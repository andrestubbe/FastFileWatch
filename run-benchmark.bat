@echo off
echo ðŸš€ Running Hero Demo...
cd examples\Benchmark
call mvn compile exec:java -Dexec.mainClass=fastfilewatch.Benchmark
cd ..\..
pause
