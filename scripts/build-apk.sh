#!/bin/bash

# Bump the version
PROPERTIES_FILE="version.properties"
if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "Error: $PROPERTIES_FILE not found"
  exit 1
fi

VERSION_CODE=$(grep ^VERSION_CODE "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d '[:space:]')
VERSION_NAME=$(grep ^VERSION_NAME "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d '[:space:]')

VERSION_CODE=$((VERSION_CODE + 1))

if [ -n "$1" ]; then
  VERSION_NAME="$1"
fi

sed -i "s/^VERSION_CODE=.*/VERSION_CODE=$VERSION_CODE/" "$PROPERTIES_FILE"
sed -i "s/^VERSION_NAME=.*/VERSION_NAME=\"$VERSION_NAME\"/" "$PROPERTIES_FILE"

# Build the APK
chmod +x ./gradlew
./gradlew assembleRelease