# Building FastFileWatch from Source

## Prerequisites

- **Windows 10+** (x86_64)
- **Java 17+**
- **Maven 3.6+**
- **CMake 3.15+**
- **Visual Studio 2019+** or **Build Tools for Visual Studio**
- **JNI headers** (included with JDK)

## Build Steps

### 1. Clone the Repository

```bash
git clone https://github.com/andrestubbe/FastFileWatch.git
cd FastFileWatch
```

### 2. Build Native Library (CMake)

```bash
mkdir build
cd build
cmake .. -G "Visual Studio 16 2019" -A x64
cmake --build . --config Release
```

This will generate `fastfilewatch.dll` in the `build` directory.

### 3. Build Java Library (Maven)

```bash
cd ..
mvn clean package
```

This will generate `fastfilewatch-v1.0.0.jar` in the `target` directory, including the native DLL.

## Running the Demo

```bash
cd examples/Demo
mvn exec:java
```

Or run directly:

```bash
java -cp target/fastfilewatch-v1.0.0.jar fastfilewatch.Demo
```

## Troubleshooting

### USN Journal Not Available

If USN Journal is not available on your system, you may need to enable it:

```powershell
fsutil usn queryjournal C:
```

If it returns "The USN Journal is not active", enable it:

```powershell
fsutil usn createjournal C: 64000 4096
```

### JNI Headers Not Found

Ensure JAVA_HOME is set correctly:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

### Build Errors

Make sure you have the correct Visual Studio version and CMake generator for your system.
