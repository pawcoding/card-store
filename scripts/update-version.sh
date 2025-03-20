#!/bin/bash

# Check if version.properties file exists
PROPERTIES_FILE="version.properties"
if [ ! -f "$PROPERTIES_FILE" ]; then
  echo "Error: $PROPERTIES_FILE not found"
  exit 1
fi

# Read version code and name from properties file
VERSION_CODE=$(grep ^VERSION_CODE "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d '[:space:]')
VERSION_NAME=$(grep ^VERSION_NAME "$PROPERTIES_FILE" | cut -d'=' -f2 | tr -d '[:space:]')

# Increment version code
VERSION_CODE=$((VERSION_CODE + 1))

# Update version name if provided as argument
if [ -n "$1" ]; then
  VERSION_NAME="$1"
fi

# Write the updated version code and name to properties file
sed -i "s/^VERSION_CODE=.*/VERSION_CODE=$VERSION_CODE/" "$PROPERTIES_FILE"
sed -i "s/^VERSION_NAME=.*/VERSION_NAME=\"$VERSION_NAME\"/" "$PROPERTIES_FILE"