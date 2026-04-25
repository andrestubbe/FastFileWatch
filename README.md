# FastFileWatch

High-performance native file system watcher with instant event delivery and minimal overhead [ALPHA].

[![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)]()
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/andrestubbe/FastFileWatch.svg)](https://jitpack.io/#andrestubbe/FastFileWatch)

## Table of Contents

- [Description](#description)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Installation](#installation)
- [Building from Source](#building-from-source)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

## Description

FastFileWatch is the third module in the FastJava file search engine trilogy:

- **FastFileIndex** - Full filesystem scan → produces a binary, mmap-capable index of all files
- **FastFileSearch** - Builds Prefix Trie, N-Gram index, Exact Match map, and Ranking engine on top of the index
- **FastFileWatch** - Uses USN Journal to keep the index + search structures live-updated with zero rescans

This architecture is similar to Everything, Spotlight, VSCode, and fsearch but modular and embeddable.

## Quick Start

```java
import fastfilewatch.FastFileWatch;
import fastfilewatch.ChangeCallback;

public class Example {
    public static void main(String[] args) {
        // Check if USN Journal is available
        if (!FastFileWatch.isUSNJournalAvailable()) {
            System.out.println("USN Journal not available on this system");
            return;
        }
        
        // Start monitoring
        String[] paths = { "C:\\" };
        FastFileWatch.start(paths, new ChangeCallback() {
            @Override
            public void onFileCreated(String path) {
                System.out.println("Created: " + path);
            }
            
            @Override
            public void onFileModified(String path) {
                System.out.println("Modified: " + path);
            }
            
            @Override
            public void onFileDeleted(String path) {
                System.out.println("Deleted: " + path);
            }
            
            @Override
            public void onFileRenamed(String oldPath, String newPath) {
                System.out.println("Renamed: " + oldPath + " -> " + newPath);
            }
        });
        
        // Keep running
        try {
            Thread.sleep(60000); // Run for 60 seconds
        } catch (InterruptedException e) {
            // Ignore
        }
        
        // Stop monitoring
        FastFileWatch.stop();
    }
}
```

## Key Features

- **USN Journal monitoring** - Zero-filesystem-access change detection
- **Real-time updates** - Instant notification of file changes
- **Event filtering** - Filter by create, modify, delete, rename operations
- **Path-based filtering** - Monitor specific directories or entire drives
- **Low overhead** - Minimal CPU and memory usage
- **Incremental updates** - Only process changed files

## Installation

### Prerequisites (Windows)

FastFileWatch requires the USN Journal to be enabled on the NTFS volumes you want to monitor. This requires **Administrator privileges**.

**Enable USN Journal on C: drive:**
```powershell
# Run as Administrator
fsutil usn createjournal C: 64000 4096
```

**Check USN Journal status:**
```powershell
fsutil usn queryjournal C:
```

**Note:** USN Journal is a Windows NTFS feature that tracks file system changes. It requires admin privileges to enable and is not enabled by default on most systems.

### Maven (JitPack)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastfilewatch</artifactId>
    <version>v1.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
    <version>v1.0.0</version>
</dependency>
```

> **Note:** FastCore handles native library loading from the JAR automatically. Both dependencies are required.

### Gradle (JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastfilewatch:v1.0.0'
    implementation 'com.github.andrestubbe:fastcore:v1.0.0'
}
```

## Building from Source

For detailed build instructions, see [COMPILE.md](COMPILE.md).

## Platform Support

- **Windows 10+** (x86_64) - Fully supported with USN Journal
- **Linux** - Planned (inotify)
- **macOS** - Planned (FSEvents)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Related Projects

- [FastFileIndex](https://github.com/andrestubbe/FastFileIndex) - Binary file indexing with mmap support
- [FastFileSearch](https://github.com/andrestubbe/FastFileSearch) - Prefix Trie, N-Gram index, and Ranking engine
- [FastCore](https://github.com/andrestubbe/FastCore) - Unified JNI loader and platform abstraction
