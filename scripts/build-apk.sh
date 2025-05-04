#!/bin/bash

chmod +x ./gradlew

# Build the APK
./gradlew assembleRelease

# Build the ABB
./gradlew bundleRelease
