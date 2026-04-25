cmake_minimum_required(VERSION 3.15)
project(FastFileWatch VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find JNI
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

# Source files
set(SOURCES
    native/FastFileWatch.cpp
)

# Build shared library
add_library(fastfilewatch SHARED ${SOURCES})

# Link libraries
target_link_libraries(fastfilewatch ${JNI_LIBRARIES})

# Set output directory
set_target_properties(fastfilewatch PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_SOURCE_DIR}/build"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_SOURCE_DIR}/build"
)

# Windows-specific settings
if(WIN32)
    set_target_properties(fastfilewatch PROPERTIES
        PREFIX ""
        OUTPUT_NAME "fastfilewatch"
        SUFFIX ".dll"
    )
endif()
