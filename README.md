# FastFileWatch v0.1.0 [ALPHA] — High-Performance Native File Monitoring for Java

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastFileWatch/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**⚡ A zero-latency file system monitoring module for the FastJava ecosystem. Real-time events for file creation,
modification, and deletion via ReadDirectoryChangesW.**

**FastFileWatch** provides instant notification of file system changes. By using direct Win32 API hooks, it eliminates
the polling and latency associated with standard Java WatchService.

> **5–12× faster than Java's Float.parseFloat / Double.parseDouble.** Zero-GC. SIMD-accelerated. Ryu-powered formatting.
> JSON/CSV/telemetry parsing without garbage collection overhead.


---

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [License](#license)

---

## Features

- **⚡ Instant Events**: Real-time notification via native Windows hooks.
- **🔎 Low Overhead**: Efficient monitoring without periodic polling.
- **📦 Recursive Support**: High-performance monitoring of entire directory trees.
- **🚀 Raw Speed**: Built for real-time indexing and automation tools.

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
   <dependency>
       <groupId>com.github.andrestubbe</groupId>
       <artifactId>fastfilewatch</artifactId>
       <version>v0.1.0</version>
   </dependency>
   <dependency>
       <groupId>com.github.andrestubbe</groupId>
       <artifactId>fastcore</artifactId>
       <version>v0.1.0</version>
   </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.andrestubbe:fastfilewatch:v0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 *
   *[fastfilewatch-v0.1.0.jar](https://github.com/andrestubbe/FastFileWatch/releases/download/v0.1.0/fastfilewatch-v0.1.0.jar)
   ** (The Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (
   The Mandatory Native Loader)

---

## Documentation

* **[COMPILE.md](COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
* **[PHILOSOPHIE.md](PHILOSOPHIE.md)**: The engineering rationale for zero-allocation performance.
* **[ROADMAP.md](ROADMAP.md)**: Future milestones and planned features.

---

## Platform Support

| Platform      | Status            |
|---------------|-------------------|
| Windows 10/11 | ✅ Fully Supported |
| Linux         | 🚧 Planned        |
| macOS         | 🚧 Planned        |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastFileIndex](https://github.com/andrestubbe/FastFileIndex) — Ultra-fast filesystem scanner
- [FastTheme](https://github.com/andrestubbe/FastTheme) — High-performance native window styling
- [FastThumb](https://github.com/andrestubbe/FastThumb) — Native Shell Image Engine

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
