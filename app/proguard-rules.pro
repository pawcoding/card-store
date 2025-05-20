# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Rules for DataStore ProtoBuf
#   To prevent: https://github.com/pawcoding/card-store/issues/15
# Keep the internal ProtoBuf classes of DataStore to avoid issues with
# serialization/deserialization.
# These rules prevent R8 from renaming or removing critical fields and
# methods that are used internally by DataStore.
-keep class androidx.datastore.preferences.protobuf.** { *; }
-keep interface androidx.datastore.preferences.protobuf.** { *; }

# Keep all classes and their members in the following packages used by DataStore
-keep class androidx.datastore.preferences.PreferencesProto$* { *; }
-keep class androidx.datastore.preferences.PreferencesMapCompat$* { *; }

# Generic rules often necessary for ProtoBuf
-dontwarn com.google.protobuf.**
-dontwarn org.apache.harmony.awt.internal.nls.**