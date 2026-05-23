# FastFileWatch — High-Performance Native File Monitoring for Java [v0.1.0]

**A zero-latency file system monitoring module for the FastJava ecosystem. Real-time events for file creation, modification, and deletion via ReadDirectoryChangesW.**

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastFileWatch/releases/tag/v0.1.0)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

**FastFileWatch** provides instant notification of file system changes. By using direct Win32 API hooks, it eliminates the polling and latency associated with standard Java WatchService.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [License](#license)

## Features
- **⚡ Instant Events**: Real-time notification via native Windows hooks.
- **🔎 Low Overhead**: Efficient monitoring without periodic polling.
- **📦 Recursive Support**: High-performance monitoring of entire directory trees.
- **🚀 Raw Speed**: Built for real-time indexing and automation tools.

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
    <!-- FastFileWatch Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastfilewatch</artifactId>
        <version>v0.1.0</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
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

1. 📦 **[fastfilewatch-v0.1.0.jar](https://github.com/andrestubbe/FastFileWatch/releases/download/v0.1.0/fastfilewatch-v0.1.0.jar)** (The Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.


## License
MIT License — See [LICENSE](LICENSE) for details.

---
**Part of the FastJava Ecosystem** — *Making the JVM faster.*
