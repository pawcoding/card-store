# Debug App Modifications - Summary

## What was implemented:

### 🏷️ App Name Changes
- **Release build**: "CardStore" (unchanged)
- **Debug build**: "CardStore Debug" 

### 📱 Package Name (already existed)
- **Release build**: `de.pawcode.cardstore`
- **Debug build**: `de.pawcode.cardstore.debug`

### 🎨 Icon Changes
- **Release build**: Original CardStore icon (unchanged)
- **Debug build**: Modified icon with:
  - Different background color (system_accent3_700 vs system_accent1_700)
  - Red debug indicator circle with "D" in top-right corner

## Files Created:

### Debug-specific resources (only apply to debug builds):
```
app/src/debug/res/
├── values/
│   ├── strings.xml                 # "CardStore Debug" app name
│   └── ic_launcher_background.xml  # Different background color
├── drawable/
│   └── ic_launcher_foreground.xml  # Icon with debug indicator
├── mipmap-anydpi-v26/
│   └── ic_launcher.xml             # Adaptive icon config
└── mipmap-anydpi/
    └── ic_launcher_round.xml       # Round icon config
```

### Test updates:
- Updated `ExampleInstrumentedTest.kt` to handle both debug and release package names
- Added test to verify debug builds have "Debug" in app name

## Result:
✅ Users can now easily distinguish between debug and release versions:
- Different app names in launcher
- Different icons (debug has red indicator)
- Different package names (allows both to be installed simultaneously)

## Verification:
Both debug and release builds compile successfully:
- Debug APK: 89.7MB (with debug symbols)
- Release APK: 25.5MB (minified/obfuscated)