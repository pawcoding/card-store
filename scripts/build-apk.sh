#!/bin/bash

chmod +x ./gradlew

# Build the ABB
./gradlew bundleRelease

# Build the APK
./gradlew assembleRelease